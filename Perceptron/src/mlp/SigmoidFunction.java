package mlp;
/**
 * Sigmoid function, Domain: [-inf , inf]; Range: (0 , 1)
 */
public class SigmoidFunction implements ActivationFunction {
	public double getValue(double inValue){
		return (1.0 / (1.0 + Math.exp(-inValue)));
	}
	public double getDerivative(double inValue){
		return (inValue * (1.0 - inValue));
	}
}
