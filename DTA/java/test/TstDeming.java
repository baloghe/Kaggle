package test;

import dta.*;

public class TstDeming {

	public static void main(String[] args) {
		testSimple();
		testHoriz();
		testVert();
		testBookExample();
		testXlsExample();
	}
	
	public static void testSimple(){
		double[][] pts = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{0.0,2.0}
				,new double[]{1.0,1.0}
				,new double[]{1.0,3.0}
				,new double[]{2.0,2.0}
				,new double[]{2.0,4.0}
				,new double[]{3.0,3.0}
				,new double[]{3.0,5.0}
				,new double[]{4.0,4.0}
				,new double[]{4.0,6.0}
				,new double[]{5.0,5.0}
				,new double[]{5.0,7.0}
			};
		Deming dmg = new Deming(pts);
		double[] est = dmg.getEstimate();
		System.out.println("testSimple :: Beta = " + Util.dblVecToString(est));
	}
	
	public static void testHoriz(){
		double[][] pts = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{0.0,2.0}
				,new double[]{1.0,0.0}
				,new double[]{1.0,2.0}
				,new double[]{2.0,0.0}
				,new double[]{2.0,2.0}
				,new double[]{3.0,0.0}
				,new double[]{3.0,2.0}
			};
		Deming dmg = new Deming(pts);
		double[] est = dmg.getEstimate();
		System.out.println("testHoriz :: Beta = " + Util.dblVecToString(est));
	}
	
	public static void testVert(){
		double[][] pts = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{2.0,0.0}
				,new double[]{0.0,2.0}
				,new double[]{2.0,2.0}
				,new double[]{0.0,4.0}
				,new double[]{2.0,4.0}
				,new double[]{0.0,6.0}
				,new double[]{2.0,6.0}
			};
		Deming dmg = new Deming(pts);
		double[] est = dmg.getEstimate();
		System.out.println("testVert :: Beta = " + Util.dblVecToString(est));
	}
	
	public static void testBookExample(){
		double[][] pts = new double[][]{
				 new double[]{7.0,7.9}
				,new double[]{8.3,8.2}
				,new double[]{10.5,9.6}
				,new double[]{9.0,9.0}
				,new double[]{5.1,6.5}
				,new double[]{8.2,7.3}
				,new double[]{10.2,10.2}
				,new double[]{10.3,10.6}
				,new double[]{7.1,6.3}
				,new double[]{5.9,5.2}
			};
		Deming dmg = new Deming(pts);
		double[] est = dmg.getEstimate();
		System.out.println("testBookExample :: Beta = " + Util.dblVecToString(est));
	}
	
	public static void testXlsExample(){
		double[][] pts = new double[][]{
				 new double[]{0.0,0.0}
				,new double[]{10.0,10.0}
				,new double[]{20.0,10.0}
				,new double[]{30.0,30.0}
				,new double[]{40.0,20.0}
				,new double[]{50.0,20.0}
			};
		Deming dmg = new Deming(pts);
		double[] est = dmg.getEstimate();
		System.out.println("testXlsExample :: Beta = " + Util.dblVecToString(est));
	}

}
