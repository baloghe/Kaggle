package mlp;

/**
 * Activation function in a Neural Net layer
 *
 */
public interface ActivationFunction {
	/**
	 * returns the function value at a given point
	 * @param inValue given point
	 * @return function value
	 */
	public double getValue(double inValue);
	
	/**
	 * returns the derivative value at a given point
	 * @param inValue given point
	 * @return derivative value
	 */
	public double getDerivative(double inValue);
}
