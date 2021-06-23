package gates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import circuitElements.Node;

public class OrGate extends Gate {
	
	Path2D.Double shape;
	public OrGate(int x, int y) {
		super(x, y);
		shape = new Path2D.Double();
		nodes = new Node[] {new Node(0,0,0,false,this), new Node(0,0,0,false,this), new Node(0,0,0,true,this)};
	}

	@Override
	public void computeOutput() {
		if(nodes[0].value == 1 || nodes[1].value == 1) {
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
		painter.draw(shape);
		painter.setColor(new Color(255,255,255));
		painter.fill(shape);
		for(Node node : nodes) node.paint(painter);
	}

	@Override
	public void initializeParts() {
		shape.reset();
		shape.moveTo(x, y-10);
		shape.curveTo(x+40,y,x+40,y, x+60,y+20);
		shape.curveTo(x+40,y+40,x+40,y+40, x,y+50);
		shape.curveTo(x+10,y+20,x+10,y+20, x,y-10);
		shape.closePath();
		nodes[0].setLocation(x, y);
		nodes[1].setLocation(x, y+40);
		nodes[2].setLocation(x+60, y+20);
		computeOutput();
	}

	@Override
	public boolean contains(int x, int y) {
		return shape.contains(x,y);
	}

	@Override
	public void runRightClickEvent() {
		// TODO Auto-generated method stub

	}

}
