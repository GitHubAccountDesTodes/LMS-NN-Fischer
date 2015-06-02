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

//	private static final String inputFileName = "res/input_big.txt";
	private static final String inputFileName = "res/input.txt";
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
		viewInputFile();
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
		System.out.println("numInputs=" + numInputs);
		System.out.println("numHiddens=" + numHiddens);
		System.out.println("numOutputs=" + numOutputs);

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
		repaint();
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

		for (int i = 0; i < 70; i++) {
			calculateHiddenWeightsNew(minWeight());
//			 drawMap();
		}
//		calculateOutputWeigths();

		// --- end output neuron ---

		// --- hidden neuron ---

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
		System.out.println("Solution (weights of output neuron):");
		net.neuron[numHiddens].printWeight();

		// aktiviere alle inputs und berechne den Fehler
		for (int i = 0; i < inputTable.length; i++) {
			net.activate(inputTable[i]);
			System.out
					.println("Fehler "
							+ i
							+ "; "
							+ (inputTable[i][inputTable[i].length - 1] - net.neuron[numHiddens].output));
		}
	}

	public int minWeight(){
		double[] weights = net.neuron[numHiddens].weight;
		double minW = Double.MAX_VALUE;
		int minWIndex = numInputs;
		for(int i = numInputs; i<weights.length; i++){
			if(Math.abs(weights[i])<Math.abs(minW)){
				minW = weights[i];
				minWIndex = i;
//				System.out.println("min: "+minW);
			}
		}
		return minWIndex-numInputs;
	}
	
	public void calculateHiddenWeightsNew(int neuronIndex) {
		// init outputArray
		double[] errors = new double[inputTable.length];
		// setze Gewicht eines Neuron auf null
		net.neuron[numHiddens].weight[numInputs + neuronIndex] = 0;
		// init maxError
		double maxError = 0;
		// Durchlaufe alle Inputs
		for (int i = 0; i < inputTable.length; i++) {
			// rechne output
			net.activate(inputTable[i]);
			// merke Fehler
			double output = net.neuron[numHiddens].output;
			errors[i] = net
					.invThreshFunction(inputTable[i][inputTable[i].length - 1])
					- net.invThreshFunction(output);
			// merke maxError
			if (Math.abs(errors[i]) > Math.abs(maxError)) {
				maxError = errors[i];
			}
		}
		// setze Gewicht des Neuron auf maxError
		net.neuron[numHiddens].weight[numInputs + neuronIndex] = maxError;
		// init Equationsolver: dim = numInputs
		EquationSolver equ = new EquationSolver(numInputs);
		// Durchlaufe alle outputs
		System.out.println("maxError: " + maxError);
		for (int i = 0; i < errors.length; i++) {
			// Target = (inv(Ti)-inv(Oi))/maxError
			double targetCandidate = errors[i] / maxError;
			double invTargetCandidate = 0;
			if (targetCandidate == 1) {
				invTargetCandidate = net.invThreshFunction(0.999999);
			} else if (targetCandidate == -1) {
				invTargetCandidate = net.invThreshFunction(-0.999999);
			} else {
				invTargetCandidate = net.invThreshFunction(targetCandidate);
			}
			// add equation
			equ.leastSquaresAdd(inputTable[i], invTargetCandidate);
		}
		// solve;
		equ.Solve();
		// setze neue Gewichte
		net.neuron[neuronIndex].weight = equ.solution;

		// Gewichte in der Konsole ausgeben
		System.out.println("Neuron " + neuronIndex + ":");
		net.neuron[neuronIndex].printWeight();
	}

	public void calculateHiddenWeights() {
		double[] inVector;
		double targetForOutput;
		double activityError;
		EquationSolver equ;
		// 1. Schritt - Ermitteln des Maximums / Minimums
		System.out
				.println("\nBeginne mit Schritt 1 - Ermitteln des Maximums / Minimums");

		double maxError = Double.MIN_VALUE;
		double error = 0;
		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[numInputs];
			for (int inputNum = 0; inputNum < numInputs; inputNum++) {
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);

			targetForOutput = inputTable[row][numInputs];
			error = net.invThreshFunction(net.neuron[numHiddens].output)
					- net.invThreshFunction(targetForOutput);
			if (Math.abs(error) > maxError) {
				maxError = Math.abs(error);
			}
		}
		maxError++;
		System.out.println("Ermittelter Max-Fehler: " + maxError);

		// 2. Schritt - Hiddenneuron mit minimaler Gewichtung finden (Kandidat
		// für Korrektur)
		System.out
				.println("\nBeginne mit Schritt 2 - Hiddenneuron mit minimaler Gewichtung finden (Kandidat für Korrektur)");

		double minWeight = Double.MAX_VALUE;
		double weigth = 0;
		int candidateNeuron = 0;
		for (int weightNum = numInputs; weightNum < net.numWeights; weightNum++) {
			weigth = net.neuron[numHiddens].weight[weightNum];
			if (Math.abs(weigth) < minWeight) {
				minWeight = Math.abs(weigth);
				candidateNeuron = weightNum - numInputs;
			}
		}
		System.out.println("Ermittelter (Index) Kandidat: " + candidateNeuron
				+ " mit Gewicht " + minWeight);

		// 4. Schritt - Zurueckfuehren des Targets
		System.out
				.println("\nBeginne mit Schritt 4 - Zurueckfuehren des Targets");

		double[] candidateTarget = new double[inputTable.length];

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
		System.out.print("Ermittelte Kandidat Targets: [");
		for (int targetNum = 0; targetNum < candidateTarget.length; targetNum++) {
			System.out.print(candidateTarget[targetNum] + ",");
		}
		System.out.println("]");

		// 3. Schritt - Korrektur des Gewichts (Vom Output-Neuron zum
		// Kandidaten-Neuron)
		System.out
				.println("\nBeginne mit Schritt 3 - Korrektur des Gewichts (Vom Output-Neuron zum Kandidaten-Neuron)");

		net.neuron[numHiddens].weight[candidateNeuron + numInputs] = maxError;
		System.out.println("net.neuron[" + numHiddens + "].weight["
				+ (candidateNeuron + numInputs) + "] = " + maxError);

		// 5. Schritt - LeastSquaresOptimum fuer Hidden Neuronen durchfuehren
		System.out
				.println("\nBeginne mit Schritt 5 - LeastSquaresOptimum fuer Kandidaten Neuronen durchfuehren");
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
		System.out.println("Solution (weights of candidate neuron):");
		for (int weightNum = 0; weightNum < net.numWeights; weightNum++) {
			System.out.println("weight[" + weightNum + "]: "
					+ net.neuron[candidateNeuron].weight[weightNum]);
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
				color = (int) (net.neuron[numHiddens].output * 254) % 255;

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
			repaint();
		}
	}
}
