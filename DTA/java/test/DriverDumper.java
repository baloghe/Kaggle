package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

import dta.*;

/** class DriverDumper.
*/
public final class DriverDumper{

	public static File DUMPFILE;
	
	public static HashMap<Integer,Route> routes;
	public static ArrayList<ObjectDoublePair<RouteCompResult>> compres;

	public static void main(String[] args) throws Exception{
		Main.init();

		Integer drvToDump = new Integer(1);
		//dumpRawCoords( drvToDump );
		dumpPwDistances( drvToDump );
   }
   
   public static void dumpPwDistances(Integer drvToDump){
	   File drvfldr = Main.driver_to_folder.get(drvToDump);
		DUMPFILE = Util.createFile("pwdst_drv" + drvToDump + ".txt" , "driver,route1,route2,len1,len2,absDist");
		routes = new HashMap<Integer,Route>();
		compres = new ArrayList<ObjectDoublePair<RouteCompResult>>();
		Main.CREATE_FIXLEN_ROUTE = true;
		Main.CREATE_SIMPLE_ROUTE_PATTERNS = true;
		
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
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }
		System.out.println("routes for " + drvToDump + " read in...");
		
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
					dumpFixDists(rcr);
					cnt++;
					if(cnt % 1000 == 0){
						System.out.println("cnt=" + cnt);
					}
				}//calc to be done
			}//next rt2
		}//next rt1
		System.out.println("calculation done, results written to the output");
		
		//sort and get first routes
		int routeNum=50;
		Collections.sort(compres,new ObjectDoublePairSorter());
		HashSet<Integer> firstRoutes = new HashSet<Integer>();
		System.out.println("first 10 relative distances:");
		for(int i=0; i<routeNum; i++){
			RouteCompResult rcr = compres.get(i).obj;
			firstRoutes.add(rcr.routeId1);
			firstRoutes.add(rcr.routeId2);
			if(i<10){
				System.out.println(rcr.toString());
			}
		}
		System.out.println("first " + routeNum + " pairs contain " + firstRoutes.size() + " routes:");
		String ss = "";
		for(Integer rt : firstRoutes){
			ss += (rt + "->" + Util.dts(Main.ROUTELEVEL_RESULT.get(drvToDump+"_"+rt) ) + ",");
		}
		System.out.println(ss);
   }

   public static void dumpRawCoords(Integer drvToDump){
	   File drvfldr = Main.driver_to_folder.get(drvToDump);
		DUMPFILE = Util.createFile("routes_drv" + drvToDump + ".txt" , "driver,route,x,y");

		String sss = "";
		try{
			for(File fileEntry : drvfldr.listFiles()){
				if (!fileEntry.isDirectory()) {
					sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);
					ArrayList<double[]> coords = readCoords(fileEntry);
					dumpCoords(drvToDump, routeId, coords);
				}
			}
		} catch(Exception e) {
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }
   }
   
   private static ArrayList<double[]> readCoords( File fileEntry ){
      ArrayList<double[]> ret = new ArrayList<double[]>();
      try{
      	BufferedReader input = new BufferedReader(new FileReader(fileEntry));
		try{
			String line = input.readLine(); //drop file header
			int cnt = 0;
			while (( line = input.readLine()) != null){
				String[] sv = line.split( Main.DRIVER_COORD_SEPARATOR );
				double x = new Double(sv[0]).doubleValue();
				double y = new Double(sv[1]).doubleValue();
				ret.add(new double[]{x , y});
				cnt++;
			}
			return ret;
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			input.close();
			//System.out.println("Route " + this.routeKey + " = " + this.statsToString());
			//System.out.println("    " + Util.dblVecToString(this.accNTiles));
		}
      } catch(Exception e){
         e.printStackTrace();
      }

      return ret;
   }

   public static void dumpCoords(Integer driver, Integer routeID, ArrayList<double[]> coords){
      try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(DUMPFILE, true));
			String pfx = driver + ","+routeID;
			for(double[] p : coords){
			      String s = Util.dts(p[0]) + "," + Util.dts(p[1]);
				writer.println(pfx + "," + s);
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
   }
   
   public static void dumpFixDists(RouteCompResult rcr){
      try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(DUMPFILE, true));
			writer.println(rcr.toString());
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
   }

}