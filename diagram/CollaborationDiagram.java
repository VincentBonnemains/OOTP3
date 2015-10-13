/**
 *  A collaboration diagram is a composite aggregate of any number of
 *	model elements.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;
import java.util.*;



public class CollaborationDiagram {

    public void		add(ModelElement element) {
    	if(element != null)
    	    modelElements.add(element);
    }
    
    
    
    public ModelElement		hitTest(int mouseX, int mouseY) {
    	final int size = modelElements.size();
    	for(int k = size - 1; k >= 0; k--) {
    	    ModelElement element = (ModelElement)modelElements.get(k);
    	    if(element.isHit(mouseX, mouseY))
    	    	return element;
    	}
    	return null;
    }
    
    public ModelElement		hitTest(Point mousePoint) {
    	return hitTest(mousePoint.x, mousePoint.y);
    }
    
    
    
    public void		draw(Graphics graphicsContext) {
    	final int size = modelElements.size();
    	for(int k = 0; k < size; k++) {
    	    ModelElement element = (ModelElement)modelElements.get(k);
    	    element.draw(graphicsContext);
    	}
    }
    
    
    
    public void			relocateElement(ModelElement element) {
    	if(element != null) {
    	    modelElements.remove(element);
    	    modelElements.add(element);
    	}
    }



    public void		deselectAll() {
    	final int size = modelElements.size();
    	for(int k = 0; k < size; k++) {
    	    ModelElement element = (ModelElement)modelElements.get(k);
    	    element.setSelected(false);
    	}
    }
    
    
    
// ---------- properties ----------------------------------

    private ArrayList		modelElements	= new ArrayList(8);
    
}	// class: CollaborationDiagram

// ============================================================================
