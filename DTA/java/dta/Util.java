package dta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import java.util.Collections;
import java.util.ArrayList;

public class Util {

	public static double[] calcNtiles(double[] data, int numTiles){
		int dl = data.length;
		ArrayList<Double> oData = new ArrayList<Double>(dl);
		int cnt = 0;
		for(double d : data){
			oData.add(new Double(d));
		}
		Collections.sort(oData);
		double[] ret = new double[numTiles];
		for(cnt=1; cnt < numTiles; cnt++){
			ret[cnt-1] = oData.get( dl * cnt / numTiles );
		}
		ret[numTiles-1] = oData.get(dl-1);
		return ret;
	}

	public static String dts(double d){
		return Double.toString(Math.round(d * 1000.0) / 1000.0);
	}

	public static String dtsSubmit(double d){
		return Double.toString(Math.round(d * 100000.0) / 100000.0);
	}

	public static double sigmoid(double x){
	      return 1.0 / (1.0 + Math.exp(-x));
	}

	public static double boundedSigmoid(double x, double lbound, double ubound){
		double xx = (x > ubound ? ubound : (x < lbound ? lbound : x));
		return 1.0 / (1.0 + Math.exp(-xx));
	}

	public static String dblVecToString(double[] vec){
      if(vec==null){
         return "NULL";
      }

      String ret = dts(vec[0]);
      for(int i=1; i<vec.length; i++){
         ret += (","+dts(vec[i]));
      }
      return ret;
   }

	public static String dblMatToString(double[][] mat){
		if(mat==null){
			return "NULL";
		} else if(mat.length==0){
			return "[]";
		}

		String ret="";
		for(int m=0; m<mat.length; m++){
			for(int n=0; n<mat[m].length; n++){
				ret += (" "+dts(mat[m][n]));
			}
			ret += "\n";
		}

		return ret;
	}

	public static double calcTriangleArea(double[] x, double[] y, double[] z){
		return Math.abs( (x[0]-z[0])*(y[1]-z[1]) - (y[0]-z[0])*(x[1]-z[1]) ) * 0.5;
	}

	public static double calcPolygonArea(double[] x, double[] y, double[] z, double[] q){
		return calcTriangleArea(x,y,z) + calcTriangleArea(z,q,x);
	}

	public static double[][] flip(double[][] r){
		//System.out.println("Before flip:");
		//System.out.println(Util.dblMatToString(r));
		int m = r.length, n = r[0].length;
		double[][] ret = new double[m][n];
		for(int i=0; i<r.length; i++){
			for(int j=0; j<n; j++){
				ret[m-1-i][j] = r[i][j];
			}
		}
		//System.out.println("After flip:");
		//System.out.println(Util.dblMatToString(ret));
		return ret;
	}

	public static double[][] rotate2D(double[][] r, double alphaRad){
		//System.out.println("Before rotate by " + alphaRad + ":");
		//System.out.println(Util.dblMatToString(r));
		int m = r.length;
		double cosa = Math.cos(alphaRad), sina = Math.sin(alphaRad);
		double[][] ret = new double[m][2];
		for(int i=0; i<r.length; i++){
			ret[i][0] = r[i][0] * cosa - r[i][1] * sina;
			ret[i][1] = r[i][0] * sina + r[i][1] * cosa;
		}
		//System.out.println("After rotate:");
		//System.out.println(Util.dblMatToString(ret));
		return ret;
	}

	public static double[][] rotate2D180(double[][] r){
		//System.out.println("Before rotate by " + alphaRad + ":");
		//System.out.println(Util.dblMatToString(r));
		int m = r.length;
		double[][] ret = new double[m][2];
		for(int i=0; i<r.length; i++){
			ret[i][0] = -1.0 * r[i][0];
			ret[i][1] = -1.0 * r[i][1];
		}
		//System.out.println("After rotate:");
		//System.out.println(Util.dblMatToString(ret));
		return ret;
	}

	public static double[][] shift(double[][] r, double[] vec){
		//System.out.println("Before shift by " + Util.dblVecToString(vec) + ":");
		//System.out.println(Util.dblMatToString(r));
		int m = r.length, n = r[0].length;
		double[][] ret = new double[m][n];
		for(int i=0; i<r.length; i++){
			for(int j=0; j<n; j++){
				ret[i][j] = r[i][j] + vec[j];
			}
		}
		//System.out.println("After shift:");
		//System.out.println(Util.dblMatToString(ret));
		return ret;
	}

	/** returns interior points only */
	public static double[][] sliceEquidist(double[] start, double[] end, int snum){
		//don't do anything with improper aguments
		if(start==null || end==null || snum<=1 || start.length==0 || end.length==0)
			return null;
		//create "divisor" vector
		int m = start.length;
		double[] div = new double[m];
		double divlen=0.0;
		for(int i=0; i<m; i++){
			div[i] = (end[i] - start[i])/(double)snum;
			divlen += (div[i]*div[i]);
		}
		//don't do anything if the divisor has only zero entries
		if(divlen==0.0) return null;

		double[][] ret = new double[snum-1][m];
		for(int i=0; i<snum-1; i++){
			for(int j=0; j<m; j++){
				ret[i][j] = start[j] + (double)(i+1) * div[j];
			}//next j
		}//next i

		return ret;
	}

	public static double[][] alignToX(double[][] vec){
		if(vec==null || vec.length==0 || vec[0]==null || vec[0].length!=2){
			return null;
		}

		//get normal vector (a,b) of last point of trajectory
		int en = vec.length;
		double len=Math.sqrt(vec[en-1][0]*vec[en-1][0] + vec[en-1][1]*vec[en-1][1]);
		double a=vec[en-1][0]/len , b = vec[en-1][1]/len;
		//now M=[ [a b]^T , [-b a]^T ] is the transition matrix
		//then M^(-1) = 1 / (a^2 + b^2) * [ [a -b]^T , [b a]^T ]. But a^2 + b^2 = 1
		//so new coordinates for P=(p1,p2):
		// P' = (a*p1 + b*p2 , -b*p1 + a*p2)
		double[][] ret = new double[en][2];
		for(int i=0; i<en; i++){
			ret[i][0] =  a*vec[i][0] + b*vec[i][1];
			ret[i][1] = -b*vec[i][0] + a*vec[i][1];
		}
		return ret;
	}

	public static double[][] mirrorOnX(double[][] vec){
		if(vec==null || vec.length==0 || vec[0]==null || vec[0].length!=2){
			return null;
		}

		int en = vec.length;
		double[][] ret = new double[en][2];
		for(int i=0; i<en; i++){
			ret[i][0] =  vec[i][0];
			ret[i][1] = -vec[i][1];
		}
		return ret;
	}

	public static File createFile(String targetFileName, String header){
		try{
			File ret = new File(targetFileName);
			PrintWriter writer = new PrintWriter(ret, "UTF-8");
			writer.println(header);
			writer.close();
			return ret;
		} catch(Exception e){
			return null;
		}
	}
	
	public static double[] interpolate(double[] startPt, double[] endPt, double ratio){
		double[] ret = new double[startPt.length];
		for(int i=0; i<startPt.length; i++){
			ret[i] = startPt[i] + ratio * ( endPt[i] - startPt[i] );
		}
		return ret;
	}

}
