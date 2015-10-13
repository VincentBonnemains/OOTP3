/**
 *  The CollieModelPanel is a placeholder for the modelling window in which
 *	the user will construct collaboration diagrams.
 *
 *  This class implements the singleton design pattern.
 *
 *  Establis the application frame as a change listener. When the model is
 *	modified by the user, then advise the frame.
 *
 *  @author	K Barclay
 */



package window;



import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

import collie.Collie;
import diagram.*;
import tool.*;



public final class CollieModelPanel 		extends JPanel implements PropertyChangeListener {

    public static CollieModelPanel	getCollieModelPanel()	{ return collieModelPanel; }
    
    
    
//
//  Source:	NodeElementAB
//  Event:	MODEL_DIRTIED_CHANGED_PROPERTY
//
//  If the model has been dirtied, then we redraw it.
//
    public void				propertyChange(PropertyChangeEvent event) {
    	if(event.getPropertyName().equals(CollieModelPanel.MODEL_DIRTIED_CHANGED_PROPERTY))
    	    this.repaint();
    }
    
    
    
    public AbstractAction[]	getActions()		{ return theActions; }
    
    
    
    protected 			CollieModelPanel() {
    	super();
    	
    	diagram = new CollaborationDiagram();
    	
    	theOffset = 0;
    	
    	this.setOpaque(true);
    	this.setBackground(Color.white);
    	
    	this.addMouseListener(new ModelPanelMouseListener());
    	this.addMouseMotionListener(new ModelPanelMouseMotionListener());
    	this.addPropertyChangeListener(MODEL_DIRTIED_CHANGED_PROPERTY, CollieFrame.getCollieFrame());
    	this.addPropertyChangeListener(MODEL_DIRTIED_CHANGED_PROPERTY, this);
    }
    
    
    
    public void			addModelElement(ModelElement element) {
    	diagram.add(element);
    }
    
    
    
    //
    //  Cycle through each element in the model and have them redraw themselves.
    //    Observe how the final entry is drawn uppermost.
    //
    public void		paintComponent(Graphics graphicsContext) {
    	super.paintComponent(graphicsContext);
    	
    	diagram.draw(graphicsContext);
    }
    
    
    
    public void		add(ModelElement element) {
    	if(element != null)
    	    diagram.add(element);
    }
    
    
    
    public ModelElement		hitTest(int mouseX, int mouseY) {
    	return diagram.hitTest(mouseX, mouseY);
    }
    
    public ModelElement		hitTest(Point mousePoint) {
    	return this.hitTest(mousePoint.x, mousePoint.y);
    }
    
    
    
    public void			deselectAll() {
    	diagram.deselectAll();
    }
    
    
    
// ---------- statics -------------------------------------

    public static final String		MODEL_DIRTIED_CHANGED_PROPERTY	= "Model dirtied";
    
    
    
    private static final String		ADD_INSTANCE		= "Add instance";
    private static final String		ADD_MULTI_INSTANCE	= "Add multi-instance";
    private static final String		ADD_ACTOR		= "Add actor";
    private static final String		ADD_NOTE		= "Add note";
    
    private static final String		SELECTION_TOOL		= "Selection tool";
    private static final String		ASSOCIATION_TOOL	= "Association tool";
    private static final String		AGGREGATION_TOOL	= "Aggregation tool";
    
    
    
// ---------- properties ----------------------------------

    private static CollieModelPanel	collieModelPanel 	= new CollieModelPanel();
        
    //
    //
    //
    private InstanceNodeFactory		theInstanceNodeFactory		= new InstanceNodeFactory();
    private ActorNodeFactory		theActorNodeFactory		= new ActorNodeFactory();
    private MultiInstanceNodeFactory	theMultiInstanceNodeFactory	= new MultiInstanceNodeFactory();
    
