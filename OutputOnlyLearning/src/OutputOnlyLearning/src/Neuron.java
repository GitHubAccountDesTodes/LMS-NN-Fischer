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
	public int numOfWeights;
	
	/**
	 * @brief: Constructor to reserve memory
	 */
	public Neuron()
	{
		weight = new double[100];
		this.numOfWeights = 30;
	}
	
	/**
	 * @brief: setter function to set the number of weights
	 * @param numOfWeights
	 */
	public void setNumOfWeights(int numOfWeights)
	{
		//System.out.println(numOfWeights);
		weight = new double[numOfWeights];
		for (int t=0;t<numOfWeights;t++){
			weight[t]=0;
		}
		this.numOfWeights = numOfWeights;
	}
	
}

