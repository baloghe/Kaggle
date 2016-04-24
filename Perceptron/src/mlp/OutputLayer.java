package mlp;

/**
 * Output layer contains some overwritten methods in comparison to Layer:
 * target values can be set,
 * SSE (Sum of Squares Error) can be calculated
 * String representation includes target values as well
 */
public class OutputLayer extends Layer {
	
	private double[] target;
	
	/**
	 * parent class constructor is called with the same parameters
	 * @param inPrecLayer
	 * @param inNeuronNum
	 * @param inLearnRate
	 * @param inActFunc
	 */
	public OutputLayer(Layer inPrecLayer, int inNeuronNum, double inLearnRate, ActivationFunction inActFunc){
		super(inPrecLayer, inNeuronNum, inLearnRate, inActFunc);
	}
	
	/**
	 * set target values
	 * @param inTarget target values
	 */
	public void propagateError(double[] inTarget){
		target = inTarget;
		for(int i=0; i<this.getNeuronNumber(); i++){
			errors[i] = (target[i] - results[i]) * activ_func.getDerivative(results[i]);
			/*
			System.out.println("errors["+i+"] := (" + Util.doubleToString(target[i])
            		+ " - " + Util.doubleToString(results[i]) + ") "
            		+ " * " + Util.doubleToString( activ_func.getDerivative(results[i]) )  
            		+ " = " + Util.doubleToString(errors[i]));
            */
		}
	}
	
	/**
	 * SSE (Sum of Squares Error) calculation
	 * @return SSE
	 */
	public double calcSSE(){
		double ret = 0.0;
		for(int i=0; i<target.length; i++){
			ret += ( (target[i] - results[i]) * (target[i] - results[i]) );
		}
		return ret/2.0;
	}
	
	/**
	 * String representation containing target, results, errors, bias and weights
	 */
	public String toString(){
		String ret = "Layer: NeuronNum=" + neuron_num + "\t" + "LearnRate=" + Util.doubleToString(this.learn_rate) + "\n"
				   + "target=\n" + Util.doubleArrayToString(this.target) + "\n"
				   + "results=\n" + Util.doubleArrayToString(this.results) + "\n"
				   + "errors=\n" + Util.doubleArrayToString(this.errors) + "\n"
				   + "bias=\n" + Util.doubleArrayToString(this.bias) + "\n"
				   + "weights=\n" + Util.doubleMatrixToString(this.weights) + "\n"
				   + "---"
				   ;
		return ret;
	}
}
