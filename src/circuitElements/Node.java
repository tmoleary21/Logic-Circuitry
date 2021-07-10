package circuitElements;
import java.awt.Graphics2D;
import java.awt.Color;
public class Node {
	
	private double x;
	private double y;
	public int value; //0 or 1
	public boolean isOutput; 
	
	private boolean hovering;
	
	private Ellipse2DCenter nodeShape;
	private Ellipse2DCenter selectionCircle;
	
	Object parent;
	
	public Node(double x, double y, int value, boolean isOutput, CircuitElement parentOfNode) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.hovering = false;
		nodeShape = new Ellipse2DCenter(0,0,0,0);
		selectionCircle = new Ellipse2DCenter(0,0,0,0);
		this.isOutput = isOutput;
		this.parent = parentOfNode;
	}
	
	public Node(double x, double y, int value, boolean isOutput, Wire parentOfNode) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.hovering = false;
		nodeShape = new Ellipse2DCenter(0,0,0,0);
		selectionCircle = new Ellipse2DCenter(0,0,0,0);
		this.isOutput = isOutput;
		this.parent = parentOfNode;
	}
	
	public void paint(Graphics2D painter) {
		//System.out.println(hovering);
		nodeShape = new Ellipse2DCenter(x, y, 4, 4);
		selectionCircle = new Ellipse2DCenter(x, y, 6, 6);
		if(value == 1) {
			painter.setColor(new Color(255, 0, 0));
		}
		else if(value == 0) {
			painter.setColor(new Color(0, 0, 0));
		}
		painter.fill(nodeShape);
		if(hovering) {
			painter.setColor(new Color(0,0,255));
			painter.draw(selectionCircle);
		}
	}
	
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public boolean contains(int x, int y) {
		return selectionCircle.contains(x, y);
	}
	
	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}
	
	public Wire createWire() {
		Wire wire = new Wire((int)x, (int)y);
		wire.startNode = this;
		return wire;
	}
	
	public void endWire(Wire wire) {
		wire.endNode = this;
	}
	
	public void updateValue(int value) {
		this.value = value;
	}
	
	public int getParentOrientation() {
		if(parent instanceof CircuitElement) {
			return ((CircuitElement) parent).orientation;
		}
		else { //instance of Wire
			return ((Wire) parent).getPointDirection();
		}
	}
	
	public String toString() {
		return parent + " " + ((isOutput) ? "output" : "input") + "Node";
	}
}
