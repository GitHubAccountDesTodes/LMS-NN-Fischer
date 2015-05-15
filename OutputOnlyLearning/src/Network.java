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
	public Neuron neuron[]; //contains the neurons of numHiddens + numOutputs neurons
	public int numWeights;	//contains numHiddens + numInputs weights
	public int numInputs;
	public int numHiddens; 
	public int numOutputs; 
	
	/**
	 * @brief: the Constructor to initialize the neural network
	 * @param numInputs
	 * @param numNeurons
	 */
	public Network(int numInputs, int numHiddens, int numOutputs) {
		this.numWeights = numHiddens + numInputs;
		this.numInputs  = numInputs;
		this.numHiddens = numHiddens;
		this.numOutputs = numOutputs;
		
		neuron = new Neuron[numHiddens+numOutputs];
//		System.out.println("Num="+numInputs+","+numNeurons);
		for (int neuronNum=0;neuronNum<neuron.length;neuronNum++){
			neuron[neuronNum] = new Neuron();
			neuron[neuronNum].setNumWeights(numWeights);
		}
		randInitialization();
	}

	/**
	 * @brief: The Threshold function, the neurons implement
	 * @param x: needed for the mapping f(x) = ...
	 * @return f(x)
	 */
	public double threshFunction(double x) {
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
	public double invThreshFunction(double x) {
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
	public void randInitialization() {
		for (int hiddenNeuronNum=0;hiddenNeuronNum<numHiddens;hiddenNeuronNum++){
			for (int i=0;i<hiddenNeuronNum+numInputs;i++){
				neuron[hiddenNeuronNum].weight[i] = generateRandomValue(-1,1);
			}
		}
	}
	
	public double generateRandomValue(double von, double bis) {
		return Math.random()*(bis-von)+von;
		//Example (von -1 bis 1):
		//0.00 		  => -1
		//0.50 		  => 0.50*(1+1)-1 = 0
		//0.999999999 => 0.99*(1+1)-1 = 0.9999
	}
	
	
	/**
	 * @brief: setter function to set the neural weights
	 * @param neuronNum
	 * @param weightNum
	 * @param weightValue
	 */
	public void setWeight(int neuronNum, int weightNum, double weightValue) {
		neuron[neuronNum].weight[weightNum] = weightValue;
	}
	
	/**
	 * @brief: activation method
	 * @param inVector
	 */
	public void activate(double inVector[]) {
		
		//System.out.println("NeuronNum:"+numNeurons+"numWeights"+numWeights);
		for (int neuronNum=0; neuronNum<neuron.length; neuronNum++){
			double sum = 0;
			for (int weightNum=0;weightNum<numWeights;weightNum++){
				if (weightNum<numInputs){
					sum += neuron[neuronNum].weight[weightNum] * inVector[weightNum];
				}
				if (weightNum>=numInputs && (weightNum-numInputs!=neuronNum)){
					sum += neuron[neuronNum].weight[weightNum] * neuron[weightNum-numInputs].output;
				}
			}
			neuron[neuronNum].output = threshFunction(sum);
		}
	}

}
