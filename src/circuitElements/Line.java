package circuitElements;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Line extends Line2D.Double{
	public Line(double x1, double y1, double x2, double y2) {
		super(x1,y1,x2,y2);
	}
	
	public double length() {
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
}
