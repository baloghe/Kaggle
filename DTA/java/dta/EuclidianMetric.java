package dta;

public class EuclidianMetric implements Metric {

	public double getDistance(double[] a, double[] b){
		if(a==null || b==null)
			return 0.0;
		double ret = 0.0;
		for(int i=0; i<a.length && i<b.length; i++){
			ret += (a[i]-b[i])*(a[i]-b[i]);
		}
		return Math.sqrt(ret);
	}
	
}