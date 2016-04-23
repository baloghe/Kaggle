package test;

import java.util.ArrayList;

import mlp.NeuralNet;
import mlp.TanHFunction;
import mlp.Util;

import org.junit.Test;

public class TstBerkeleyOnly {

	@Test
	public void testNet5(){
		//source: http://www-inst.eecs.berkeley.edu/~cs188/sp08/projects/backprop/project6.html
		System.out.println("testNet :: berkeley homework  -- Start");
		double trainInputs[][] = new double[][] {   
				{  -7.7947021e-01,   8.3822138e-01},
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
		
		nn.train(inp, out, 1000);
		
		System.out.println("NN - After TRAINING");
		System.out.println(nn.toString());
		
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
