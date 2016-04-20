package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import dta.*;

public class TstLogRegFeatures {

	public static String RTRF_PFX;
	public static String header = 
			"drv,route,out,sp1,sp2,sp3,sp4,sp5,sp6,sp7,sp8,sp9,sp10,"
			+ "ro1,ro2,ro3,ro4,ro5,ro6,ro7,ro8,ro9,ro10,"
			+ "ac1,ac2,ac3,ac4,ac5,ac6,ac7,ac8,ac9,ac10,ar1,ar2,ar3,ar4,ar5,ar6,ar7,ar8,ar9,ar10,"
			+ "la1,la2,la3,la4,la5,la6,la7,la8,la9,la10,"
			+ "gp1,gp2,gp3,gp4,gp5,gp6,gp7,gp8,gp9,gp10,gp11,gp12,gp13,"
			+ "as1,as2,as3,as4,as5,"
			+ "l21,l22,l23,l24,l25,l26,l27,l28,l29,l210,"
			+ "l31,l32,l33,l34,l35,l36,l37,l38,l39,l310,"
			+ "g1,g2,g3,g4,g5,g6,g7,g8,g9,g10,"
			+ "AvgSp,StdSp,IdleR,BeeR,DimR"
			;
	public static int counter;

	public static void main(String[] args) {
		Main.init();

		RTRF_PFX = System.getProperty("user.name").equalsIgnoreCase("baloghend") ? "../data/" : "";
		
		counter = 0;

		/*
		dumpFeaturesSingleDriver(
				new Integer(1)
				,new Integer[]{new Integer(2) , new Integer(3) , new Integer(100) , new Integer(101)}
				,new NumerousVelocitiesExtractor()
				,RTRF_PFX + "rtrf/feat.1.csv"
				,"drv,route,out,sp1,sp2,sp3,sp4,sp5,sp6,sp7,ro1,ro2,ro3,ro4,ro5,ro6,ro7,"
					+ "ac1,ac2,ac3,ac4,ac5,ac6,ac7,ar1,ar2,ar3,ar4,ar5,ar6,ar7,"
					+ "la1,la2,la3,la4,la5,la6,la7,gp1,gp2,gp3,gp4,gp5,gp6,gp7,"
					+ "gp8,gp9,gp10,gp11,gp12,gp13,AvgSp,StdSp,IdleR,BeeR,DimR"
				);

		*/
/*
		dumpFeaturesSingleDriver(
				new Integer(1343)
				,new Integer[]{new Integer(2) , new Integer(3) , new Integer(100) , new Integer(101)}
				,new CombinedExtractor()
				,RTRF_PFX + "rtrf/feat.1.csv"
				,header
				);
*/

		generalDumper();

		System.out.println("test finished");
	}

	public static void generalDumper(){

		for(Integer drv : Main.driver_to_folder.keySet()){
			Integer[] fdrv = new Integer[RouteEvaluator.LOGREG_OPPONENT_NUM];

			Integer[] falseDrvs = Main.getPermutation(Main.driver_to_folder.keySet());
			int cnt = 0;
			int idx = 0;
			while(cnt < RouteEvaluator.LOGREG_OPPONENT_NUM){
				fdrv[cnt] = falseDrvs[idx];
				if(fdrv[cnt].intValue() != drv.intValue()){
					cnt++;
				}//endif fDrv != drv
				idx++;
			}

			String fname = RTRF_PFX + "rtrf/feat." + drv.intValue() + ".csv";

			dumpFeaturesSingleDriver(
					drv
					,fdrv
					,new CombinedExtractor()
					,fname
					,header
					);
		}//next drv

	}

	public static void dumpFeaturesSingleDriver(Integer inTrueDrv, Integer[] inFalseDrivers
			, FeatureExtractor inFext, String outFName, String inHeader){

		//System.out.println("Process true=" + inTrueDrv );
		File trueFolder = Main.driver_to_folder.get(inTrueDrv);
		ArrayList<Route> tr = Main.processFolder(inTrueDrv, trueFolder);
		//RouteEvaluator reval = new RouteEvaluator(inTrueDrv.intValue(), inFext);

		ArrayList<Route> fr = new ArrayList<Route>();
		int cnt = 0;
		while(cnt < inFalseDrivers.length){
			Integer fDrv = inFalseDrivers[cnt];
			//System.out.println("Process false=" + fDrv );
			File falseFolder = Main.driver_to_folder.get(fDrv);
			fr.addAll(Main.processFolder(fDrv, falseFolder));
			cnt++;
		}

		ArrayList<Route> trainset = new ArrayList<Route>();
		Collections.shuffle(tr);
		Collections.shuffle(fr);
		trainset.addAll(tr.subList(0, RouteEvaluator.LOGREG_CHOSEN_ROUTE_NUM));
		trainset.addAll(fr.subList(0, RouteEvaluator.LOGREG_CHOSEN_ROUTE_NUM * inFalseDrivers.length));
		Collections.shuffle(trainset);

		File outFile = Util.createFile(outFName, inHeader);
		int recnum = 0;
		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(outFile, true));
			for(Route rt : trainset){
				double[] fs = inFext.extractFeatures(rt);
				boolean isTrue = ( rt.getDriver() == inTrueDrv);
				String outLine = rt.getKey().replace("_", ",") + "," + (isTrue ? "1" : "0") + "," + Util.dblVecToString(fs);
				writer.println(outLine);
				recnum++;
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		counter++;
		System.out.println(counter + " :: " + recnum + " records written to " + outFile.getName() );

	}

}
