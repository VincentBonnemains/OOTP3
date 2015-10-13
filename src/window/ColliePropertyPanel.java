/**
 *  The ColliePropertyPanel is a placeholder panel which will be used to
 *	display the properties of selected model elements.
 *
 *  This class implements the singleton design pattern.
 *
 *  @author	K Barclay
 */



package window;



import java.awt.*;
import javax.swing.*;



public final class ColliePropertyPanel 		extends JPanel {

    public static ColliePropertyPanel	getColliePropertyPanel()	{ return colliePropertyPanel; }
    
    
    
    protected 				ColliePropertyPanel() {
    	super();
    }
    
    
    
// ---------- properties ----------------------------------

    private static ColliePropertyPanel	colliePropertyPanel = new ColliePropertyPanel();
    
}	// class ColliePropertyPanel

// ==================================================================
