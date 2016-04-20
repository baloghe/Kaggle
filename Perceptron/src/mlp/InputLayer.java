package mlp;

public class InputLayer extends Layer {

	public InputLayer(int inNeuronNum, double inLearnRate){
		super(null, inNeuronNum, inLearnRate);
	}
	
	@Override
	public void calcFeedForward(double[] inInputValues){
		results = inInputValues;
	}
	
	
}