    //
    //  Toolbar button action objects
    //
    private SelectionTool		theSelectionTool		= new SelectionTool();
    private AssociationTool		theAssociationTool		= new AssociationTool();
    private AggregationTool		theAggregationTool		= new AggregationTool();
    
    
    private AbstractAction[]	theActions = {
    	new AddInstanceAction(ADD_INSTANCE,		new ImageIcon(Collie.class.getResource("/images/instanceButton_large.gif")),	theInstanceNodeFactory),
    	new AddInstanceAction(ADD_MULTI_INSTANCE,	new ImageIcon(Collie.class.getResource("/images/multiButton_large.gif")),	theMultiInstanceNodeFactory),
    	new AddInstanceAction(ADD_ACTOR,		new ImageIcon(Collie.class.getResource("/images/actorButton_large.gif")),	theActorNodeFactory),
    	null,
    	new AddNoteAction(ADD_NOTE,			new ImageIcon(Collie.class.getResource("/images/noteButton_large.gif"))),
    	null,
    	new RelationToolAction(SELECTION_TOOL,		new ImageIcon(Collie.class.getResource("/images/selectionButton_large.gif")),	theSelectionTool,	Cursor.DEFAULT_CURSOR),
    	new RelationToolAction(ASSOCIATION_TOOL,	new ImageIcon(Collie.class.getResource("/images/associationButton_large.gif")),	theAssociationTool,	Cursor.CROSSHAIR_CURSOR),
    	new RelationToolAction(AGGREGATION_TOOL,	new ImageIcon(Collie.class.getResource("/images/aggregationButton_large.gif")),	theAggregationTool,	Cursor.CROSSHAIR_CURSOR)
    };
    
    //
    //  Curent tool.
    //
    RelationToolIF			theRelationTool			= theSelectionTool;
    
   
    
    //
    //  Collection of model elements that form the diagram
    //
    private CollaborationDiagram		diagram;
    
    //
    //  When dragging occurs.
    //
    private ModelElement			selectedElement;
    private Graphics				graphicsContext;
    
    private int					theXLocation;
    private int					theYLocation;
    
    private int					theOffset;
    
    
    
// ---------- inner class AddInstanceAction ---------------

    public class AddInstanceAction		extends AbstractAction {
    
    	public		AddInstanceAction(String label, Icon icon, NodeFactory factory) {
    	    super(label, icon);
    	    
    	    theFactory = factory;
    	}
    	
    	
    	
    	public void	actionPerformed(ActionEvent event) {
    	    CollieModelPanel.this.diagram.deselectAll();
    	    
    	    int x;
    	    int y;
    	    
    	    if(event.getActionCommand().equals(ADD_INSTANCE) ||
    	       event.getActionCommand().equals(ADD_ACTOR)) {
    	    	x = CollieModelPanel.this.theXLocation;
    	    	y = CollieModelPanel.this.theYLocation;
    	    } else {
    	    	CollieModelPanel.this.theOffset++;
    	    	x = 10 * CollieModelPanel.this.theOffset;
    	    	y = 10 * CollieModelPanel.this.theOffset;
    	    	if(CollieModelPanel.this.theOffset == 5)
    	    	    CollieModelPanel.this.theOffset = 0;
    	    }
    	    
    	    NodeElement nodeElement = theFactory.makeNode(x, y);
    	    nodeElement.setSelected(true);
            //InstanceSymbol instance = new InstanceSymbol(x, y);
            //instance.setSelected(true);
            CollieModelPanel.this.addModelElement(nodeElement);
            
            CollieModelPanel.this.repaint();
    	}
    	
    // ---------- properties ------------------------------
    
        private NodeFactory		theFactory;
    	
    }	// inner class: AddInstanceAction
    
    
    
// ---------- inner class AddNoteAction -------------------

    public class AddNoteAction		extends AbstractAction {
    
    	public		AddNoteAction(String label, Icon icon) {
    	    super(label, icon);
    	}
    	
    	
    	
    	public void	actionPerformed(ActionEvent event) {
    	    System.out.println("AddNoteAction.actionPerformed: " + event.getActionCommand());
    	}
    	
    }	// inner class: AddNoteAction
    
    
    
// ---------- inner class RelationToolAction -------------------

    public class RelationToolAction		extends AbstractAction {
    
    	public		RelationToolAction(String label, Icon icon, RelationToolIF tool, int cursor) {
    	    super(label, icon);
	    
	    theTool = tool;
	    theCursor = cursor;
    	}
    	
    	
    	
