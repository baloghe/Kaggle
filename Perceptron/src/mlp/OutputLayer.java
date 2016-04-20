package mlp;

public class OutputLayer extends Layer {
	
	private double[] target;
	
	public OutputLayer(Layer inPrecLayer, int inNeuronNum, double inLearnRate){
		super(inPrecLayer, inNeuronNum, inLearnRate);
	}
	
	public void propagateError(double[] inTarget){
		target = inTarget;
		for(int i=0; i<this.getNeuronNumber(); i++){
			errors[i] = (target[i] - results[i]) * Util.sigmoidDerivative(results[i]);
		}
	}
	
	public double calcSSE(){
		double ret = 0.0;
		for(int i=0; i<target.length; i++){
			ret += ( (target[i] - results[i]) * (target[i] - results[i]) );
		}
		return ret/2.0;
	}
	
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
