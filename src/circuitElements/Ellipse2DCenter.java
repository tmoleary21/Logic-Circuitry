package circuitElements;

import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class Ellipse2DCenter extends Ellipse2D.Double{
	
	public Ellipse2DCenter(double x, double y, double xDiameter, double yDiameter){
		super(x - xDiameter/2, y - yDiameter/2, xDiameter, yDiameter);
	}
}