    	public void	actionPerformed(ActionEvent event) {
    	    CollieModelPanel.this.theRelationTool = theTool;
    	    CollieModelPanel.this.setCursor(Cursor.getPredefinedCursor(theCursor));
    	}
    	
    	
    	
    	// ---------- properties --------------------------
    	
    	private RelationToolIF		theTool;
    	private int			theCursor;
    	
    }	// inner class: RelationToolAction
    
    
    
// ---------- inner class ModelPanelMouseListener ---------

    public class ModelPanelMouseListener	extends MouseAdapter {
    
    	//
    	//  Intercept the right mouse button press and render a new
    	//    instance symbol with its upper left at the mouse position.
    	//    Additionally, add this new symbol to the collection of
    	//    model elements maintained by the panel.
    	//
    	//  A left mouse button press signals the possible start of dragging
    	//    a model element across the panel.
    	//
        public void		mousePressed(MouseEvent event) {
            CollieModelPanel.this.theXLocation = event.getX();
            CollieModelPanel.this.theYLocation = event.getY();
            
            CollieModelPanel.this.diagram.deselectAll();
            CollieModelPanel.this.selectedElement = CollieModelPanel.this.diagram.hitTest(event.getPoint());
            if(CollieModelPanel.this.selectedElement != null) {
            	CollieModelPanel.this.diagram.relocateElement(CollieModelPanel.this.selectedElement);
            	CollieModelPanel.this.selectedElement.setSelected(true);
            }
            CollieModelPanel.this.graphicsContext = CollieModelPanel.this.getGraphics();
            if(SwingUtilities.isRightMouseButton(event)) {
            	if(CollieModelPanel.this.selectedElement != null)
            	    CollieModelPanel.this.selectedElement.doRightMousePressed(event, CollieModelPanel.this.graphicsContext);
            	else {
            	    //panelPopup.show(CollieModelPanel.this, event.getX(), event.getY());
            	}
            } else /**if(SwingUtilities.isLeftMouseButton(event))**/ {
            	if(CollieModelPanel.this.selectedElement != null) {
            	    ////CollieModelPanel.this.graphicsContext.setXORMode(Color.white);
            	    CollieModelPanel.this.theRelationTool.doLeftMousePressed(event, selectedElement);
            	}
            }
            
            CollieModelPanel.this.repaint();
        }
        
        
        
        //
        //  If the mouse release is from the left button, then release the element
        //	that was dragged.
        //
        public void		mouseReleased(MouseEvent event) {
            if(CollieModelPanel.this.selectedElement != null && SwingUtilities.isLeftMouseButton(event)) {
            	//CollieModelPanel.this.graphicsContext.setPaintMode();
            	CollieModelPanel.this.theRelationTool.doLeftMouseReleased(event, selectedElement);
            }
            	
            CollieModelPanel.this.repaint();
        }
        
    }	// inner class:	ModelPanelMouseListener
    
    
    
// ---------- inner class ModelPanelMouseMotionListener ---

    public class ModelPanelMouseMotionListener	extends MouseMotionAdapter {
    
        //
        //  If the left mouse button is used for dragging, then drag the selected
        //	model element, if we have one.
        //
    	public void				mouseDragged(MouseEvent event) {
            if(CollieModelPanel.this.selectedElement != null && SwingUtilities.isLeftMouseButton(event))
            	CollieModelPanel.this.theRelationTool.doLeftMouseDragged(event, selectedElement);
    	}
    	
    }	// inner class: ModelPanelMouseMotionListener
    
        
}	// class: CollieModelPanel



interface	NodeFactory {

    public abstract NodeElement		makeNode(int x, int y);
    
}



class		InstanceNodeFactory	implements NodeFactory {

    public NodeElement		makeNode(int x, int y) {
    	return new InstanceSymbol(x, y);
    }
    
}



class		ActorNodeFactory	implements NodeFactory {

    public NodeElement		makeNode(int x, int y) {
    	return new ActorSymbol(x, y);
    }
    
}



class		MultiInstanceNodeFactory	implements NodeFactory {

    public NodeElement		makeNode(int x, int y) {
    	return new MultiInstanceSymbol(x, y);
    }
    
}

// ==================================================================
