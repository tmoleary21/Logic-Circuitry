package gates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import circuitElements.Ellipse2DCenter;
import circuitElements.Node;

public class AndGate extends Gate {

	Rectangle leftBody;
	Ellipse2DCenter rightBody;
	
	public AndGate(int x, int y) {
		super(x, y);
		leftBody = new Rectangle(0,0,0,0);
		rightBody = new Ellipse2DCenter(0,0,0,0);
		nodes = new Node[] {new Node(0,0,0,false,this), new Node(0,0,0,false,this), new Node(0,0,0,true,this)};
	}

	@Override
	public void computeOutput() {
		if(nodes[0].value == 1 && nodes[1].value == 1) {
			nodes[2].value = 1;
		}
		else {
			nodes[2].value = 0;
		}
	}

	@Override
	public void paint(Graphics2D painter) {
		initializeParts();
		painter.setColor(new Color(0,0,0));
		painter.draw(leftBody);
		painter.draw(rightBody);
		painter.setColor(new Color(255,255,255));
		painter.fill(leftBody);
		painter.fill(rightBody);
		for(Node node : nodes) node.paint(painter);
	}

	@Override
	public void initializeParts() {
		if(orientation == 0) {
			leftBody = new Rectangle(x, y-10, 40, 60);
			rightBody = new Ellipse2DCenter(x+40, y+20, 40, 60);
			nodes[0].setLocation(x, y);
			nodes[1].setLocation(x, y+40);
			nodes[2].setLocation(x+60, y+20);
			computeOutput();
		}
		if(orientation == 1) {
			
		}
		if(orientation == 2) {
			
		}
		if(orientation == 3) {
	
		}
	}

	@Override
	public boolean contains(int x, int y) {
		return leftBody.contains(x,y) || rightBody.contains(x,y);
	}

	@Override
	public void runRightClickEvent() {}

}
