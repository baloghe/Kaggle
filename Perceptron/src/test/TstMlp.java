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
		
		Layer lay0 = new InputLayer(4,0.2);
		System.out.println("LAY0");
		System.out.println(lay0.toString());
		
		lay0.setup();
		System.out.println("LAY0 - after setup");
		System.out.println(lay0.toString());
		
		lay0.calcFeedForward(inputs);
		System.out.println("LAY0 - after feedforward");
		System.out.println(lay0.toString());
		
		Layer lay1 = new Layer(lay0,6,0.2);
		System.out.println("LAY1");
		System.out.println(lay1.toString());
		
		lay1.setup();
		System.out.println("LAY1 - after setup");
		System.out.println(lay1.toString());
		
		lay1.calcFeedForward(lay0.getResults());
		System.out.println("LAY1 - after feedforward");
		System.out.println(lay1.toString());
		
		OutputLayer lay2 = new OutputLayer(lay1,14,0.2);
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
		
		NeuralNet nn = new NeuralNet( new int[]{4,6,14}, 0.2 );
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
		
		NeuralNet nn = new NeuralNet( new int[]{2,2,1}, 0.2 );
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
		int epoch_num = 10000;
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<sample_num; i++){
			double[] coords = getRandomCoords();
			inp.add(coords);
			out.add(getPositionVector(coords));
		}
		
		System.out.println("inp");
		System.out.println(Util.doubleMatrixToString(inp));
		System.out.println("out");
		System.out.println(Util.doubleMatrixToString(out));
		
		NeuralNet nn = new NeuralNet( new int[]{2,8,8,8,5}, 0.2 );
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
	
	private double[] getRandomCoords(){
		return new double[]{
				new Random().nextDouble() , new Random().nextDouble()
		};
	}
	
	private double[] getDiscretizedInput(double[] inCoords){
		return null;
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
			return new double[]{1,0,0,0,0};
		else if(inCoords[0] <= 0.5 && inCoords[1] > 0.5)
			return new double[]{0,1,0,0,0};
		else if(inCoords[0] > 0.5 && inCoords[1] > 0.5)
			return new double[]{0,0,1,0,0};
		else if(inCoords[0] > 0.5 && inCoords[1] <= 0.5)
			return new double[]{0,0,0,1,0};
		else return new double[]{0,0,0,0,1};
	}
	
}
