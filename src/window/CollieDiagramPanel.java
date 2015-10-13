/**
 *  The CollieDiagramPanel is a placeholder panel which we will populate
 *	with the modelling window.
 *
 *  This class implements the singleton design pattern.
 *
 *  @author	K Barclay
 */



package window;



import java.awt.*;
import javax.swing.*;



public final class CollieDiagramPanel 		extends JPanel {

    public static CollieDiagramPanel	getCollieDiagramPanel()		{ return collieDiagramPanel; }
    
    
    
    protected 				CollieDiagramPanel() {
    	super(new BorderLayout());
    	
    	this.assembleToolBar();
    	
    	this.add(toolBar, 		BorderLayout.NORTH);
    	this.add(collieModelPanel,	BorderLayout.CENTER);
    }
    
    
    
    private void			assembleToolBar() {
    	AbstractAction[] actions = collieModelPanel.getActions();
    	for(int k = 0; k < actions.length; k++) {
    	    AbstractAction action = actions[k];
    	    if(action == null)
    	    	toolBar.addSeparator();
    	    else {
    	    	JButton button = toolBar.add(action);
    	    	button.setActionCommand("Toolbar:" + (String)action.getValue(Action.NAME));
    	    	button.setToolTipText((String)action.getValue(Action.NAME));
    	    }
    	}
    }
    
    
    
// ---------- properties ----------------------------------

    private static CollieDiagramPanel	collieDiagramPanel 	= new CollieDiagramPanel();
    private CollieModelPanel	collieModelPanel		= CollieModelPanel.getCollieModelPanel();
    
    private JToolBar			toolBar		= new JToolBar();
    
}	// class CollieDiagramPanel

// ==================================================================
