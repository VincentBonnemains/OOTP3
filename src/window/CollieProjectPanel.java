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
	private static JTree arbre;
	String name = "project";

    public static CollieProjectPanel	getCollieProjectPanel()		{ return collieProjectPanel; }
    
    
    
    protected 				CollieProjectPanel() {
    	super();
    	JScrollPane pane;
    	arbre = new JTree();
    	arbre.setShowsRootHandles(true);
    	DefaultMutableTreeNode racine = new DefaultMutableTreeNode(name, true);
    	arbre.setModel(new DefaultTreeModel(racine));
    	pane =  new JScrollPane(arbre);
    	this.setLayout(new BorderLayout());
    	this.add(pane,BorderLayout.CENTER);
    }
    
    public static void build(){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	ArrayList elements = CollieModelPanel.getCollieModelPanel().getDiagram().getElements();
    	DefaultMutableTreeNode node,leaf;
    	NodeElement current;
    	ArrayList attributes;
    	
    	for(int i = 0;i < elements.size();++i){
    		current = (NodeElement) elements.get(i);
    		node = new DefaultMutableTreeNode(current.getName()+":"+current.getClassifier(),true);
    		attributes = current.getAttributes();
    		for(int j = 0;j < attributes.size();++j){
    			leaf = new DefaultMutableTreeNode(attributes.get(j),false);
    			node.add(leaf);
    		}
    		racine.add(node);
    	}
    	((DefaultTreeModel) arbre.getModel()).reload();
    }
    
    public static void rebuild(){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	racine.removeAllChildren();
    	build();
    }
    
    private static int getIndexNode(DefaultMutableTreeNode node,String name){
    	DefaultMutableTreeNode child;
    	for(int i =0;i < node.getChildCount();++i){
    		child = (DefaultMutableTreeNode) node.getChildAt(i);
    		if(child.getUserObject().equals(name))
    			return i;
    	}
    	return 0;
    }
    
    public static void rebuildAttributes(NodeElement node){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	int index = getIndexNode(racine,node.getName());
    	DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) arbre.getModel().getChild(racine, index),leaf;
    	treeNode.removeAllChildren();
    	ArrayList attributes = node.getAttributes();
    	for(int j = 0;j < attributes.size();++j){
			leaf = new DefaultMutableTreeNode(attributes.get(j),false);
			treeNode.add(leaf);
		}
    	((DefaultTreeModel) arbre.getModel()).reload(treeNode);
    }
    
    
    
// ---------- properties ----------------------------------

    private static CollieProjectPanel	collieProjectPanel = new CollieProjectPanel();
    
}	// class CollieProjectPanel

// ==================================================================
