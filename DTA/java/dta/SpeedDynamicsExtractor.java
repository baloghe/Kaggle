package dta;

public class SpeedDynamicsExtractor implements FeatureExtractor{
	
	public static int FEAT_NUM = Route.SPEED_NTILES_NUM;
	public static final int LAB_NUM = 1;
	
	public SpeedDynamicsExtractor(){}
	
	public int getFeatureNum(){return FEAT_NUM;}
	public double[] extractFeatures(Route rt){
		double[] ret = new double[FEAT_NUM];
		//obtain features...
		ret = rt.getSpeedNTiles();
		//return them
		return ret;
	}
	
	public int getLabelNum(){return LAB_NUM;}
	public double[] extractLabels(Route rt){
		double[] ret = new double[LAB_NUM];
		//obtain features...
		
		//return them
		return ret;
	}
}
