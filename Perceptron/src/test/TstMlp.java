package test;

import org.junit.Test;

import java.util.ArrayList;
import org.junit.Assert;

import java.util.Random;

import mlp.*;

public class TstMlp {
	
	@Test
	public void testLayers(){
		double[] inputs = new double[]{ 1, 1, 1, 0 };
		double[] outputs = new double[]{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		
		//Creation and FeedForward
		
		Layer lay0 = new InputLayer(4,0.2, new SigmoidFunction());
		System.out.println("LAY0");
		System.out.println(lay0.toString());
		
		lay0.setup();
		System.out.println("LAY0 - after setup");
		System.out.println(lay0.toString());
		
		lay0.calcFeedForward(inputs);
		System.out.println("LAY0 - after feedforward");
		System.out.println(lay0.toString());
		
		Layer lay1 = new Layer(lay0,6,0.2, new SigmoidFunction());
		System.out.println("LAY1");
		System.out.println(lay1.toString());
		
		lay1.setup();
		System.out.println("LAY1 - after setup");
		System.out.println(lay1.toString());
		
		lay1.calcFeedForward(lay0.getResults());
		System.out.println("LAY1 - after feedforward");
		System.out.println(lay1.toString());
		
		OutputLayer lay2 = new OutputLayer(lay1,14,0.2, new SigmoidFunction());
		System.out.println("LAY2");
		System.out.println(lay2.toString());
		
		lay2.setup();
		System.out.println("LAY2 - after setup");
		System.out.println(lay2.toString());
		
		lay2.calcFeedForward(lay1.getResults());
		System.out.println("LAY2 - after feedforward");
		System.out.println(lay2.toString());
		
		//Backpropagation of errors
		
		System.out.println("LAY2 - errors");
		System.out.println(Util.doubleArrayToString(lay2.getErrors()));
		
		lay2.propagateError(outputs);
		System.out.println("LAY2 - after error backprop");
		System.out.println(lay2.toString());
		
		lay1.propagateError(lay2);
		System.out.println("LAY1 - after error backprop");
		System.out.println(lay1.toString());
		
		//Weight update
		
		lay2.updateWeights(lay1);
		System.out.println("LAY2 - after weight update");
		System.out.println(lay2.toString());
		
		lay1.updateWeights(lay0);
		System.out.println("LAY1 - after weight update");
		System.out.println(lay1.toString());
	}
	
	@Test
	public void testNet(){
		System.out.println("testNet :: orig Example_4x6x16  -- Start");
		double trainInputs[][] = new double[][] {   {1, 1, 1, 0}, 
											        {1, 1, 0, 0}, 
											        {0, 1, 1, 0}, 
											        {1, 0, 1, 0}, 
											        {1, 0, 0, 0}, 
											        {0, 1, 0, 0}, 
											        {0, 0, 1, 0}, 
											        {1, 1, 1, 1}, 
											        {1, 1, 0, 1}, 
											        {0, 1, 1, 1}, 
											        {1, 0, 1, 1}, 
											        {1, 0, 0, 1}, 
											        {0, 1, 0, 1}, 
											        {0, 0, 1, 1}  };

		double trainOutput[][] = new double[][] 
								{{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, 
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<trainInputs.length; i++){
			inp.add(trainInputs[i]);
			out.add(trainOutput[i]);
		}
		
		NeuralNet nn = new NeuralNet( new int[]{4,6,14}, 0.2, new SigmoidFunction() );
		nn.init();
		
		nn.train(inp, out, 10000);
		
		System.out.println("NN - trial");
		double[] trial = new double[]{1, 1, 1, 0};
		System.out.println("Input: " + Util.doubleArrayToString(trial));
		System.out.println("Output: " + Util.doubleArrayToString(nn.predict(trial)));
		
		System.out.println("testNet :: orig Example_4x6x16  -- End");
	}
	
	@Test
	public void testNet2(){
		System.out.println("testNet2 :: classic XOR  -- Start");
		
		double trainInputs[][] = new double[][] {   
				{0, 0}, 
		        {1, 0}, 
		        {0, 1}, 
		        {1, 1}  };
		
		double trainOutput[][] = new double[][] {   
				{0}, 
		        {1}, 
		        {1}, 
		        {0}  };
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<trainInputs.length; i++){
			inp.add(trainInputs[i]);
			out.add(trainOutput[i]);
		}
		
		NeuralNet nn = new NeuralNet( new int[]{2,2,1}, 0.2, new SigmoidFunction() );
		nn.init();
		
		nn.train(inp, out, 10000);
		
		System.out.println("NN - trial");
		double[] trial = new double[]{1, 1};
		System.out.println("Input: " + Util.doubleArrayToString(trial));
		System.out.println("Output: " + Util.doubleArrayToString(nn.predict(trial)));
		
		System.out.println("testNet2 :: classic XOR  -- End");
	}
	
	@Test
	public void testNet3(){
		System.out.println("testNet3 :: positions in an 1x1 box  -- Start");
		
		int sample_num = 100;
		int epoch_num = 100;
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<sample_num; i++){
			double[] coords = getRandomCoords();
			inp.add(coords);
			out.add(getPositionVector(coords));
		}
		/*
		System.out.println("inp");
		System.out.println(Util.doubleMatrixToString(inp));
		System.out.println("out");
		System.out.println(Util.doubleMatrixToString(out));
		*/
		NeuralNet nn = new NeuralNet( new int[]{2,8,8,8,5}, 0.2, new SigmoidFunction() );
		nn.init();
		
		nn.train(inp, out, epoch_num);
		
		System.out.println("NN - trial");
		for(int i=0; i<5; i++){
			System.out.println("TestCases");
			double[] trial = getRandomCoords();
			System.out.println("  Input : " + Util.doubleArrayToString(trial));
			System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
			System.out.println("  Output: " + Util.doubleArrayToString(nn.predict(trial)));
		}
		
		System.out.println("testNet3 :: positions in an 1x1 box  -- End");
	}
	
