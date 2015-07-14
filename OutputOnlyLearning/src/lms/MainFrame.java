package lms;

/************************************************************************
 * \brief: Main method reading in the spiral data and learning the 		*
 *         mapping using the Least Mean Squares method within a neural	*
 *         network.                                                      *
 *																		*
 * (c) copyright by Jörn Fischer										*
 *                                                                       *																		* 
 * @autor: Prof.Dr.Jörn Fischer											*
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
	 * Main
	 */
	public static void main(String[] args) {
		new MainFrame(args);
	}
	
	/**
	 * @brief: Constructor: Creates the frame and canvas and runs the further program
	 */
	public MainFrame(String[] args) {
		super("Neural Network Output");

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
	
	/**
	 * @brief: initialization
	 */
	public void initialization() {
		net = new Network(numInputs, numHiddens, numOutputs);
	}

	/**
	 * @brief: reads the input file
	 */
	public void readInputFile() {
		FileIOP inFile = new FileIOP(inputFileName);
		try {
			inputTable = inFile.readTable();
			numInputs = inFile.readSingleValue(1);
			numHiddens = inFile.readSingleValue(2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		numOutputs = inputTable[0].length - numInputs;
		bias = inputTable[0][0];
	}

	/**
	 * @brief: draw inputs in squares
	 */
	public void viewInputFile() {

		// draw data to learn
		for (int row = 0; row < inputTable.length - 1; row++) {
			int x = (int) (inputTable[row][1] * imageWidth);
			int y = (int) (inputTable[row][2] * imageHeight);

			int color = (int) (inputTable[row][3] * 127 + 100);
			if (color < 0)
				color = 0;
			if (color > 255)
				color = 255;

			inputOutput.fillRect(x, y, 2, 2, new Color(color, 255, 100));
		}
	}
	
	/**
	 * @brief: draws the spiral and the neural mapping
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

	/**
	 * @brief: calculates the LMS optimum
	 */
	public void calculateLeastSquaresOptimum() {
		double maxError;
		int candidateNeuron;
		double[] candidateTarget;
		double oldError;
		double error;
		
		// No-Propagation
//		calculateOutputWeigths();

		// IOL
//		for (int i=0;i<30;i++) {
//			candidateNeuron = determineCandidate();
//			resetWeight(candidateNeuron);
//			maxError = determineMaximum();
//			candidateTarget = determineTarget(maxError);
//			correctWeight(candidateNeuron, -maxError);
//			LMSforHN(candidateTarget,candidateNeuron);
//			System.out.println("Error bei Durchlauf "+candidateNeuron+": "+getSumQuadError());
//		}

		// HR AHNW No-Propagation
//		oldError = getSumQuadError();
//		for (int i = 0; i < 100; i++) {
//			hillrunner_AHNW_and_OOL_v1();
//			drawMap();
//			repaint();
//			error = getSumQuadError();
//			System.out.println("Error im Durchlauf "+i+": " + error);
//			if(oldError == error) {
//				System.out.println("Keine Gesamtverbesserung mehr - Beende Iteration");
//				break;
//			} else {
//			oldError = error;
//			}
//		}

		// HR IOL
//		oldError = getSumQuadError();
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
//			oldError = error;
//			}
//		}

		// HR AHNW OOL global
//		oldError = getSumQuadError();
//		for (int i = 0; i < 10; i++) {
//			hillrunner_AHNW_and_OOL_global();
//			error = getSumQuadError();
//			System.out.println("\nError im Durchlauf " + i + ": " + error);
//			if (oldError == error) {
//				System.out.println("\nKeine Gesamtverbesserung mehr - Beende Iteration");
//				break;
//			} else {
//				oldError = error;
//			}
//		}
//		System.out.println("\nError am Ende: " + getSumQuadError());
		
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

			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
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
			net.neuron[numHiddens].weight[numInputs + weightNum] = equ.solution[weightNum];
		}

		// Gebe das Ergebnis in der Konsole aus
//		System.out.println("Solution (weights of output neuron):");
//		for (int weightNum = 0; weightNum < net.numWeights; weightNum++) {
//			System.out.println("weight[" + weightNum + "]: " + net.neuron[numHiddens].weight[weightNum]);
//		}
	}

	/**
	 * @brief: clones the neuron
	 */
	public Neuron[] cloneNeuron(Neuron[] neuron) {
		Neuron[] newNeuron = new Neuron[neuron.length];
		for (int neuronNum = 0; neuronNum < newNeuron.length; neuronNum++) {
			newNeuron[neuronNum] = new Neuron();
			newNeuron[neuronNum].setNumWeights(neuron[neuronNum].weight.length);
			newNeuron[neuronNum].output = neuron[neuronNum].output;
			for (int weightNum = 0; weightNum < newNeuron[neuronNum].weight.length; weightNum++) {
				newNeuron[neuronNum].weight[weightNum] = neuron[neuronNum].weight[weightNum];
			}
		}
		return newNeuron;
	}

	/**
	 * @brief: returns the sum of the square error of all input patterns
	 */
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

	/**
	 * @brief: 1. Schritt - Hiddenneuron mit minimaler Gewichtung finden
	 *         (Kandidat fuer Korrektur)
	 */
	public int determineCandidate() {
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

	/**
	 * @brief: 2. Schritt - Setze das Gewicht vom Kandidaten-Neuron zum
	 *         Output-Neuron auf 0
	 */
	public void resetWeight(int candidateNeuron) {
		net.neuron[numHiddens].weight[candidateNeuron + numInputs] = 0;
		// System.out.println("net.neuron["+numHiddens+"].weight["+(candidateNeuron+numInputs)+"] = "+0);
	}

	/**
	 * @brief: 3. Schritt - Ermitteln des Maximums / Minimums
	 */
	public double determineMaximum() {
		double[] inVector;
		double targetForOutput;
		double recentAverageError = 0;
		double maxError = 0;
		double error = 0;
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
				maxError = error;
			}
		}
		recentAverageError /= inputTable.length;
		maxError *= 1.000000000000001;
		// System.out.println("Ermittelter Max-Fehler: "+maxError+"\nDurchschnittlicher Fehler: "+recentAverageError+"\nMax-Faktor: "+1.000000000000001);
		return maxError;
	}

	/**
	 * @brief: 4. Schritt - Zurueckfuehren des Targets (Ansatz 1)
	 */
	public double[] determineTarget(double maxError) {
		double[] inVector;
		double targetForOutput;
		double error = 0;
		double[] candidateTarget = new double[inputTable.length];
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
			candidateTarget[row] = error / maxError;
		}
		// System.out.print("Ermittelte Kandidat Targets: [");
		for (int targetNum = 0; targetNum < candidateTarget.length; targetNum++) {
			// System.out.print(candidateTarget[targetNum]+",");
		}
		// System.out.println("]");
		return candidateTarget;
	}

	/**
	 * @brief: 5. Schritt - Korrektur des Gewichts (Vom Output-Neuron zum
	 *         Kandidaten-Neuron)
	 */
	public void correctWeight(int candidateNeuron, double correction) {
		// System.out.println("\nBeginne mit Schritt 5 - Korrektur des Gewichts (Vom Output-Neuron zum Kandidaten-Neuron)");
		net.neuron[numHiddens].weight[candidateNeuron + numInputs] = correction;
		// System.out.println("net.neuron["+numHiddens+"].weight["+(candidateNeuron+numInputs)+"] = "+correction);
	}

	/**
	 * @brief: 6. Schritt - LeastSquaresOptimum fuer Hidden Neuronen durchfuehren
	 */
	public void LMSforHN(double[] candidateTarget, int candidateNeuron) {
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
			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);
			targetForOutput = candidateTarget[row];
			activityError = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();

		for (int weightNum = 0; weightNum < MDims; weightNum++) {
			net.neuron[candidateNeuron].weight[weightNum] = equ.solution[weightNum];

		}

		// Gebe Ergebnis in der Konsole aus
