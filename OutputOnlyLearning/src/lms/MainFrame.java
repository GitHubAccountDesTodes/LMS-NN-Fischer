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

//	private static final String inputFileName = "res/input_big2.txt";
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
		drawMap(numHiddens, net);
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
	}

	public void initialization() {
		MDims = numInputs + numHiddens; // output not included ...
		net = new Network(numInputs, numHiddens, numOutputs);
	}

	public void calculateLeastSquaresOptimum() {
		/**
		 * Diese Methode berechnet das least Squares Optimum bisher nur f�r das
		 * Output Neuron. F�r die Hidden Neuronen wird noch nichts berechnet.
		 */

		// --- output neuron
		// ------------------------------------------------------------------------------------
		double[] inVector;
		double targetForOutput;
		double activityError;
		EquationSolver equ;

		equ = new EquationSolver(MDims);

		for (int row = 0; row < inputTable.length; row++) {
			inVector = new double[MDims];

			for (int inputNum = 0; inputNum < numInputs; inputNum++) { // First
				// input
				// values
				inVector[inputNum] = inputTable[row][inputNum];
			}
			net.activate(inVector);
			for (int hiddenNum = 0; hiddenNum < numHiddens; hiddenNum++) {
				inVector[numInputs + hiddenNum] = net.neuron[hiddenNum].output;
			}
			targetForOutput = inputTable[row][numInputs];
			activityError = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();

		//gebe Ergebnis in die Konsole aus
		System.out.println("Solution:");
		for (int i = 0; i < numInputs + numHiddens; i++) {
			net.neuron[numHiddens].weight[i] = equ.solution[i]; // weight from
			System.out.println("weight[" + i + "]: "
					+ net.neuron[numHiddens].weight[i]);
		}

		// --- end output neuron ------------------------------------

		// --- hidden neuron
		// ------------------------------------------------------------------------------------
		// for (int h=0;h<numHiddens;h++) {
		// equ = new EquationSolver(MDims-h-1);
		//
		// for (int row=0; row<inFile.maxRow; row++){
		// inVector = new double[MDims-h-1];
		//
		// for (int inputNum=0;inputNum<numInputs;inputNum++){ // First input
		// values
		// inVector[inputNum] = inFile.value[inputNum][row];
		// }
		// net.activate(inVector);
		// for (int hiddenNum=0;hiddenNum<numHiddens-h-1; hiddenNum++){
		// inVector[numInputs+hiddenNum] = net.neuron[hiddenNum].output;
		// }
		// targetForOutput = inFile.value[numInputs][row];
		// activityError = net.invThreshFunction(targetForOutput);
		// equ.leastSquaresAdd(inVector, activityError);
		// }
		//
		// equ.Solve();
		//
		// for (int i=0;i<numInputs+numHiddens-h-1;i++){
		// //double debugg = equ->solution[i];
		// net.neuron[numHiddens-h-1].weight[i] = equ.solution[i]; // weight
		// from Neuron i to output neuron outnum
		// }
		// }
		// --- end hidden neuron
		// --------------------------------------------------------------------------------
	}

	/**
	 * @brief: draws the spiral and the neural mapping
	 * @param numHiddens
	 * @param net
	 * @param inFile
	 */
	public void drawMap(int numHiddens, Network net) {
		double inVector[] = new double[4];
		// Draw classification map
		for (int y = 0; y < imageHeight; y += 1) {
			for (int x = 0; x < imageWidth; x += 1) {
				int color;
				inVector[0] = bias;
				inVector[1] = x / (double) imageWidth;
				inVector[2] = y / (double) imageHeight;
				net.activate(inVector);
//				boolean border = false;
//				for (int t = 0; t < numHiddens; t++) {
//					if (net.neuron[t].output > -0.002
//							&& net.neuron[t].output < 0.002) {
//						border = true;
//						break;
//					}
//				}
				color = (int) (net.neuron[numHiddens].output * 2.0 * 127) % 255;

				if (color < 0)
					color = 0;
				if (color > 255)
					color = 255;
				inputOutput.drawPixel(x, y, new Color(color, 0, 255));
//				if (border) {
//					// schwarze Linien
//					inputOutput.drawPixel(x, y, new Color(0, 0, 0));
//				} else {
//					inputOutput.drawPixel(x, y, new Color(color, 0, 255));
//				}
			}
		}

		// draw spiral data
		viewInputFile();
	}
}
