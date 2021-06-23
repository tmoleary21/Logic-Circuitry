package circuitElements;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class CircuitElement {
	public int x;
	public int y;
	
	public int orientation;
	public final int RIGHT = 0;
	public final int DOWN = 1;
	public final int LEFT = 2;
	public final int UP = 3;
	
	public Node[] nodes;
	
	public CircuitElement(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public CircuitElement(int x, int y, int orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void rotateRight() {
		orientation = (orientation + 1) % 4;
	}
	
	public void rotateLeft() {
		orientation = (orientation + 3) % 4;
	}
	
	public abstract void paint(Graphics2D painter);
	public abstract void initializeParts();
	public abstract boolean contains(int x, int y);
	public abstract void runRightClickEvent();
}
