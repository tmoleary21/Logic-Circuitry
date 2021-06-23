package circuitElements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Wire{
	
	int startx;
	int starty;
	
	int value;
	private ArrayList<Line> segments;
	Node startNode;
	Node endNode;
	
	//Temporary segments used when extending wire
	private Line verticalSegment;
	private Line horizontalSegment;
	private boolean horizontalPrimary;
	
	public Wire(int startx, int starty) {
		this.startx = startx;
		this.starty = starty;
		value = 0;
		segments = new ArrayList<Line>();
		segments.add(new Line(startx, starty, startx, starty));
		verticalSegment = new Line(startx, starty, startx, starty);
		horizontalSegment = verticalSegment;
		horizontalPrimary = false;
	}
	
	public void paint(Graphics2D painter) {
		if(value == 0) painter.setColor(new Color(0));
		else if(value == 1) painter.setColor(new Color(255, 0, 0));
		for(Line segment : segments) {
			painter.draw(segment);
		}
		
		if(verticalSegment != null) {
			painter.draw(verticalSegment);			
		}
		if(horizontalSegment != null) {
			painter.draw(horizontalSegment);
		}
	}
	
	public Line getLastSegment() {
		return segments.get(segments.size()-1);
	}
	
	public void setLocation(int startx, int starty) {
		this.startx = startx;
		this.starty = starty;
	}
	
	public void addSegment(int endx, int endy) {
		Line lastSegment = this.getLastSegment();
		segments.add(new Line(lastSegment.getX2(), lastSegment.getY2(), endx, endy));
	}
	
	//This is only run while the mouse is held/dragging
	public void extender(MouseEvent e, int prevMouseX, int prevMouseY) {
		Line lastSegment = getLastSegment();
		int mouseGridX = Math.round(e.getX() / 10) * 10;
		int mouseGridY = Math.round(e.getY() / 10) * 10;
		System.out.println("Mouse on Grid: " + mouseGridX + ", " + mouseGridY);
		System.out.println(startx + ", " + starty);
		
		if( ((mouseGridY < (mouseGridX - startx) + starty) && (mouseGridY < -(mouseGridX - startx) + starty) 
			|| (mouseGridY > (mouseGridX - startx) + starty) && (mouseGridY > -(mouseGridX - startx) + starty) //Checks if mouse is within upper or lower quadrant.(separated by y = x lines centered on (startx,starty)
			|| (verticalSegment.length() != 0 && Line.linesIntersect(verticalSegment.getX1(),verticalSegment.getY1(),verticalSegment.getX2(),verticalSegment.getY2(),e.getX(),e.getY(),prevMouseX,prevMouseY)/*verticalSegment.getX1() == startx*/)) //Checks if there is already some vertical segment created as primary
			&& !horizontalPrimary)
		{
			System.out.println("Top/Bottom region");
			verticalSegment = new Line(lastSegment.getX2(), lastSegment.getY2(), lastSegment.getX2(), mouseGridY);
			horizontalSegment = new Line(verticalSegment.getX2(), verticalSegment.getY2(), mouseGridX, verticalSegment.getY2());
		}
		else if( (mouseGridX < (mouseGridY - starty) + startx) && (mouseGridX < -(mouseGridY - starty) + startx) 
			|| (mouseGridX > (mouseGridY - starty) + startx) && (mouseGridX > -(mouseGridY - starty) + startx) //Checks if mouse is within right or left quadrant
			|| (horizontalSegment.length() != 0 && /*Line.linesIntersect(horizontalSegment.getX1(),horizontalSegment.getY1(),horizontalSegment.getX2(),horizontalSegment.getY2(),e.getX(),e.getY(),prevMouseX,prevMouseY)*/horizontalSegment.getY1() == starty) )// Checks if there is already some horizontal segment created as primary
		{
			System.out.println("Right/Left region");
			horizontalPrimary = true;
			horizontalSegment = new Line(lastSegment.getX2(), lastSegment.getY2(), mouseGridX, lastSegment.getY2());
			verticalSegment = new Line(horizontalSegment.getX2(), horizontalSegment.getY2(), horizontalSegment.getX2(), mouseGridY);
			if(horizontalSegment.length() == 0) {
				horizontalPrimary = false;
			}
		}
		else { //Case for being right on boundary lines between quadrants. Currently just sets to 0 length lines.
			System.out.println("else case");
			verticalSegment = new Line(lastSegment.getX2(), lastSegment.getY2(),lastSegment.getX2(),lastSegment.getY2());
			horizontalSegment = new Line(lastSegment.getX2(), lastSegment.getY2(),lastSegment.getX2(),lastSegment.getY2());
		}
		
	}
	
	public void diagonalExtender(MouseEvent e) {
		Line lastSegment = getLastSegment();
		int mouseGridX = Math.round(e.getX() / 10) * 10;
		int mouseGridY = Math.round(e.getY() / 10) * 10;
		verticalSegment = new Line(lastSegment.getX2(), 
								   lastSegment.getY2(), 
								   lastSegment.getX2()+Math.max(mouseGridX-lastSegment.getX2(), mouseGridY-lastSegment.getY2()), 
								   lastSegment.getY2()+Math.max(mouseGridX-lastSegment.getX2(), mouseGridY-lastSegment.getY2()));
	}
	
	public void addExtension() {
		if(verticalSegment.getX1() == getLastSegment().getX2() && verticalSegment.getY1() == getLastSegment().getY2()) {
			segments.add(verticalSegment);
			segments.add(horizontalSegment);
		}
		else {
			segments.add(horizontalSegment);
			segments.add(verticalSegment);
		}
		//Reset segments
		verticalSegment = new Line(startx, starty, startx, starty);
		horizontalSegment = verticalSegment;
		horizontalPrimary = false;
	}
	
	public void updateValue() {
		if((startNode != null && startNode.isOutput && startNode.value == 1) 
				|| (endNode != null && endNode.isOutput && endNode.value == 1)) {
			this.value = 1;
		}
		else {
			this.value = 0;
		}
		if(startNode != null && !startNode.isOutput) 
		startNode.updateValue(value);
		if(endNode != null && !endNode.isOutput) 
		endNode.updateValue(value);
	}
}
