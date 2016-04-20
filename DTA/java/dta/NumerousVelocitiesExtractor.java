package dta;

public class NumerousVelocitiesExtractor implements FeatureExtractor{

	public static int FEAT_NUM;
	public static final int LAB_NUM = 1;

	public NumerousVelocitiesExtractor(){
		FEAT_NUM = Route.SPEED_NTILES_NUM
		        + Route.ROTATION_NTILES_NUM
		        + Route.ACCEL_NTILES_NUM  /* accel() */
				+ Route.ACCEL_NTILES_NUM  /* accel2() */
		        + Route.ACCEL_NTILES_NUM  /* lataccel() */
		        + Route.GPAREAS.length
				+ 5; //+1 for selfCross to be added!
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
		s = rt.getAccel2NTiles();
		for(int i=0;i<Route.ACCEL_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getLatAccelNTiles();
		for(int i=0;i<Route.ACCEL_NTILES_NUM;i++){
			ret[pos] = s[i];
			pos++;
		}
		s = rt.getGPlotRatios();
		for(int i=0;i<Route.GPAREAS.length;i++){
			ret[pos] = s[i];
			pos++;
		}
		ret[pos] = rt.getAvgSpeed(); pos++;
		ret[pos] = rt.getStdSpeed(); pos++;
		ret[pos] = rt.getIdleRatio(); pos++;
		ret[pos] = rt.getBeeLine() / rt.getLenMeter(); pos++;
		ret[pos] = rt.getDimRatio(); pos++;
		//ret[pos] = rt.getBeeLine();

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
