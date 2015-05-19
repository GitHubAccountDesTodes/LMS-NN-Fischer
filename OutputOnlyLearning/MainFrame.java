package main.java; /************************************************************************
* \brief: Main method reading in the spiral data and learning the       *
*         mapping using the Least Mean Squares method within a neural   *
*         network.
*																		*
* (c) copyright by JÃ¶rn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.JÃ¶rn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : main.java.MainFrame.java                                                *
*************************************************************************/

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final String inputFileName 	= "input.txt";
	public static final int inputTableCols		= 30;
	public static final int inputTableRows  	= 100;
	public static final int	imageWidth			= 600;
	public static final int	imageHeight			= 600;
	public int 				frameWidth			= imageWidth + 100;
	public int 				frameHeight			= imageHeight + 100;
	public InputOutput		inputOutput			= new InputOutput(this);
	public boolean			stop				= false;
	ImagePanel				canvas				= new ImagePanel();
	ImageObserver			imo					= null;
	Image					renderTarget		= null;

	private FileIO inFile;
	private Network net;
	private int numInputs;
	private int numHiddens;
	private int numOutputs;
	private int MDims; 		// Matrix Dimensions

	public MainFrame(String[] args) {
		super("Output Only Learning Networks");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLocation(getX()-frameWidth/2,getY()-frameHeight/2);
		setVisible(true);
		setLayout(null);
		int horizontalInsets = getInsets().right;
		int verticalInsets = getInsets().top+getInsets().bottom;
		setSize(frameWidth+horizontalInsets,frameHeight+verticalInsets);
		
		canvas.img = createImage(imageWidth, imageHeight);
		canvas.setLocation((frameWidth-getInsets().left-imageWidth)/2, (frameHeight-imageHeight)/2);
		canvas.setSize(imageWidth, imageHeight);
		add(canvas);

		run();
	}
	
	/**
	 * @brief: run method calls my Main and puts the results on the screen
	 */
	public void run() {
		readInputFile();
		viewInputFile();
		initialization();
		calculateLeastSquaresOptimum();
		drawMap(numHiddens, net, inFile);
		repaint();
	}

	public void readInputFile() {
//		main.java.FileIO debug("debug.txt","wb");
//		main.java.FileIO outFile("output.txt","wb");
		inFile= new FileIO(inputFileName);

		numInputs = (int) inFile.readSingleValue();		// first line defines number of input neurons
		numHiddens = (int) inFile.readSingleValue();
		
		inFile.readTable(inputTableCols,inputTableRows); // 30 values in up to 1000 lines
		numOutputs = inFile.maxCol - numInputs;
	}
	
	public void viewInputFile() {
		System.out.println("numInputs="+numInputs);
		System.out.println("numHiddens="+numHiddens);
		System.out.println("numOutputs="+numOutputs);

		// draw data to learn
		for (int row=0;row<inFile.maxRow-1;row++) {
			int x = (int)(inFile.value[1][row]*imageWidth);
			int y = (int)(inFile.value[2][row]*imageHeight);

			int color=(int)(inFile.value[3][row]*127+100);
			if (color<0) color=0;
			if (color>255) color=255;
			
			inputOutput.fillRect(x, y, 2, 2, new Color(color,255,100));
		}
	}
	
	public void initialization() {
		MDims = numInputs+numHiddens; // output not included ...
		net = new Network(numInputs, numHiddens, numOutputs);
	}
	
	public void calculateLeastSquaresOptimum() {
		/**
		 * Diese Methode berechnet das least Squares Optimum bisher nur für das Output main.java.Neuron.
		 * Für die Hidden Neuronen wird noch nichts berechnet.
		 */
		
		// --- output neuron ------------------------------------------------------------------------------------
		double[] inVector;
		double targetForOutput; 
		double activityError;
		EquationSolver equ;
		
		equ = new EquationSolver(MDims);
		
		for (int row=0; row<inFile.maxRow; row++){
			inVector = new  double[MDims];
			
			for (int inputNum=0;inputNum<numInputs;inputNum++){    // First input values
				inVector[inputNum] = inFile.value[inputNum][row];
			}
			net.activate(inVector);
			for (int hiddenNum=0;hiddenNum<numHiddens; hiddenNum++){
				inVector[numInputs+hiddenNum] = net.neuron[hiddenNum].output;
			}
			targetForOutput      = inFile.value[numInputs][row];
			activityError        = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();

		for (int i=0;i<numInputs+numHiddens;i++){
			//double debugg = equ->solution[i];
			net.neuron[numHiddens].weight[i] = equ.solution[i];	// weight from main.java.Neuron i to output neuron outnum
			System.out.println("weight["+i+"]: "+net.neuron[numHiddens].weight[i]);
		}
		
		// --- end output neuron --------------------------------------------------------------------------------
		
		// --- hidden neuron ------------------------------------------------------------------------------------
//		for (int h=0;h<numHiddens;h++) {
//			equ = new main.java.EquationSolver(MDims-h-1);
//			
//			for (int row=0; row<inFile.maxRow; row++){
//				inVector = new  double[MDims-h-1];
//				
//				for (int inputNum=0;inputNum<numInputs;inputNum++){    // First input values
//					inVector[inputNum] = inFile.value[inputNum][row];
//				}
//				net.activate(inVector);
//				for (int hiddenNum=0;hiddenNum<numHiddens-h-1; hiddenNum++){
//					inVector[numInputs+hiddenNum] = net.neuron[hiddenNum].output;
//				}
//				targetForOutput      = inFile.value[numInputs][row];
//				activityError        = net.invThreshFunction(targetForOutput);
//				equ.leastSquaresAdd(inVector, activityError);
//			}
//	
//			equ.Solve();
//	
//			for (int i=0;i<numInputs+numHiddens-h-1;i++){
//				//double debugg = equ->solution[i];
//				net.neuron[numHiddens-h-1].weight[i] = equ.solution[i];	// weight from main.java.Neuron i to output neuron outnum
//			}
//		}
		// --- end hidden neuron --------------------------------------------------------------------------------
	}

	/**
	 * @brief: draws the spiral and the neural mapping
	 * @param numHiddens
	 * @param net
	 * @param inFile
	 */
	public void drawMap(int numHiddens, Network net,FileIO inFile) {
		double inVector[] = new double[4];
		// Draw classification map	
		for (int y=0;y<imageHeight;y+=1){
			for (int x=0;x<imageWidth;x+=1){
				int color;
				inVector[0]=1.0;
				inVector[1]=x/(double)imageWidth;
				inVector[2]=y/(double)imageHeight;
				net.activate(inVector);
				boolean border = false;
				for (int t=0;t<numHiddens;t++){
					if (net.neuron[t].output>-0.002 && net.neuron[t].output<0.002 ){
						border = true;
					}
				}
				color = (int)(net.neuron[numHiddens].output * 2 * 127 + 127) % 255;

				if (color<0) color=0;
				if (color>255) color=255;
				if (border){
					inputOutput.drawPixel(x,y,new Color(0,0,0));
				}else{
					inputOutput.drawPixel(x,y,new Color(color,0,255));
				}
			}
		}
		
		// draw spiral data
		for (int row=0;row<inFile.maxRow-1;row++){
			int x = (int)(inFile.value[1][row]*imageWidth);
			int y = (int)(inFile.value[2][row]*imageHeight);

			int color=(int)(inFile.value[3][row]*127+100);
			if (color<0) color=0;
			if (color>255) color=255;
			
			inputOutput.fillRect(x,y ,2,2 ,new Color(color,255,100));
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
	 * Construct main frame
	 * 
	 * @param args
	 *            passed to main.java.MainFrame
	 */
	public static void main(String[] args) {
		new MainFrame(args);
	}
}