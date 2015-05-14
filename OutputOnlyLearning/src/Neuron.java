/************************************************************************
* \brief: a single Neuron class                                         *
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : Neuron.java                                                   *
*************************************************************************/

public class Neuron {
	public double output;
	public double activity;
	public double weight[];
	public int numWeights;
	
	/**
	 * @brief: Constructor to reserve memory
	 */
	public Neuron() {
		
	}
	
	/**
	 * @brief: setter function to set the number of weights
	 * @param numOfWeights
	 */
	public void setNumWeights(int numWeights) {
		this.numWeights = numWeights;
		weight = new double[numWeights];
	}
}