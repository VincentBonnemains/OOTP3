/**
 *  A relation tool is the basic tool for establishing a relationship between
 *	any two model elements (nodes). It augments the GraphicTool protocol
 *	with methods to determine if the relation can start or end on the
 *	specified nodes, and a method to form the appropriate relation.
 *
 *  @author	K Barclay
 */
 
package tool;



import java.awt.*; 
import java.awt.event.*;

import diagram.*;
import window.*;



public abstract class RelationTool	implements	RelationToolIF {

    /**
     * Use the relation tool prepare for possible dragging and creation of a relation
     *	model element. Dragging can result in either one or two line segments according
     *	to the relative positions of the two node boxes. If there is only one then we
     *	refer to it as the second segment. If there are two segments then the second
     *	is nearest the mouse.
     */
    public void		doLeftMousePressed(MouseEvent event, ModelElement selectedElement) {
    	CollieModelPanel modelPanel = CollieModelPanel.getCollieModelPanel();
    	
    	if(selectedElement != null) {
    	    theSourceNode = (NodeElement)selectedElement;
    	
    	    modelPanel.deselectAll();
    	    
    	    theSecondSegment = new LineSegment(new Point(event.getPoint()), new Point(event.getPoint()));
    	    theFirstSegment = null;
    	
    	    theGraphicsContext = modelPanel.getGraphics();
    	    theGraphicsContext.setXORMode(Color.white);
    	    theSecondSegment.draw(theGraphicsContext);
    	}
    }
    
    
    
    /**
     * If the mouse up has occurred above a node box then it acts as the
     *	destination for the relation. Prepare a new relation, add
     *	it to the model and advise both nodes of its inclusion. The final
     *	segment is clipped to the destination node edge before establishing
     *	the actual relation.
     */
    public void		doLeftMouseReleased(MouseEvent event, ModelElement selectedElement) {
    	CollieModelPanel modelPanel = CollieModelPanel.getCollieModelPanel();

    	ModelElement destinationSymbol = modelPanel.hitTest(event.getPoint());
    	if(destinationSymbol != null) {
    	    theDestinationNode = (NodeElement)destinationSymbol;
    	    
    	    if(theSourceNode != theDestinationNode) {
    	        final Point finish = theSecondSegment.getFinish();
    	        final Rectangle destinationBounds = theDestinationNode.getBounds();
    	    	    
    	        switch(theDestinationNode.getCompassBearing(theSecondSegment.getStart())) {
    	       	    case NodeElement.NORTH :
    	       	    	theSecondSegment.setFinish(finish.x, destinationBounds.y);
    	        	break;
    	    	    case NodeElement.SOUTH :
    	    	    	theSecondSegment.setFinish(finish.x, destinationBounds.y + destinationBounds.height);
    	    	    	break;
    	    	    case NodeElement.EAST :
    	    	    	theSecondSegment.setFinish(destinationBounds.x + destinationBounds.width, finish.y);
    	    	    	break;
    	    	    case NodeElement.WEST :
    	    	    	theSecondSegment.setFinish(destinationBounds.x, finish.y);
    	    	    	break;
    	        }
    	    	    
    	        LinkageElement relation = this.makeLinkage(theSourceNode, theDestinationNode, theFirstSegment, theSecondSegment);
    	    	    
    	        modelPanel.addModelElement(relation);
    	        theSourceNode.addRelation(relation);
    	        theDestinationNode.addRelation(relation);
    	    	    
    	    }
    	}
    }
    
    
    
