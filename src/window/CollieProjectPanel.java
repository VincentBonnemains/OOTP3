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
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import diagram.NodeElement;



public final class CollieProjectPanel 		extends JPanel {
	JTree arbre;
	String name = "project";

    public static CollieProjectPanel	getCollieProjectPanel()		{ return collieProjectPanel; }
    
    
    
    protected 				CollieProjectPanel() {
    	super();
    	JScrollPane pane;
    	arbre = new JTree();
    	arbre.setShowsRootHandles(false);
    	DefaultMutableTreeNode racine = new DefaultMutableTreeNode(name);
    	arbre.setModel(new DefaultTreeModel(racine));
    	pane =  new JScrollPane(arbre);
    	this.setLayout(new BorderLayout());
    	this.add(pane,BorderLayout.CENTER);
    }
    
    public void build(){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	ArrayList elements = CollieModelPanel.getCollieModelPanel().getDiagram().getElements();
    	DefaultMutableTreeNode node,leaf;
    	NodeElement current;
    	ArrayList attributes;
    	
    	for(int i = 0;i < elements.size();++i){
    		current = (NodeElement) elements.get(i);
    		node = new DefaultMutableTreeNode(current.getName()+":"+current.getClassifier());
    		attributes = current.getAttributes();
    		for(int j = 0;j < attributes.size();++j){
    			leaf = new DefaultMutableTreeNode(attributes.get(j));
    			node.add(leaf);
    		}
    		racine.add(node);
    	}
    	((DefaultTreeModel) arbre.getModel()).reload();
    }
    
    public void rebuid(){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	racine.removeAllChildren();
    	build();
    }
    
    
    
// ---------- properties ----------------------------------

    private static CollieProjectPanel	collieProjectPanel = new CollieProjectPanel();
    
}	// class CollieProjectPanel

// ==================================================================
