package mlp;

//import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Frequently used utility functions
 *
 */
public class Util {

	/**
	 * returns a random double in range (inFrom , inTo)
	 * @param inFrom lower bound
	 * @param inTo upper bound
	 * @return random number in range (inFrom , inTo)
	 */
	public static double getRandomInRange(double inFrom, double inTo){
		double ret = new Random().nextDouble();
		
		if(inFrom == inTo) 
			return 0.0;
		else if(inFrom > inTo ){
			ret *= (inFrom - inTo);
			ret += inTo;
		} else {
			ret *= (inTo - inFrom);
			ret += inFrom;
		}
		
		return ret;
	}
	
	/**
	 * transforms a double into a String with a generally used precision
	 * @param inDbl
	 * @return
	 */
	public static String doubleToString(double inDbl){
		//return (new DecimalFormat("#.000")).format(inDbl);
		return String.format(Locale.ENGLISH, "%.6f", inDbl);
	}
	
	/**
	 * transforms a double array into a String with a generally used precision, using the given separator
	 * @param inDblArr array
	 * @param sep separator String inserted between array elements
	 * @return NULL when the array is NULL, EMPTY when the array has no elements, String containing the array elements otherwise
	 */
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
	
	/**
	 * transforms a double array into a String with a generally used precision, TAB separated
	 * @param inDblArr array
	 * @return NULL when the array is NULL, EMPTY when the array has no elements, String containing the array elements otherwise
	 */
	public static String doubleArrayToString(double[] inDblArr){
		return doubleArrayToString(inDblArr, "\t");
	}
	
	/**
	 * transforms a two-dimensional double array into a String with a generally used precision, columns: TAB separated, rows: new line
	 * @param inDblMat two-dimensional double array
	 * @return NULL when the array is NULL, EMPTY when the array has no elements, String containing the array elements otherwise
	 */
	public static String doubleMatrixToString(double[][] inDblMat){
		return doubleMatrixToString(inDblMat, "\t");
	}
	
	/**
	 * transforms a two-dimensional double array into a String with a generally used precision, columns: separated by given String, rows: new line
	 * @param inDblMat two-dimensional double array
	 * @param sep column separator String
	 * @return NULL when the array is NULL, EMPTY when the array has no elements, String containing the array elements otherwise
	 */
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
		
	/**
	 * transforms an ArrayList of double arrays (=matrix of doubles) into a String with a generally used precision, columns: separated by given String, rows: new line
	 * @param inDblMat ArrayList of double arrays
	 * @param sep column separator String
	 * @return NULL when the array is NULL, EMPTY when the array has no elements, String containing the array elements otherwise
	 */
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
	
	/**
	 * transforms an ArrayList of double arrays (=matrix of doubles) into a String with a generally used precision, columns: TAB-separated, rows: new line
	 * @param inDblMat ArrayList of double arrays
	 * @return NULL when the array is NULL, EMPTY when the array has no elements, String containing the array elements otherwise
	 */
	public static String doubleMatrixToString(ArrayList<double[]> inDblMat){
		return doubleMatrixToString(inDblMat, "\t");
	}
}
