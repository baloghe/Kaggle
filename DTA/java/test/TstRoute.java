package test;

import dta.*;

import java.io.File;
import java.util.ArrayList;

public class TstRoute {

	public static void main(String[] args){
		Main.init();
		//tstSimple();
		tstFixLen();
	}

	public static void tstSimple(){
		File f = new File("datatst/tstroute.csv");
		Route r = new Route(1, 1, f);
		Route r2 = new Route(2, 1, f);
		System.out.println(r.statsToString());
		System.out.println("Speed: " + Util.dblVecToString(r.getSpeedNTiles()) );
		System.out.println("Accel: " + Util.dblVecToString(r.getAccelNTiles()) );
		System.out.println("Rota: " + Util.dblVecToString(r.getRotationNTiles()) );

		ArrayList<Route> tr = new ArrayList<Route>(); tr.add(r);
		ArrayList<Route> fr = new ArrayList<Route>(); fr.add(r2);

		RouteEvaluator reval = new RouteEvaluator(1,new NoPatternExtractor() );
		reval.calcEstimates(tr,fr);
	}

	public static void tstFixLen(){
		Main.CREATE_FIXLEN_ROUTE = true;
		Main.CREATE_SIMPLE_ROUTE_PATTERNS = false;


		File f = new File("datatst/tstrouteFixLen.csv");
		Route r = new Route(1, 1, f);

		System.out.println("orig Coords:" );
		System.out.println(Util.dblMatToString(r.getCoords()) );
		System.out.println("fixLenCoords:" );
		System.out.println(Util.dblMatToString(r.getFixLenCoords()) );
	}

}
