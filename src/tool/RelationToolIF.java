/**
 *  The RelationTool interface represents the protocol we associate with
 *	any type of relation tool, eg selection tool or association tool.
 *	The protocol comprises pseudo-event handlers for mouse events.
 *
 *  @author	K Barclay
 */
 
package tool;
 
 

import java.awt.event.*;

import diagram.*;



public interface RelationToolIF {

    /**
     * Pseudo-event handlers for processing relations.
     */
    public abstract void		doLeftMousePressed(MouseEvent event, ModelElement selectedElement);
    public abstract void		doLeftMouseReleased(MouseEvent event, ModelElement selectedElement);
    public abstract void		doLeftMouseDragged(MouseEvent event, ModelElement selectedElement);
    public abstract void		doRightMousePressed(MouseEvent event, ModelElement selectedElement);
    
    public abstract LinkageElement	makeLinkage(NodeElement source, NodeElement destination, LineSegment first, LineSegment second);

}	// interface:	RelationToolIF

// ============================================================================
