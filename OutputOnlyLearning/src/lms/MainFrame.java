
package lms;

/************************************************************************
 * \brief: Main method reading in the spiral data and learning the 		*
 *         mapping using the Least Mean Squares method within a neural	*
 *         network.                                                      *
 *																		*
 * (c) copyright by J��rn Fischer										*
 *                                                                       *																		* 
 * @autor: Prof.Dr.J��rn Fischer											*
 * @email: j.fischer@hs-mannheim.de										*
 *                                                                       *
 * @file : Network.java                                                  *
 *************************************************************************/

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// private static final String inputFileName = "res/input_big2.txt";
	private static final String inputFileName = "res/input.txt";
	// private static final String inputFileName = "res/input_rect.txt";
	private static final int imageWidth = 600;
	private static final int imageHeight = 600;
	private static final int border = 100;
	private int frameWidth = imageWidth + border;
	private int frameHeight = imageHeight + border;
	ImagePanel canvas = new ImagePanel();
	public InputOutput inputOutput = new InputOutput(this);

	private Network net;
	private double[][] inputTable;
	private double bias;
	private int numInputs;
	private int numHiddens;
	private int numOutputs;
	private int MDims; // Matrix Dimensions

	/**
	 * Construct main frame
	 * 
	 * @param args
	 *            passed to MainFrame
	 */
	public static void main(String[] args) {
		new MainFrame(args);
	}

	public MainFrame(String[] args) {
		super("Output Only Learning Networks");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLocation(getX() - frameWidth / 2, getY() - frameHeight / 2);
		setVisible(true);
		setLayout(null);
		int horizontalInsets = getInsets().right;
		int verticalInsets = getInsets().top + getInsets().bottom;
		setSize(frameWidth + horizontalInsets, frameHeight + verticalInsets);

		canvas.img = createImage(imageWidth, imageHeight);
		canvas.setLocation((frameWidth - getInsets().left - imageWidth) / 2,
				(frameHeight - imageHeight) / 2);
		canvas.setSize(imageWidth, imageHeight);
		add(canvas);

		run();
	}

	/**
	 * @brief: run method calls my Main and puts the results on the screen
	 */
	public void run() {
		readInputFile();
		initialization();
		calculateLeastSquaresOptimum();
		drawMap();
		repaint();
	}

	public void readInputFile() {
		FileIOP inFile = new FileIOP(inputFileName);
		try {
			inputTable = inFile.readTable();
			numInputs = inFile.readSingleValue(1);
			// first line defines number of input neurons
			numHiddens = inFile.readSingleValue(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numOutputs = inputTable[0].length - numInputs;
		bias = inputTable[0][0];
	}

	/**
	 * zeichne Inputs in Vierecken
	 */
	public void viewInputFile() {
		// System.out.println("numInputs=" + numInputs);
		// System.out.println("numHiddens=" + numHiddens);
		// System.out.println("numOutputs=" + numOutputs);

		// draw data to learn
		for (int row = 0; row < inputTable.length - 1; row++) {
			int x = (int) (inputTable[row][1] * imageWidth);
			int y = (int) (inputTable[row][2] * imageHeight);

			// die Farbe ist von Target abh�ngig
			int color = (int) (inputTable[row][3] * 127 + 100);
			if (color < 0)
				color = 0;
			if (color > 255)
				color = 255;

			inputOutput.fillRect(x, y, 2, 2, new Color(color, 255, 100));
		}
	}

	public void initialization() {
		net = new Network(numInputs, numHiddens, numOutputs);
	}

	public void calculateLeastSquaresOptimum() {
		/**
		 * Diese Methode berechnet das least Squares Optimum bisher nur f�r das
		 * Output Neuron. F�r die Hidden Neuronen wird noch nichts berechnet.
		 */

		// --- output neuron ---

	

		 System.out.println("Before: "+getSumQuadError());
		 calculateHiddenWeights();
		 System.out.println("After : "+getSumQuadError());
		 calculateOutputWeigths();
		 System.out.println("After : "+getSumQuadError());

		// --- end output neuron ---

		// --- hidden neuron ---
			
//		double oldError;
//		double error;
//		oldError = getSumQuadError();
//		for (int i = 0; i < 100; i++) {
//			calculateHiddenWeights();
//			drawMap();
//			repaint();
//			error = getSumQuadError();
//			System.out.println("Error im Durchlauf "+i+": " + error);
//			if(oldError == error) {
//				System.out.println("Keine Gesamtverbesserung mehr - Beende Iteration");
//				break;
//			} else {
//				oldError = error;
//			}
//		}
		
//		double oldError;
//		double error;
//		oldError = getSumQuadError();
//		for (int i = 0; i < 100; i++) {
//			hillrunner_IOL_MitSchritt1();
//			drawMap();
//			repaint();
//			error = getSumQuadError();
//			System.out.println("Error im Durchlauf "+i+": " + error);
//			if(oldError == error) {
//				System.out.println("Keine Gesamtverbesserung mehr - Beende Iteration");
//				break;
//			} else {
//				oldError = error;
//			}
//		}
		
//		for (int i = 0; i < 100; i++) {
//			hillrunner_IOL();
//			drawMap();
//			repaint();
//			error = getSumQuadError();
//			System.out.println("Error im Durchlauf "+i+": " + error);
//			if(oldError == error) {
//				System.out.println("Keine Gesamtverbesserung mehr - Beende Iteration");
//				break;
//			} else {
//				oldError = error;
//			}
//		}
		
//		oldError = getSumQuadError();
//		for (int i = 0; i < 10; i++) {
//			hillrunner_outputweights();
//			error = getSumQuadError();
//			System.out.println("Error im Durchlauf "+i+": " + error);
//			if(oldError == error) {
//				System.out.println("Keine Gesamtverbesserung mehr - Beende Iteration");
//				break;
//			} else {
//				oldError = error;
//			}
//		}
//		System.out.println("Error am Ende: " + getSumQuadError());

		// --- end hidden neuron ---
	}

	public void calculateOutputWeigths() {
		double[] inVector;
		double targetForOutput;
		double activityError;
		EquationSolver equ;

		MDims = numHiddens;

		equ = new EquationSolver(MDims);

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[numInputs];

			for (int inputNum = 0; inputNum < numInputs; inputNum++) { // First
				// input
				// values
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);

			inVector = new double[MDims];
			for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
				inVector[hiddenNum] = net.neuron[hiddenNum].output;
			}
			targetForOutput = inputTable[row][numInputs];
			activityError = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();

		for (int weightNum = 0; weightNum < MDims; weightNum++) {
			net.neuron[numHiddens].weight[numInputs + weightNum] = equ.solution[weightNum]; // weight
																							// from
		}

		// Gebe Ergebnis in der Konsole aus
		// System.out.println("Solution (weights of output neuron):");
		for (int weightNum = 0; weightNum < net.numWeights; weightNum++) {
			// System.out.println("weight[" + weightNum + "]: "
			// + net.neuron[numHiddens].weight[weightNum]);
		}
	}

	public void calculateHiddenWeights() {
		double maxError;
		int candidateNeuron;
		double[] candidateTarget;

		// hillRunner();

//		hillrunner_AHNW_and_OOL();
//		hillrunner_IOL_Mit();
		//drawMap();
		//repaint();
//		for (int i=0;i<2;i++) {
//			System.out.println("Starte Durchlauf "+i+": ");
//			System.out.println("Before: "+getSumQuadError());
//			maxError = schritt3();
//			candidateTarget = schritt4(maxError);
//			//schritt5(candidateNeuron, -maxError);
//			schrittAlternative(candidateTarget,maxError);
//			System.out.println("MaxError: "+maxError);
//			System.out.println("After: "+getSumQuadError());
//			System.out.println("Beende Durchlauf "+i+"\n");
//			//drawMap();
//			//repaint();
//		}
		
//		 for (candidateNeuron=0;candidateNeuron<numHiddens;candidateNeuron++) {
//			 schritt2(candidateNeuron);
//			 maxError = schritt3();
//			 candidateTarget = schritt4(maxError);
//			 schritt5(candidateNeuron, -maxError);
//			 schritt6(candidateTarget,candidateNeuron);
//			 System.out.println("Error: "+getSumQuadError());
//		 }

		// pingsIdea();
	}
	
	public void schrittAlternative(double[] candidateTarget, double maxError) {
		double[] inVector;
		double targetForOutput;
		double activityError;
		EquationSolver equ;

		MDims = numInputs*numHiddens;

		equ = new EquationSolver(MDims);

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[MDims];

			for(int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
				for (int inputNum = 0; inputNum < numInputs; inputNum++) { // First
					// input
					// values
					inVector[(hiddenNum*numInputs)+inputNum] = inputTable[row][inputNum];
				}
			}
			net.activate(inVector);
			targetForOutput = candidateTarget[row];
			activityError = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();
		for(int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
				net.neuron[hiddenNum].weight[inputNum] = equ.solution[(hiddenNum*numInputs)+inputNum]; // weight
			}
		}
		
		for(int weightNum = numInputs; weightNum < numHiddens; weightNum++) {
			net.neuron[numHiddens].weight[weightNum] = maxError;
		}
	}

	public void hillRunner_AoW() {
		String protocol = "";
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double sign = 1;
		double increment;
		Neuron[] neuron;

		sumQuadErrorOld = getSumQuadError();
		neuron = cloneNeuron(net.neuron);
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			// //System.out.println("hiddenNum["+hiddenNum+"] start");
			for (int weightNum = 0; weightNum < numInputs; weightNum++) {
				// //System.out.println("weightNum["+weightNum+"] start");
				protocol = "";
				increment = 10;
				do {
					net.neuron[hiddenNum].weight[weightNum] += sign * increment;
					// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);
					
					if (delta > 0) {
						//System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
						//System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
						protocol += "1"; //'1' protocols the successful improvement
						neuron = cloneNeuron(net.neuron);
						sumQuadErrorOld = sumQuadErrorNew;		
					} else {
						//System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
						protocol += "0"; //'0' protocols the unsuccessful improvement
						net.neuron = cloneNeuron(neuron);
						if (protocol.endsWith("00")) {
							increment = increment / 10;
							if (increment == 0.00001) {
								break;
							}
							protocol += "D"; //'D' protocols the Decrementation of adjustment stepping size
						} else {
							sign = (-1) * sign;
						}
					}
				} while (true);
				// //System.out.println("weightNum["+weightNum+"] ende");
			}
			// //System.out.println("hiddenNum["+hiddenNum+"] ende");
		}
	}

	public void hillrunner_AHNW_and_OOL() {
		String protocol = "";
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double sumQuadErrorBegin;
		double sumQuadErrorEnd;
		double delta;
		double sign = -1;
		double increment;
		Neuron[] neuron;
		
		
		sumQuadErrorOld = getSumQuadError();
		sumQuadErrorBegin = sumQuadErrorOld;
		neuron = cloneNeuron(net.neuron);
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			// //System.out.println("hiddenNum["+hiddenNum+"] start");
			for (int weightNum = 0; weightNum < numInputs; weightNum++) {
				// //System.out.println("weightNum["+weightNum+"] start");
				protocol = "";
				increment = 10;
				do {
					net.neuron[hiddenNum].weight[weightNum] += sign * increment;
					// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);

					calculateOutputWeigths();
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);
					
					if (delta > 0) {
						//System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
						//System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
						protocol += "1"; //'1' protocols the successful improvement
						neuron = cloneNeuron(net.neuron);
						sumQuadErrorOld = sumQuadErrorNew;		
					} else {
						//System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
						protocol += "0"; //'0' protocols the unsuccessful improvement
						//net.neuron = cloneNeuron(neuron);
						if (protocol.endsWith("00")) {
							increment = increment / 10;
							if (increment == 0.00001) {
								break;
							}
							protocol += "D"; //'D' protocols the Decrementation of adjustment stepping size
						} else {
							sign = (-1) * sign;
						}
					}
				} while (true);
				// //System.out.println("weightNum["+weightNum+"] ende");
			}
			// //System.out.println("hiddenNum["+hiddenNum+"] ende");
		}
		sumQuadErrorEnd = getSumQuadError();
		if(sumQuadErrorEnd > sumQuadErrorBegin) {
			net.neuron = neuron;
		}
	}
	
	public void hillrunner_AHNW_and_OOL_02() {
		String protocol = "";
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double sign = -1;
		double increment;
		//Neuron[] neuron;

		sumQuadErrorOld = getSumQuadError();
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			// //System.out.println("hiddenNum["+hiddenNum+"] start");
			for (int weightNum = 0; weightNum < numInputs; weightNum++) {
				// //System.out.println("weightNum["+weightNum+"] start");
				protocol = "";
				increment = 10;
				//neuron = cloneNeuron(net.neuron);
				do {
					net.neuron[hiddenNum].weight[weightNum] += sign * increment;
					// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);

					calculateOutputWeigths();
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);
					
					if (delta > 0) {
						//System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
						//System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
						protocol += "1"; //'1' protocols the successful improvement
						//neuron = cloneNeuron(net.neuron);
						sumQuadErrorOld = sumQuadErrorNew;		
					} else {
						//System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
						protocol += "0"; //'0' protocols the unsuccessful improvement
						//net.neuron = cloneNeuron(neuron);
						if (protocol.endsWith("00")) {
							increment = increment / 10;
							if (increment == 0.00001) {
								break;
							}
							protocol += "D"; //'D' protocols the Decrementation of adjustment stepping size
						} else {
							sign = (-1) * sign;
						}
					}
				} while (true);
				// //System.out.println("weightNum["+weightNum+"] ende");
			}
			// //System.out.println("hiddenNum["+hiddenNum+"] ende");
		}
	}
	
	public void hillrunner_AONW_and_OOL() {
		String protocol = "";
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double sign = -1;
		double increment;
		Neuron[] neuron;
		
		sumQuadErrorOld = getSumQuadError();
		neuron = cloneNeuron(net.neuron);
		for (int weightNum = 0; weightNum < numInputs; weightNum++) {
			// //System.out.println("weightNum["+weightNum+"] start");
			protocol = "";
			increment = 10;
			do {
				net.neuron[numHiddens].weight[weightNum] += sign * increment;
				// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);
				calculateOutputWeigths();
				sumQuadErrorNew = getSumQuadError();
				delta = Math.abs(sumQuadErrorOld)
						- Math.abs(sumQuadErrorNew);
				
				if (delta > 0) {
					//System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
					//System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
					protocol += "1"; //'1' protocols the successful improvement
					neuron = cloneNeuron(net.neuron);
					sumQuadErrorOld = sumQuadErrorNew;		
				} else {
					//System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
					protocol += "0"; //'0' protocols the unsuccessful improvement
					net.neuron = cloneNeuron(neuron);
					if (protocol.endsWith("00")) {
						increment = increment / 10;
						if (increment == 0.00001) {
							break;
						}
						protocol += "D"; //'D' protocols the Decrementation of adjustment stepping size
					} else {
						sign = (-1) * sign;
					}
				}
			} while (true);
			// //System.out.println("weightNum["+weightNum+"] ende");
		}
	}
	
	public void hillrunner_IOL() {
		Neuron[] neuron;
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double maxError;
		double[] candidateTarget;
		
		sumQuadErrorOld = getSumQuadError();
		neuron = cloneNeuron(net.neuron);
		for(int hiddenNum=0; hiddenNum<numHiddens; hiddenNum++) {
			
			schritt2(hiddenNum);
			maxError = schritt3();
			candidateTarget = schritt4(maxError);
			schritt5(hiddenNum, -maxError);
			schritt6(candidateTarget,hiddenNum);
			
			sumQuadErrorNew = getSumQuadError();
			delta = Math.abs(sumQuadErrorOld)
					- Math.abs(sumQuadErrorNew);
			//System.out.println("vorher:"+sumQuadErrorOld+", nacher: "+sumQuadErrorNew+", delta: "+delta);
			//System.out.println("vorher:"+neuron[hiddenNum].weight[0]+"\nnacher: "+net.neuron[hiddenNum].weight[0]);
			if (delta > 0) {
				System.out.println("hiddenNum "+hiddenNum+" wurde verbessert");
				System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
				neuron = cloneNeuron(net.neuron);
				sumQuadErrorOld = sumQuadErrorNew;
			} else {
				//System.out.println("hiddenNum "+hiddenNum+" verschlechtert");
				net.neuron = cloneNeuron(neuron);
			}
		}
	}
	
	public void hillrunner_IOL_MitSchritt1() {
		Neuron[] neuron;
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double maxError;
		double[] candidateTarget;
		int candidateNeuron;
		
		sumQuadErrorOld = getSumQuadError();
		neuron = cloneNeuron(net.neuron);
		
		candidateNeuron = schritt1();
		schritt2(candidateNeuron);
		maxError = schritt3();
		candidateTarget = schritt4(maxError);
		schritt5(candidateNeuron, -maxError);
		schritt6(candidateTarget,candidateNeuron);
		
		sumQuadErrorNew = getSumQuadError();
		delta = Math.abs(sumQuadErrorOld)
				- Math.abs(sumQuadErrorNew);
		//System.out.println("vorher:"+sumQuadErrorOld+", nacher: "+sumQuadErrorNew+", delta: "+delta);
		//System.out.println("vorher:"+neuron[hiddenNum].weight[0]+"\nnacher: "+net.neuron[hiddenNum].weight[0]);
		if (delta > 0) {
			System.out.println("hiddenNum "+candidateNeuron+" wurde verbessert");
			System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
			sumQuadErrorOld = sumQuadErrorNew;
		} else {
			//System.out.println("hiddenNum "+hiddenNum+" verschlechtert");
			net.neuron = cloneNeuron(neuron);
		}
	}
	
	public void hillrunner_AONW_and_IOL() {
		String protocol = "";
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double sign = -1;
		double increment;

		sumQuadErrorOld = getSumQuadError();
		for (int weightNum = 0; weightNum < numInputs; weightNum++) {
			// //System.out.println("weightNum["+weightNum+"] start");
			protocol = "";
			increment = 10;
			do {
				net.neuron[numHiddens].weight[weightNum] += sign * increment;
				// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);
				calculateOutputWeigths();
				sumQuadErrorNew = getSumQuadError();
				delta = Math.abs(sumQuadErrorOld)
						- Math.abs(sumQuadErrorNew);
				// System.out.println("vorher:"+sumQuadErrorOld+", nacher: "+sumQuadErrorNew+", delta: "+delta+"\nincrement: "+increment+", weight: "+net.neuron[hiddenNum].weight[weightNum]);
				if (delta > 0) {
					 System.out.println("besser");
					if (protocol.endsWith("010")) {
						increment = increment / 10;
						if (increment == 0.00001) {
							break;
						}
					}
					protocol += "1";
				} else {
					if (delta < 0) {
						// System.out.println("schlechter");

						sign = (-1) * sign;
						protocol += "0";
					} else {
						if (delta == 0) {
							break;
						}
					}

				}

				sumQuadErrorOld = sumQuadErrorNew;
			} while (true);
			// //System.out.println("weightNum["+weightNum+"] ende");
		}
	}
	
	public void hillrunner_iol_ool() {
		Neuron[] neuron;
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double maxError;
		double[] candidateTarget;
		
		sumQuadErrorOld = getSumQuadError();
		for(int hiddenNum=0; hiddenNum<numHiddens; hiddenNum++) {
			do {
				neuron = cloneNeuron(net.neuron);
				
				schritt2(hiddenNum);
				maxError = schritt3();
				candidateTarget = schritt4(maxError);
				schritt5(hiddenNum, -maxError);
				schritt6(candidateTarget,hiddenNum);
				calculateOutputWeigths();
				sumQuadErrorNew = getSumQuadError();
				delta = Math.abs(sumQuadErrorOld)
						- Math.abs(sumQuadErrorNew);
				//System.out.println("vorher:"+sumQuadErrorOld+", nacher: "+sumQuadErrorNew+", delta: "+delta);
				//System.out.println("vorher:"+neuron[hiddenNum].weight[0]+"\nnacher: "+net.neuron[hiddenNum].weight[0]);
				if (delta > 0) {
					System.out.println("hiddenNum "+hiddenNum+" wurde verbessert");
					sumQuadErrorOld = sumQuadErrorNew;
				} else {
					//System.out.println("hiddenNum "+hiddenNum+" verschlechtert");
					net.neuron = cloneNeuron(neuron);
					break;
				}
			} while (true);
		}
	}
	
	public Neuron[] cloneNeuron(Neuron[] neuron) {
		Neuron[] newNeuron = new Neuron[neuron.length];
		for(int neuronNum=0; neuronNum<newNeuron.length; neuronNum++) {
			newNeuron[neuronNum] = new Neuron();
			newNeuron[neuronNum].setNumWeights(neuron[neuronNum].weight.length);
			newNeuron[neuronNum].output = neuron[neuronNum].output;
			for (int weightNum = 0; weightNum < newNeuron[neuronNum].weight.length; weightNum++) {
				newNeuron[neuronNum].weight[weightNum] = neuron[neuronNum].weight[weightNum];
			}
		}
		return newNeuron;
	}
	
	public double getSumQuadError() {
		double[] inVector;
		double targetForOutput;
		double sumQuadError = 0;

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[numInputs];

			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);

			targetForOutput = inputTable[row][numInputs];
			sumQuadError += Math.pow(targetForOutput
					- net.neuron[numHiddens].output, 2);
		}
		return sumQuadError;
	}

	public void pingsIdea() {
		double[][] targets = new double[numHiddens][inputTable.length];
		double percentage;
		double sumWeights = 0;
		// Schritt 1 - Bestimme (prozentual) alle Targets fuer alle
		// Hidden-Neuronen
		for (int weightNum = 0; weightNum < net.numWeights; weightNum++) {
			sumWeights += net.neuron[numHiddens].weight[weightNum];
		}

		for (int row = 0; row < inputTable.length; row++) {
			for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
				targets[hiddenNum][row] = inputTable[row][3] / sumWeights
						* net.neuron[numHiddens].weight[numInputs + hiddenNum];
			}
		}

		// Schritt 2 - LeastSquare fuer alle Hidden-Neuronen
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			schritt6(targets[hiddenNum], hiddenNum);
		}
	}

	public int schritt1() {
		// 1. Schritt - Hiddenneuron mit minimaler Gewichtung finden (Kandidat
		// fuer Korrektur)
		// System.out.println("\nBeginne mit Schritt 1 - Hiddenneuron mit minimaler Gewichtung finden (Kandidat für Korrektur)");

		double minWeight = Double.MAX_VALUE;
		double weigth = 0;
		int candidateNeuron = 0;
		for (int weightNum = numInputs; weightNum < net.numWeights; weightNum++) {
			weigth = net.neuron[numHiddens].weight[weightNum];
			if (Math.abs(weigth) < Math.abs(minWeight)) {
				minWeight = weigth;
				candidateNeuron = weightNum - numInputs;
			}
		}
		// System.out.println("Ermittelter (Index) Kandidat: "+candidateNeuron+" mit Gewicht "+minWeight);
		return candidateNeuron;
	}

	public void schritt2(int candidateNeuron) {
		// 2. Schritt - Setze das Gewicht vom Kandidaten-Neuron zum
		// Output-Neuron auf null
		net.neuron[numHiddens].weight[candidateNeuron + numInputs] = 0;
		// System.out.println("net.neuron["+numHiddens+"].weight["+(candidateNeuron+numInputs)+"] = "+0);
	}

	public double schritt3() {
		double[] inVector;
		double targetForOutput;
		double recentAverageError = 0;
		double maxError = Double.MIN_VALUE;
		double error = 0;
		// 3. Schritt - Ermitteln des Maximums / Minimums
		// System.out.println("\nBeginne mit Schritt 1 - Ermitteln des Maximums / Minimums");

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[numInputs];
			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);

			targetForOutput = inputTable[row][numInputs];
			error = net.invThreshFunction(net.neuron[numHiddens].output)
					- net.invThreshFunction(targetForOutput);
			recentAverageError += error;
			if (Math.abs(error) > Math.abs(maxError)) {
				// System.out.println(net.invThreshFunction(net.neuron[numHiddens].output)+
				// " - "+net.invThreshFunction(targetForOutput)+" Wert 1: "+net.neuron[numHiddens].output+" Wert 2: "+targetForOutput);
				System.out.println(error);
				maxError = error;
			}
		}
		recentAverageError /= inputTable.length;
		maxError *= 1.0001;
		// System.out.println("Ermittelter Max-Fehler: "+maxError+" Durchschnittlicher Fehler: "+recentAverageError);
		return maxError;
	}

	public double[] schritt4(double maxError) {
		double[] inVector;
		double targetForOutput;
		double error = 0;
		double[] candidateTarget = new double[inputTable.length];

		// 4. Schritt - Zurueckfuehren des Targets
		// System.out.println("\nBeginne mit Schritt 4 - Zurueckfuehren des Targets");

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[numInputs];
			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);

			targetForOutput = inputTable[row][numInputs];
			error = net.invThreshFunction(net.neuron[numHiddens].output)
					- net.invThreshFunction(targetForOutput);
			candidateTarget[row] = net.invThreshFunction(error / maxError);
		}
		// System.out.print("Ermittelte Kandidat Targets: [");
		for (int targetNum = 0; targetNum < candidateTarget.length; targetNum++) {
			// System.out.print(candidateTarget[targetNum]+",");
		}
		// System.out.println("]");
		return candidateTarget;
	}
	public void schritt5(int candidateNeuron, double correction) {
		// 5. Schritt - Korrektur des Gewichts (Vom Output-Neuron zum
		// Kandidaten-Neuron)
		// System.out.println("\nBeginne mit Schritt 5 - Korrektur des Gewichts (Vom Output-Neuron zum Kandidaten-Neuron)");

		net.neuron[numHiddens].weight[candidateNeuron + numInputs] = correction;
		// System.out.println("net.neuron["+numHiddens+"].weight["+(candidateNeuron+numInputs)+"] = "+correction);
	}
	public void schritt6(double[] candidateTarget, int candidateNeuron) {
		double[] inVector;
		double targetForOutput;
		double activityError;
		EquationSolver equ;

		// 6. Schritt - LeastSquaresOptimum fuer Hidden Neuronen durchfuehren
		// System.out.println("\nBeginne mit Schritt 6 - LeastSquaresOptimum fuer Kandidaten Neuronen durchfuehren");
		MDims = numInputs;
		equ = new EquationSolver(MDims);

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[MDims];
			for (int inputNum = 0; inputNum < numInputs; inputNum++) { // First
				// input
				// values
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);
			targetForOutput = candidateTarget[row];
			activityError = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();

		for (int weightNum = 0; weightNum < MDims; weightNum++) {
			net.neuron[candidateNeuron].weight[weightNum] = equ.solution[weightNum]; // weight
																						// from
		}

		// Gebe Ergebnis in der Konsole aus
		// System.out.println("Solution (weights of candidate neuron):");
		for (int weightNum = 0; weightNum < net.numWeights; weightNum++) {
			// System.out.println("weight[" + weightNum + "]: "
			// + net.neuron[candidateNeuron].weight[weightNum]);
		}
	}

	/**
	 * @brief: draws the spiral and the neural mapping
	 * @param numHiddens
	 * @param net
	 * @param inFile
	 */
	public void drawMap() {
		double inVector[] = new double[4];
		// Draw classification map
		for (int y = 0; y < imageHeight; y += 1) {
			for (int x = 0; x < imageWidth; x += 1) {
				int color;
				inVector[0] = bias;
				inVector[1] = x / (double) imageWidth;
				inVector[2] = y / (double) imageHeight;
				net.activate(inVector);
				boolean border = false;
				for (int t = 0; t < numHiddens; t++) {
					if (net.neuron[t].output > -0.002
							&& net.neuron[t].output < 0.002) {
						border = true;
						break;
					}
				}
				color = (int) (net.neuron[numHiddens].output * 2.0 * 127) % 255;

				if (color < 0)
					color = 0;
				if (color > 255)
					color = 255;

				if (border) {
					// schwarze Linien
					inputOutput.drawPixel(x, y, new Color(0, 0, 0));
				} else {
					inputOutput.drawPixel(x, y, new Color(color, 0, 255));
				}
			}
		}

		// draw spiral data
		viewInputFile();
	}
}