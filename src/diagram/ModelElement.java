/**
 *  A model element represents any kind of symbol that appears in a
 *	collaboration diagram. A concrete example of this is an
 *	instance symbol.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;
import java.awt.event.*;



public abstract class ModelElement {

    public boolean		isSelected()			{ return selectedFlag; }
    public void			setSelected(boolean selected)	{ selectedFlag = selected; }
    
    public abstract boolean	isHit(int mouseX, int mouseY);
    public boolean		isHit(Point mousePoint)		{ return this.isHit(mousePoint.x, mousePoint.y); }

    public abstract void	draw(Graphics graphicsContext);
    
    public abstract void	doRightMousePressed(MouseEvent event, Graphics graphicsContext);
    public abstract void	doLeftMousePressed(MouseEvent event, Graphics graphicsContext);
    public abstract void	doLeftMouseReleased(MouseEvent event, Graphics graphicsContext);
    public abstract void	doLeftMouseDragged(MouseEvent event, Graphics graphicsContext);
    
    
    
// ---------- properties ----------------------------------

    private boolean		selectedFlag;
    
}	// class: ModelElement

// ============================================================================
