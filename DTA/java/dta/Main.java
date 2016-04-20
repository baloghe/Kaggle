package dta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;


public class Main {

	public static String DRIVER_COORD_SEPARATOR = ",";
	public static String DRIVERS_DIRECTORY;

	public static final int RUNMODE_TRAIN = 0;
	public static final int RUNMODE_WRITE = 1;
	public static int RUNMODE;

	public static File SUBMISSION_FILE;

	public static boolean CREATE_SIMPLE_ROUTE_PATTERNS = true;
	public static boolean CREATE_VLEN_ROUTE_PATTERNS = true;
	public static boolean CREATE_FIXLEN_ROUTE = true;
	public static double GEOM_SIMILAR_TOLERANCE = 0.025;

	private static long startTime;

	public static HashMap<String, Double> ROUTELEVEL_RESULT;
	public static HashMap<String, Double> GEOMSIM_RESULT;
	public static File GEOMSIM_FILE;
	public static HashMap<Integer, HashSet<Integer>> GEOM_SIMILAR_ROUTES;

 	public static File ROUTES_SET_TO_1_FILE;
	public static HashSet<String> ROUTES_SET_TO_1;
	public static HashSet<Integer> DRIVERS_PROCESSED;

	public static HashMap<Integer, File> driver_to_folder;

	public static void main(String[] args){
		init();

		//solveRouteLevel();
		//solveVLenPattern();

		//
		solveGeomSimilarity();

		//merge results from different calculations
		//mergeResults();

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 60000;
		System.out.println("Main :: duration = " + duration + " min");
	}

	public static void init(){
		startTime = System.currentTimeMillis();

	   	String username=System.getProperty("user.name");
		System.out.println("USER=" + username);
		if(username.equalsIgnoreCase("baloghend")){
		   DRIVERS_DIRECTORY = "../data/drivers";
		} else {
			//DRIVERS_DIRECTORY = "data/drivers";
			DRIVERS_DIRECTORY = "../../../kaggle/DTA/data/drivers";
		}

		SUBMISSION_FILE = Util.createFile("submission.csv", "driver_trip,prob");
		GEOMSIM_FILE = Util.createFile("geomsim.csv", "driver_trip,prob");

		readFolders(DRIVERS_DIRECTORY);

		//Read stored results
		ROUTELEVEL_RESULT = Main.readPreviousResult("routelevelProbs.csv", ",");
	}

	private static void solveVLenPattern(){
		//Walk through the drivers one by one
		int drvcnt = 0;
		for(Integer drv : driver_to_folder.keySet()){
			long duration = (System.currentTimeMillis() - startTime) / 60000;
			System.out.println("   driver=" + drv + " = " + drvcnt + "/" + driver_to_folder.size() + " -> " + duration + " mins");

			if(!DRIVERS_PROCESSED.contains(drv)){

				//get the TRUE observations
				File trueFolder = driver_to_folder.get(drv);
				ArrayList<Route> tr = Main.processFolder(drv, trueFolder);

				//compute pairwise distances
				HashSet<CompRoute> crset = new HashSet<CompRoute>();
				for(Route ri : tr){
					for(Route rj : tr){
						int numi = new Integer(ri.getKey().split("_")[1]).intValue();
						int numj = new Integer(rj.getKey().split("_")[1]).intValue();
						//compare different routes
						if( numi < numj ){
							VLenRouteEvaluator eval = new VLenRouteEvaluator(ri, rj);
							crset.add( new CompRoute( ri, rj, eval.calcDistance() ) );
							System.out.println("     " + numi + " vs " + numj + " processed");
						}
					}//next rj
				}//next ri

				//sort them
				CompRoute[] xxx = (CompRoute[])crset.toArray();
				ArrayList<CompRoute> crlist = new ArrayList<CompRoute>();
				for(CompRoute f : xxx){
					crlist.add(f);
				}
				Collections.sort(crlist, new CompRouteComparator() );

				//select to N elem and assign prob=1.0 to them
				System.out.println("     Routes to be set to 1.0");
				HashSet<String> outset = new HashSet<String>();
				for(int i=0; i<1000 && outset.size() < 50; i++){
					outset.add(crlist.get(i).r1.getKey());
					outset.add(crlist.get(i).r2.getKey());
					System.out.println("       " + crlist.get(i).getKey());
				}
				//output results
				Main.writeRoutesSetToOne(Main.ROUTES_SET_TO_1_FILE,outset);
			}//diver not processed yet
		}//next drv
	}