//		System.out.println("Solution (weights of candidate neuron):");
//		for (int weightNum = 0; weightNum < net.numWeights; weightNum++) {
//			System.out.println("weight[" + weightNum + "]: " + net.neuron[candidateNeuron].weight[weightNum]);
//		}
	}
	
	/**
	 * @brief: hillrunner with optimisation strategy: adjustment of (all) weights
	 */
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
			for (int weightNum = 0; weightNum < numInputs; weightNum++) {
				protocol = "";
				increment = 10;
				do {
					net.neuron[hiddenNum].weight[weightNum] += sign * increment;
					// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);

					if (delta > 0) {
						// System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
						// System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
						protocol += "1"; // '1' protocols the successful
											// improvement
						neuron = cloneNeuron(net.neuron);
						sumQuadErrorOld = sumQuadErrorNew;
					} else {
						// System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
						protocol += "0"; // '0' protocols the unsuccessful
											// improvement
						net.neuron = cloneNeuron(neuron);
						if (protocol.endsWith("00")) {
							increment = increment / 10;
							if (increment == 0.00001) {
								break;
							}
							protocol += "D"; // 'D' protocols the Decrementation
												// of adjustment stepping size
						} else {
							sign = (-1) * sign;
						}
					}
				} while (true);
			}
		}
	}

	/**
	 * @brief: hillrunner with optimisation strategy: adjustment of hidden
	 *         neuron weights with OOL.
	 */
	public void hillrunner_AHNW_and_OOL_v1() {
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
			for (int weightNum = 0; weightNum < numInputs; weightNum++) {
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
						// System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
						// System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
						protocol += "1"; // '1' protocols the successful
											// improvement
						neuron = cloneNeuron(net.neuron);
						sumQuadErrorOld = sumQuadErrorNew;
					} else {
						// System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
						protocol += "0"; // '0' protocols the unsuccessful
											// improvement
						net.neuron = cloneNeuron(neuron);
						if (protocol.endsWith("00")) {
							increment = increment / 10;
							if (increment == 0.00001) {
								break;
							}
							protocol += "D"; // 'D' protocols the Decrementation
												// of adjustment stepping size
						} else {
							sign = (-1) * sign;
						}
					}
				} while (true);
			}
		}
		sumQuadErrorEnd = getSumQuadError();
		if (sumQuadErrorEnd > sumQuadErrorBegin) {
			net.neuron = neuron;
		}
	}

	/**
	 * @brief: hillrunner with optimisation strategy: adjustment of hidden
	 *         neuron weights with OOL.
	 */
	public void hillrunner_AHNW_and_OOL_v2() {
		String protocol = "";
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double sign = -1;
		double increment;
		Neuron[] neuron;

		sumQuadErrorOld = getSumQuadError();
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			for (int weightNum = 0; weightNum < numInputs; weightNum++) {
				protocol = "";
				increment = 10;
				neuron = cloneNeuron(net.neuron);
				do {
					net.neuron[hiddenNum].weight[weightNum] += sign * increment;
					// System.out.println("neues weight: "+net.neuron[hiddenNum].weight[weightNum]);

					calculateOutputWeigths();
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);

					if (delta > 0) {
						// System.out.println("hiddenNum "+hiddenNum+" wurde verbessert (adjustment stepsize: "+increment+")");
						// System.out.println("old: "+sumQuadErrorOld+", new: "+sumQuadErrorNew);
						protocol += "1"; // '1' protocols the successful
											// improvement
						neuron = cloneNeuron(net.neuron);
						sumQuadErrorOld = sumQuadErrorNew;
					} else {
						// System.out.println("hiddenNum "+hiddenNum+" verschlechtert (adjustment stepsize: "+increment+")");
						protocol += "0"; // '0' protocols the unsuccessful
											// improvement
						net.neuron = cloneNeuron(neuron);
						if (protocol.endsWith("00")) {
							increment = increment / 10;
							if (increment == 0.00001) {
								break;
							}
							protocol += "D"; // 'D' protocols the Decrementation
												// of adjustment stepping size
						} else {
							sign = (-1) * sign;
						}
					}
				} while (true);
			}
		}
	}
	
	/**
	 * @brief: hillrunner with optimisation strategy: adjustment of hidden
	 *         neuron weights with OOL. "global" stands for searching the weight
	 *         with the best possible improvement.
	 */
	public void hillrunner_AHNW_and_OOL_global() {
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double sign = -1;
		double increment;
		Neuron[] neuron;
		double[] valueCollector;

		valueCollector = new double[numHiddens * numInputs * 2];
		calculateOutputWeigths();
		neuron = cloneNeuron(net.neuron);
		increment = 10;

		drawMap();
		repaint();

		do {
			sumQuadErrorOld = getSumQuadError();
			for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
				// //System.out.println("hiddenNum["+hiddenNum+"] start");
				for (int weightNum = 0; weightNum < numInputs; weightNum++) {

					net.neuron[hiddenNum].weight[weightNum] += sign * increment;

					calculateOutputWeigths();
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);

					valueCollector[hiddenNum * numInputs + weightNum] = delta;

					net.neuron = cloneNeuron(neuron);
				}
			}

			for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
				for (int weightNum = 0; weightNum < numInputs; weightNum++) {

					net.neuron[hiddenNum].weight[weightNum] -= sign * increment;

					calculateOutputWeigths();
					sumQuadErrorNew = getSumQuadError();
					delta = Math.abs(sumQuadErrorOld)
							- Math.abs(sumQuadErrorNew);

					valueCollector[numHiddens * numInputs + hiddenNum * numInputs + weightNum] = delta;

					net.neuron = cloneNeuron(neuron);
				}
			}

			int candidate = -1;
			delta = 0;
			for (int i = 0; i < valueCollector.length; i++) {
				if (valueCollector[i] > delta) {
					candidate = i;
					delta = valueCollector[i];
				}
			}

			if (delta > 0) {

				if (candidate < numHiddens * numInputs) {
					System.out.println("Candidate " + candidate + " bringt die hoechste Verbesserung (adjustment stepsize: " + increment + ")");
					System.out.println("Error Old: " + sumQuadErrorOld + ", Error New: " + (sumQuadErrorOld - delta) + ", Delta: " + delta);
					net.neuron[candidate / 3].weight[candidate % 3] += sign * increment;
				} else {
					System.out.println("Candidate " + (candidate - numHiddens * numInputs) + " bringt die hoechste Verbesserung (adjustment stepsize: " + (-1) * increment + ")");
					System.out.println("Error Old: " + sumQuadErrorOld + ", Error New: " + (sumQuadErrorOld - delta) + ", Delta: " + delta);
					net.neuron[(candidate - numHiddens * numInputs) / 3].weight[candidate % 3] -= sign * increment;
				}
				calculateOutputWeigths();
				sumQuadErrorNew = getSumQuadError();
				sumQuadErrorOld = sumQuadErrorNew;
				neuron = cloneNeuron(net.neuron);
			} else {
				increment = increment / 10;
				System.out.println("Keine Verbesserung moeglich, setze adjustment stepsize auf "+ increment);

				if (increment == 0.00001) {
					break;
				}
			}
			System.out.println();
		} while (true);

		drawMap();
		repaint();
	}

	/**
	 * @brief: hillrunner with optimisation strategy: IOL (without the
	 *         determination of the candidate neuron - Schritt 1)
	 */
	public void hillrunner_IOL() {
		Neuron[] neuron;
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double maxError;
		double[] candidateTarget;

		sumQuadErrorOld = getSumQuadError();
		neuron = cloneNeuron(net.neuron);
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {

			resetWeight(hiddenNum);
			maxError = determineMaximum();
			candidateTarget = determineTarget(maxError);
			correctWeight(hiddenNum, -maxError);
			LMSforHN(candidateTarget, hiddenNum);

			sumQuadErrorNew = getSumQuadError();
			delta = Math.abs(sumQuadErrorOld) - Math.abs(sumQuadErrorNew);
			// System.out.println("vorher:"+sumQuadErrorOld+", nacher: "+sumQuadErrorNew+", delta: "+delta);
			// System.out.println("vorher:"+neuron[hiddenNum].weight[0]+"\nnacher: "+net.neuron[hiddenNum].weight[0]);
			if (delta > 0) {
				System.out.println("hiddenNum " + hiddenNum
						+ " wurde verbessert");
				System.out.println("old: " + sumQuadErrorOld + ", new: "
						+ sumQuadErrorNew);
				neuron = cloneNeuron(net.neuron);
				sumQuadErrorOld = sumQuadErrorNew;
			} else {
				// System.out.println("hiddenNum "+hiddenNum+" verschlechtert");
				net.neuron = cloneNeuron(neuron);
			}
		}
	}

	/**
	 * @brief: hillrunner with optimisation strategy: IOL with the determination
	 *         of the candidate neuron (Schritt 1)
	 */
	public void hillrunner_IOL_DOC() {
		Neuron[] neuron;
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double maxError;
		double[] candidateTarget;
		int candidateNeuron;

		sumQuadErrorOld = getSumQuadError();
		neuron = cloneNeuron(net.neuron);

		candidateNeuron = determineCandidate();
		resetWeight(candidateNeuron);
		maxError = determineMaximum();
		candidateTarget = determineTarget(maxError);
		correctWeight(candidateNeuron, -maxError);
		LMSforHN(candidateTarget, candidateNeuron);

		sumQuadErrorNew = getSumQuadError();
		delta = Math.abs(sumQuadErrorOld) - Math.abs(sumQuadErrorNew);
		if (delta > 0) {
			sumQuadErrorOld = sumQuadErrorNew;
		} else {
			net.neuron = cloneNeuron(neuron);
		}
	}

	/**
	 * @brief: hillrunner with optimisation strategy: IOL (without the
	 *         determination of the candidate neuron - Schritt 1) with OOL
	 */
	public void hillrunner_IOL_OOL() {
		Neuron[] neuron;
		double sumQuadErrorOld;
		double sumQuadErrorNew;
		double delta;
		double maxError;
		double[] candidateTarget;

		sumQuadErrorOld = getSumQuadError();
		for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
			do {
				neuron = cloneNeuron(net.neuron);

				resetWeight(hiddenNum);
				maxError = determineMaximum();
				candidateTarget = determineTarget(maxError);
				correctWeight(hiddenNum, -maxError);
				LMSforHN(candidateTarget, hiddenNum);
				calculateOutputWeigths();
				sumQuadErrorNew = getSumQuadError();
				delta = Math.abs(sumQuadErrorOld) - Math.abs(sumQuadErrorNew);
				if (delta > 0) {
					System.out.println("hiddenNum " + hiddenNum
							+ " wurde verbessert");
					sumQuadErrorOld = sumQuadErrorNew;
				} else {
					net.neuron = cloneNeuron(neuron);
					break;
				}
			} while (true);
		}
	}
}