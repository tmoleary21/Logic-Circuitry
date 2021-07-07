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
		if(segments.size() == 0) {
			return new Line(startx, starty, startx, starty);
		}
		return segments.get(segments.size()-1);
	}
	
	public void setLocation(int startx, int starty) {
		this.startx = startx;
		this.starty = starty;
	}
	
	//Deprecated 
	public void addSegment(int endx, int endy) {
		Line lastSegment = this.getLastSegment();
		segments.add(new Line(lastSegment.getX2(), lastSegment.getY2(), endx, endy));
	}
	
	public void startExtension() {
		if(startNode.parent.orientation == CircuitElement.UP || startNode.parent.orientation == CircuitElement.DOWN) {
			horizontalPrimary = false;
			verticalSegment = new Line(startx, starty, startx, starty);
			horizontalSegment = new Line(verticalSegment.getX2(), verticalSegment.getY2(), verticalSegment.getX2(), verticalSegment.getY2());
		}
		else {
			horizontalPrimary = true;
			horizontalSegment = new Line(startx, starty, startx, starty);
			verticalSegment = new Line(horizontalSegment.getX2(), horizontalSegment.getY2(), horizontalSegment.getX2(), horizontalSegment.getY2());
		}
	}
	
	//Returns a string representation of the triangular region the mouse is in 
	//relative to the node this wire is connected to
	//Returns "top", "bottom", "left", or "right"
	private String getMouseRegion(MouseEvent e) {
		int mouseGridX = Math.round(e.getX() / 10) * 10;
		int mouseGridY = Math.round(e.getY() / 10) * 10;
		if((mouseGridY < (mouseGridX - startx) + starty) && (mouseGridY < -(mouseGridX - startx) + starty)) {
			return "top";
		}
		if((mouseGridY > (mouseGridX - startx) + starty) && (mouseGridY > -(mouseGridX - startx) + starty)) {
			return "bottom";
		}
		if((mouseGridX < (mouseGridY - starty) + startx) && (mouseGridX < -(mouseGridY - starty) + startx)) {
			return "left";
		}
		if((mouseGridX > (mouseGridY - starty) + startx) && (mouseGridX > -(mouseGridY - starty) + startx)) {
			return "right";
		}
		return "";
	}
	
	//Checks the vertical/horizontal segments and swaps if necessary
	public void setPrimary(int mouseGridX, int mouseGridY, int prevMouseX, int prevMouseY) {
		if(horizontalPrimary) {
			//Mouse crosses vertical
			if(Line.linesIntersect(prevMouseX, prevMouseY, mouseGridX, mouseGridY, startx, prevMouseY, startx, mouseGridY)) {
				System.out.println("Swap to vertical primary");
				horizontalPrimary = false;
			}
		}
		else {
			//Mouse crosses horizontal
			if(Line.linesIntersect(prevMouseX, prevMouseY, mouseGridX, mouseGridY, prevMouseX, starty, mouseGridX , starty)) {
				System.out.println("Swap to horizontal primary");
				horizontalPrimary = true;
			}
		}
	}
	
	//This is only run while the mouse is held/dragging
	public void extender(MouseEvent e, int prevMouseX, int prevMouseY) {
		int mouseGridX = Math.round(e.getX() / 10) * 10;
		int mouseGridY = Math.round(e.getY() / 10) * 10;
		setPrimary(mouseGridX, mouseGridY, prevMouseX, prevMouseY);
		if(horizontalPrimary) {
			horizontalSegment = new Line(startx, starty, mouseGridX, starty);
			verticalSegment = new Line(mouseGridX, starty, mouseGridX, mouseGridY); //Extends off horizontalSegment
		}
		else {
			verticalSegment = new Line(startx, starty, startx, mouseGridY);
			horizontalSegment = new Line(startx, mouseGridY, mouseGridX, mouseGridY); //Extends off verticalSegment
		}
	}
	
	public void diagonalExtender(MouseEvent e) {
		horizontalSegment = new Line(startx, starty, startx, starty);
		int mouseGridX = Math.round(e.getX() / 10) * 10;
		int mouseGridY = Math.round(e.getY() / 10) * 10;
		/* Quadrants:
		 * 3	|	4
		 * 		|
		 * -----------
		 * 		|			
		 * 	2	| 	1
		 */
		int length = Math.max(Math.abs(mouseGridX-startx), Math.abs(mouseGridY-starty));
		if(mouseGridX-startx >= 0 && mouseGridY-starty >= 0) {
			//Quadrant 1
			verticalSegment = new Line(startx, starty, startx+length, starty+length);
		}
		else if(mouseGridX-startx <= 0 && mouseGridY-starty <= 0) {
			//Quadrant 3
			verticalSegment = new Line(startx, starty, startx-length, starty-length);			
		}
		else if(mouseGridX-startx >= 0 && mouseGridY-starty <= 0) {
			//Quadrant 4
			verticalSegment = new Line(startx, starty, startx+length, starty-length);
		}
		else {
			//Quadrant 2
			verticalSegment = new Line(startx, starty, startx-length, starty+length);
		}
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
