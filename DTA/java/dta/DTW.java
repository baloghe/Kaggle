package dta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class DTW {
	
	private Metric metric;
	private int xTol;
	private int yTol;
	private double[][] r1orig;
	private double[][] r2orig;
	
	private class DPElem {
		public int i;
		public int j;
		public double value;
		public DPElem pred;
		public double area;
		
		public DPElem(int inI, int inJ, double inValue, DPElem inPred){
			i = inI;
			j = inJ;
			value = inValue;
			area = 0.0;
			pred = inPred;
		}
		
		public void setArea(double inArea){
			area = inArea;
		}
		
		public String getKey(){return i + "_" + j;}
		
		public String toString(){
			return "[" + i + ", " + j + " -> " 
					+ Util.dts(this.value) + ", " + Util.dts(this.area) + "]";
		}
	}
	
	public DTW(Metric inMetric){
		metric = inMetric;
	}
	
	public double getDistance(double[][] a, double[][] b, int inXTol, int inYTol){
		xTol = inXTol;
		yTol = inYTol;
		r1orig = a;
		r2orig = b;
		
		double dv0 = calcv0(r1orig,r2orig);
		double dv1 = calcv1(r1orig,r2orig);
		return (dv0 < dv1 ? dv0 : dv1);
	}
	
	public double getDistance(double[][] a, double[][] b, int inXTol, int inYTol, double inSliceUnit){
		r1orig = slice(a, inSliceUnit);
		r2orig = slice(b, inSliceUnit);
		xTol = Math.abs(r1orig.length - r2orig.length) + 2;
		yTol = xTol;
		if(xTol < inXTol) xTol = inXTol;
		if(yTol < inYTol) yTol = inYTol;
		/*
		System.out.println("Routes after being sliced up:");
		System.out.println(Util.dblMatToString(r1orig));
		System.out.println("");
		System.out.println(Util.dblMatToString(r2orig));
		*/
		double dv0 = calcv0(r1orig,r2orig);
		double dv1 = calcv1(r1orig,r2orig);
		return (dv0 < dv1 ? dv0 : dv1);
	}
	
	private double[][] slice(double[][] v, double inSliceUnit){
		if(v==null || v.length==0 || inSliceUnit <= 0.0)
			return null;
		
		//create intermediate variable
		int m = v[0].length;
		ArrayList<Double[]> arr = new ArrayList<Double[]>();
		//add route start
		Double[] st = new Double[m];
		for(int k=0; k<m; k++){
			st[k] = new Double(v[0][k]);
		}
		arr.add(st);
		//iterate through the route
		double[] prv = v[0];
		for(int i=1; i<v.length; i++){
			double[] act = v[i];
			double dist = this.metric.getDistance(act, prv);
			//perform slicing only if the distance is nonzero
			if(dist > 0.0){
				int sliceNum = (int) Math.floor(dist / inSliceUnit);
				//slicing makes sense only if there is at least two slices
				//System.out.println("   dist(" + Util.dblVecToString(prv) + ", " + Util.dblVecToString(act) + ")=" + Util.dts(dist) + ", sliceNum=" + sliceNum);
				if(sliceNum > 1){
					double[][] interiors = Util.sliceEquidist(prv, act, sliceNum);
					if(interiors != null){
						for(double[] point : interiors){
							Double[] dv = new Double[m];
							for(int k=0; k<m; k++){
								dv[k] = new Double(point[k]);
							}
							arr.add(dv);
						}//next interior point
					}//slicing resulted something meaningful
				}//slicenum was 1
				//add endpoint too -- only if the distance was bigger than 0.0
				Double[] dv = new Double[m];
				for(int k=0; k<m; k++){
					dv[k] = new Double(act[k]);
				}
				arr.add(dv);
			}//dist was 0.0
			//next
			prv = act;
		}
		
		//prepare array to be returned
		double[][] ret = new double[arr.size()][m];
		int n=0;
		for(Double[] dd : arr){
			ret[n] = new double[m];
			int j=0;
			for(Double d : dd){
				ret[n][j] = d.doubleValue();
				j++;
			}
			n++;
		}
		return ret;
	}
	
	private double calcv0(double[][] r1, double[][] r2){
		//shift both to the origin and rotate r2
		double[] cr1 = getCenter(r1);
		double[] cr2 = getCenter(r2);
		
		double[][] shr1 = Util.shift(r1,new double[]{-cr1[0], -cr1[1]});
		double[][] shr2 = Util.shift(r2,new double[]{-cr2[0], -cr2[1]});
		
		
		double[] dr1 = (new Deming(shr1)).getEstimate();
		double[] dr2 = (new Deming(shr2)).getEstimate();
		
		double alpha1 = Math.atan(dr1[1]);
		double alpha2 = Math.atan(dr2[1]);
		double[][] r2rot = Util.rotate2D(shr2, alpha1-alpha2);
		
		/*
		System.out.println("DTW calcv0 :: shr1 =");
		System.out.println(Util.dblMatToString(shr1));
		System.out.println("DTW calcv0 :: r2rot =");
		System.out.println(Util.dblMatToString(r2rot));
		*/
		double dst1 = calcDistance(shr1,r2rot);
		
		/*
		System.out.println("calcV0 :: shr1=");
		System.out.println(Util.dblMatToString(shr1));
		System.out.println("calcV0 :: r2rot=");
		System.out.println(Util.dblMatToString(r2rot));
		*/
		
		//2nd trial: rotate by another 180 degrees
		r2rot = Util.rotate2D(r2rot, Math.PI);
		double dst2 = calcDistance(shr1,r2rot);
		
		/*
		System.out.println("calcV0 :: shr1=");
		System.out.println(Util.dblMatToString(shr1));
		System.out.println("calcV0 :: r2rot + PI=");
		System.out.println(Util.dblMatToString(r2rot));
		*/
		
		return (dst1 < dst2 ? dst1 : dst2);
	}
	
	private double calcv1(double[][] r1, double[][] r2){
		//flip the second route and do the same as v0
		double[][] r2flip = Util.flip(r2);		
		return calcv0(r1orig,r2flip);
	}
		
	private double[] getCenter(double[][] r){
		int m = r.length, n = r[0].length;
		double[] ret = new double[n];
		for(int j=0; j<n; j++){
			for(int i=0; i<m; i++){
				ret[j] += r[i][j];
			}
			ret[j] /= (double)m;
		}
		return ret;
	}
	
	private double calcDistance(double[][] r1, double[][] r2){
		int stepcnt = 0;
		double ret = 0.0;
		
		HashSet<DPElem> face = new HashSet<DPElem>();
		DPElem start = new DPElem(0,0,metric.getDistance(r1[0], r2[0]),null);
		DPElem sel = null;
		face.add(start);
		
		//System.out.println("DTW :: r1.length=" + r1.length + ", r2.length=" + r2.length);
		
		while(face.size() > 0 /*&& stepcnt < 3*/){
			HashMap<String, DPElem> toadd = new HashMap<String, DPElem>();
			ret = Double.MAX_VALUE;
			//loop through the actual upper-right elements
			for(DPElem e : face){
				//System.out.println(stepcnt + " DPElem e=" + e.toString());
				
				//is it a finishing element?
				if(e.i == (r1.length-1) && e.j == (r2.length-1) ){
					if( sel==null || e.value < sel.value ){
						sel = e;
					}
				}
				
				if(e.value < ret){
					ret = e.value;
				}
				if(e.i+1 - e.j < this.xTol && e.i+1<r1.length){
					DPElem c1 = new DPElem(e.i+1,e.j,e.value+metric.getDistance(r1[e.i+1], r2[e.j]),e);
					DPElem old = toadd.get(c1.getKey());
					if(old==null || old.value >= c1.value){
						toadd.put(c1.getKey(), c1);
						c1.setArea( Util.calcTriangleArea(r1[e.i], r1[e.i+1], r2[e.j]) );
						//System.out.println("      c1=" + c1.toString() + " added");
					} else {
						//System.out.println("      c1=" + c1.toString() + " discarded");
					}
				} else {
					//System.out.println("      c1 rejected :: e.i+1 - e.j = " + (e.i+1 - e.j) );
				}
				if(e.j+1 - e.i < this.yTol && e.j+1<r2.length){
					DPElem c2 = new DPElem(e.i,e.j+1,e.value+metric.getDistance(r1[e.i], r2[e.j+1]),e);
					DPElem old = toadd.get(c2.getKey());
					if(old==null || old.value >= c2.value){
						toadd.put(c2.getKey(), c2);
						c2.setArea( Util.calcTriangleArea(r1[e.i], r2[e.j], r2[e.j+1]) );
						//System.out.println("      c2=" + c2.toString() + " added");
					} else  {
						//System.out.println("      c2=" + c2.toString() + " discarded");
					}
				} else {
					//System.out.println("      c2 rejected :: e.j+1 - e.j = " + (e.j+1 - e.i) );
				}
				if(e.i+1<r1.length && e.j+1<r2.length){
					DPElem c3 = new DPElem(e.i+1,e.j+1,e.value+metric.getDistance(r1[e.i+1], r2[e.j+1]),e);
					DPElem old = toadd.get(c3.getKey());
					if(old==null || old.value >= c3.value){
						toadd.put(c3.getKey(), c3);
						c3.setArea( Util.calcPolygonArea(r1[e.i], r2[e.j], r1[e.i+1], r2[e.j+1]) );
						//System.out.println("      c3=" + c3.toString() + " added");
					} else {
						//System.out.println("      c3=" + c3.toString() + " discarded");
					}
				} else {
					//System.out.println("      c3 rejected");
				}
				//System.out.println("   DPElem e=" + e.toString());
			}//next DPElem from face.values()
			//flush face and add new elems
			face = new HashSet<DPElem>();
			face.addAll(toadd.values());
			stepcnt++;
			//System.out.println(stepcnt + ", ret=" + Util.dts(ret) + ", sel=" + (sel==null ? "NULL" : sel.toString()) );
		}//next turn: inspect all elements in face, if any
		
		double totArea = 0.0;
		DPElem e = sel;
		//System.out.println("Area components:");
		//System.out.println("sel=" + sel.toString());
		while(e.pred != null){
			totArea += e.area;
			//System.out.println(e);
			e = e.pred;
		}
		
		//return sel.value;
		return totArea;
	}
}
 