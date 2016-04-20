package dta;

public class SimpleTrajectoryDistance {

	private Metric metric;

	private double[][] r1orig;
	private double[][] r2orig;

	public SimpleTrajectoryDistance(Metric inMetric){
		metric = inMetric;
	}

	public double getDistance(double[][] a, double[][] b){
		r1orig = a;
		r2orig = b;

		double dv0 = calcv0(r1orig,r2orig);
		double dv1 = calcv1(r1orig,r2orig);
		return (dv0 < dv1 ? dv0 : dv1);
	}

	private double calcv0(double[][] a, double[][] b){
		double[][] r1 = Util.alignToX(a);
		double[][] r2 = Util.alignToX(b);
		double[][] r2rot = Util.mirrorOnX(r2);

		double d0 = getNaiveDistance(r1,r2);
		double d1 = getNaiveDistance(r1,r2rot);

		//System.out.println("STD.calcv0 :: d0=" + Util.dts(d0) + ", d1=" + Util.dts(d1) );

		return (d0 < d1 ? d0 : d1);
	}

	private double calcv1(double[][] a, double[][] b){
		double[][] r1 = Util.alignToX(a);
		double[][] r2 = Util.flip(Util.alignToX(b));
		double[][] r2rot = Util.mirrorOnX(r2);

		double d0 = getNaiveDistance(r1,r2);
		double d1 = getNaiveDistance(r1,r2rot);

		//System.out.println("STD.calcv1 :: d0=" + Util.dts(d0) + ", d1=" + Util.dts(d1) );

		return (d0 < d1 ? d0 : d1);
	}

	private double getNaiveDistance(double[][] r1, double[][] r2){
		int en = (r1.length > r2.length ? r2.length : r1.length);
		double ret = 0.0;
		int maxDiffIdx = 0;
		for(int i=0; i<en; i++){
			double y1 = (i >= r1.length ? r1[r1.length-1][1] : r1[i][1]);
			double y2 = (i >= r2.length ? r2[r2.length-1][1] : r2[i][1]);
			double x1 = (i >= r1.length ? r1[r1.length-1][0] : r1[i][0]);
			double x2 = (i >= r2.length ? r2[r2.length-1][0] : r2[i][0]);
			//double d = Math.abs(y2 - y1);
			double d = metric.getDistance(new double[]{x1,y1}, new double[]{x2,y2});
			//ret = (d > ret ? d : ret);
			if(d > ret){
				ret = d;
				maxDiffIdx = i;
			}
		}
		//System.out.println("STD.getNaiveDistance :: ret=" + Util.dts(ret) + ", maxDiffIdx=" + maxDiffIdx );
		return ret;
	}
}
