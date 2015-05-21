/************************************************************************
 * \brief: a single Neuron class * * (c) copyright by Jörn Fischer * * *
 *
 * @autor: Prof.Dr.Jörn Fischer *
 * @email: j.fischer@hs-mannheim.de * *
 * @file : Neuron.java *
 *************************************************************************/

public class Neuron {
	public double output;
	public double weight[];
//    public double gradient;
//    public double activity;
//    public int numWeights;

	/**
	 * @param numOfWeights
	 * @brief: setter function to set the number of weights
	 */
	public void setNumOfWeights(int numWeights) {
//		this.numWeights = numWeights;
		weight = new double[numWeights];
	}

	// static private double transferFunctionDerivative(double x) {
	// // tanh derivative
	// //System.out.println("transferFunctionDerivative: "+(1.0-x*x));
	// return 1.0 - x * x;
	// }
	//
	// public void calcOutputGradients(double targetForOutput) {
	// double delta = targetForOutput - output;
	// //System.out.println("delta: "+delta);
	// gradient = delta * transferFunctionDerivative(output);
	// //System.out.println("gradient: "+gradient);
	// }
	//
	// public void calcHiddenGradients(Vector<Neuron> nextLayer) {
	// double dow = sumDOW(nextLayer);
	// gradient = dow * transferFunctionDerivative(output);
	// }
	//
	// private double sumDOW(Vector<Neuron> nextLayer) {
	// double sum = 0.0;
	//
	// // Sum our contributions of the errors at the nodes we need
	//
	// for (int neuronNum = 0; neuronNum < nextLayer.size() - 1; neuronNum++) {
	// sum += outputWeights.get(neuronNum).getWeight()
	// * nextLayer.get(neuronNum).gradient;
	// }
	// return sum;
	// }
}