package test;

import java.io.File;
import java.util.ArrayList;

import dta.*;

public class TstAccelAfterStop {

	public static int AFTER_STOP_MAX_SEC = 15;

	public static void main(String[] args){
		Main.init();

		dumpDriver(new Integer(1));
		//singleRoute(new Integer(1),new Integer(127) );
	}

	public static void singleRoute(Integer drv, Integer inRouteId){
	   	tstAccelAfterStop(drv,inRouteId,false);
	}

	public static void dumpDriver(Integer drv){
		for(int i=1; i<= 200; i++){
			tstAccelAfterStop(drv, new Integer(i));
		}
	}

	public static void tstAccelAfterStop(Integer inDrv, Integer inRouteId){
		tstAccelAfterStop(inDrv, inRouteId, true);
	}

	public static void tstAccelAfterStop(Integer inDrv, Integer inRouteId, boolean silent){
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
		} catch(Exception e) {
			System.out.println("DriverDumper: sss=" + sss + ", drvfldr=" + drvfldr.getName());
		 	e.printStackTrace();
	    }

		System.out.println( inRouteId + " :: " + Util.dblVecToString(searchAfterStop(r,silent)) );
	}

	public static double[] searchAfterStop(Route r, boolean silent){

		//ami kell:
		//  hossz
		//  végsebesség
		//  ebből: gyorsult-e 10 m/s-re? 20, 30 m/s
		//    ha igen: hány mp alatt?

		ArrayList<REAfterStop> asts = new ArrayList<REAfterStop>();

		RouteElement lagRe = null;
		REAfterStop actElem = null;
		int recnt = 0;
		int seccnt = 0;
		for(RouteElement re : r.getElems()){
			if(lagRe == null){
				lagRe = re;
				//System.out.println(seccnt + ": lagRe == null  => lagRe := " + lagRe);
			} else {
				//after-stop condition met
				if(actElem==null && lagRe.vecLen()==0.0 && re.vecLen() != 0.0){
					actElem = new REAfterStop(re,seccnt);
					recnt = 1;
					//System.out.println(seccnt + ": case 1 -> new actElem created, lagRe=" + lagRe);
				} else if(actElem != null && re.vecLen() > lagRe.vecLen() && recnt < AFTER_STOP_MAX_SEC){
					//ongoing acceleration
					actElem.addRE(re);
					recnt++;
					//System.out.println(seccnt + ": case 2 -> re added to actElem");
				} else if(actElem != null){
					//acceleration finished or threshold met
					asts.add(actElem.copyRE());
					actElem = null;
					recnt = 0;
					//System.out.println(seccnt + ": case 3 -> actElem closed");
				}
			}
			seccnt++;
			lagRe = re;
		}//next RouteElement

		//dump results
		if(!silent) System.out.println("REAfterStop elements identified:");
		double[] avgREAfterStop = new double[REAfterStop.VELOCITIES_TO_HIT.length];
		double[] cnt = new double[REAfterStop.VELOCITIES_TO_HIT.length];
		for(REAfterStop reas : asts){
			if(!silent) System.out.println(reas.toString());
			if(reas.lenSec() > 6){
				for(int j=0; j<avgREAfterStop.length; j++){
					avgREAfterStop[j] += reas.hitSec()[j];
					if(reas.hitVelocity()[j]){
						cnt[j] += 1.0;
					}
				}
			}
		}

		for(int j=0; j<avgREAfterStop.length; j++){
			avgREAfterStop[j] /= cnt[j];
		}

		return avgREAfterStop;
	}
}
