package mlp;

/**
 * Neural net Hidden layer. Input and Output layer are specializations of this class.
 * Chosen activation function applies for all neurons in the layer
 * Each neurons have a bias value.
 * Weights and biases are initialized by random values
 * 
 */
public class Layer {

	/**
	 * Bias and Weights values are initialized in range ( -RANDOM_RANGE , +RANDOM_RANGE)
	 */
	public double RANDOM_RANGE = 0.5;
	
	/**
	 * chosen activation function
	 */
	protected ActivationFunction activ_func;
	
	protected int neuron_num;
	protected double[] bias;
	protected Layer prec_layer;
	
	/**
	 * learning rate (a.k.a RHO, sometimes ETHA)
	 */
	protected double learn_rate;
	
	/**
	 * weight matrix. Dimensions: [neuron_num of preceding layer] x [neuron_num of THIS layer]
	 */
	protected double[][] weights; //weights[i][j]: Neuron(i) in PRECEDING layer to Neuron(j) in THIS layer
	protected double[] results;
	protected double[] errors;
	
	/**
	 * general constructor
	 * @param inPrecLayer preceding layer (input layer: NULL)
	 * @param inNeuronNum number of neurons in the layer
	 * @param inLearnRate learning rate to be applied at backpropagation
	 * @param inActFunc activation function to be applied at feedforward and backpropagation
	 */
	public Layer(Layer inPrecLayer, int inNeuronNum, double inLearnRate, ActivationFunction inActFunc){
		prec_layer = inPrecLayer;
		neuron_num = inNeuronNum;
		learn_rate = inLearnRate;
		activ_func = inActFunc;
	}
	
	/**
	 * layer initialization (should be called before usage)
	 */
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
	
	/**
	 * calculated feed-forward (=results of this layer) given the results of the previous layer
	 * @param precLayerOutputs
	 */
	public void calcFeedForward(double[] precLayerOutputs){
		
		//System.out.println("calcFeedForward :: in=" + Util.doubleArrayToString(precLayerOutputs)  );
		//System.out.println("calcFeedForward :: weights size=" + weights.length + ", " + weights[0].length  );
		
		for(int j=0; j<neuron_num; j++){
			double sum = 0.0;
			for(int i=0; i<precLayerOutputs.length; i++){
				sum += ( precLayerOutputs[i] * weights[i][j] );
				//System.out.println("calcFeedForward :: i=" + i + ",j=" + j + " -> sum=" + Util.doubleToString(sum));
			}
			//add bias
			sum += bias[j];
			//take SIGMOID(.) as result
			//results[j] = Util.sigmoid(sum);
			results[j] = activ_func.getValue(sum);
			//System.out.println("results[" + j + "]=" + Util.doubleToString(results[j]));
		}
		
	}
	
	/**
	 * back-propagation step 1/2: back-propagation of errors
	 * @param nextLayer link to the next layer in net
	 */
	public void propagateError(Layer nextLayer){
		
		for(int i=0; i<nextLayer.getWeights().length; i++){
			errors[i] = 0.0;
			for(int j=0; j<nextLayer.getNeuronNumber(); j++){
				errors[i] += ( nextLayer.getErrors()[j] * nextLayer.getWeights()[i][j] );
			}
			
			//take SIGMOID_der(.) as result
			//errors[i] *= Util.sigmoidDerivative(results[i]);
			errors[i] *= activ_func.getDerivative(results[i]);
		}
		
	}
	
	/**
	 * back-propagation step 2/2: weight updates
	 * @param precLayer link to the preceding layer in net
	 */
	public void updateWeights(Layer precLayer){
		
		// Update the weights
		for(int j=0; j<neuron_num; j++){
			for(int i=0; i<precLayer.getNeuronNumber(); i++){
				weights[i][j] += ( learn_rate * errors[j] * precLayer.getResults()[i] );
			}
		}
		
		// Update bias
		for(int j=0; j<neuron_num; j++){
			bias[j] += ( learn_rate * errors[j] );
		}
		
	}
		
	/**
	 * assign random weights and bias values, by calling Util.getRandomInRange
	 */
	protected void assignRandomWeights(){
		//assign random weights in [-0.5 , +0.5]
		for(int i=0; i<weights.length; i++){
			for(int j=0; j<weights[i].length; j++){
				//weights[i][j] = new Random().nextDouble() - 0.5;
				weights[i][j] = Util.getRandomInRange(- RANDOM_RANGE, RANDOM_RANGE);
				//weights[i][j] = (i+j+1)/100.0;
			}
		}
		//assign random bias in [-0.5 , +0.5]
		for(int j=0; j<bias.length; j++){
			//bias[j] = new Random().nextDouble() - 0.5;
			bias[j] = Util.getRandomInRange(- RANDOM_RANGE, RANDOM_RANGE);
			//bias[j] = -(j+1)/100.0;
		}
	}
	
	/**
	 * calculation results of this layer
	 * @return calculation results
	 */
	public double[] getResults(){return results;}
	
	/**
	 * number of neurons
	 * @return number of neurons
	 */
	public int getNeuronNumber(){return neuron_num;}
	
	/**
	 * errors of this layer
	 * @return errors
	 */
	public double[] getErrors(){return errors;}
	
	/**
	 * weights of this layer
	 * @return weights matrix
	 */
	public double[][] getWeights(){return weights;}
	
	/**
	 * String representation containing results, errors, bias and weights
	 */
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
