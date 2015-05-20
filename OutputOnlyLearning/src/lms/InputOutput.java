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

	private MainFrame mainFrame;
	private Graphics graphics;

	public InputOutput(MainFrame frame) {
		this.mainFrame = frame;
	}

	/**
	 * zeichne kleines Viereck in 1*1 pixel
	 * @param x x-Position
	 * @param y y-Position
	 * @param color Pixel in (x,y) wird in color gefärbt
	 */
	public synchronized void drawPixel(int x, int y, Color color) {
		if (graphics == null)
			graphics = mainFrame.canvas.img.getGraphics();

		graphics.setColor(color);
		graphics.fillRect(x, y, 1, 1);
	}

	/**
	 * fillRect(xPos, yPos, width, height, color);
	 * @param x x-Position
	 * @param y y-Position
	 * @param width Breite des Vierecks
	 * @param height Höhe des Vierecks
	 * @param color Farbe des Vierecks
	 */
	public synchronized void fillRect(int x, int y, int width, int height,
			Color color) {
		if (graphics == null)
			graphics = mainFrame.canvas.img.getGraphics();

		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
	}
}
