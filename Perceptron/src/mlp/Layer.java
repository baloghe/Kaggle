package mlp;

import java.util.Random;

public class Layer {

	protected int neuron_num;
	protected double[] bias;
	protected Layer prec_layer;
	protected double learn_rate;
	
	protected double[][] weights; //weights[i][j]: Neuron(i) in PRECEDING layer to Neuron(j) in THIS layer
	protected double[] results;
	protected double[] errors;
	
	public Layer(Layer inPrecLayer, int inNeuronNum, double inLearnRate){
		prec_layer = inPrecLayer;
		neuron_num = inNeuronNum;
		learn_rate = inLearnRate;
	}
	
	public void setup(){
		//create private vars
		this.bias = new double[neuron_num];
		this.results = new double[neuron_num];
		this.errors = new double[neuron_num];
		if(prec_layer != null){
			weights = new double[prec_layer.getNeuronNumber()][this.neuron_num];
			//assign random weights and biases
			assignRandomWeights();
		}
	}
	
	public void calcFeedForward(double[] precLayerOutputs){
		
		//System.out.println("calcFeedForward :: in=" + Util.doubleArrayToString(precLayerOutputs)  );
		//System.out.println("calcFeedForward :: weights size=" + weights.length + ", " + weights[0].length  );
		
		for(int j=0; j<neuron_num; j++){
			double sum = 0.0;
			for(int i=0; i<precLayerOutputs.length; i++){
				sum += precLayerOutputs[i] * weights[i][j];
				//System.out.println("calcFeedForward :: i=" + i + ",j=" + j + " -> sum=" + Util.doubleToString(sum));
			}
			//add bias
			sum += bias[j];
			//take SIGMOID(.) as result
			results[j] = Util.sigmoid(sum);
			//System.out.println("results[" + j + "]=" + Util.doubleToString(results[j]));
		}
		
	}
	
	public void propagateError(Layer nextLayer){
		
		for(int i=0; i<nextLayer.getWeights().length; i++){
			errors[i] = 0.0;
			for(int j=0; j<nextLayer.getNeuronNumber(); j++){
				errors[i] += nextLayer.getErrors()[j] * nextLayer.getWeights()[i][j];
			}
			
			//take SIGMOID_der(.) as result
			errors[i] *= Util.sigmoidDerivative(results[i]);
		}
		
	}
	
	public void updateWeights(Layer precLayer){
		
		// Update the weights
		for(int j=0; j<neuron_num; j++){
			for(int i=0; i<precLayer.getNeuronNumber(); i++){
				weights[i][j] += (learn_rate * errors[j] * precLayer.getResults()[i]);
			}
		}
		
		// Update bias
		for(int j=0; j<neuron_num; j++){
			bias[j] += (learn_rate * errors[j]);
		}
		
	}
	
	public void calcBackPropagation(double[] precLayerOutputs, double[] inErrorsFromNextLayer){
		
		// Update the weights
		for(int j=0; j<neuron_num; j++){
			for(int i=0; j<weights.length; i++){
				weights[i][j] += (learn_rate * precLayerOutputs[i] * inErrorsFromNextLayer[j]);
			}
		}
		
		// Update bias
		for(int j=0; j<neuron_num; j++){
			bias[j] += (learn_rate * inErrorsFromNextLayer[j]);
		}
		
	}
	
	protected void assignRandomWeights(){
		//assign random weights in [-0.5 , +0.5]
		for(int i=0; i<weights.length; i++){
			for(int j=0; j<weights[i].length; j++){
				weights[i][j] = new Random().nextDouble() - 0.5;
				//weights[i][j] = 0.1;
			}
		}
		//assign random bias in [-0.5 , +0.5]
		for(int j=0; j<bias.length; j++){
			bias[j] = new Random().nextDouble() - 0.5;
			//bias[j] = 0.1;
		}
	}
	
	public double[] getResults(){return results;}
	
	public int getNeuronNumber(){return neuron_num;}
	
	public double[] getErrors(){return errors;}
	
	public double[][] getWeights(){return weights;}
	
	public String toString(){
		String ret = "Layer: NeuronNum=" + neuron_num + "\t" + "LearnRate=" + Util.doubleToString(this.learn_rate) + "\n"
				   + "results=\n" + Util.doubleArrayToString(this.results) + "\n"
				   + "errors=\n" + Util.doubleArrayToString(this.errors) + "\n"
				   + "bias=\n" + Util.doubleArrayToString(this.bias) + "\n"
				   + "weights=\n" + Util.doubleMatrixToString(this.weights) + "\n"
				   + "---"
				   ;
		return ret;
	}
	
}
