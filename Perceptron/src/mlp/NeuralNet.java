package mlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class NeuralNet {

	private ArrayList<Layer> layers;
	private InputLayer input_layer;
	private OutputLayer output_layer;
	private ArrayList<Double> sse;
	private int cnt_sample;
	private int cnt_epoch;
	
	public NeuralNet(int[] inLayerSizes
			        ,double[] inLearnRates){
		performCreation(inLayerSizes, inLearnRates);
	}
	
	public NeuralNet(int[] inLayerSizes, double inCommonLearnRate){
		double[] lrs = new double[inLayerSizes.length];
		for(int i=0; i<lrs.length; i++) lrs[i] = inCommonLearnRate;
		performCreation(inLayerSizes, lrs);
	}
	
	private void performCreation(int[] inLayerSizes, double[] inLearnRates){
		layers = new ArrayList<Layer>();
		
		//Input layer
		InputLayer lin = new InputLayer(inLayerSizes[0], inLearnRates[0]);
		layers.add(lin);
		input_layer = lin;
		//Hidden layers
		for(int i=1; i<inLayerSizes.length-1; i++){
			Layer lay = new Layer(layers.get(i-1), inLayerSizes[i], inLearnRates[i]);
			layers.add(lay);
		}
		//Output layer
		OutputLayer lout = new OutputLayer(layers.get(layers.size()-1), inLayerSizes[inLayerSizes.length-1], inLearnRates[inLearnRates.length-1]);
		layers.add(lout);
		output_layer = lout;
		
	}
	
	public void init(){
		//init all layers
		for(Layer lay : layers){
			lay.setup();
		}
		
		//init SSE (Sum of Squared Errors)
		sse = new ArrayList<Double>();
		
		//set epoch and sample counter to zero
		cnt_sample = 0;
		cnt_epoch = 0;
	}
	
	public void runSample(double[] inInput, double[] inOutput){
		//increase sample counter
		cnt_sample++;
		
		//set inputs
		input_layer.calcFeedForward(inInput);
		//set outputs
		output_layer.propagateError(inOutput);
		
		//feedforward
		for(int i=1; i<layers.size(); i++){
			Layer actL = layers.get(i);
			Layer prvL = layers.get(i-1);
			actL.calcFeedForward(prvL.getResults());
		}
		
		//backpropagate
		for(int i=layers.size()-2; i>0; i--){
			Layer actL = layers.get(i);
			Layer nxtL = layers.get(i+1);
			actL.propagateError(nxtL);
		}
		
		//weight update
		for(int i=layers.size()-1; i>0; i--){
			Layer actL = layers.get(i);
			Layer prvL = layers.get(i-1);
			actL.updateWeights(prvL);
		}
		
		//account for SSE
		Double actSSE = new Double( output_layer.calcSSE() );
		sse.add(actSSE);
	}
	
	public void train(ArrayList<double[]> inInputs, ArrayList<double[]> inOutputs, int inNumEpochs){
		
		//create vector [0 .. N-1] to randomize sample order within epochs
		List<Integer> order = new ArrayList<>();
		for(int i=0; i<inInputs.size(); i++){
			order.add(i);
		}
		
		//run epocs x samplesize samples through the net
		for(int j=0; j<inNumEpochs; j++){
			cnt_epoch++;
			Collections.shuffle(order);
			for(Integer I : order){
				int i = I.intValue();
				runSample( inInputs.get(i) , inOutputs.get(i) );
			}//next i
		}//next j
		
	}
	
	public double[] predict(double[] inInput){
		
		//set inputs
		input_layer.calcFeedForward(inInput);
				
		//feedforward
		for(int i=1; i<layers.size(); i++){
			Layer actL = layers.get(i);
			Layer prvL = layers.get(i-1);
			actL.calcFeedForward(prvL.getResults());
		}
		
		//return Results from the output layer
		return output_layer.getResults();
	}
}

