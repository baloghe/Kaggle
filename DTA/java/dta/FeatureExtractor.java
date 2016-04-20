package dta;

public interface FeatureExtractor {
	public int getFeatureNum();
	public int getLabelNum();
	public double[] extractLabels(Route rt);
	public double[] extractFeatures(Route rt);
}
