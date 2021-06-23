package gates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import circuitElements.Ellipse2DCenter;
import circuitElements.Node;

public class NotGate extends Gate{
	
	private Polygon body;
	private Ellipse2DCenter endBulb;
	
	public NotGate(int x, int y) {
		super(x, y);
		body = new Polygon();
		endBulb = new Ellipse2DCenter(0,0,0,0);
		nodes = new Node[] {new Node(0,0,0,false,this), new Node(0,0,0,true,this)};
	}
	
	public NotGate(int x, int y, int orientation) {
		super(x, y, orientation);
		body = new Polygon();
		endBulb = new Ellipse2DCenter(0,0,0,0);
		nodes = new Node[] {new Node(0,0,0,false,this), new Node(0,0,0,true,this)};
	}
	
	@Override
	public void paint(Graphics2D painter) {
		
		initializeParts();
		
		painter.setColor(new Color(0));
		painter.draw(body);
		painter.draw(endBulb);
		painter.setColor(new Color(255, 255, 255));
		painter.fill(body);
		painter.fill(endBulb);
		
//		inputNode.paint(painter);
//		outputNode.paint(painter);
		for(Node node : nodes) {
			node.paint(painter);
		}
		
	}
	
	public void initializeParts() {
		if(orientation == RIGHT) {
			body = new Polygon();
			body.addPoint(x, y-10);
			body.addPoint(x, y+10);
			body.addPoint(x+35, y);
			endBulb = new Ellipse2DCenter(x+35, y, 10, 10);
			nodes[0].setLocation(x, y);
			nodes[1].setLocation(x+40, y);
			computeOutput();
		}
		if(orientation == DOWN) {
			body = new Polygon();
			body.addPoint(x+10, y);
			body.addPoint(x-10, y);
			body.addPoint(x, y+35);
			endBulb = new Ellipse2DCenter(x, y+35, 10, 10);
			nodes[0].setLocation(x, y);
			nodes[1].setLocation(x, y+40);
			computeOutput();
		}
		if(orientation == LEFT) {
			body = new Polygon();
			body.addPoint(x, y+10);
			body.addPoint(x, y-10);
			body.addPoint(x-35, y);
			endBulb = new Ellipse2DCenter(x-35, y, 10, 10);
			nodes[0].setLocation(x, y);
			nodes[1].setLocation(x-40, y);
			computeOutput();
		}
		if(orientation == UP) {
			body = new Polygon();
			body.addPoint(x-10, y);
			body.addPoint(x+10, y);
			body.addPoint(x, y-35);
			endBulb = new Ellipse2DCenter(x, y-35, 10, 10);
			nodes[0].setLocation(x, y);
			nodes[1].setLocation(x, y-40);
			computeOutput();
		}
	}
	
	@Override
	public boolean contains(int x, int y) {
		return body.contains(x,y) || endBulb.contains(x, y);
	}
	
	@Override
	public void computeOutput() { //Not operation
		nodes[1].value = (nodes[0].value == 0) ? 1 : 0;
	}
	@Override
	public void runRightClickEvent() {};
	
	
	public String toString() {
		return "Not Gate";
	}
}