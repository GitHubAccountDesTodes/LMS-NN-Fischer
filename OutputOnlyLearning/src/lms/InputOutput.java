package lms;
/************************************************************************
* \brief: inputOutput class                                             *
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : inputOutput.java                                              *
*************************************************************************/

import java.awt.Color;
import java.awt.Graphics;

public class InputOutput {

	public MainFrame	mainFrame	= null;
	Graphics			graphics	= null;

	public InputOutput(MainFrame frame) {
		this.mainFrame = frame;
	}

	public synchronized void drawPixel(int x, int y, Color color) {
		if (graphics == null)
			graphics = mainFrame.canvas.img.getGraphics();

		graphics.setColor(color);
		graphics.fillRect(x, y, 1, 1);
	}

	/**
	 * fillRect(xPos, yPos, width, height, color);
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public synchronized void fillRect(int x, int y, int width, int height,
			Color color) {
		if (graphics == null)
			graphics = mainFrame.canvas.img.getGraphics();

		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
	}
}
