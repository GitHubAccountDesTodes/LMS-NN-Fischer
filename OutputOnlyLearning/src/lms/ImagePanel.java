package lms;
/************************************************************************
* \brief: imagePanel class                                              *
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : ImagePanel.java                                               *
*************************************************************************/

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	public Image			img				= null;
	public ImageObserver	imageObserver	= null;

	public void paint(Graphics g) {
		super.paint(g);

		g.drawImage(img, 0, 0, imageObserver);
	}
}
