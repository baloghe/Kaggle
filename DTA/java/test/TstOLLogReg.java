package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import dta.*;

public class TstOLLogReg {

	public static void main(String[] args) {
		
		//test1();
		test2(new File("datatst/binary.csv"),",");

	}
	
	public static void test1(){
		int featNum = 2;
		int regrNum = 100;
		int labNum = 1;
		double alpha = 0.1;
		int iterNum = 100;
		
		double[][] x = new double[regrNum][featNum+1];
		double[][] y = new double[regrNum][labNum];
		
		for(int i=0; i<regrNum; i++){
			x[i][0] = 1.0;
			for(int k=1; k<featNum+1; k++){
				x[i][k] = Math.random() * 10.0 + (k-1) * 10;
			}
			if(x[i][1] + x[i][2] + 0.01 * (Math.random() - 0.5) < 20.0){
				y[i][0] = 1.0;
			} else {
				y[i][0] = 0.0;
			}
			System.out.println("y["+i+"]="+y[i][0]);
		}
		
		OLLogReg reg = new OLLogReg(featNum+1,regrNum,labNum,alpha,iterNum);
		reg.trainModel(x, y);
		
		System.out.println("Theta=");
		System.out.println(Util.dblMatToString(reg.getTheta()));
		
		double[][] ests = reg.getEstimates();
		double[][] predout = new double[regrNum][2*labNum];
		for(int i=0; i<regrNum; i++){
			for(int j=0; j<labNum; j++){
				predout[i][j] = y[i][j];
				predout[i][labNum+j] = (ests[i][j] > 0.5 ? 1.0 : 0.0);
			}
		}
		
		System.out.println("Predictions=");
		System.out.println(Util.dblMatToString(predout));
	}
	
	public static void test2(File inFile, String sep){
				
		ArrayList<double[]> obs = new ArrayList<double[]>();
		
		try{
			BufferedReader input = new BufferedReader(new FileReader(inFile));
			String line = input.readLine(); //drop file header
			int cnt = 0;
			while (( line = input.readLine()) != null){
				String[] raws = line.split(sep);
				double[] ob = new double[6];
				ob[0] = new Double(raws[0]).doubleValue();
				ob[1] = new Double(raws[1]).doubleValue();
				ob[2] = new Double(raws[2]).doubleValue();
				int k = new Integer(raws[3]);
				if(k>1){
					ob[1+k]=1.0;
				}
				obs.add(ob);
				cnt++;
			}
			System.out.println(cnt + " observations read in from " + inFile.getAbsolutePath());
			input.close();
		}catch(Exception e){
			System.out.println("ROUTES_SET_TO_1 could not be opened!");
			e.printStackTrace();
		}
		
		int regrNum = obs.size();
		int featNum = 5;
		int labNum = 1;
		double alpha = 0.1;
		int iterNum = 100;
		
		double[][] x = new double[regrNum][featNum+1];
		double[][] y = new double[regrNum][labNum];
		
		int i=0;
		for(double[] ob : obs){
			x[i][0] = 1.0;
			for(int j=1; j<ob.length; j++){
				x[i][j] = ob[j];
			}
			y[i][0] = ob[0];
			i++;
		}
		
		OLLogReg reg = new OLLogReg(featNum+1,regrNum,labNum,alpha,iterNum);
		reg.trainModel(x, y);
		
		System.out.println("Theta=");
		System.out.println(Util.dblMatToString(reg.getTheta()));
	}
}
