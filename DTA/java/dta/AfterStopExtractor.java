package dta;

import java.util.ArrayList;

public class AfterStopExtractor implements FeatureExtractor {

	public static int FEAT_NUM;
	public static final int LAB_NUM = 1;

	public static int AFTER_STOP_MAX_SEC = 15;

	public AfterStopExtractor(){
		FEAT_NUM = Route.SPEED_NTILES_NUM + Route.ROTATION_NTILES_NUM + Route.ACCEL_NTILES_NUM
				 + 5
				 + REAfterStop.VELOCITIES_TO_HIT.length
				 ; //+1 for selfCross to be added!
	}

	public int getFeatureNum(){return FEAT_NUM;}
	public int getLabelNum(){return LAB_NUM;}

	public double[] extractFeatures(Route rt){
		double[] ret = new double[FEAT_NUM];
		//obtain features...
		//Speed/Rot/Accel_Quantiles,
		//avgSpeed, stdSpeed, idleRatio, lenMeter, dimRatio, beeLine //, selfCrossNum
		int pos=0;
		double[] s = rt.getSpeedNTiles();
		for(int i=0;i<Route.SPEED_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getRotationNTiles();
		for(int i=0;i<Route.ROTATION_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getAccelNTiles();
		for(int i=0;i<Route.ACCEL_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		ret[pos] = rt.getAvgSpeed(); pos++;
		ret[pos] = rt.getStdSpeed(); pos++;
		ret[pos] = rt.getIdleRatio(); pos++;
		ret[pos] = rt.getBeeLine() / rt.getLenMeter(); pos++;
		ret[pos] = rt.getDimRatio(); pos++;


		s = searchAfterStop(rt,true);
		for(int i=0;i<REAfterStop2.SECS_TO_MEASURE.length;i++){
			ret[pos] = s[i];
			pos++;
		}


		//System.out.println(Util.dblVecToString(ret));

		//return them
		return ret;
	}

	private double[] searchAfterStop(Route r, boolean silent){

		//ami kell:
		//  hossz
		//  végsebesség
		//  ebből: gyorsult-e 10 m/s-re? 20, 30 m/s
		//    ha igen: hány mp alatt?

		ArrayList<REAfterStop2> asts = new ArrayList<REAfterStop2>();

		RouteElement lagRe = null;
		REAfterStop2 actElem = null;
		int recnt = 0;
		int seccnt = 0;
		for(RouteElement re : r.getElems()){
			if(lagRe == null){
				lagRe = re;
				//System.out.println(seccnt + ": lagRe == null  => lagRe := " + lagRe);
			} else {
				//after-stop condition met
				if(actElem==null && lagRe.vecLen()==0.0 && re.vecLen() != 0.0){
					actElem = new REAfterStop2(re,seccnt);
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
		double[] avgREAfterStop = new double[REAfterStop2.SECS_TO_MEASURE.length];
		double[] cnt = new double[REAfterStop2.SECS_TO_MEASURE.length];
		for(REAfterStop2 reas : asts){
			if(!silent) System.out.println(reas.toString());
			if(reas.lenSec() > 2){
				for(int j=0; j<avgREAfterStop.length; j++){
					avgREAfterStop[j] += reas.measuredVelocity()[j];
					if(reas.hitSec()[j]){
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

	public double[] extractLabels(Route rt){
		double[] ret = new double[LAB_NUM];
		//obtain features...

		//return them
		return ret;
	}
}
