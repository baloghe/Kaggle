package dta;

import java.util.ArrayList;
import java.util.Collections;

public class RouteEvaluator {

	public static double LOGREG_ALPHA = 0.2;
	public static int LOGREG_ITERNUM = 200;
	public static int LOGREG_OPPONENT_NUM = 12;
	public static int LOGREG_CHOSEN_ROUTE_NUM = 200;
	public static int LOGREG_RETRIAL_NUM = 1;

	protected ArrayList<Route> trueRoutes;
	protected ArrayList<Route> falseRoutes;
	protected String[] keys;
	protected double[] preds;
	FeatureExtractor featExtr;
	private int driver;

	private int featNum;
	private int trsetSize;
	private OLLogReg reg;

	public RouteEvaluator(int inDriver, FeatureExtractor inFeatExtr){
		featExtr = inFeatExtr;
		driver = inDriver;

		featNum = featExtr.getFeatureNum();
		trsetSize = 2 * LOGREG_CHOSEN_ROUTE_NUM;
		reg = new OLLogReg(featNum+1, trsetSize, 1, LOGREG_ALPHA, LOGREG_ITERNUM);
	}

	public void calcEstimates( ArrayList<Route> inTrueRoutes, ArrayList<Route> inFalseRoutes ){

		trueRoutes = inTrueRoutes;
		falseRoutes = inFalseRoutes;

		for(int rtr=0; rtr<RouteEvaluator.LOGREG_OPPONENT_NUM; rtr++){
			//setup trainset
			ArrayList<Route> trainset = new ArrayList<Route>();
			Collections.shuffle(trueRoutes);
			Collections.shuffle(falseRoutes);
			trainset.addAll(trueRoutes.subList(0, LOGREG_CHOSEN_ROUTE_NUM));
			trainset.addAll(falseRoutes.subList(0, LOGREG_CHOSEN_ROUTE_NUM));
			Collections.shuffle(trainset);

			//obtain features and labels
			double[][] feats = new double[trsetSize][featNum+1];
			double[][] labs = new double[trsetSize][1]; // 1 label only!

			int cnt = 0;
			for(Route rt : trainset){
				double[] fs = featExtr.extractFeatures(rt);
				boolean isTrue = ( rt.getDriver() == this.driver);
				for(int j=0; j<featNum; j++){
					feats[cnt][0] = 1; //intercept
					feats[cnt][j+1] = fs[j];
				}
				labs[cnt][0] = (isTrue ? 1.0 : 0.0);
				cnt++;
			}
			//continue training
			reg.trainModel(feats, labs);
		}//next rtr

		//LogReg estimate for TRUE routes
		double[][] trueFeats = new double[trueRoutes.size()][featNum+1];
		keys = new String[trueRoutes.size()];

		int trcnt = 0;
		for(Route rt : trueRoutes){
			double[] fs = featExtr.extractFeatures(rt);
			for(int j=0; j<featNum; j++){
				trueFeats[trcnt][0] = 1; //intercept
				trueFeats[trcnt][j+1] = fs[j];
				keys[trcnt] = rt.getKey();
			}
			trcnt++;
		}

		double[][] ps = reg.calcEstimates(trueFeats);
		preds = new double[ps.length];
		for(int j=0; j< ps.length; j++){
			preds[j] = ps[j][0];
		}

		//double[][] w = reg.getTheta();
		//System.out.println("      Thetas:");
		//System.out.println(Util.dblMatToString(w));
	}

	public ArrayList<String> getSubmission(){
		ArrayList<String> ret = new ArrayList<String>();
		for(int i=0; i<keys.length; i++){
			String s = keys[i] + "," + Util.dtsSubmit(preds[i]);
			ret.add(s);
		}
		return ret;
	}
	
	public double getSumPreds(){
		double ret = 0.0;
		for(double d : preds){
			ret += d;
		}
		return ret;
	}
}
