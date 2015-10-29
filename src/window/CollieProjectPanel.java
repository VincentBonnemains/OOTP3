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
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import diagram.NodeElement;



public final class CollieProjectPanel 		extends JPanel {
	private static JTree arbre;

    public static CollieProjectPanel	getCollieProjectPanel()		{ return collieProjectPanel; }
    
    
    
    protected 				CollieProjectPanel() {
    	super();
    	JScrollPane pane;
    	//Affichage de l'arbre vide
    	arbre = new JTree();
    	arbre.setShowsRootHandles(true);
    	DefaultMutableTreeNode racine = new DefaultMutableTreeNode("project", true);
    	arbre.setModel(new DefaultTreeModel(racine));
    	pane =  new JScrollPane(arbre);
    	this.setLayout(new BorderLayout());
    	this.add(pane,BorderLayout.CENTER);
    }
    
    //Fonction qui renomme la racine de l'arbre en fonction du fichier selectionné
    public static void rename(File f){
    		DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    		//Recuperation du nom du fichier
    		if(f != null)
    			racine.setUserObject(f.getName().substring(0, (f.getName().length())-4));
    		else
        		racine.setUserObject("Project");
    		//Maj affichage
    		((DefaultTreeModel) arbre.getModel()).nodeChanged(racine);
    }
    
    
    //Fonction de construction de l'arbre en fonction du diagram de collaboration
    public static void build(File f){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	
    	//Recuperation du nom du fichier pour nommer racine
    	if(f != null)
    		racine.setUserObject(f.getName().substring(0, (f.getName().length())-4));
    	else
    		racine.setUserObject("Project");
    	
    	ArrayList elements = CollieModelPanel.getCollieModelPanel().getDiagram().getElements();
    	DefaultMutableTreeNode node,leaf;
    	NodeElement current;
    	ArrayList attributes;
    	
    	//Parcours du CollaborationDiagram et construction de l'arbre
    	for(int i = 0;i < elements.size();++i){
    		if(elements.get(i) instanceof NodeElement){
	    		current = (NodeElement) elements.get(i);
	    		node = new DefaultMutableTreeNode(current.getName()+":"+current.getClassifier(),true);
	    		attributes = current.getAttributes();
	    		for(int j = 0;j < attributes.size();++j){
	    			leaf = new DefaultMutableTreeNode(attributes.get(j),false);
	    			node.add(leaf);
	    		}
	    		racine.add(node);
	    	}
    	}
    	//Maj de l'affichage
    	((DefaultTreeModel) arbre.getModel()).reload();
    }
    
    //Fonction de construction sans fichier
    public static void build(){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	
    	ArrayList elements = CollieModelPanel.getCollieModelPanel().getDiagram().getElements();
    	DefaultMutableTreeNode node,leaf;
    	NodeElement current;
    	ArrayList attributes;
    	
    	for(int i = 0;i < elements.size();++i){
    		if(elements.get(i) instanceof NodeElement){
	    		current = (NodeElement) elements.get(i);
	    		node = new DefaultMutableTreeNode(current.getName()+":"+current.getClassifier(),true);
	    		attributes = current.getAttributes();
	    		for(int j = 0;j < attributes.size();++j){
	    			leaf = new DefaultMutableTreeNode(attributes.get(j),false);
	    			node.add(leaf);
	    		}
	    		racine.add(node);
	    	}
    	}
    	((DefaultTreeModel) arbre.getModel()).reload();
    }
    
    //Fonction de reconstruction de l'arbre en cas de changement (comme par example ouverture d'un autre fichier)
    public static void rebuild(File f){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	racine.removeAllChildren();
    	build(f);
    }
    
    //Fonction de reconstruction de l'arbre sans nom de fichier
    public static void rebuild(){
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	racine.removeAllChildren();
    	build();
    }
    
    
    //Retourne l'index correspondant au noeud de l'arbre ayant le nom "name", recherche effectuée à partir du parent "node"
    private static int getIndexNode(DefaultMutableTreeNode node,String name){
    	DefaultMutableTreeNode child;
    	for(int i =0;i < node.getChildCount();++i){
    		child = (DefaultMutableTreeNode) node.getChildAt(i);
    		if(child.getUserObject().equals(name))
    			return i;
    	}
    	return 0;
    }
    
    //Fonction de reconstruction d'une branche, ne modifiant que la branche attributs du noeud correspondant à "node"
    public static void rebuildAttributes(NodeElement node){
    	//Recuperation du noeud correspondant à node
    	DefaultMutableTreeNode racine = (DefaultMutableTreeNode) arbre.getModel().getRoot();
    	int index = getIndexNode(racine,node.getName()+":"+node.getClassifier());
    	DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) arbre.getModel().getChild(racine, index),leaf;
    	
    	//Effacement de la branche avec données anciennes
    	treeNode.removeAllChildren();
    	ArrayList attributes = node.getAttributes();
    	//Parcours des attributs
    	for(int j = 0;j < attributes.size();++j){
			leaf = new DefaultMutableTreeNode(attributes.get(j),false);
			treeNode.add(leaf);
		}
    	//Maj de l'affichage
    	((DefaultTreeModel) arbre.getModel()).reload(treeNode);
    }
    
    
    
// ---------- properties ----------------------------------

    private static CollieProjectPanel	collieProjectPanel = new CollieProjectPanel();
    
}	// class CollieProjectPanel

// ==================================================================
