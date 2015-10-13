/**
 *  An aggregation tool is a specialised relation tool for forming an
 *	association symbol. It may exist bewteen class symbols and any
 *	kind of instance symbol.
 *
 *  @author	K Barclay
 */
 
package tool;



import diagram.*;



public class AggregationTool	extends		RelationTool {

    public LinkageElement	makeLinkage(NodeElement source, NodeElement destination, LineSegment first, LineSegment second) {
        return new AggregationSymbol(source, destination, first, second);
    }
    
}	// class:	AggregationTool

// ============================================================================