    /**
     * At its simplest we simply obliterate the old segment(s) and draw anew the new
     *	segment(s). According to the mouse movements and its geographic position
     *	relative to the source node we may create or remove the first segment. The
     *	segment adjacent to the source node is clipped to the appropriate geographic
     *	boundary.
     */
    public void		doLeftMouseDragged(MouseEvent event, ModelElement selectedElement) {
    	if(selectedElement != null) {
    	
    	    if(theSourceNode.isHit(event.getPoint()) == false) {
    	    	if(theFirstSegment != null)				// obliterate the old
    	    	    theFirstSegment.draw(theGraphicsContext);
    	    	theSecondSegment.draw(theGraphicsContext);
    	
    		final Rectangle bounds = theSourceNode.getBounds();
    		final Point eventPoint = event.getPoint();
    		int eventX = eventPoint.x;
    		int eventY = eventPoint.y;
    		final int bearing = theSourceNode.getCompassBearing(eventPoint);
    		
    		switch(bearing) {
    		    case NodeElement.NORTHWEST :
    		    case NodeElement.NORTHEAST :
    		    case NodeElement.SOUTHWEST :
    		    case NodeElement.SOUTHEAST :
    		    	if(theFirstSegment != null) {
    		    	    final Point firstStart = theFirstSegment.getStart();
    		    	    
    		    	    switch(bearing) {
    		    		case NodeElement.NORTHWEST :
    		    		case NodeElement.SOUTHWEST :
    		    		    theFirstSegment.setStart(bounds.x, firstStart.y);
    		    		    break;
    		    		case NodeElement.NORTHEAST :
    		    		case NodeElement.SOUTHEAST :
    		    		    theFirstSegment.setStart(bounds.x + bounds.width, firstStart.y);
    		    		    break;
    		    	    }
    		    	    
    		    	    theFirstSegment.setFinish(eventPoint.x, theFirstSegment.getFinish().y);
    		    	    eventY = theFirstSegment.getFinish().y;
    		    	
    		    	} else {
    		    	    int startX = 0;
    		    	    int startY = 0;
    		    	    eventY = (bounds.y + bounds.y + bounds.height) / 2;
    		    	    
    		    	    switch(bearing) {
    		    	    	case NodeElement.NORTHWEST :
    		    	    	case NodeElement.SOUTHWEST :
    		    	    	    startX = bounds.x;
    		    	    	    startY = eventY;
    		    	    	    break;
    		    	    	case NodeElement.NORTHEAST :
    		    	    	case NodeElement.SOUTHEAST :
    		    	    	    startX = bounds.x + bounds.width;
    		    	    	    startY = eventY;
    		    	    	    break;
    		    	    }
    		    	    theFirstSegment = new LineSegment(new Point(startX, startY), new Point(eventPoint.x, eventY));
    		    	}
    		    	break;
    		    	
    		    case NodeElement.NORTH :
    		    case NodeElement.WEST :
    		    case NodeElement.SOUTH :
    		    case NodeElement.EAST : {
    		    	switch(bearing) {
    		    	    case NodeElement.NORTH :
    		    	    	eventY = bounds.y;
    		    	    	break;
    		    	    case NodeElement.WEST :
    		    	    	eventX = bounds.x;
    		    	    	break;
    		    	    case NodeElement.SOUTH :
    		    	    	eventY = bounds.y + bounds.height;
    		    	    	break;
    		    	    case NodeElement.EAST :
    		    	    	eventX = bounds.x + bounds.width;
    		    	    	break;
    		    	}
    		    	if(theFirstSegment != null)
    		    	    theFirstSegment = null;
    		    	break;
    		    }
    		}    // switch on bearing
    		
    	    	theSecondSegment.setStart(eventX, eventY);
    	    	theSecondSegment.setFinish(eventPoint.x, eventPoint.y);
    	    	if(theFirstSegment != null)				// redraw the new
    	    	    theFirstSegment.draw(theGraphicsContext);
    	    	theSecondSegment.draw(theGraphicsContext);
    	    }
    	}
    }
    
    public void		doRightMousePressed(MouseEvent event, ModelElement selectedElement) {
    	System.out.println("RelationTool: doRightMousePressed");
    }

    
    
// ---------- properties ----------------------------------

    NodeElement		theSourceNode		= null;
    NodeElement		theDestinationNode	= null;
    
    LineSegment		theSecondSegment	= null;
    LineSegment		theFirstSegment		= null;
    
    Graphics 		theGraphicsContext	= null;

}	// class:	RelationTool

// ============================================================================
