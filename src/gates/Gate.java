package gates;

import java.awt.Graphics2D;
import circuitElements.CircuitElement;

public abstract class Gate extends CircuitElement{

	public Gate(int x, int y){
		super(x, y);
	}
	
	public Gate(int x, int y, int orientation) {
		super(x, y, orientation);
	}
	
	public abstract void computeOutput();
}
