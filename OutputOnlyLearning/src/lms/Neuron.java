package lms;

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

    /**
     * @brief: setter function to set the number of weights
     * @param numOfWeights
     */
    public void setNumWeights(int numWeights) {
		weight = new double[numWeights];
    }
}