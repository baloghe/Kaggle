package test;

import dta.*;

public class TstNTiles {

	public static void main(String[] args) {
		double[] data = new double[100];
		for(int i=0; i<data.length/2; i++){
			data[i] = (double)i * Math.random();
		}
		
		double[] nts = Util.calcNtiles(data, 10);
		System.out.println("nts[10]=" + dblArrToString(nts));
		
	}
	
	public static String dblArrToString(double[] arr){
		String ret =  "[" + Util.dts(arr[0]);
		for(int i=1; i<arr.length; i++){
			ret += (", " + Util.dts(arr[i]));
		}
		return ret + "]";
	}	

}