	@Test
	public void testNet4(){
		System.out.println("testNet4 :: positions in an 1x1 box discretized, TanH  -- Start");
		
		int sample_num = 100;
		int epoch_num = 100;
		
		int splitnum = 10;
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<sample_num; i++){
			double[] coords = getRandomCoords();
			inp.add( getDiscretizedInput(coords, splitnum) );
			out.add( getPositionVector(coords) );
		}
		/*
		System.out.println("inp");
		System.out.println(Util.doubleMatrixToString(inp));
		System.out.println("out");
		System.out.println(Util.doubleMatrixToString(out));
		*/
		NeuralNet nn = new NeuralNet( new int[]{100,20,10,5}, 0.01, new TanHFunction() );
		nn.init();
		
		nn.train(inp, out, epoch_num);
		
		System.out.println("NN - trial");
		System.out.println("TestCases");
		double[] trial;
		
		trial = new double[]{0.5 , 0.5};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		System.out.println("  Output: " + Util.doubleArrayToString( nn.predict( getDiscretizedInput(trial, splitnum) ) ) );
		
		trial = new double[]{0.04 , 0.96};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		System.out.println("  Output: " + Util.doubleArrayToString( nn.predict( getDiscretizedInput(trial, splitnum) ) ) );
		
		trial = new double[]{0.96 , 0.96};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		System.out.println("  Output: " + Util.doubleArrayToString( nn.predict( getDiscretizedInput(trial, splitnum) ) ) );
		
		trial = new double[]{0.96 , 0.04};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		System.out.println("  Output: " + Util.doubleArrayToString( nn.predict( getDiscretizedInput(trial, splitnum) ) ) );
		
