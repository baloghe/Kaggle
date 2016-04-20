package dta;

public class OLLogReg {

	public static final double SLB = -20.0;
	public static final double SUB =  20.0;
	
	private int featNum;
	private int regrNum;
	private int labNum;
	private double alpha;
	private int iterNum;
	
	private double[] x;
	private double[][] w;
	private double[][] absGrad;
	private double[] y;
	private double[][] preds;
	
	public OLLogReg(int inFeatNum, int inRegrNum, int inLabNum, double inAlpha, int inIterNum){
		featNum = inFeatNum;
		regrNum = inRegrNum;
		labNum = inLabNum;
		alpha = inAlpha;
		iterNum = inIterNum;
		
		//setup vars
		initTrain();
	}
	
	/** inFeatures=obsNum x featNum ; inLabels=obsNum x labNum */
	public void trainModel(double[][] inFeatures, double[][] inLabels){
		//check dimensions
		if(inFeatures==null || inLabels==null)
			return;
		int dimRegrNumF = inFeatures.length, dimRegrNumL = inLabels.length;
		if(dimRegrNumF==0 || dimRegrNumL==0)
			return;
		int dimFeatNum = inFeatures[0].length, dimLabNum = inLabels[0].length;
		if(dimRegrNumF != this.regrNum  || dimRegrNumL != this.regrNum
				|| dimFeatNum != this.featNum || dimLabNum != this.labNum)
			return;
		
		for(int itNum=0; itNum < iterNum; itNum++){
			for(int cnt=0; cnt < regrNum; cnt++){
				y = inLabels[cnt];
				x = inFeatures[cnt];
				for(int k=0; k<labNum; k++){
					double p = predict(x, w[k]);
					preds[cnt][k] = p;
					update(k, x, p);
				}//next label
			}//next observation features
		}//next iteration
		//System.out.println("trainig finished");
	}
	
	protected void initTrain(){
		w = new double[labNum][featNum];
		absGrad = new double[labNum][featNum];
		preds = new double[regrNum][labNum];
	}
	
	public double[][] calcEstimates(double[][] inFeatures){
		//check dimensions
		if(inFeatures==null)
			return null;
		int dimRegrNum = inFeatures.length;
		if(dimRegrNum==0)
			return null;
		int dimFeatNum = inFeatures[0].length;
		if(dimFeatNum != this.featNum)
			return null;
		//System.out.println("calcEstimates");
		//calculate
		double[][] ret = new double[dimRegrNum][labNum];
		for(int cnt=0; cnt < dimRegrNum; cnt++){
			for(int k=0; k<labNum; k++){
				ret[cnt][k] = predict(inFeatures[cnt] , w[k]);
			}//next label
		}//next observation features
		return ret;
	}
	
	//probability of p(y = 1 | x; w)
	private double predict(double[] x, double[] w){
		double wTx = 0.0;
		for(int i=0; i<x.length; i++){
			wTx += (w[i] * x[i]);
		}
		//bounded sigmoid
		return Util.boundedSigmoid(wTx, SLB, SUB);
	}
		
	private void update(int lab, double[] x, double pred){
		for(int i=0; i<featNum; i++){
			absGrad[lab][i] += (Math.abs(pred - y[lab]));
			w[lab][i] -= (pred - y[lab]) * x[i] * alpha / Math.sqrt(absGrad[lab][i]);
		}
	}
	
	public double[][] getTheta(){return w;}
	public double[][] getEstimates(){return preds;}
	
}
