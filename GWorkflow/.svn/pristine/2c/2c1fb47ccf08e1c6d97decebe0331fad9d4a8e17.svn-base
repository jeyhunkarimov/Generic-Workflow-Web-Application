package com.karimovceyhun.workflow.image;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.karimovceyhun.workflow.data.Workflow;


public class WorkOrderImageGenerator implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2277204402038034544L;

	public static BufferedImage getBufferedImage(Workflow wf)
	{
		final JFrame f = new JFrame("Test Screenshot");

        JPanel p = new JPanel( new BorderLayout(5,5) );
        f.setContentPane( p );
        
        JgraphComponentCreator jcc = new JgraphComponentCreator(wf.getStartProcess());
        
        p.add(jcc.getMxGraphComponent());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    BufferedImage image = new BufferedImage( f.getContentPane().getWidth(),f.getContentPane().getHeight(),BufferedImage.TYPE_INT_RGB);
	    f.getContentPane().paint( image.getGraphics() );
	    return image;
	}

}
