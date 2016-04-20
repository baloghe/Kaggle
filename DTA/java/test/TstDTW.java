package test;

import dta.*;

public class TstDTW {
	
	public static void main(String[] args){
		//testDumb();
		//testSimilar();
		//smallDiff();
		//xlsPld();
		//xlsPld2();
		//tstEquidist();
		xlsPld3();
		
	}
	
	public static void testDumb(){
		double[][] r1 = new double[][]{
			new double[]{0,0}	
		};
		
		double[][] r2 = new double[][]{
				new double[]{0,0}	
			};
		
		DTW dt = new DTW( new EuclidianMetric() );
		System.out.println( "Distance = " + dt.getDistance(r1, r2, 5, 5) );
	}
	
	public static void testSimilar(){
		double[][] r1 = new double[][]{
				 new double[]{0,0}
				,new double[]{1,1}
				,new double[]{2,1}
				,new double[]{3,3}
				,new double[]{4,2}
				,new double[]{5,2}
				
			};
		
		double[][] r2 = new double[][]{
				 new double[]{0,0}
				,new double[]{1,1}
				,new double[]{2,1}
				,new double[]{3,3}
				,new double[]{4,2}
				,new double[]{5,2}
			};
		
		DTW dt = new DTW( new EuclidianMetric() );
		System.out.println( "Distance = " + dt.getDistance(r1, r2, 5, 5) );
	}
	
	public static void smallDiff(){
		double[][] r1 = new double[][]{
				 new double[]{0,0}
				,new double[]{1,1}
				,new double[]{2,1}
				,new double[]{3,3}
				,new double[]{4,2}
				,new double[]{5,2}
				
			};
		
		double[][] r2 = new double[][]{
				 new double[]{0,0}
				,new double[]{1,0}
				,new double[]{2,1}
				,new double[]{3,3}
				,new double[]{4,2}
				,new double[]{5,2}
			};
		
		DTW dt = new DTW( new EuclidianMetric() );
		System.out.println( "Distance = " + dt.getDistance(r1, r2, 5, 5) );
	}
	
	public static void xlsPld(){
		double[][] r1 = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{10.0,10.0}
				,new double[]{20.0,10.0}
				,new double[]{30.0,30.0}
				,new double[]{40.0,20.0}
				,new double[]{50.0,20.0}
				
			};
		
		double[][] r2 = new double[][]{
				 new double[]{8.0,-5.0}
				,new double[]{11.6602540378,8.6602540378}
				,new double[]{20.3205080757,13.6602540378}
				,new double[]{18.9807621135,35.9807621135}
				,new double[]{32.6410161514,32.3205080757}
				,new double[]{41.3012701892,37.3205080757}
			};
		
		DTW dt = new DTW( new EuclidianMetric() );
		System.out.println( "Distance = " + dt.getDistance(r1, r2, 5, 5) );
	}
	
	public static void xlsPld2(){
		double[][] r1 = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{10.0,10.0}
				,new double[]{20.0,10.0}
				,new double[]{30.0,30.0}
				,new double[]{40.0,20.0}
				,new double[]{50.0,20.0}
				
			};
		
		double[][] r2 = new double[][]{
				 new double[]{41.3012701892,37.3205080757}
				,new double[]{32.6410161514,32.3205080757}
				,new double[]{18.9807621135,35.9807621135}
				,new double[]{20.3205080757,13.6602540378}
				,new double[]{11.6602540378,8.6602540378}
				,new double[]{8.0,-5.0}
			};
		
		DTW dt = new DTW( new EuclidianMetric() );
		System.out.println( "Distance = " + dt.getDistance(r1, r2, 5, 5) );
	}
	
	public static void tstEquidist(){
		double[] s = new double[]{0.0,0.0};
		double[] e = new double[]{6.0,6.0};
		int snum = 3;
		
		System.out.println(
				Util.dblVecToString(s) + " ->(" + snum + ")-> " + Util.dblVecToString(e)
				);
		System.out.println(Util.dblMatToString(Util.sliceEquidist(s,e,snum)));
		
		
		s = new double[]{-3.0,-3.0};
		System.out.println(
				Util.dblVecToString(s) + " ->(" + snum + ")-> " + Util.dblVecToString(e)
				);
		System.out.println(Util.dblMatToString(Util.sliceEquidist(s,e,snum)));
		
		s = new double[]{-1.0,-1.0};
		e = new double[]{1.0,1.0};
		snum = 2;
		System.out.println(
				Util.dblVecToString(s) + " ->(" + snum + ")-> " + Util.dblVecToString(e)
				);
		System.out.println(Util.dblMatToString(Util.sliceEquidist(s,e,snum)));
		
		s = new double[]{-1.0,-1.0};
		e = new double[]{1.0,1.0};
		snum = 1;
		System.out.println(
				Util.dblVecToString(s) + " ->(" + snum + ")-> " + Util.dblVecToString(e)
				);
		System.out.println(Util.dblMatToString(Util.sliceEquidist(s,e,snum)));
		
		s = new double[]{-1.0,-1.0};
		e = new double[]{1.0,1.0};
		snum = 6;
		System.out.println(
				Util.dblVecToString(s) + " ->(" + snum + ")-> " + Util.dblVecToString(e)
				);
		System.out.println(Util.dblMatToString(Util.sliceEquidist(s,e,snum)));
	}
	
	public static void xlsPld3(){
		System.out.println("Triangle area=" + Util.calcTriangleArea(new double[]{0.0,0.0}, new double[]{10.0,50.0}, new double[]{20.0,20.0}));
		
		double[][] r1 = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{2.0,2.0}
				,new double[]{10.0,10.0}
				,new double[]{12.0,10.0}
				,new double[]{20.0,10.0}
				,new double[]{30.0,30.0}
				,new double[]{40.0,20.0}
				,new double[]{48.0,20.0}
				,new double[]{50.0,20.0}
				
			};
		
		double[][] r2 = new double[][]{
				 new double[]{41.3012701892,37.3205080757}
				,new double[]{32.6410161514,32.3205080757}
				,new double[]{18.9807621135,35.9807621135}
				,new double[]{20.3205080757,13.6602540378}
				,new double[]{11.6602540378,8.6602540378}
				,new double[]{8.0,-5.0}
			};
		
		DTW dt = new DTW( new EuclidianMetric() );
		System.out.println( "Distance = " + dt.getDistance(r1, r2, 5, 5, 2.0) );
	}

}
