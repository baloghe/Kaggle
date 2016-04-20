package test;

import java.io.File;
import java.util.ArrayList;

import dta.*;


public final class TstGPlot{

	public static void main(String[] args) throws Exception{
		Main.init();

      	//singleRoute( new Integer(1), new Integer(136) );
      	//singleRoute( new Integer(3612), new Integer(105) );
      	singleRouteAreas( new Integer(1), new Integer(1) );
      	singleRouteAreas( new Integer(10), new Integer(1) );
      	singleRouteAreas( new Integer(100), new Integer(1) );
      	singleRouteAreas( new Integer(3612), new Integer(1) );
      }

	public static void singleRouteAreas(Integer inDrv, Integer inRouteId){
		File drvfldr = Main.driver_to_folder.get(inDrv);
		String sss = "";
		Route r = null;
		//read routes
		try{
			for(File fileEntry : drvfldr.listFiles()){
				if (!fileEntry.isDirectory()) {
					sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);
					if(routeId.intValue()==inRouteId.intValue()){
						r = new Route(inDrv, routeId, fileEntry);
						//routes.put(routeId, r);

						//System.out.println("Route loaded");
					}
				}
			}
			dumpGPlotArea(r);
		} catch(Exception e) {
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }
	}
	
	public static void singleRoute(Integer inDrv, Integer inRouteId){
		File drvfldr = Main.driver_to_folder.get(inDrv);
		String sss = "";
		Route r = null;
		//read routes
		try{
			for(File fileEntry : drvfldr.listFiles()){
				if (!fileEntry.isDirectory()) {
					sss = fileEntry.getName().replace(".csv", "");
					Integer routeId = new Integer(sss);
					if(routeId.intValue()==inRouteId.intValue()){
						r = new Route(inDrv, routeId, fileEntry);
						//routes.put(routeId, r);

						//System.out.println("Route loaded");
					}
				}
			}
			dumpGPlot(r);
		} catch(Exception e) {
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }
	}

	public static void dumpGPlot(Route r){
		for(RouteElement re : r.getElems()){
			System.out.println(Util.dblVecToString( re.gplot() ));
		}
	}
	
	public static void dumpGPlotArea(Route r){
		System.out.println(Util.dblVecToString( r.getGPlotRatios() ));
	}
}