	private static void mergeResults(){
		if(GEOM_SIMILAR_ROUTES != null){
			for(Integer drv : GEOM_SIMILAR_ROUTES.keySet()){
				HashSet<Integer> geomsim = GEOM_SIMILAR_ROUTES.get(drv);
				for(Integer rt : geomsim){
					String key = drv + "_" + rt;
					double actProb = ROUTELEVEL_RESULT.get(key).doubleValue();
					if(actProb > 0.6){
						ROUTELEVEL_RESULT.put(key, new Double(1.0));
					}
				}//next route of drv
			}//next drv in GEOM_SIMILAR_ROUTES
		}//GEOM_SIMILAR_ROUTES != null
		//submit
		submit(SUBMISSION_FILE, ROUTELEVEL_RESULT);
		System.out.println("Main.mergeResults finished");
	}

	private static void solveGeomSimilarity(){
		GEOM_SIMILAR_ROUTES = new HashMap<Integer,HashSet<Integer>>();
		GEOMSIM_RESULT = new HashMap<String, Double>();
		for(Integer drv : driver_to_folder.keySet()){
			findClosestRoutes(drv);
		}
		//to csv
		submit(GEOMSIM_FILE, GEOMSIM_RESULT);
	}

	private static void findClosestRoutes(Integer drvToDump){
	   File drvfldr = Main.driver_to_folder.get(drvToDump);
		HashMap<Integer,Route> routes = new HashMap<Integer,Route>();
		ArrayList<ObjectDoublePair<RouteCompResult>> compres = new ArrayList<ObjectDoublePair<RouteCompResult>>();
		/*
		Main.CREATE_FIXLEN_ROUTE = true;
		Main.CREATE_SIMPLE_ROUTE_PATTERNS = true;
		*/

		String sss = "";
		//read routes
		try{
			for(File fileEntry : drvfldr.listFiles()){
				if (!fileEntry.isDirectory()) {
					sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);
					Route r = new Route(drvToDump, routeId, fileEntry);
					routes.put(routeId, r);
				}
			}
		} catch(Exception e) {
			System.out.println("Main.findClosestRoutes: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }

		//calculate pairwise distances
		SimpleTrajectoryDistance std = new SimpleTrajectoryDistance( new EuclidianMetric() );
		int cnt = 0;
		for(Integer rt1 : routes.keySet()){
			for(Integer rt2 : routes.keySet()){
				if( rt1.intValue() < rt2.intValue() ){
					Route r1 = routes.get(rt1);
					Route r2 = routes.get(rt2);
					double len1 = r1.getLenMeter();
					double len2 = r2.getLenMeter();
					double fixlenDst = std.getDistance(r1.getFixLenCoords(), r2.getFixLenCoords() );

					RouteCompResult rcr = new RouteCompResult(drvToDump,rt1,rt2,len1,len2,fixlenDst);
					compres.add( new ObjectDoublePair<RouteCompResult>( rcr,rcr.getWeight() ) );
					cnt++;
					/*
					if(cnt % 1000 == 0){
						System.out.println("cnt=" + cnt);
					}
					*/
				}//calc to be done
			}//next rt2
		}//next rt1
		//System.out.println("calculation done, results written to the output");

		//sort and get first routes
		//int routeNum=GEOM_SIMILAR_TO_TAKE;
		Collections.sort(compres,new ObjectDoublePairSorter());
		HashSet<Integer> firstRoutes = new HashSet<Integer>();
		//System.out.println("first 10 relative distances:");
		double lastRelDist = 0.0;
		cnt = 0;
		while(lastRelDist <= GEOM_SIMILAR_TOLERANCE){
			RouteCompResult rcr = compres.get(cnt).obj;
			lastRelDist = rcr.getWeight();
			firstRoutes.add(rcr.routeId1);
			firstRoutes.add(rcr.routeId2);
			cnt++;
			/*
			if(i<10){
				System.out.println(rcr.toString());
			}
			*/
		}
		/*
		System.out.println("first " + routeNum + " pairs contain " + firstRoutes.size() + " routes:");
		String ss = "";
		for(Integer rt : firstRoutes){
			ss += (rt + ",");
		}
		System.out.println(ss);
		*/
		GEOM_SIMILAR_ROUTES.put(drvToDump, firstRoutes);
		for(Integer r : firstRoutes){
			GEOMSIM_RESULT.put(drvToDump +"_"+r, new Double(1.0));
		}
		System.out.println("drv=" + drvToDump + " -> " + firstRoutes.size() + " similar routes, "
					+ "last rel.dist=" + Util.dts(lastRelDist) );
   }

	private static void writeRoutesSetTo1(){

	}

	private static void solveRouteLevel(){
		//Walk through the drivers one by one
		int drvcnt = 0;
		for(Integer drv : driver_to_folder.keySet()){
			//System.out.println("   driver=" + drv);

			//get the TRUE observations
			File trueFolder = driver_to_folder.get(drv);
			ArrayList<Route> tr = Main.processFolder(drv, trueFolder);
			//System.out.println("     tr obtained");

			//setup a new route evaluator for this driver
			//RouteEvaluator reval = new RouteEvaluator(drv.intValue(), tr, fr, new SpeedDynamicsExtractor());
			//RouteEvaluator reval = new RouteEvaluator(drv.intValue(), tr, fr, new NoPatternExtractor());
			//RouteEvaluator reval = new RouteEvaluator(drv.intValue(), tr, fr, new SimplePatternExtractor());
			//RouteEvaluator reval = new RouteEvaluator(drv.intValue(), new AfterStopExtractor());
			//RouteEvaluator reval = new RouteEvaluator(drv.intValue(), new NumerousVelocitiesExtractor());
			//RouteEvaluator reval = new RouteEvaluator(drv.intValue(), new NoPatternExtractor());
			RouteEvaluator reval = new RouteEvaluator(drv.intValue(), new CombinedExtractor());

			//get the FALSE observations  -- iterate LOGREG_RETRIAL_NUM times
			//       with a new set of opponents every time
			for(int rtr=0; rtr<RouteEvaluator.LOGREG_RETRIAL_NUM; rtr++){
				ArrayList<Route> fr = new ArrayList<Route>();
				Integer[] falseDrvs = Main.getPermutation(driver_to_folder.keySet());
				int cnt = 0;
				int idx = 0;
				while(cnt < RouteEvaluator.LOGREG_OPPONENT_NUM){
					Integer fDrv = falseDrvs[idx];
					if(fDrv.intValue() != drv.intValue()){
						File falseFolder = driver_to_folder.get(fDrv);
						fr.addAll(Main.processFolder(fDrv, falseFolder));
						cnt++;
					}//endif fDrv != drv
					idx++;
				}//wend next fDrv
				//System.out.println("     fr obtained");

				//Evaluate
				reval.calcEstimates(tr, fr);
			}//next rtr = next set of opponents

			long duration = (System.currentTimeMillis() - startTime) / 60000;
			System.out.println("   driver=" + drv + " = " + drvcnt + "/" + driver_to_folder.size() + " -> " + duration + " mins, sump=" + Util.dts(reval.getSumPreds()));

			//finally submit
			submit(SUBMISSION_FILE, reval);
			//System.out.println("     line submitted");
			drvcnt++;
		}//next drv

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 60000;
		System.out.println("solveRouteLevel :: duration = " + duration + " min");
	}

	public static void readFolders(String folderName){
		driver_to_folder = new HashMap<Integer,File>();

		try{
			int cnt = 0;
			File folder = new File(folderName);
			for(File fileEntry : folder.listFiles()){

				if (fileEntry.isDirectory()) {
					Integer key = new Integer(fileEntry.getName());
					driver_to_folder.put(key, fileEntry);
					cnt++;
				}
			}//next fileEntry
			System.out.println("Drivers: " + cnt + " drivers to be processed.");
		} catch(Exception e) {
		 	e.printStackTrace();
	    }
	}

	public static ArrayList<Route> processFolder(Integer key, File folderName){
		ArrayList<Route> ret = new ArrayList<Route>();
		try{
			for(File fileEntry : folderName.listFiles()){
				if (!fileEntry.isDirectory()) {
					String sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);
					Route rt = new Route(key, routeId, fileEntry);
					ret.add(rt);
				}
			}
		} catch(Exception e) {
			System.out.println("Drivers.processFolder: key=" + key + ", folderName=" + folderName.getName());
		 	e.printStackTrace();
	    }
		return ret;
	}

