import java.util.ArrayList;
//import java.util.Timer; //Runs on separate thread. Actions that run on the EDT (Event Dispatch Thread) Should not be run anywhere else.

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.Timer; // Runs on the EDT (Event Dispatch Thread), which is the thread that all swing tasks run off of.
//Tasks are sent to a queue on the EDT and run sequentially. Need to be careful what is sent and how often something is sent to the EDT to prevent lag.

import circuitElements.CircuitElement;
import circuitElements.Ellipse2DCenter;
import circuitElements.InputSwitch;
import circuitElements.Node;
import circuitElements.Wire;
import circuitElements.outputLED;
import gates.AndGate;
import gates.NotGate;
import gates.OrGate;

@SuppressWarnings("serial")
public class BoardComponent extends JComponent 
implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener, ActionListener{
	
	ArrayList<CircuitElement> onScreenElements;
	ArrayList<Wire> wires;
	CircuitElement LMBSelection;
	CircuitElement RMBSelection;
	Wire selectedWire;
	
	int prevMouseX, prevMouseY;
	boolean alt;
	
	private Timer timer;
	final int DELAY = 1000/60;
	
	
	public BoardComponent() {
		onScreenElements = new ArrayList<CircuitElement>();
		wires = new ArrayList<Wire>();
		
		timer = new Timer(DELAY, this);
		timer.start();
		
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		
		//Temporary spot for initializing CircuitElements to show on screen. Eventually, will be initialized on their respective factory classes
		onScreenElements.add(new NotGate(40, 40));
		onScreenElements.add(new InputSwitch(40, 80));
		onScreenElements.add(new outputLED(40, 120));
		onScreenElements.add(new AndGate(40, 200));
		onScreenElements.add(new OrGate(40, 300));
		alt = false;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D painter = (Graphics2D) g;
		drawGrid(painter);		
		for(CircuitElement element : onScreenElements) {
			element.paint(painter);
		}
		for(Wire wire : wires) {
			wire.paint(painter);
		}
	}
	
	public void drawGrid(Graphics2D painter) {
		//painter.setColor(new Color(150, 150, 150));
		//painter.setColor(new Color(255,255,255));
		painter.setColor(new Color(200,210,230));
		for(int i = 0; i < 1200; i += 10/*20*/) {
			for (int j = 0; j < 750; j += 10/*20*/) {
				//painter.draw(new Rectangle(i, j, 20, 20));
				painter.fill(new Ellipse2DCenter(i, j, 2, 2));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { //Runs 60 times per second because of the timer.	
		for(Wire wire : wires) wire.updateValue();
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		//Determines if clicking on an on screen circuit element (Gate, button, etc)
		for(CircuitElement element : onScreenElements) {
			for(Node node : element.nodes) {
				if(node.contains(e.getX(), e.getY())) {
					selectedWire = node.createWire();
					wires.add(selectedWire);
					return;
				}
			}
			if(element.contains(e.getX(), e.getY())) {
				if(e.getButton() == MouseEvent.BUTTON1) LMBSelection = element;
				if(e.getButton() == MouseEvent.BUTTON3) RMBSelection = element;
				return;
			}
		} 
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Running right click events for CircuitElements
		if(RMBSelection != null) {
			RMBSelection.runRightClickEvent();
		}
		
		if(selectedWire != null) {
			selectedWire.addExtension();
			//Check if hovering over node when released
			for(CircuitElement element : onScreenElements) {
				for(Node node : element.nodes) {
					if(node.contains(e.getX(), e.getY())) {
						node.endWire(selectedWire);
						if(!node.contains((int)selectedWire.getLastSegment().getX2(), (int)selectedWire.getLastSegment().getY2())) {
							selectedWire.addSegment((int)node.getX(), (int)node.getY());
						}
					}
				}
			}
			
		}
		
		//Reset mouse selection
		LMBSelection = null;
		RMBSelection = null;
		selectedWire = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(LMBSelection != null) {
			LMBSelection.setLocation((int)Math.round(e.getX() / 10.0) * 10, (int)Math.round(e.getY() / 10.0) * 10);		
		}
		if(selectedWire != null) { //Wire
			if(alt) {
				selectedWire.diagonalExtender(e);
			}
			else {
				selectedWire.extender(e, prevMouseX, prevMouseY);
			}
		}
		//Check if hovering over node while dragging
		for(CircuitElement element : onScreenElements) {
			for(Node node : element.nodes) {
				if(node.contains(e.getX(), e.getY())) {
					System.out.println(node + " :contained");
					node.setHovering(true);
				}
				else{
					node.setHovering(false);
				}
			}
		}
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println(e.getX() + " " + e.getY());
		//Check if hovering over node
		for(CircuitElement element : onScreenElements) {
			for(Node node : element.nodes) {
				if(node.contains(e.getX(), e.getY())) {
					System.out.println(node + " :contained");
					node.setHovering(true);
				}
				else{
					node.setHovering(false);
				}
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(LMBSelection != null) {
			int notches = e.getWheelRotation();
			if(notches < 0) { //Mouse Wheel Up
				LMBSelection.rotateRight();
			}
			if(notches > 0) { //Mouse Wheel Down
				LMBSelection.rotateLeft();
			}
		}
	}

	/**TODO: Seems like I need to use KEYBINDINGS instead of these methods for some reason 
	 * 		 Not sure why these don't work though										*/
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("key code: " + e.getKeyCode());
		if(e.getKeyCode() == KeyEvent.VK_ALT) {
			alt = true;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("Released");
		alt = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Typed");
	}

}
