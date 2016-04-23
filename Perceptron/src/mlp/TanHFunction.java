package mlp;

/**
 * Tangent Hyperbolic function, Domain: [-inf , inf]; Range: (-1 , 1)
 */
public class TanHFunction implements ActivationFunction {
	public double getValue(double inValue){
		return (Math.exp(inValue) - Math.exp(-inValue))/(Math.exp(inValue) + Math.exp(-inValue));
	}
	public double getDerivative(double inValue){
		double temp = 2.0 / (Math.exp(inValue) + Math.exp(-inValue));
    	return (temp * temp);
	}
}