	/*
	public static File createFile(String targetFileName, String header){
		try{
			File ret = new File(targetFileName);
			PrintWriter writer = new PrintWriter(ret, "UTF-8");
			writer.println(header);
			writer.close();
			return ret;
		} catch(Exception e){
			return null;
		}
	}
	*/

	private static void readProcessedRoutes(File inFile, String sep){
		ROUTES_SET_TO_1 = new HashSet<String>();
		DRIVERS_PROCESSED = new HashSet<Integer>();
		try{
			BufferedReader input = new BufferedReader(new FileReader(inFile));
			String line = input.readLine(); //drop file header
			int cnt = 0;
			while (( line = input.readLine()) != null){
				String[] drv_route = line.split("_");
				Integer drv = new Integer(drv_route[0]);
				ROUTES_SET_TO_1.add(line);
				DRIVERS_PROCESSED.add(drv);
				cnt++;
			}
			System.out.println(cnt + " distances for " + DRIVERS_PROCESSED.size() +
					" drivers read from " + inFile.getName());
			input.close();
		}catch(Exception e){
			System.out.println("ROUTES_SET_TO_1 could not be opened!");
			e.printStackTrace();
		}
	}

	public static HashMap<String,Double> readPreviousResult(String inFileName, String sep){
		HashMap<String,Double> ret = new HashMap<String,Double>();
		try{
			BufferedReader input = new BufferedReader(new FileReader(new File(inFileName)));
			String line = input.readLine(); //drop file header
			int cnt = 0;
			while (( line = input.readLine()) != null){
				String[] sv = line.split(sep);
				String key = sv[0];
				Double prob = new Double(sv[1]).doubleValue();
				ret.put(key, prob);
				cnt++;
			}
			System.out.println(cnt + " estimates read from " + inFileName);
			input.close();
		} catch(Exception e){
			System.out.println("ROUTELEVEL_INPUTFILE could not be opened!");
			e.printStackTrace();
		}
		return ret;
	}

	public static void writeRoutesSetToOne(File targetFile, HashSet<String> routeKeys){
		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(targetFile, true));
			for(String s : routeKeys){
				writer.println(s);
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void submit(File targetFile, RouteEvaluator reval){
		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(targetFile, true));
			ArrayList<String> as = reval.getSubmission();
			for(String s : as){
				writer.println(s);
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void submit(File targetFile, HashMap<String,Double> results){
		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(targetFile, true));
			for(String key : results.keySet()){
				writer.println(key + "," + Util.dtsSubmit(results.get(key).doubleValue()));
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Integer[] getPermutation(Collection<Integer> inSeq){
		ArrayList<Integer> lst = new ArrayList<Integer>(inSeq);
		Collections.shuffle(lst);
		Integer[] ret = new Integer[inSeq.size()];
		int cnt = 0;
		for(Integer i : lst){
			ret[cnt] = i;
			cnt++;
		}
		return ret;
	}
}
