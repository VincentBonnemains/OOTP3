/**
 *  A linkage element represents a relationship that is formed
 *	between instances.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;
import java.awt.event.*;
import java.util.*;



public class LinkageElement extends ModelElement {

    public			LinkageElement(NodeElement source, NodeElement destination, LineSegment first, LineSegment second) {
    	theSourceNode = source;
    	theDestinationNode = destination;
    	
    	if(first != null)
    	    theLineSegments.add(first);
    	if(second != null)
    	    theLineSegments.add(second);
    }
    
    
    
    public boolean	isHit(int mouseX, int mouseY) {
    	int size = theLineSegments.size();
    	for(int k = 0; k < size; k++) {
    	    LineSegment segment = (LineSegment)theLineSegments.get(k);
    	    if(segment != null && segment.isHit(mouseX, mouseY))
    	    	return true;
    	}
    	
    	return false;
    }
    
    

    public void		draw(Graphics graphicsContext) {
    	Color lineColor = this.isSelected() ? Color.blue : Color.black;
    	graphicsContext.setColor(lineColor);
    	
    	int size = theLineSegments.size();
    	for(int k = 0; k < size; k++) {
    	    LineSegment segment = (LineSegment)theLineSegments.get(k);
    	    if(segment != null)
    	    	segment.draw(graphicsContext);
    	}
    }
    
    
    
    public void		doRightMousePressed(MouseEvent event, Graphics graphicsContext) {
    	/* DO NOTHING!!! */
    }
    
    
    
    public void		doLeftMousePressed(MouseEvent event, Graphics graphicsContext) {
    	theSelectedSegment = null;
    	theSelectedIndex = -1;
    	
        final int size = theLineSegments.size();
        for(int k = 0; k < size; k++) {
            theSelectedSegment = (LineSegment)theLineSegments.get(k);
            if(theSelectedSegment != null && theSelectedSegment.isHit(event.getPoint())) {
            	theSelectedIndex = k;
            	break;
            }
        }
    }
    
    
    
    public void		doLeftMouseReleased(MouseEvent event, Graphics graphicsContext) {
    	/* DO NOTHING!!! */
    }
    
    
    
    public void		doLeftMouseDragged(MouseEvent event, Graphics graphicsContext) {
    	if(theSelectedSegment != null) {
    	    this.draw(graphicsContext);		// obliterate the old
    	    
    	    final Point mousePoint = event.getPoint();
    	    final boolean selectedSegmentIsHorizontal = theSelectedSegment.isHorizontal();
    	    final Rectangle sourceBounds = theSourceNode.getBounds();
    	    final Rectangle destinationBounds = theDestinationNode.getBounds();
    	    
    	    if(selectedSegmentIsHorizontal) {
    	    	theSelectedSegment.setStart(theSelectedSegment.getStart().x, mousePoint.y);
    	    	theSelectedSegment.setFinish(theSelectedSegment.getFinish().x, mousePoint.y);
    	    } else {
    	    	theSelectedSegment.setStart(mousePoint.x, theSelectedSegment.getStart().y);
    	    	theSelectedSegment.setFinish(mousePoint.x, theSelectedSegment.getFinish().y);
    	    }
    	    
    	    if(theSelectedIndex == 0)
    	    	this.processFirstSegment(selectedSegmentIsHorizontal, mousePoint);
    	    if(theSelectedIndex == theLineSegments.size() - 1)
    	    	this.processFinalSegment(selectedSegmentIsHorizontal, mousePoint);
    	    if(theSelectedIndex > 0)
    	    	this.processSecondSegment();
    	    if(theSelectedIndex < theLineSegments.size() - 1)
    	    	this.processPenultimateSegment();
    	    
    	    this.draw(graphicsContext);		// redraw the new
    	}
    }



    private void		processFirstSegment(boolean segmentIsHorizontal, Point mousePoint) {
    	final Rectangle sourceBounds = theSourceNode.getBounds();
    	if(segmentIsHorizontal && ! theSourceNode.isLatitudinal(mousePoint)) {			// insert new vertical segment
    	    theSelectedSegment.setStart((sourceBounds.x + sourceBounds.x + sourceBounds.width) / 2, theSelectedSegment.getStart().y);
    	    
    	    final int startX = theSelectedSegment.getStart().x;
    	    final int startY = (theSourceNode.isNortherly(mousePoint)) ? sourceBounds.y : sourceBounds.y + sourceBounds.height;
    	    
    	    LineSegment newSegment = new LineSegment(startX, startY, theSelectedSegment.getStart().x, theSelectedSegment.getStart().y);
    	    this.insertSegment(0, newSegment);
    	    theSelectedIndex++;
    	    
    	} else if( ! segmentIsHorizontal && ! theSourceNode.isLongitudinal(mousePoint)) {	// insert new horizontal segment
    	    theSelectedSegment.setStart(theSelectedSegment.getStart().x, (sourceBounds.y + sourceBounds.y + sourceBounds.height) / 2);
    	    
    	    final int startX = (theSourceNode.isWesterly(mousePoint)) ? sourceBounds.x : sourceBounds.x + sourceBounds.width;
    	    final int startY = theSelectedSegment.getStart().y;
    	    
    	    LineSegment newSegment = new LineSegment(startX, startY, theSelectedSegment.getStart().x, theSelectedSegment.getStart().y);
    	    this.insertSegment(0, newSegment);
    	    theSelectedIndex++;
    	}
    }
    
    
    
    private void		processFinalSegment(boolean segmentIsHorizontal, Point mousePoint) {
    	final Rectangle destinationBounds = theDestinationNode.getBounds();
    	if(segmentIsHorizontal && ! theDestinationNode.isLatitudinal(mousePoint)){		// insert new vertical segment
    	    theSelectedSegment.setFinish((destinationBounds.x + destinationBounds.x + destinationBounds.width) / 2, theSelectedSegment.getFinish().y);
    	    
    	    final int finishX = theSelectedSegment.getFinish().x;
    	    final int finishY = (theDestinationNode.isNortherly(mousePoint)) ? destinationBounds.y : destinationBounds.y + destinationBounds.height;
    	    
    	    LineSegment newSegment = new LineSegment(theSelectedSegment.getFinish().x, theSelectedSegment.getFinish().y, finishX, finishY);
    	    this.addSegment(newSegment);
    	    
    	} else if( ! segmentIsHorizontal && ! theDestinationNode.isLongitudinal(mousePoint)) {	// insert new horizontal segment
    	    theSelectedSegment.setFinish(theSelectedSegment.getFinish().x, (destinationBounds.y + destinationBounds.y + destinationBounds.height) / 2);
    	    
    	    final int finishX = (theDestinationNode.isWesterly(mousePoint)) ? destinationBounds.x : destinationBounds.x + destinationBounds.width;
    	    final int finishY = theSelectedSegment.getFinish().y;
    	    
    	    LineSegment newSegment = new LineSegment(theSelectedSegment.getFinish().x, theSelectedSegment.getFinish().y, finishX, finishY);
    	    this.addSegment(newSegment);
    	}
    }
    
    
    
    private void		processSecondSegment() {
    	final Rectangle sourceBounds = theSourceNode.getBounds();
    	final LineSegment precedingSegment = (LineSegment)theLineSegments.get(theSelectedIndex - 1);
    	precedingSegment.setFinish(theSelectedSegment.getStart().x, theSelectedSegment.getStart().y);
    	
    	if(theSelectedIndex == 1) {
    	    if(sourceBounds.contains(precedingSegment.getFinish())) {
    	    	theSelectedIndex--;
    	    	this.removeSegment(0);
    	    	
    	    	final int bearing = theSourceNode.getCompassBearing(theSelectedSegment.getFinish());
    	    	switch(bearing) {
    	    	    case NodeElement.NORTH :
    	    	    	theSelectedSegment.setStart(theSelectedSegment.getStart().x, sourceBounds.y);
    	    	    	break;
    	    	    case NodeElement.SOUTH :
    	    	    	theSelectedSegment.setStart(theSelectedSegment.getStart().x, sourceBounds.y + sourceBounds.height);
    	    	    	break;
    	    	    case NodeElement.EAST :
    	    	    	theSelectedSegment.setStart(sourceBounds.x + sourceBounds.width, theSelectedSegment.getStart().y);
    	    	    	break;
    	    	    case NodeElement.WEST :
    	    	    	theSelectedSegment.setStart(sourceBounds.x, theSelectedSegment.getStart().y);
    	    	    	break;
    	    	}
    	    }
    	}
    }
    
    
    
    private void		processPenultimateSegment() {
    	final Rectangle destinationBounds = theDestinationNode.getBounds();
    	final LineSegment followingSegment = (LineSegment)theLineSegments.get(theSelectedIndex + 1);
    	followingSegment.setStart(theSelectedSegment.getFinish().x, theSelectedSegment.getFinish().y);
    	
    	if(theSelectedIndex == theLineSegments.size() - 2) {
    	    if(destinationBounds.contains(followingSegment.getStart())) {
    	    	this.removeSegment(theLineSegments.size() - 1);
    	    	
    	    	final int bearing = theDestinationNode.getCompassBearing(theSelectedSegment.getStart());
    	    	switch(bearing) {
    	    	    case NodeElement.NORTH :
    	    	    	theSelectedSegment.setFinish(theSelectedSegment.getFinish().x, destinationBounds.y);
    	    	    	break;
    	    	    case NodeElement.SOUTH :
    	    	    	theSelectedSegment.setFinish(theSelectedSegment.getFinish().x, destinationBounds.y + destinationBounds.height);
    	    	    	break;
    	    	    case NodeElement.EAST :
    	    	    	theSelectedSegment.setFinish(destinationBounds.x + destinationBounds.width, theSelectedSegment.getFinish().y);
    	    	    	break;
    	    	    case NodeElement.WEST :
    	    	    	theSelectedSegment.setFinish(destinationBounds.x, theSelectedSegment.getFinish().y);
    	    	    	break;
    	    	}
    	    }
    	}
    }
    
    
    
    
    
    
    private void		insertSegment(int index, LineSegment segment) {
    	theLineSegments.add(index, segment);
    }
    
    private void		addSegment(LineSegment segment) {
    	theLineSegments.add(segment);
    }
    
    private void		removeSegment(int index) {
    	theLineSegments.remove(index);
    }
    
    
    
    /**
     * The given node has moved and this link must be updated to reflect the
     *    new position of the node.
     */
    public void			update(NodeElement movedNode, int offsetX, int offsetY) {
    	if(theSourceNode == movedNode) {
    	   this.updateMovedSourceNode(movedNode);
    	   
    	   if(theLineSegments.size() == 1) {
    	       LineSegment segment = (LineSegment)theLineSegments.get(0);
    	       this.updateCrossOver(theDestinationNode, segment.getFinish(), segment.getStart());
    	   }
	}

    	if(theDestinationNode == movedNode) {
    	    this.updateMovedDestinationNode(movedNode);
    	   
    	   if(theLineSegments.size() == 1) {
    	   	LineSegment segment = (LineSegment)theLineSegments.get(0);
    	        this.updateCrossOver(theSourceNode, segment.getStart(), segment.getFinish());
    	   }
    	}
    }



    /**
     * The source node has moved. There are two situations to consider: the
     *    node has been positioned on an existing segment or it has moved
     *    away from the first segment.
     */
    private void		updateMovedSourceNode(NodeElement movedNode) {
    //
    //	Test to see if the node has relocated itself on to an existing segment.
    //	  If so, the lose all other segments nearest the node and clip
    //	  the intersected segment to the node boundary.
    //
    	final int size = theLineSegments.size();
    	for(int k = size - 1; k >= 0; k--) {
    	    final LineSegment segment = (LineSegment)theLineSegments.get(k);
    	    final Point start = segment.getStart();
    	    final Point finish = segment.getFinish();
    	    final Rectangle bounds = movedNode.getBounds();
    	    Rectangle segmentRectangle = null;
    	    if(start.x < finish.x || start.y < finish.y)
    	    	segmentRectangle = new Rectangle(start.x - 1, start.y - 1, finish.x + 2 - start.x, finish.y + 2 - start.y);
    	    else
    	    	segmentRectangle = new Rectangle(finish.x - 1, finish.y - 1, start.x + 2 - finish.x, start.y +  2 - finish.y);
    	    
    	    if(bounds.intersects(segmentRectangle)) {
    	    	for(int j = k - 1; j >= 0; j--)
    	    	    this.removeSegment(j);
    	    	    
    	    	final int bearing = movedNode.getCompassBearing(segment.getFinish());
    	    	switch(bearing) {
    	    	    case NodeElement.NORTH :
    	    	    	segment.setStart(segment.getFinish().x, bounds.y);
    	    	    	break;
    	    	    case NodeElement.SOUTH :
    	    	    	segment.setStart(segment.getFinish().x, bounds.y + bounds.height);
    	    	    	break;
    	    	    case NodeElement.EAST :
    	    	    	segment.setStart(bounds.x + bounds.width, segment.getFinish().y);
    	    	    	break;
    	    	    case NodeElement.WEST :
    	    	    	segment.setStart(bounds.x, segment.getFinish().y);
    	    	    	break;
    	    	}
    	    	return;
    	    }
    	}
    	
    //
    //	Otherwise, the source has moved away from the connecting segment S[0]. Either
    //		we extend the initial segment, or we both extend it and add another
    //		new initial segment.
    //
    	LineSegment additionalSegment = null;
    	LineSegment firstSegment = (LineSegment)theLineSegments.get(0);
    	
    	if(firstSegment.isHorizontal())
    	    additionalSegment = this.updateSourceHorizontalSegment(movedNode, firstSegment);
    	else
    	    additionalSegment = this.updateSourceVerticalSegment(movedNode, firstSegment);
    	    
    	if(additionalSegment != null)
    	    this.insertSegment(0, additionalSegment);
    }
    
    
    
    /**
     * The source node has moved away from the final segment. This final segment
     *    is horizontal.
     */
    private LineSegment		updateSourceHorizontalSegment(NodeElement movedNode, LineSegment firstSegment) {
    	final Rectangle bounds = movedNode.getBounds();
    	LineSegment additionalSegment = null;
    	
    	final int firstX = firstSegment.getStart().x;
    	final int firstY = firstSegment.getStart().y;
    	
    	final int bearing = movedNode.getCompassBearing(firstSegment.getStart());
    	switch(bearing) {
    	    case NodeElement.NORTHWEST :
    	    	firstSegment.setStart((bounds.x + bounds.x + bounds.width) / 2, firstY);
    	    	additionalSegment = new LineSegment(firstSegment.getStart().x, bounds.y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.NORTH :
    	    	additionalSegment = new LineSegment(firstX, bounds.y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.NORTHEAST :
    	    	firstSegment.setStart((bounds.x + bounds.x + bounds.width) / 2, firstY);
    	    	additionalSegment = new LineSegment(firstSegment.getStart().x, bounds.y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.WEST :
    	    	firstSegment.setStart(bounds.x, firstY);
    	    	break;
    	    case NodeElement.CENTRE :
    	    	break;
    	    case NodeElement.EAST :
    	    	firstSegment.setStart(bounds.x + bounds.width, firstY);
    	    	break;
    	    case NodeElement.SOUTHWEST :
    	    	firstSegment.setStart((bounds.x + bounds.x + bounds.width) / 2, firstY);
    	    	additionalSegment = new LineSegment(firstSegment.getStart().x, bounds.y + bounds.height, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.SOUTH :
    	    	additionalSegment = new LineSegment(firstX, bounds.y + bounds.height, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.SOUTHEAST :
    	    	firstSegment.setStart((bounds.x + bounds.x + bounds.width) / 2, firstY);
    	    	additionalSegment = new LineSegment(firstSegment.getStart().x, bounds.y + bounds.height, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	}
    	return additionalSegment;
    }
    
    
    
    /**
     * The source node has moved away from the final segment. This final segment
     *    is vertical.
     */
    private LineSegment		updateSourceVerticalSegment(NodeElement movedNode, LineSegment firstSegment) {
    	final Rectangle bounds = movedNode.getBounds();
    	LineSegment additionalSegment = null;
    	
    	final int firstX = firstSegment.getStart().x;
    	final int firstY = firstSegment.getStart().y;
    	
    	final int bearing = movedNode.getCompassBearing(firstSegment.getStart());
    	switch(bearing) {
    	    case NodeElement.NORTHWEST :
    	    	firstSegment.setStart(firstX, (bounds.y + bounds.y + bounds.height) / 2);
    	    	additionalSegment = new LineSegment(bounds.x, firstSegment.getStart().y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.NORTH :
    	    	firstSegment.setStart(firstX, bounds.y);
    	    	break;
    	    case NodeElement.NORTHEAST :
    	    	firstSegment.setStart(firstX, (bounds.y + bounds.y + bounds.height) / 2);
    	    	additionalSegment = new LineSegment(bounds.x + bounds.width, firstSegment.getStart().y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.WEST :
    	    	additionalSegment = new LineSegment(bounds.x, firstY, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.CENTRE :
    	    	break;
    	    case NodeElement.EAST :
    	    	additionalSegment = new LineSegment(bounds.x + bounds.width, firstY, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.SOUTHWEST :
    	    	firstSegment.setStart(firstX, (bounds.y + bounds.y + bounds.height) / 2);
    	    	additionalSegment = new LineSegment(bounds.x, firstSegment.getStart().y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	    case NodeElement.SOUTH :
    	    	firstSegment.setStart(firstX, bounds.y + bounds.height);
    	    	break;
    	    case NodeElement.SOUTHEAST :
    	    	firstSegment.setStart(firstX, (bounds.y + bounds.y + bounds.height) / 2);
    	    	additionalSegment = new LineSegment(bounds.x + bounds.width, firstSegment.getStart().y, firstSegment.getStart().x, firstSegment.getStart().y);
    	    	break;
    	}
    	return additionalSegment;
    }



    /**
     * The destination node has moved. There are two situations to consider: the
     *    node has been positioned on an existing segment or it has moved
     *    away from the final segment.
     */
    private void		updateMovedDestinationNode(NodeElement movedNode) {
    //
    //	Test to see if the node has relocated itself on to an existing segment.
    //	  If so, the lose all other segments nearest the node and clip
    //	  the intersected segment to the node boundary.
    //
    	final int size = theLineSegments.size();
    	for(int k = 0; k < size; k++) {
    	    final LineSegment segment = (LineSegment)theLineSegments.get(k);
    	    final Point start = segment.getStart();
    	    final Point finish = segment.getFinish();
    	    final Rectangle bounds = movedNode.getBounds();
    	    Rectangle segmentRectangle = null;
    	    if(start.x < finish.x || start.y < finish.y)
    	    	segmentRectangle = new Rectangle(start.x - 1, start.y - 1, finish.x + 2 - start.x, finish.y + 2 - start.y);
    	    else
    	    	segmentRectangle = new Rectangle(finish.x - 1, finish.y - 1, start.x + 2 - finish.x, start.y +  2 - finish.y);
    	    
    	    if(bounds.intersects(segmentRectangle)) {
    	    	for(int j = k + 1; j < size; j++)
    	    	    this.removeSegment(j);
    	    	    
    	    	final int bearing = movedNode.getCompassBearing(segment.getStart());
    	    	switch(bearing) {
    	    	    case NodeElement.NORTH :
    	    	    	segment.setFinish(segment.getStart().x, bounds.y);
    	    	    	break;
    	    	    case NodeElement.SOUTH :
    	    	    	segment.setFinish(segment.getStart().x, bounds.y + bounds.height);
    	    	    	break;
    	    	    case NodeElement.EAST :
    	    	    	segment.setFinish(bounds.x + bounds.width, segment.getStart().y);
    	    	    	break;
    	    	    case NodeElement.WEST :
    	    	    	segment.setFinish(bounds.x, segment.getStart().y);
    	    	    	break;
    	    	}
    	    	return;
    	    }
    	}
    	
    //
    //	Otherwise, the node has moved away from the connecting segment S[size-1]. Either
    //		we extend the final segment, or we both extend it and add another
    //		new final segment.
    //
    	LineSegment additionalSegment = null;
    	LineSegment finalSegment = (LineSegment)theLineSegments.get(theLineSegments.size() - 1);
    	
    	if(finalSegment.isHorizontal())
    	    additionalSegment = this.updateDestinationHorizontalSegment(movedNode, finalSegment);
    	else
    	    additionalSegment = this.updateDestinationVerticalSegment(movedNode, finalSegment);
    	    
    	if(additionalSegment != null)
    	    this.addSegment(additionalSegment);
    }
    
    
    
    /**
     * The destination node has moved away from the final segment. This final segment
     *    is horizontal.
     */
    private LineSegment		updateDestinationHorizontalSegment(NodeElement movedNode, LineSegment finalSegment) {
    	LineSegment additionalSegment = null;

    	final Rectangle	bounds	= movedNode.getBounds();
    	final int	left	= bounds.x;
    	final int	right	= bounds.x + bounds.width;
    	final int	top	= bounds.y;
    	final int	bottom	= bounds.y + bounds.height;
    	final int	midX	= (left + right) / 2;
    	final int	midY	= (top + bottom) / 2;
    	
    	final Point	finalFinish	= finalSegment.getFinish();
    	final int	finalX		= finalFinish.x;
    	final int	finalY		= finalFinish.y;
    	
    	final int bearing = movedNode.getCompassBearing(finalSegment.getFinish());
    	switch(bearing) {
    	    case NodeElement.NORTHWEST :
    	    	finalSegment.setFinish(midX, finalY);
    	    	additionalSegment = new LineSegment(midX, finalY, midX, top);
    	    	break;
    	    case NodeElement.NORTH :
    	    	additionalSegment = new LineSegment(finalX, finalY, finalX, top);
    	    	break;
    	    case NodeElement.NORTHEAST :
    	    	finalSegment.setFinish(midX, finalY);
    	    	additionalSegment = new LineSegment(midX, finalY, midX, top);
    	    	break;
    	    case NodeElement.WEST :
    	    	finalSegment.setFinish(left, finalY);
    	    	break;
    	    case NodeElement.CENTRE :
    	    	break;
    	    case NodeElement.EAST :
    	    	finalSegment.setFinish(right, finalY);
    	    	break;
    	    case NodeElement.SOUTHWEST :
    	    	finalSegment.setFinish(midX, finalY);
    	    	additionalSegment = new LineSegment(midX, finalY, midX, bottom);
    	    	break;
    	    case NodeElement.SOUTH :
    	    	additionalSegment = new LineSegment(finalX, finalY, finalX, bottom);
    	    	break;
    	    case NodeElement.SOUTHEAST :
    	    	finalSegment.setFinish(midX, finalY);
    	    	additionalSegment = new LineSegment(midX, finalY, midX, bottom);
    	    	break;
    	}
    	return additionalSegment;
    }
    
    
    
    /**
     * The destination node has moved away from the final segment. This final segment
     *    is vertical.
     */
    private LineSegment		updateDestinationVerticalSegment(NodeElement movedNode, LineSegment finalSegment) {
    	LineSegment additionalSegment = null;

    	final Rectangle	bounds	= movedNode.getBounds();
    	final int	left	= bounds.x;
    	final int	right	= bounds.x + bounds.width;
    	final int	top	= bounds.y;
    	final int	bottom	= bounds.y + bounds.height;
    	final int	midX	= (left + right) / 2;
    	final int	midY	= (top + bottom) / 2;
    	
    	final Point	finalFinish	= finalSegment.getFinish();
    	final int	finalX		= finalFinish.x;
    	final int	finalY		= finalFinish.y;
    	
    	final int bearing = movedNode.getCompassBearing(finalFinish);
    	switch(bearing) {
    	    case NodeElement.NORTHWEST :
    	    	finalSegment.setFinish(finalX, midY);
    	    	additionalSegment = new LineSegment(finalX, midY, left, midY);
    	    	break;
    	    case NodeElement.NORTH :
    	    	finalSegment.setFinish(finalX, top);
    	    	break;
    	    case NodeElement.NORTHEAST :
    	    	finalSegment.setFinish(finalX, midY);
    	    	additionalSegment = new LineSegment(right, midY, finalX, midY);
    	    	break;
    	    case NodeElement.WEST :
    	    	additionalSegment = new LineSegment(finalX, finalY, left, finalY);
    	    	break;
    	    case NodeElement.CENTRE :
    	    	break;
    	    case NodeElement.EAST :
    	    	additionalSegment = new LineSegment(right, finalY, finalX, finalY);
    	    	break;
    	    case NodeElement.SOUTHWEST :
    	    	finalSegment.setFinish(finalX, midY);
    	    	additionalSegment = new LineSegment(finalX, midY, left, midY);
    	    	break;
    	    case NodeElement.SOUTH :
    	    	finalSegment.setFinish(finalX, bottom);
    	    	break;
    	    case NodeElement.SOUTHEAST :
    	    	finalSegment.setFinish(finalX, midY);
    	    	additionalSegment = new LineSegment(right, midY, finalX, midY);
    	    	break;
    	}
    	return additionalSegment;
    }
    
    
    
    private void		updateCrossOver(NodeElement node, Point adjacent, Point remote) {
    	Rectangle bounds = node.getBounds();
    	int pointX = adjacent.x;
    	int pointY = adjacent.y;
    	int bearing = node.getCompassBearing(remote);
    	switch(bearing) {
    	    case NodeElement.NORTH :
    	    	pointY -= 5;
    	    	break;
    	    case NodeElement.WEST :
    	    	pointX -= 5;
    	    	break;
    	    case NodeElement.EAST :
    	    	pointX += 5;
    	    	break;
    	    case NodeElement.SOUTH :
    	    	pointY += 5;
    	    	break;
    	}
    	
    	if(bounds.contains(pointX, pointY)) {
    	    switch(bearing) {
    	    	case NodeElement.NORTH :
    	    	    adjacent.y = bounds.y;
    	    	    break;
    	    	case NodeElement.WEST :
    	    	    adjacent.x = bounds.x;
    	    	    break;
    	    	case NodeElement.EAST :
    	    	    adjacent.x = bounds.x + bounds.width;
    	    	    break;
    	    	case NodeElement.SOUTH :
    	    	    adjacent.y = bounds.y + bounds.height;
    	    	    break;
    	    }
    	}
    }
    
    
    
// ---------- properties ----------------------------------

    protected NodeElement	theSourceNode;
    protected NodeElement	theDestinationNode;
    
    protected ArrayList		theLineSegments		= new ArrayList(8);
    
    
    
    protected LineSegment	theSelectedSegment;
    protected int		theSelectedIndex;
    
}	// class: LinkageElement

// ============================================================================
