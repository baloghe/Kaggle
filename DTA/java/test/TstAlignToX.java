package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import java.util.ArrayList;

import dta.*;

public final class TstAlignToX
{

   public static File DUMPFILE;

   public static void main(String[] args) throws Exception {
	Main.init();

		Integer drvToDump = new Integer(1);
		Integer routeToDump = new Integer(2);

		//alignOneRoute( drvToDump, routeToDump );
		alignOneDriver( drvToDump );
   }

   public static void alignOneRoute( Integer drvToDump, Integer routeToDump ){
            Route rtToDump = null;
		File drvfldr = Main.driver_to_folder.get(drvToDump);
		DUMPFILE = Util.createFile("routes_drv" + drvToDump + "_route" + routeToDump + ".txt" , "driver,route,x,y");

		String sss = "";
		try{
			for(File fileEntry : drvfldr.listFiles()){
				if (!fileEntry.isDirectory()) {
					sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);
					if(routeId.intValue()==routeToDump.intValue()){
						rtToDump = new Route(drvToDump,routeToDump,fileEntry);
						double[][] alignedCoords = Util.alignToX( rtToDump.getCoords() );
						dumpCoords(drvToDump,routeToDump,alignedCoords);
					}
				}
			}
		} catch(Exception e) {
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }
   }

   public static void alignOneDriver( Integer drvToDump ){
            Route rtToDump = null;
		File drvfldr = Main.driver_to_folder.get(drvToDump);
		DUMPFILE = Util.createFile("alignedRoutes_drv" + drvToDump + ".txt" , "driver,route,x,y");

		String sss = "";
		try{
			for(File fileEntry : drvfldr.listFiles()){
				if (!fileEntry.isDirectory()) {
					sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);

					rtToDump = new Route(drvToDump,routeId,fileEntry);
					double[][] alignedCoords = Util.alignToX( rtToDump.getCoords() );
					dumpCoords(drvToDump,routeId,alignedCoords);

				}
			}
		} catch(Exception e) {
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }
   }

      public static void dumpCoords(Integer driver, Integer routeID, double[][] coords){
      try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(DUMPFILE, true));
			String pfx = driver + ","+routeID;
			for(int i=0; i<coords.length; i++){
			      String s = Util.dts(coords[i][0]) + "," + Util.dts(coords[i][1]);
				writer.println(pfx + "," + s);
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
   }

}