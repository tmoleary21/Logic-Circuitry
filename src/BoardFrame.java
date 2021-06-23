import javax.swing.JFrame;

import gates.NotGate;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
public class BoardFrame extends JFrame{
	
	int width;
	int height;
	
	BoardFrame(){
		width = 1200;
		height = 750;
		setTitle("Untitled Circuit");
		setSize(width, height);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//getContentPane().setBackground(new Color(175, 175, 185)); //Middle gray, slight blue tint. Nice subtle background color. Maybe could be a bit lighter
		getContentPane().setBackground(new Color(175, 175, 210)); //More blue tint
		
		BoardComponent workingBoard = new BoardComponent();
		add(workingBoard);
		/*
		 * Thinking about having a way to save circuits, maybe as a file format. JFileChooser will probably be useful.
		 */
	}
}
