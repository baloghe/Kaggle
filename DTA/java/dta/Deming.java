package dta;

public class Deming {
	
	private double[][] points2d;
	private double delta;
	private double means[];
	
	public Deming(double[][] inPoints2d, double inDelta){
		points2d = inPoints2d;
		delta = inDelta;
	}
	
	public Deming(double[][] inPoints2d){
		points2d = inPoints2d;
		delta = 1.0;
	}
	
	public double[] getEstimate(){
		means = calcMeans();
		return doCalc();
	}
	
	public double[] getEstimate(double[] inMeans){
		means = inMeans;
		return doCalc();
	}
	
	private double[] doCalc(){
		double[][] sxy = calcSxy(means);
		
		
		double q1 = sxy[1][1] - delta * sxy[0][0];
		double slope = ( q1 + Math.sqrt(q1*q1 + 4*delta*sxy[1][0]*sxy[1][0]) ) / 2.0 / sxy[1][0];
				
		double fixpt = means[1] - slope * means[0];
		
		return new double[]{fixpt, slope};
	}
	
	private double[] calcMeans(){
		double xmean=0.0, ymean=0.0;
		for(int i=0; i<points2d.length; i++){
			xmean += points2d[i][0];
			ymean += points2d[i][1];
		}
		xmean /= (double)points2d.length;
		ymean /= (double)points2d.length;
		//System.out.println("  means=" + Util.dts(xmean) + ", " + Util.dts(ymean));
		
		return new double[]{xmean,ymean};
	}
	
	private double[][] calcSxy(double[] means){
		double sxx=0.0, sxy=0.0, syy=0.0;
		for(int i=0; i<points2d.length; i++){
			sxx += ( (points2d[i][0] - means[0]) * (points2d[i][0] - means[0]) );
			sxy += ( (points2d[i][0] - means[0]) * (points2d[i][1] - means[1]) );
			syy += ( (points2d[i][1] - means[1]) * (points2d[i][1] - means[1]) );
		}
		sxx /= (double)(points2d.length - 1);
		sxy /= (double)(points2d.length - 1);
		syy /= (double)(points2d.length - 1);
		//System.out.println("  sxx=" + Util.dts(sxx) + ", sxy=" + Util.dts(sxy)  + ", syy=" + Util.dts(syy));
		return new double[][]{
				 new double[]{sxx, sxy}
				,new double[]{sxy, syy}
			};
	}
}
