/**
 *  An association symbol represents a relationship that is formed
 *	between instances.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;
import java.awt.event.*;
import java.util.*;



public class AssociationSymbol extends LinkageElement {

    public	AssociationSymbol(NodeElement source, NodeElement destination, LineSegment first, LineSegment second) {
    	super(source, destination, first, second);
    }

}	// class:	AssociationSymbol

// ============================================================================

