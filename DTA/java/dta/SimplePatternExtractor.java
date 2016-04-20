package dta;

public class SimplePatternExtractor implements FeatureExtractor {
	
	public static int FEAT_NUM;
	public static final int LAB_NUM = 1;
	
	public SimplePatternExtractor(){
		FEAT_NUM = Route.SPEED_NTILES_NUM + Route.ROTATION_NTILES_NUM + Route.ACCEL_NTILES_NUM
				 + Route.TGSPEED_NTILES_NUM + Route.STDSPEED_NTILES_NUM
				 + 4; //+1 for selfCross to be added!
	}
	
	public int getFeatureNum(){return FEAT_NUM;}
	public int getLabelNum(){return LAB_NUM;}
	
	public double[] extractFeatures(Route rt){
		double[] ret = new double[FEAT_NUM];
		//obtain features...
		//Speed/Rot/Accel_Quantiles, 
		//avgSpeed, stdSpeed, idleRatio, lenMeter, dimRatio, beeLine //, selfCrossNum
		int pos=0;
		double[] s = rt.getSpeedNTilesPat();
		for(int i=0;i<Route.SPEED_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getTgSpeedNTilesPat();
		for(int i=0;i<Route.TGSPEED_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getStdSpeedNTilesPat();
		for(int i=0;i<Route.STDSPEED_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getRotationNTilesPat();
		for(int i=0;i<Route.ROTATION_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getAccelNTilesPat();
		for(int i=0;i<Route.ACCEL_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		ret[pos] = rt.getAvgSpeed(); pos++;
		ret[pos] = rt.getStdSpeed(); pos++;
		ret[pos] = rt.getIdleRatio(); pos++;
		ret[pos] = rt.getBeeLine() / rt.getLenMeter(); pos++;
		
		//System.out.println(Util.dblVecToString(ret));
		
		//return them
		return ret;
	}
	
	public double[] extractLabels(Route rt){
		double[] ret = new double[LAB_NUM];
		//obtain features...
		
		//return them
		return ret;
	}
}
