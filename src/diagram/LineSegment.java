// ==================================================================
//
//	File:		LineSegment.java
//	Author:		K Barclay
//
// ==================================================================



package diagram;



import java.awt.*;



public class LineSegment {

    public			LineSegment(Point start, Point finish) {
	theStart = start;
	theFinish = finish;
    }



    public			LineSegment(int startX, int startY, int finishX, int finishY) {
	theStart = new Point(startX, startY);
	theFinish = new Point(finishX, finishY);
    }



    public Point		getStart() {
	return theStart;
    }

    public Point		getFinish() {
	return theFinish;
    }
    
    
    
    public void			setStart(int x, int y) {
    	theStart.x = x;
    	theStart.y = y;
    }
    
    public void			setFinish(int x, int y) {
    	theFinish.x = x;
    	theFinish.y = y;
    }



    public boolean		isHorizontal() {
	return (theStart.y == theFinish.y);
    }

    public boolean		isVertical() {
	return (theStart.x == theFinish.x);
    }



    public boolean		isHit(int mouseX, int mouseY) {
	final int x = Math.min(theStart.x, theFinish.x);
	final int y = Math.min(theStart.y, theFinish.y);
	final int width = Math.abs(theFinish.x - theStart.x) + 2;
	final int height = Math.abs(theFinish.y - theStart.y) + 2;
	Rectangle bounds = new Rectangle(x - 1, y - 1, width, height);
	return bounds.contains(mouseX, mouseY);
    }
    
    public boolean		isHit(Point p) {
    	return this.isHit(p.x, p.y);
    }



    public void			draw(Graphics graphicsContext) {
	graphicsContext.drawLine(theStart.x, theStart.y, theFinish.x, theFinish.y);
    }



// ---------- properties ------------------------------------------------------
// ---------- properties ------------------------------------------------------
// ---------- properties ------------------------------------------------------

    private Point		theStart;
    private Point		theFinish;

}

// ==================================================================

