package dta;

import java.util.ArrayList;

public class VLenPattern {

	//private ArrayList<RouteElement> elems;
	private ArrayList<double[]> coords;
	private double[] last;
	private double measurableLength;
	private Metric metric;
		
	public VLenPattern(double[] inCoords, Metric inMetric){
		measurableLength = 0.0;
		//elems = new ArrayList<RouteElement>();
		coords = new ArrayList<double[]>();
		metric = inMetric;
		coords.add(inCoords);
		last = inCoords;
	}
	
	public void addElement(double[] inCoords){
		double dist = metric.getDistance(last, inCoords);
		if(dist > 0.0){
			coords.add(inCoords);
			last = inCoords;
			measurableLength += dist;
		}
	}
	
	public double[][] getCoordinates(){
		double[][] ret = new double[coords.size()][2];
		int i=0;
		for(double[] d : coords){
			ret[i] = d;
			i++;
		}
		return ret;
	}
	
	public double[] getLastCoord(){return last;}
	
	public double getMeasureableLength(){return measurableLength;}
	
}
