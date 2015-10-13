/**
 *  The CollieProjectPanel is a placeholder panel which we will populate
 *	with the project tree.
 *
 *  This class implements the singleton design pattern.
 *
 *  @author	K Barclay
 */



package window;



import java.awt.*;
import javax.swing.*;



public final class CollieProjectPanel 		extends JPanel {

    public static CollieProjectPanel	getCollieProjectPanel()		{ return collieProjectPanel; }
    
    
    
    protected 				CollieProjectPanel() {
    	super();
    }
    
    
    
// ---------- properties ----------------------------------

    private static CollieProjectPanel	collieProjectPanel = new CollieProjectPanel();
    
}	// class CollieProjectPanel

// ==================================================================
