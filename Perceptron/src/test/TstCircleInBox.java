package test;

import java.util.ArrayList;
import java.util.Random;

import mlp.NeuralNet;
import mlp.TanHFunction;
import mlp.Util;

import org.junit.Assert;
import org.junit.Test;

/**
 * A circle divides its surrounding box into five areas. The task is to "predict" which area a randomly chosen point would fall into, based on its coordinates.
 * 
 * In order to formulate the problem in the "language" of neural nets, the box is split up into smaller boxes. The randomly chosen point would fall exactly in one of them.
 * The five different area would also be encoded into a tuple. 
 * 
 * To train the net, a number of randomly chosen point would be generated. In each case, the expected outcome is calculated (given the coordinates of the point). 
 * The point will then be "discretized" and thus fed into the net. 
 * 
 * Note that as the border of the circle divides each small box into two different part it could very well happen, that the same small box ("discretized" 2D point) 
 * could once be labeled as within the circle and sometimes labeled as being outside of the circle (in one of the remaining 4 areas).
 * 
 */
public class TstCircleInBox {

	@Test
	public void testNet(){
		
		int sample_num = 5000;
		int epoch_num = 1000;
		
		int splitnum = 20;
		
		ArrayList<double[]> inp = new ArrayList<double[]>();
		ArrayList<double[]> out = new ArrayList<double[]>();
		
		for(int i=0; i<sample_num; i++){
			double[] coords = getRandomCoords();
			inp.add( getDiscretizedInput(coords, splitnum) );
			out.add( getPositionVector(coords) );
		}
		
		NeuralNet nn = new NeuralNet( new int[]{splitnum * splitnum,20,20,5}, 0.01, new TanHFunction() );
		nn.init();
		
		nn.train(inp, out, epoch_num);
		
		System.out.println("NN - trial");
		System.out.println("TestCases");
		double[] trial, prediction;
		
		trial = new double[]{0.5 , 0.5};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		prediction = nn.predict( getDiscretizedInput(trial, splitnum) );
		System.out.println("  Output: " + Util.doubleArrayToString( prediction ) );
		Assert.assertArrayEquals(getPositionVector(trial), translatePrediction( prediction ), 0.01);
		
		trial = new double[]{0.04 , 0.96};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		prediction = nn.predict( getDiscretizedInput(trial, splitnum) );
		System.out.println("  Output: " + Util.doubleArrayToString( prediction ) );
		Assert.assertArrayEquals(getPositionVector(trial), translatePrediction( prediction ), 0.01);
		
		trial = new double[]{0.96 , 0.96};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		prediction = nn.predict( getDiscretizedInput(trial, splitnum) );
		System.out.println("  Output: " + Util.doubleArrayToString( prediction ) );
		Assert.assertArrayEquals(getPositionVector(trial), translatePrediction( prediction ), 0.01);
		
		trial = new double[]{0.96 , 0.04};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		prediction = nn.predict( getDiscretizedInput(trial, splitnum) );
		System.out.println("  Output: " + Util.doubleArrayToString( prediction ) );
		Assert.assertArrayEquals(getPositionVector(trial), translatePrediction( prediction ), 0.01);
		
		trial = new double[]{0.04 , 0.04};
		System.out.println("  Input : " + Util.doubleArrayToString(trial));
		System.out.println("  Target: " + Util.doubleArrayToString(getPositionVector(trial)));
		prediction = nn.predict( getDiscretizedInput(trial, splitnum) );
		System.out.println("  Output: " + Util.doubleArrayToString( prediction ) );
		Assert.assertArrayEquals(getPositionVector(trial), translatePrediction( prediction ), 0.01);
		
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
	
	private double[] translatePrediction(double[] inPrediction){
		double[] ret = new double[inPrediction.length];
		for(int i=0; i<ret.length; i++){
			ret[i] = (inPrediction[i] > 0.0) ? 1.0 : -1.0;
		}
		return ret;
	}
	
}
