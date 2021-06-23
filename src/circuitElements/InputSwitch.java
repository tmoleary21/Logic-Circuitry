package circuitElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class InputSwitch extends CircuitElement{
	
	public int value;
	
	private Ellipse2DCenter border;
	private Ellipse2DCenter button;
	
	public InputSwitch(int x, int y) {
		super(x, y);
		value = 0;
		border = new Ellipse2DCenter(0,0,0,0);
		button = new Ellipse2DCenter(0,0,0,0);
		nodes = new Node[] {new Node(0,0,0,true,this)};
	}
	
	public void paint(Graphics2D painter) {
		initializeParts();
		
		painter.setColor(new Color(0));
		painter.draw(border);
		painter.setColor(new Color(255, 255, 255));
		painter.fill(border);
		
		painter.setStroke(new BasicStroke(2));
		painter.setColor(new Color(0));
		painter.draw(button);
		painter.setStroke(new BasicStroke(1));
		if(value == 0) {
			painter.setColor(new Color(255, 255, 255));
		}
		else if(value == 1) {
			painter.setColor(new Color(255, 0, 0));
		}
		painter.fill(button);
		
		nodes[0].paint(painter);
	}
	
	public void initializeParts() {
		if(orientation == 0) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			button = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x+20, y);
			nodes[0].value = value;
		}
		if(orientation == 1) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			button = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x, y+20);
			nodes[0].value = value;
		}
		if(orientation == 2) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			button = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x-20, y);
			nodes[0].value = value;
		}
		if(orientation == 3) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			button = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x, y-20);
			nodes[0].value = value;
		}
	}

	@Override
	public boolean contains(int x, int y) {
		return border.contains(x, y);
	}
	
	@Override
	public void runRightClickEvent() {
		value = (value == 0) ? 1 : 0;
	}
	
	public String toString() {
		return "Input Switch";
	}
}
