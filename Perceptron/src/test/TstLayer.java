package test;

import org.junit.Test;

import java.util.ArrayList;

import org.junit.Assert;

import java.util.Random;

import mlp.*;

public class TstLayer {

	@Test
	public void testLayers_TanH(){
		double[] inputs1 = new double[]{ -7.7947021e-01,   8.3822138e-01 };
		double[] outputs1 = new double[]{ 1 };
		
		double[] inputs2 = new double[]{ -1.9598312e-01,  -3.7548716e-01 };
		double[] outputs2 = new double[]{ -1 };
		
		//Creation and FeedForward -- Pass #1
		
		Layer lay0 = new InputLayer(2,0.01, new TanHFunction());
		System.out.println("LAY0 - #1");
		System.out.println(lay0.toString());
		
		lay0.setup();
		System.out.println("LAY0 - after setup - #1");
		System.out.println(lay0.toString());
		
		lay0.calcFeedForward(inputs1);
		System.out.println("LAY0 - after feedforward - #1");
		System.out.println(lay0.toString());
		
		Layer lay1 = new Layer(lay0,3,0.01, new TanHFunction());
		System.out.println("LAY1");
		System.out.println(lay1.toString());
		
		lay1.setup();
		System.out.println("LAY1 - after setup - #1");
		System.out.println(lay1.toString());
		
		lay1.calcFeedForward(lay0.getResults());
		System.out.println("LAY1 - after feedforward - #1");
		System.out.println(lay1.toString());
		
		OutputLayer lay2 = new OutputLayer(lay1,1,0.01, new TanHFunction());
		System.out.println("LAY2");
		System.out.println(lay2.toString());
		
		lay2.setup();
		System.out.println("LAY2 - after setup - #1");
		System.out.println(lay2.toString());
		
		lay2.calcFeedForward(lay1.getResults());
		System.out.println("LAY2 - after feedforward - #1");
		System.out.println(lay2.toString());
		
		//Backpropagation of errors -- Pass #1
		
		System.out.println("LAY2 - errors - #1");
		System.out.println(Util.doubleArrayToString(lay2.getErrors()));
		
		lay2.propagateError(outputs1);
		System.out.println("LAY2 - after error backprop - #1");
		System.out.println(lay2.toString());
		
		lay1.propagateError(lay2);
		System.out.println("LAY1 - after error backprop - #1");
		System.out.println(lay1.toString());
		
		//Weight update -- Pass #1
		
		lay2.updateWeights(lay1);
		System.out.println("LAY2 - after weight update - #1");
		System.out.println(lay2.toString());
		
		lay1.updateWeights(lay0);
		System.out.println("LAY1 - after weight update - #1");
		System.out.println(lay1.toString());
		
		//FeedForward -- Pass #2
		
		lay0.calcFeedForward(inputs2);
		System.out.println("LAY0 - after feedforward - #2");
		System.out.println(lay0.toString());
		
		lay1.calcFeedForward(lay0.getResults());
		System.out.println("LAY1 - after feedforward - #2");
		System.out.println(lay1.toString());
		
		lay2.calcFeedForward(lay1.getResults());
		System.out.println("LAY2 - after feedforward - #2");
		System.out.println(lay2.toString());
		
		//Backpropagation of errors -- Pass #2
		
		System.out.println("LAY2 - errors - #2");
		System.out.println(Util.doubleArrayToString(lay2.getErrors()));
		
		lay2.propagateError(outputs2);
		System.out.println("LAY2 - after error backprop - #2");
		System.out.println(lay2.toString());
		
		lay1.propagateError(lay2);
		System.out.println("LAY1 - after error backprop - #2");
		System.out.println(lay1.toString());
		
		//Weight update -- Pass #2
		
		lay2.updateWeights(lay1);
		System.out.println("LAY2 - after weight update - #2");
		System.out.println(lay2.toString());
		
		lay1.updateWeights(lay0);
		System.out.println("LAY1 - after weight update - #2");
		System.out.println(lay1.toString());
		
		//FeedForward -- Pass #3
		
		lay0.calcFeedForward(inputs1);
		System.out.println("LAY0 - after feedforward - #3");
		System.out.println(lay0.toString());
		
		lay1.calcFeedForward(lay0.getResults());
		System.out.println("LAY1 - after feedforward - #3");
		System.out.println(lay1.toString());
		
		lay2.calcFeedForward(lay1.getResults());
		System.out.println("LAY2 - after feedforward - #3");
		System.out.println(lay2.toString());
		
		//Backpropagation of errors -- Pass #3
		
		System.out.println("LAY2 - errors - #3");
		System.out.println(Util.doubleArrayToString(lay2.getErrors()));
		
		lay2.propagateError(outputs1);
		System.out.println("LAY2 - after error backprop - #3");
		System.out.println(lay2.toString());
		
		lay1.propagateError(lay2);
		System.out.println("LAY1 - after error backprop - #3");
		System.out.println(lay1.toString());
		
		//Weight update -- Pass #3
		
		lay2.updateWeights(lay1);
		System.out.println("LAY2 - after weight update - #3");
		System.out.println(lay2.toString());
		
		lay1.updateWeights(lay0);
		System.out.println("LAY1 - after weight update - #3");
		System.out.println(lay1.toString());
		
		//FeedForward -- Pass #4
		
		lay0.calcFeedForward(inputs2);
		System.out.println("LAY0 - after feedforward - #4");
		System.out.println(lay0.toString());
		
		lay1.calcFeedForward(lay0.getResults());
		System.out.println("LAY1 - after feedforward - #4");
		System.out.println(lay1.toString());
		
		lay2.calcFeedForward(lay1.getResults());
		System.out.println("LAY2 - after feedforward - #4");
		System.out.println(lay2.toString());
		
		//Backpropagation of errors -- Pass #4
		
		System.out.println("LAY2 - errors - #4");
		System.out.println(Util.doubleArrayToString(lay2.getErrors()));
		
		lay2.propagateError(outputs2);
		System.out.println("LAY2 - after error backprop - #4");
		System.out.println(lay2.toString());
		
		lay1.propagateError(lay2);
		System.out.println("LAY1 - after error backprop - #4");
		System.out.println(lay1.toString());
		
		//Weight update -- Pass #3
		
		lay2.updateWeights(lay1);
		System.out.println("LAY2 - after weight update - #4");
		System.out.println(lay2.toString());
		
		lay1.updateWeights(lay0);
		System.out.println("LAY1 - after weight update - #4");
		System.out.println(lay1.toString());
		
	}
	
}
