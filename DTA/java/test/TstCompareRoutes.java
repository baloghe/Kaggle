package test;

import java.util.ArrayList;

import dta.*;

public class TstCompareRoutes {

	/*
	 examples of similar trips for driver 1 are: ??

	 */

	public static void main(String[] args){
		tstSimple();
		//oneDriver( new Integer(1) );
	}
	
	public static void tstSimple(){
		Main.init();
		Integer drv = new Integer(1);
		ArrayList<Route> rts = Main.processFolder(drv, Main.driver_to_folder.get(drv));

		int key1 = 49, key2 = 190;
		Route r1=null, r2=null;
		for(Route r : rts){
			if(r.getKey().equalsIgnoreCase(drv.toString() + "_" + key1)) r1 = r;
			if(r.getKey().equalsIgnoreCase(drv.toString() + "_" + key2)) r2 = r;
		}
		System.out.println("r1=" + r1.statsToString());
		System.out.println("r2=" + r2.statsToString());

		SimpleTrajectoryDistance std = new SimpleTrajectoryDistance( new EuclidianMetric() );

		System.out.println("dist(r1,r2)=" + std.getDistance(r1.getCoords(), r2.getCoords() ) );
		System.out.println("distFixLen(r1,r2)=" + std.getDistance(r1.getFixLenCoords(), r2.getFixLenCoords() ) );
	}
	
	public static void oneDriver(Integer drv){
		
	}

}
