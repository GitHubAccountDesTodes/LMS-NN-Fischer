/************************************************************************
* \brief: implementation of a network of neurons and its activation     *    
*         method                                                        *
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : Network.java                                                  *
*************************************************************************/

public class Network {
	public Neuron neuron[];
	public int numOfWeights, numOfInputs, numOfNeurons;
	
	/**
	 * @brief: the Constructor to initialize the neural network
	 * @param numOfInputs
	 * @param numOfNeurons
	 */
	public Network( int numOfInputs,int numOfNeurons)
	{
		//neuron = new Neuron[100];
		neuron = new Neuron[numOfNeurons];
	//	System.out.println("Num="+numOfInputs+","+numOfNeurons);
		for (int t=0;t<numOfNeurons;t++){
			neuron[t] = new Neuron();
			neuron[t].setNumOfWeights( numOfNeurons + numOfInputs );
		}
		this.numOfWeights = numOfNeurons + numOfInputs;
		this.numOfInputs  = numOfInputs;
		this.numOfNeurons = numOfNeurons;

	}

	/**
	 * @brief: The Threshold function, the neurons implement
	 * @param x: needed for the mapping f(x) = ...
	 * @return f(x)
	 */
	public double ThreshFunction(double x)
	{
	 double y;	
	 y=1-2.0/(Math.exp(x*2.0)+1.0);  // Tan hyperbolicus

	// y=1.0/(1.0+Math.exp(-x)); // standard Sigmoid;
	 return y;
	}
	/**
	 * The inverse threshold function
	 * @param x: is the output value
	 * @return: the activation value
	 */
	public double invThreshFunction(double x)
	{
		double y;
		// --- x=Math.tanh=1-2/(exp(2y)+1)	-> 2/(1-x)=exp(2y)+1
		y = Math.log(2.0/(1.0-x)-1.0)/2.0; // atanh
		
		// y=1.0/(1.0+exp(-x)); -> log(1.0/y-1.0)=-x
		//	y=-log(1.0/x-1.0);

		return y;// linear neurons
	}

	/**
	 * @brief: Initializes the neurons with random weights
	 */
	public void randInitialize()
	{
		for (int t=0; t<numOfNeurons; t++){
			for (int i=0;i<numOfInputs;i++){
				neuron[t].weight[i] = Math.random()*2.0-1.0;
			}
		}
	}
	
	/**
	 * @brief: setter function to set the neural weights
	 * @param neuronNum
	 * @param weightNum
	 * @param weightValue
	 */
	public void setWeight(int neuronNum, int weightNum, double weightValue)
	{
		neuron[neuronNum].weight[weightNum] = weightValue;
	}
	
	/**
	 * @brief: activation method
	 * @param inVector
	 */
	public void activate(double inVector[])
	{
		
		//System.out.println("NeuronNum:"+numOfNeurons+"NumOfWeights"+numOfWeights);
		for (int t=0; t<numOfNeurons; t++){
			double sum = 0;
			for (int i=0;i<numOfWeights;i++){
				if (i<numOfInputs){
					sum += neuron[t].weight[i] * inVector[i];
				}
				if (i>=numOfInputs && (i-numOfInputs!=t)){
					sum += neuron[t].weight[i] * neuron[i-numOfInputs].output;
				}	
			}
			neuron[t].output = ThreshFunction(sum);
		}
	}

}
