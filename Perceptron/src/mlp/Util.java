package mlp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Util {

	public static double sigmoid(double val){
        return (1.0 / (1.0 + Math.exp(-val)));
    }

	public static double sigmoidDerivative(double val){
        return (val * (1.0 - val));
    }
	
	public static String doubleToString(double inDbl){
		//return (new DecimalFormat("#.000")).format(inDbl);
		return String.format(Locale.ENGLISH, "%.3f", inDbl);
	}
	
	public static String doubleArrayToString(double[] inDblArr, String sep){
		String ret = "";
		if(inDblArr == null) ret = "NULL";
		else if(inDblArr.length == 0) ret = "EMPTY";
		else {
			ret = doubleToString(inDblArr[0]);
			for(int i=1; i<inDblArr.length; i++){
				ret += (sep + doubleToString(inDblArr[i]) );
			}
		}
		return ret;
	}
	
	public static String doubleArrayToString(double[] inDblArr){
		return doubleArrayToString(inDblArr, "\t");
	}
	
	public static String doubleMatrixToString(double[][] inDblMat){
		return doubleMatrixToString(inDblMat, "\t");
	}
	
	public static String doubleMatrixToString(double[][] inDblMat, String sep){
		String ret = "";
		if(inDblMat == null) ret = "NULL";
		else if(inDblMat.length == 0) ret = "EMPTY";
		else {
			ret = doubleArrayToString(inDblMat[0], sep);
			for(int i=1; i<inDblMat.length; i++){
				ret += ("\n" + doubleArrayToString(inDblMat[i], sep));
			}
		}
		return ret;
	}
		
	public static String doubleMatrixToString(ArrayList<double[]> inDblMat, String sep){
		String ret = "";
		if(inDblMat == null) ret = "NULL";
		else if(inDblMat.size() == 0) ret = "EMPTY";
		else {
			ret = doubleArrayToString(inDblMat.get(0), sep);
			for(double[] vec : inDblMat){
				ret += ("\n" + doubleArrayToString(vec, sep));
			}
		}
		return ret;
	}
	
	public static String doubleMatrixToString(ArrayList<double[]> inDblMat){
		return doubleMatrixToString(inDblMat, "\t");
	}
}
