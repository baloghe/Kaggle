package mlp;

/**
 * Input layer contains some overwritten methods in comparison to Layer:
 * calcFeedForward :: input values can be set
 */
public class InputLayer extends Layer {

	public InputLayer(int inNeuronNum, double inLearnRate, ActivationFunction inActFunc){
		super(null, inNeuronNum, inLearnRate, inActFunc);
	}

	@Override
	public void calcFeedForward(double[] inInputValues){
		results = inInputValues;
	}
	
	
}
