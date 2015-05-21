/************************************************************************
* \brief: Main method reading in the spiral data and learning the       *
*         mapping using the Least Mean Squares method within a neural   *
*         network.
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : MainFrame.java                                                *
*************************************************************************/

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final int	imageWidth		= 600;
	public static final int	imageHeight		= 600;
	public InputOutput		inputOutput		= new InputOutput(this);
	public boolean			stop			= false;
	ImagePanel				canvas			= new ImagePanel();
	ImageObserver			imo				= null;
	Image					renderTarget	= null;

	public MainFrame(String[] args) {
		super("Output Only Learning Networks");

		getContentPane().setSize(imageWidth, imageHeight);
		setSize(imageWidth + 100, imageHeight + 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		canvas.img = createImage(imageWidth, imageHeight);

		add(canvas);

		run();
	}
	/**
	 * @brief: run method calls my Main and puts the results on the screen
	 */
	public void run() {

		myMain();

		repaint();
		setVisible(true);
		do{
		}while(!stop);
		
		dispose();
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
	
	/**
	 * @brief: draws the spiral and the neural mapping
	 * @param hiddenNeuronNum
	 * @param net
	 * @param inFile
	 */
	public void drawMap(int hiddenNeuronNum, Network net,FileIO inFile)
	{
		double inVector[] = new double[4];
		    // Draw classification map	
		for (int y=0;y<600;y+=1){
			for (int x=0;x<600;x+=1){
				int col;
				inVector[0]=1.0;
				inVector[1]=x/600.0;
				inVector[2]=y/600.0;
				net.activate(inVector);
				col = (int)(net.neuron[hiddenNeuronNum].output *2* 127+127) %255;

				if (col<0) col=0;
				if (col>255) col=255;
				inputOutput.drawPixel(x,y,new Color(col,0,255));
			}
		}
		
		// draw spiral data
		for (int t=0;t<inFile.maxRow-1;t++){
			int x1 = (int)(inFile.value[1][t]*600.0);
			int x2 = (int)(inFile.value[2][t]*600.0);

			int col=(int)(inFile.value[3][t]*127+100);
			if (col<0) col=0;
			if (col>255) col=255;
			//		System.out.println("color="+col);
				inputOutput.fillRect(x1,x2 ,2,2 ,new Color(col,255,100));
			//}
		}
		
	}

	/**
	 * @brief: This is the main method, reading the table, learning the neural mapping
	 *         and drawing the result
	 */
	public void myMain()
	{

	//	FileIO debug("debug.txt","wb");
	//	FileIO outFile("output.txt","wb");
		FileIO inFile= new FileIO("input.txt");

		//----------------------------------------------------------------------------------------------
		// --- read input File -------------------------------------------------------------------------
		// ---------------------------------------------------------------------------------------------
		int inputNeuronNum = (int) inFile.readSingleValue();		// first line defines number of input neurons
		int hiddenNeuronNum = (int) inFile.readSingleValue();
		
		inFile.readTable(30,10000); // 30 values in up to 1000 lines
		int outputNeuronNum = inFile.maxCol - inputNeuronNum;

		//----------------------------------------------------------------------------------------------
		// --- view input File -------------------------------------------------------------------------
		// ---------------------------------------------------------------------------------------------
		
		int MDims = inputNeuronNum+hiddenNeuronNum; // output not included ...

		System.out.println("inputNeuronNum="+inputNeuronNum);
		System.out.println("hiddenNeuronNum="+hiddenNeuronNum);
		System.out.println("outputNeuronNum="+outputNeuronNum);

		// draw data to learn
		for (int t=0;t<inFile.maxRow-1;t++){
			int x1 = (int)(inFile.value[1][t]*600.0);
			int x2 = (int)(inFile.value[2][t]*600.0);

			int col=(int)(inFile.value[3][t]*127+100);
			if (col<0) col=0;
			if (col>255) col=255;
		//		System.out.println("color="+col);
				inputOutput.fillRect(x1,x2 ,2,2 ,new Color(col,255,100));
			//}
		}
		

		repaint();
		setVisible(true);

		// -------------------------------------------------------------------------------------------------------
		// ------------------------ calculate least Squares Optimum for Hidden Neuron ----------------------------
		// -------------------------------------------------------------------------------------------------------

		
		Network net = new Network(inputNeuronNum, hiddenNeuronNum + outputNeuronNum);

		double inVector[] = new  double[MDims+10];
		// ---
		double squareError=0;
		// ############################################################################################
		// ##### random structure #####################################################################
		// ############################################################################################
		for (int t=0;t<hiddenNeuronNum;t++){
			for (int i=0;i<t+inputNeuronNum;i++){
				net.neuron[t].weight[i]=Math.random()*2.0-1.0;//(rand()%2000)/1000.0-1.0;
			}
		}
		//---- output neuron -------------------------------------------------------------------------------
		EquationSolver equ;
		equ = new EquationSolver(inputNeuronNum + hiddenNeuronNum);	
		for (int time=0; time<inFile.maxRow; time++){
			for (int t=0;t<MDims;t++){
				inVector[t]=0;
			}
			for (int t=0;t<inputNeuronNum;t++){    // First input values
				inVector[t] = inFile.value[t][time];
			}
			net.activate(inVector);
			for (int t=0;t<hiddenNeuronNum; t++){
				inVector[inputNeuronNum+t] = net.neuron[t].output;
			}
			double targetForOutput      = inFile.value[inputNeuronNum][time];
			double activityError        = net.invThreshFunction(targetForOutput);
			equ.leastSquaresAdd(inVector, activityError);

		}

		equ.Solve();

		for (int t=0;t<inputNeuronNum+hiddenNeuronNum;t++){
			//double debugg= equ->solution[t];
			net.neuron[hiddenNeuronNum].weight[t] = equ.solution[t];	// weight from Neuron t to output neuron outnum
		}
		// --- end outputneuron --------------------------------------------------------------------------------	
	
		 drawMap(hiddenNeuronNum, net, inFile);

	}

	/*
	public void mouseReleased(MouseEvent e) {
		System.out.println(e.toString());
	}

	public void mousePressed(MouseEvent e) {
		System.out.println(e.toString());
	}

	public void mouseExited(MouseEvent e) {
		System.out.println(e.toString());
	}

	public void mouseEntered(MouseEvent e) {
		System.out.println(e.toString());
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println(e.toString());
	}

	public void mouseMoved(MouseEvent e) {
		// System.out.println(e.toString());
	}

	public void mouseDragged(MouseEvent e) {
		System.out.println(e.toString());
	}

	public void keyTyped(KeyEvent e) {
		System.out.println(e.toString());
	}

	public void keyReleased(KeyEvent e) {
		System.out.println(e.toString());
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(e.toString());
	}
*/

}
