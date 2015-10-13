/**
 *  An association tool is a specialised relation tool for forming an
 *	association symbol. It may exist bewteen class symbols and any
 *	kind of instance symbol.
 *
 *  @author	K Barclay
 */
 
package tool;



import diagram.*;



public class AssociationTool	extends		RelationTool {

    public LinkageElement	makeLinkage(NodeElement source, NodeElement destination, LineSegment first, LineSegment second) {
    	return new AssociationSymbol(source, destination, first, second);
    }

    
}	// class:	AssociationTool

// ============================================================================
