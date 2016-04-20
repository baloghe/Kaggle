package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import java.util.HashMap;
import java.util.ArrayList;

import dta.*;

public final class SubmissionMerger
{

   private static HashMap<String,Double> routeToWeight;
   private static ArrayList<HashMap<String,Double>> filesToResults;
   public static File SUBMISSION_FILE;

   	public static void main(String[] args) throws Exception{
		String[] fileNames = new String[]{
				"v1_again_plus_ntiles7.csv", "geomsim_0025pct.csv"
			};
		double[] weights = new double[]{
				0.8 , 0.2
			};

		routeToWeight = new HashMap<String,Double>();
		SUBMISSION_FILE = Util.createFile("submission.csv", "driver_trip,prob");

		//call merge
		readCandidates(fileNames);
		mergeSubmissions(weights);
		//mergeSubmissionsMax();

		//write output
		Main.submit(SUBMISSION_FILE,routeToWeight);

		System.out.println("Merge done to " + SUBMISSION_FILE.getCanonicalPath());
	}

	public static void readCandidates(String[] fileNames){
		//read files and load destination keyset
		filesToResults = new ArrayList<HashMap<String,Double>>(fileNames.length);;
		int i = 0;
		for(String fname : fileNames){
			filesToResults.add( Main.readPreviousResult(fname, ",") );
			//ensure keyset is full in the destination map
			for(String key : filesToResults.get(i).keySet()){
				routeToWeight.put(key,null);
			}
			i++;
		}//nest input file
	}

	public static void mergeSubmissions(double[] weights){

		//perform merge
		for(String key : routeToWeight.keySet()){
			boolean[] keyFound = new boolean[filesToResults.size()];
			double[] candidates = new double[filesToResults.size()];
			double weightSum = 0.0;
			for(int i=0; i<filesToResults.size(); i++){
				Double dd = filesToResults.get(i).get(key);
				if(dd == null){
					keyFound[i] = false;
				} else {
					keyFound[i] = true;
					candidates[i] = dd.doubleValue();
					weightSum += weights[i];
				}
			}
			double res = 0.0;
			for(int i=0; i<filesToResults.size(); i++){
				if(keyFound[i]){
					res += (candidates[i] * weights[i]);
				}
			}
			res /= weightSum;
			routeToWeight.put( key , new Double(res) );
		}
	}

	public static void mergeSubmissionsMax(){

		//perform merge
		for(String key : routeToWeight.keySet()){
			boolean[] keyFound = new boolean[filesToResults.size()];
			double[] candidates = new double[filesToResults.size()];
			for(int i=0; i<filesToResults.size(); i++){
				Double dd = filesToResults.get(i).get(key);
				if(dd == null){
					keyFound[i] = false;
				} else {
					keyFound[i] = true;
					candidates[i] = dd.doubleValue();
				}
			}
			double res = 0.0;
			for(int i=0; i<filesToResults.size(); i++){
				if(keyFound[i] && candidates[i] > res){
					res = candidates[i];
				}
			}
			routeToWeight.put( key , new Double(res) );
		}
	}


}