/**
 *  A selection tool is used to select and/or drag the selected model
 *	element. It is also used to present a context sensitive right
 *	mouse click.
 *
 *  @author	K Barclay
 */
 
package tool;


 
import java.awt.*;
import java.awt.event.*;

import diagram.*;
import window.*;



public final class SelectionTool	implements	RelationToolIF {

    		// CONTROLLER
    public void		doLeftMousePressed(MouseEvent event, ModelElement selectedElement) {
    	////RomeModelPanel modelPanel = RomeModelPanel.getRomeModelPanel();
    	CollieModelPanel modelPanel = (CollieModelPanel)event.getSource();
    	theGraphicsContext = modelPanel.getGraphics();
    	theGraphicsContext.setXORMode(Color.white);
    	selectedElement.doLeftMousePressed(event, theGraphicsContext);
    }
    
    public void		doLeftMouseReleased(MouseEvent event, ModelElement selectedElement) {
    	theGraphicsContext.setPaintMode();
    	selectedElement.doLeftMouseReleased(event, theGraphicsContext);
    }
    
    public void		doLeftMouseDragged(MouseEvent event, ModelElement selectedElement) {
    	selectedElement.doLeftMouseDragged(event, theGraphicsContext);
    }
    
    public void		doRightMousePressed(MouseEvent event, ModelElement selectedElement) {
    	//selectedElement.doRightMousePressed(event);
    }
    
    
    
    public LinkageElement	makeLinkage(NodeElement source, NodeElement destination, LineSegment first, LineSegment second) {
    	return null;
    }
    
    
    
// ---------- properties ----------------------------------
    
    private static Graphics 	theGraphicsContext;

}	// class:	SelectionTool

// ============================================================================
