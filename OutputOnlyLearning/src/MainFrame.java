/************************************************************************
* \brief: Main method reading in the spiral data and learning the       *
*         mapping using the Least Mean Squares method within a neural   *
*         network.
*																		*
* (c) copyright by JÃ¶rn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.JÃ¶rn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : MainFrame.java                                                *
*************************************************************************/

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final String inputFileName 	= "input.txt";
	public static final int inputTableCols		= 30;
	public static final int inputTableRows  	= 10000;
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
	private int numInputNeurons;
	private int numHiddenNeurons;
	private int numOutputNeurons;
	private int MDims;

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
		drawMap(numHiddenNeurons, net, inFile);
		repaint();
	}

	public void readInputFile() {
//		FileIO debug("debug.txt","wb");
//		FileIO outFile("output.txt","wb");
		inFile= new FileIO(inputFileName);

		numInputNeurons = (int) inFile.readSingleValue();		// first line defines number of input neurons
		numHiddenNeurons = (int) inFile.readSingleValue();
		
		inFile.readTable(inputTableCols,inputTableRows); // 30 values in up to 1000 lines
		numOutputNeurons = inFile.maxCol - numInputNeurons;
	}
	
	public void viewInputFile() {
		System.out.println("numInputNeurons="+numInputNeurons);
		System.out.println("numHiddenNeurons="+numHiddenNeurons);
		System.out.println("numOutputNeurons="+numOutputNeurons);

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
		MDims = numInputNeurons+numHiddenNeurons; // Multi-Domain Information Model (output not included ...)
		
		net = new Network(numInputNeurons, numHiddenNeurons, numOutputNeurons);

		// ############################################################################################
		// ##### random structure #####################################################################
		// ############################################################################################
		
		for (int hiddenNeuronNum=0;hiddenNeuronNum<numHiddenNeurons;hiddenNeuronNum++){
			for (int i=0;i<hiddenNeuronNum+numInputNeurons;i++){
				net.neuron[hiddenNeuronNum].weight[i] = generateRandomValue(-1,1);
			}
		}
	}
	
	public void calculateLeastSquaresOptimum() {
		/**
		 * Diese Methode berechnet das least Squares Optimum bisher nur für das Output Neuron.
		 * Für die Hidden Neuronen wird noch nichts berechnet.
		 */
		
		double inVector[] = new  double[MDims];
		
		// --- output neuron ------------------------------------------------------------------------------------
		EquationSolver equ;
		equ = new EquationSolver(numInputNeurons + numHiddenNeurons);	
		for (int row=0; row<inFile.maxRow; row++){
			for (int i=0;i<MDims;i++){
				inVector[i]=0;
			}
			for (int inputNeuronNum=0;inputNeuronNum<numInputNeurons;inputNeuronNum++){    // First input values
				inVector[inputNeuronNum] = inFile.value[inputNeuronNum][row];
			}
			net.activate(inVector);
			for (int hiddenNeuronNum=0;hiddenNeuronNum<numHiddenNeurons; hiddenNeuronNum++){
				inVector[numInputNeurons+hiddenNeuronNum] = net.neuron[hiddenNeuronNum].output;
			}
			double targetForOutput      = inFile.value[numInputNeurons][row];
			double activityError        = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);
		}

		equ.Solve();

		for (int i=0;i<numInputNeurons+numHiddenNeurons;i++){
			//double debugg = equ->solution[i];
			net.neuron[numHiddenNeurons].weight[i] = equ.solution[i];	// weight from Neuron i to output neuron outnum
		}
		// --- end output neuron --------------------------------------------------------------------------------
		
		for(int h=0;h<net.neuron.length;h++) {
			System.out.println("\nNeuron: "+h);
			for (int i=0;i<net.neuron[h].weight.length;i++) {
				System.out.println("i["+i+"]: "+net.neuron[h].weight[i]);
			}
		}
	}

	/**
	 * @brief: draws the spiral and the neural mapping
	 * @param numHiddenNeurons
	 * @param net
	 * @param inFile
	 */
	public void drawMap(int numHiddenNeurons, Network net,FileIO inFile) {
		double inVector[] = new double[4];
		// Draw classification map	
		for (int y=0;y<imageHeight;y+=1){
			for (int x=0;x<imageWidth;x+=1){
				int color;
				inVector[0]=1.0;
				inVector[1]=x/(double)imageWidth;
				inVector[2]=y/(double)imageHeight;
				net.activate(inVector);
				color = (int)(net.neuron[numHiddenNeurons].output * 2 * 127 + 127) % 255;

				if (color<0) color=0;
				if (color>255) color=255;
				inputOutput.drawPixel(x,y,new Color(color,0,255));
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
	 *            passed to MainFrame
	 */
	public static void main(String[] args) {
		new MainFrame(args);
	}
}