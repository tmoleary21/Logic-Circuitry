package circuitElements;

import java.awt.Color;
import java.awt.Graphics2D;

public class outputLED extends CircuitElement {

	int value;
	
	private Ellipse2DCenter border;
	private Ellipse2DCenter LED;
	
	public outputLED(int x, int y) {
		super(x, y);
		border = new Ellipse2DCenter(0,0,0,0);
		LED = new Ellipse2DCenter(0,0,0,0);
		nodes = new Node[] {new Node(0,0,0, false, this)};
	}
	
	public outputLED(int x, int y, int orientation) {
		super(x, y, orientation);
		border = new Ellipse2DCenter(0,0,0,0);
		LED = new Ellipse2DCenter(0,0,0,0);
		nodes = new Node[] {new Node(0,0,0, false, this)};
	}
	
	@Override
	public void paint(Graphics2D painter) {
		initializeParts();
		
		painter.setColor(new Color(100,100,100));
		painter.draw(border);
		painter.fill(border);
		painter.draw(LED);
		if(value == 0) {
			painter.setColor(new Color(255,255,255));
		}
		else {
			painter.setColor(new Color(255,0,0));
		}
		painter.fill(LED);
		nodes[0].paint(painter);
	}

	@Override
	public void initializeParts() {
		if(orientation == 0) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			LED = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x+20, y);
			this.value = nodes[0].value;
		}
		if(orientation == 1) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			LED = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x, y+20);
			this.value = nodes[0].value;
		}
		if(orientation == 2) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			LED = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x-20, y);
			this.value = nodes[0].value;
		}
		if(orientation == 3) {
			border = new Ellipse2DCenter(x, y, 40, 40);
			LED = new Ellipse2DCenter(x, y, 30, 30);
			nodes[0].setLocation(x, y-20);
			this.value = nodes[0].value;
		}
		
	}

	@Override
	public boolean contains(int x, int y) {
		return border.contains(x,y);
	}

	@Override
	public void runRightClickEvent() {}

}
