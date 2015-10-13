/**
 *  The ColliePanel represents the main application panel. This panel fully
 *	populates the main application frame. It comprises three split panes.
 *	On the left is the project pane sharing with the diagram pane on the
 *	right. Together, these two share with the properties pane which occupies
 *	the full width of the lower region.
 *
 *  This class implements the singleton design pattern.
 *
 *  @author	K Barclay
 */



package window;



import java.awt.*;
import javax.swing.*;



public final class ColliePanel 		extends JPanel {

    public static ColliePanel		getColliePanel()	{ return colliePanel; }
    
    
    
    protected 				ColliePanel() {
    	super(new BorderLayout());
    	
    	CollieProjectPanel projectPanel = CollieProjectPanel.getCollieProjectPanel();
    	CollieDiagramPanel diagramPanel = CollieDiagramPanel.getCollieDiagramPanel();
    	ColliePropertyPanel propertyPanel = ColliePropertyPanel.getColliePropertyPanel();
    	projectDiagramSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectPanel, diagramPanel);
    	projectDiagramPropertySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectDiagramSplitPane, propertyPanel);
    	
    	this.add(projectDiagramPropertySplitPane, BorderLayout.CENTER);
    }
    
    
    
    public void		    	setDivider() {
    	projectDiagramSplitPane.setDividerLocation(0.2);
    	projectDiagramPropertySplitPane.setDividerLocation(0.8);
    }
    
    
    
// ---------- properties ----------------------------------

    private static ColliePanel	colliePanel = new ColliePanel();
    
    private JSplitPane		projectDiagramSplitPane;
    private JSplitPane		projectDiagramPropertySplitPane;
    
}	// class ColliePanel

// ==================================================================
