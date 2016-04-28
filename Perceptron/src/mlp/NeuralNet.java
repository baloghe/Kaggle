package mlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Neural net implementation. Features:
 * More than one hidden layers can be set
 * Layers may have different learning rates and activation functions
 * All nodes are supposed to be connected between subsequent layers
 * Feed-forward and error back-propagation is used
 * Collects SSE values after each sample
 */
public class NeuralNet {

	private ArrayList<Layer> layers;
	private InputLayer input_layer;
	private OutputLayer output_layer;
	private ArrayList<Double> sse;
	private int cnt_sample;
	private int cnt_epoch;
	
	/**
	 * general constructor 
	 * @param inLayerSizes array containing number of neurons (layer-by-layer, starting from the input layer)
	 * @param inLearnRates array containing learning rates (layer-by-layer, starting from the input layer)
	 * @param inActFuncs array containing activation functions (layer-by-layer, starting from the input layer)
	 */
	public NeuralNet(int[] inLayerSizes
			        ,double[] inLearnRates
			        ,ActivationFunction[] inActFuncs){
		performCreation(inLayerSizes, inLearnRates, inActFuncs);
	}
	
	/**
	 * constructor supposing learning rates and activation functions are identical in all layers
	 * @param inLayerSizes array containing number of neurons (layer-by-layer, starting from the input layer)
	 * @param inCommonLearnRate common learning rate to be applied in each layer
	 * @param inCommonActFunc common activation function to be applied in each layer
	 */
	public NeuralNet(int[] inLayerSizes, double inCommonLearnRate, ActivationFunction inCommonActFunc){
		double[] lrs = new double[inLayerSizes.length];
		ActivationFunction[] afs = new ActivationFunction[inLayerSizes.length];
		for(int i=0; i<lrs.length; i++) lrs[i] = inCommonLearnRate;
		for(int i=0; i<lrs.length; i++) afs[i] = inCommonActFunc;
		performCreation(inLayerSizes, lrs, afs);
	}
	
	private void performCreation(int[] inLayerSizes, double[] inLearnRates, ActivationFunction[] inActFuncs){
		layers = new ArrayList<Layer>();
		
		//Input layer
		InputLayer lin = new InputLayer(inLayerSizes[0], inLearnRates[0], inActFuncs[0]);
		layers.add(lin);
		input_layer = lin;
		//Hidden layers
		for(int i=1; i<inLayerSizes.length-1; i++){
			Layer lay = new Layer(layers.get(i-1), inLayerSizes[i], inLearnRates[i], inActFuncs[i]);
			layers.add(lay);
		}
		//Output layer
		OutputLayer lout = new OutputLayer(layers.get(layers.size()-1), inLayerSizes[inLayerSizes.length-1], inLearnRates[inLearnRates.length-1], inActFuncs[inActFuncs.length-1]);
		layers.add(lout);
		output_layer = lout;
		
	}
	
	/**
	 * initialization method for the net: calls setup() in each layers, initializes SSE
	 */
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
	
	/**
	 * runs a single sample through the net and back-propagates errors
	 * @param inInput sample matching the size of the input layer
	 * @param inOutput target values matching the size of the output layer
	 */
	public void runSample(double[] inInput, double[] inOutput){
		//increase sample counter
		cnt_sample++;
		
		//set inputs
		input_layer.calcFeedForward(inInput);
		
		//feedforward
		for(int i=1; i<layers.size(); i++){
			Layer actL = layers.get(i);
			Layer prvL = layers.get(i-1);
			actL.calcFeedForward(prvL.getResults());
		}
		
		//if(cnt_sample == 1) System.out.println("1st sample, BEFORE feedfw #---\n" + this.toString() + "\n---");
		
		//backpropagate -- output layer == set outputs
		output_layer.propagateError(inOutput);
		
		//backpropagate -- hidden layers
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
		
		//System.out.println("NN after Sample #" + cnt_sample + "---\n" + this.toString() + "\n---");
	}
	
	/**
	 * trains the net with a given set of inputs. Samples are fed in at a random order in each epoch
	 * @param inInputs samples
	 * @param inOutputs target values 
	 * @param inNumEpochs number of training epochs
	 */
	public void train(ArrayList<double[]> inInputs, ArrayList<double[]> inOutputs, int inNumEpochs){
		
		//create vector [0 .. N-1] to randomize sample order within epochs
		List<Integer> order = new ArrayList<>();
		for(int i=0; i<inInputs.size(); i++){
			order.add(i);
		}
		
		//run epochs x samplesize samples through the net
		for(int j=0; j<inNumEpochs; j++){
			cnt_epoch++;
			Collections.shuffle(order);
			for(Integer I : order){
				int i = I.intValue();
				runSample( inInputs.get(i) , inOutputs.get(i) );
			}//next i
		}//next j
		
	}
	
	/**
	 * prediction for a given input
	 * @param inInput input matching the size of the input layer
	 * @return output matching the size of the output layer
	 */
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
		double[] ret = new double[output_layer.getNeuronNumber()];
		for(int i=0; i<ret.length; i++){
			ret[i] = output_layer.getResults()[i];
		}
		return ret;
	}
	
	/**
	 * returns predictions for a given input set
	 * @param inInputs ArrayList of inputs matching the size of the input layer
	 * @return ArrayList of outputs matching the size of the output layer
	 */
	public ArrayList<double[]> massPredict(ArrayList<double[]> inInputs){
		ArrayList<double[]> ret = new ArrayList<double[]>();
		
		for(double[] sample : inInputs){
			ret.add( this.predict(sample) );
		}
		
		return ret;
	}
	
	/**
	 * returns the SSE history built while training the net
	 * @return SSE history
	 */
	public ArrayList<Double> getSSE(){
		return this.sse;
	}
	
	/**
	 * String representation calling toString() for all layers
	 */
	public String toString(){
		String ret = "";
		int cnt = 0;
		for(Layer lay : this.layers){
			ret += (cnt + ". Layer  -- START\n");
			ret += (lay.toString() + "\n");
			ret += (cnt + ". Layer  -- END\n");
			cnt++;
		}
		return ret;
	}
}