		trial = new double[]{0.04 , 0.04};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		System.out.println("  Output: " + Util.doubleArrayToString( nn.predict( getDiscretizedInput(trial, splitnum) ) ) );
		
		
		System.out.println("testNet4 :: positions in an 1x1 box discretized, TanH  -- End");
	}
	
	private double[] getRandomCoords(){
		return new double[]{
				new Random().nextDouble() , new Random().nextDouble()
		};
	}
	
	private double[] getDiscretizedInput(double[] inCoords, int inSplitNum){
		double[] ret = new double[ inSplitNum * inSplitNum ];
		
		int posX = (int) Math.floor(inCoords[0] * (double)inSplitNum);
		int posY = (int) Math.floor(inCoords[1] * (double)inSplitNum);
		
		for(int i=0; i<ret.length; i++){
			ret[i] = -1.0;
		}
		ret[ posY * inSplitNum + posX ] = 1.0;
		
		return ret;
	}
	
	private double[] getPositionVector( double[] inCoords ){
		/* To be returned:
		 * [1,0,0,0,0] if point is in circle(0.5,0.5,0.5)
		 * [0,1,0,0,0] if point is in top-left corner
		 * [0,0,1,0,0] if point is in top-right corner
		 * [0,0,0,1,0] if point is in bottom-right corner
		 * [0,0,0,0,1] if point is in bottom-left corner
		 * */
		
		if( Math.sqrt( (inCoords[0] - 0.5)*(inCoords[0] - 0.5) + (inCoords[1] - 0.5)*(inCoords[1] - 0.5) ) <= 0.5 )
			return new double[]{1,-1,-1,-1,-1};
		else if(inCoords[0] <= 0.5 && inCoords[1] > 0.5)
			return new double[]{-1,1,-1,-1,-1};
		else if(inCoords[0] > 0.5 && inCoords[1] > 0.5)
			return new double[]{-1,-1,1,-1,-1};
		else if(inCoords[0] > 0.5 && inCoords[1] <= 0.5)
			return new double[]{-1,-1,-1,1,-1};
		else return new double[]{-1,-1,-1,-1,1};
	}
	
	
	
	
	@Test
	public void testNet5(){
		//source: http://www-inst.eecs.berkeley.edu/~cs188/sp08/projects/backprop/project6.html
		System.out.println("testNet :: berkeley homework  -- Start");
		double trainInputs[][] = new double[][] {   
				{  -7.7947021e-01,   8.3822138e-01} ,
				{   1.5563491e-01,   8.9537743e-01},
				{  -5.9907703e-02,  -7.1777995e-01},
				{   2.0759636e-01,   7.5893338e-01},
				{  -1.9598312e-01,  -3.7548716e-01},
				{   5.8848947e-01,  -8.4255381e-01},
				{   7.1985874e-03,  -5.4831650e-01},
				{   7.3883852e-01,  -6.0339369e-01},
				{   7.0464808e-01,  -2.0420052e-02},
				{   9.6992666e-01,   6.4137120e-01},
				{   4.3543099e-01,   7.4477254e-01},
				{  -8.4425822e-01,   7.4235423e-01},
				{   5.9142471e-01,  -5.4602118e-01},
				{  -6.9093124e-02,   3.7659995e-02},
				{  -9.5154865e-01,  -7.3305502e-01},
				{  -1.2988138e-01,   7.5676096e-01},
				{  -4.9534647e-01,  -5.6627908e-01},
				{  -9.0399413e-01,   5.0922150e-01},
				{   2.9235128e-01,   1.6089015e-01},
				{   6.4798552e-01,  -7.7933769e-01},
				{   3.7595574e-01,   7.8203087e-02},
				{   2.4588993e-01,   4.5146739e-03},
				{  -4.5719155e-01,   4.2390461e-01},
				{  -4.4127876e-01,   7.0571892e-01},
				{   5.0744669e-01,   7.5872586e-01}  };

		double trainOutput[][] = new double[][] 
								{{  1.0 } 
								,{  1.0 }
								,{  1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{ -1.0 }
								,{ -1.0 }
								,{  1.0 }
								,{  1.0 }
								,{ -1.0 }   };
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<trainInputs.length; i++){
			inp.add(trainInputs[i]);
			out.add(trainOutput[i]);
		}
		
		NeuralNet nn = new NeuralNet( new int[]{2,3,1}, 0.01, new TanHFunction() );
		nn.init();
		
		nn.train(inp, out, 10000);
		
		System.out.println("NN - trial");
		double[] trial = new double[]{-1.0600562e-01,  -8.1467034e-02};
		System.out.println("Input: " + Util.doubleArrayToString(trial));
		System.out.println("Target: " + "-1.0");
		System.out.println("Output: " + Util.doubleArrayToString(nn.predict(trial)));
		
		System.out.println("NN - trial");
		trial = new double[]{1.0216153e-01,   7.1825825e-01};
		System.out.println("Input: " + Util.doubleArrayToString(trial));
		System.out.println("Target: " + "+1.0");
		System.out.println("Output: " + Util.doubleArrayToString(nn.predict(trial)));
		
		System.out.println("testNet :: berkeley homework  -- End");
	}
	
}
