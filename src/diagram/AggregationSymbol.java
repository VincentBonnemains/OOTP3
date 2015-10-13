/**
 *  An aggregation symbol represents a relationship that is formed
 *	between instances.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;
import java.awt.event.*;
import java.util.*;



public class AggregationSymbol extends LinkageElement {

    public	AggregationSymbol(NodeElement source, NodeElement destination, LineSegment first, LineSegment second) {
    	super(source, destination, first, second);
    }
    
    
    
    public void		draw(Graphics graphicsContext) {
    	super.draw(graphicsContext);
    	
    	this.drawDiamond(graphicsContext);
    }
    
    
    
    private void	drawDiamond(Graphics graphicsContext) {
	final int cardinality = theLineSegments.size();
	LineSegment finalSegment = (LineSegment)theLineSegments.get(cardinality - 1);
	Point arrowHead = finalSegment.getFinish();
	int arrowHeadX = arrowHead.x;
	int arrowHeadY = arrowHead.y;
	Polygon polygon = new Polygon();

	int geographicBearing = theDestinationNode.getCompassBearing(finalSegment.getStart());
	switch(geographicBearing) {
	    case NodeElement.NORTH : {
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		    polygon.addPoint(arrowHeadX-DIAMONDSIZE, arrowHeadY-DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX, arrowHeadY-(DIAMONDSIZE + DIAMONDSIZE));
		    polygon.addPoint(arrowHeadX+DIAMONDSIZE, arrowHeadY-DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		}
		break;
	    case NodeElement.SOUTH : {
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		    polygon.addPoint(arrowHeadX+DIAMONDSIZE, arrowHeadY+DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX, arrowHeadY+(DIAMONDSIZE + DIAMONDSIZE));
		    polygon.addPoint(arrowHeadX-DIAMONDSIZE, arrowHeadY+DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		}
		break;
	    case NodeElement.EAST : {
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		    polygon.addPoint(arrowHeadX+DIAMONDSIZE, arrowHeadY-DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX+DIAMONDSIZE + DIAMONDSIZE, arrowHeadY);
		    polygon.addPoint(arrowHeadX+DIAMONDSIZE, arrowHeadY+DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		}
		break;
	    case NodeElement.WEST : {
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		    polygon.addPoint(arrowHeadX-DIAMONDSIZE, arrowHeadY+DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX-(DIAMONDSIZE + DIAMONDSIZE), arrowHeadY);
		    polygon.addPoint(arrowHeadX-DIAMONDSIZE, arrowHeadY-DIAMONDSIZE);
		    polygon.addPoint(arrowHeadX, arrowHeadY);
		}
		break;
	}

	if(theCompositeFlag)
	    graphicsContext.fillPolygon(polygon);
	else {
	    graphicsContext.fillPolygon(polygon);
	    graphicsContext.drawPolygon(polygon);
	    graphicsContext.drawLine(polygon.xpoints[0], polygon.ypoints[0], polygon.xpoints[2], polygon.ypoints[2]);
	}
    }
    
    
    
// ---------- properties ------------------------------------------------------

    private boolean			theCompositeFlag = false;

    private final static int		DIAMONDSIZE = 10;

}	// class:	AggregationSymbol

// ============================================================================

