/**
 *  The ColliePropertyPanel is a placeholder panel which will be used to
 *	display the properties of selected model elements.
 *
 *  This class implements the singleton design pattern.
 *
 *  @author	K Barclay
 */



package window;



import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import diagram.NodeElement;



public final class ColliePropertyPanel extends JPanel {

    public static ColliePropertyPanel	getColliePropertyPanel()	{ return colliePropertyPanel; }
    //Noeud dont on affiche les données
    private NodeElement node_Selected;
    //tableau des attributs de l'objet
    private JTable tableau_droite;
    //tableau ref de l'objet
    private JTable tableau_gauche;
    
    protected 				ColliePropertyPanel() {
    	super();
    	this.setLayout(new GridLayout(1,2));
        tableau_droite = new JTable(new modelTableAttributs());     
        tableau_gauche = new JTable(new modeleRef());
        JScrollPane pleft = new JScrollPane(tableau_gauche);
    	JScrollPane pright = new JScrollPane(tableau_droite);
    	
    	this.add(pleft);
    	this.add(pright);
    	
    }
    
    //Appelé en cas de selection d'un autre noeud
    public void setNode(NodeElement e){
    	node_Selected = e;
    	((AbstractTableModel) tableau_droite.getModel()).fireTableDataChanged();
    	((AbstractTableModel) tableau_gauche.getModel()).fireTableDataChanged();
    }
    
    public JTable getTableau_droite(){
    	return tableau_droite;
    }
    
    public JTable getTableau_gauche(){
    	return tableau_gauche;
    }
    
    
// ---------- properties ----------------------------------

    private static ColliePropertyPanel	colliePropertyPanel = new ColliePropertyPanel();
    
// ---------- special class -------------------------------
    //Model du tableau des ref de l'objet
    private class modeleRef extends AbstractTableModel {
    	private final String[] entetes = {"Reference", "Classe"};
		@Override
		
		public boolean isCellEditable(int row,int column){
			if(node_Selected != null)
				return true;
			else
				return false;
		}
		
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return 1;
		}
		
		public String getColumnName(int columnIndex) {
            return entetes[columnIndex];
        }

		public Object getValueAt(int rowIndex, int columnIndex) {        	
        	if(node_Selected != null){
        		if(columnIndex == 0)
        			return node_Selected.getName();
        		else
        			return node_Selected.getClassifier();
        	}
        	return "";
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        	if(columnIndex == 0)
        		node_Selected.setName((String)value);
        	else
        		node_Selected.setClassifier((String) value);
        	//Maj de l'affichage
        	fireTableDataChanged();
        	CollieModelPanel.getCollieModelPanel().repaint();
        	CollieProjectPanel.rebuild();
        }
    
    }
    
    //Model du tableau des attributs
    private class modelTableAttributs extends AbstractTableModel {
     
        private final String[] entetes = {"Attributes", "Value"};
     
        public modelTableAttributs() {
            super();     
        }
     
        /*On ne peut éditer que dans les cas suivant:
         * l'attribut a un nom (pour éditer une valeur)
         * On choisit la prochaine case des noms vide pour un nouvel attribut
         */
        public boolean isCellEditable(int row, int column) {
        	//Si pas de noeud selectionné ou si case pas suivante de derniere case remplie
        	if(node_Selected == null || row > node_Selected.getAttributes().size()) return false;
        	
        	//Si l'attribut n'a pas de nom, alors on ne peut pas entrer de valeur
        	if(column == 1 && row >= node_Selected.getAttributes().size()) 
        		return false;
        	
        	return true;
        }
        
        public int getRowCount() {
            return 8;//donnees.length;
        }
     
        public int getColumnCount() {
            return entetes.length;
        }
     
        public String getColumnName(int columnIndex) {
            return entetes[columnIndex];
        }
        
        //On affiche le nom de l'attribut, séparé de sa valeur par " : "
        public Object getValueAt(int rowIndex, int columnIndex) {        	
        	if(node_Selected == null || rowIndex >= node_Selected.getAttributes().size()) return "";
        	String s1 = (String) node_Selected.getAttributes().get(rowIndex);
        	String[] ts = s1.split(" : ");
        	if(ts.length > 1) 
        		return ts[columnIndex];
        	else if(ts.length == 1 && columnIndex == 0) 
        		return ts[columnIndex];
        	else return "";
        	
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        	String s = ((String)value).replaceAll(":", "_");
        	
        	//SI nouvel attribut
        	if(rowIndex >= node_Selected.getAttributes().size()){
        		//Si on ne remplit rien on n'ajoute pas de nouvel attribut dans la liste
        		if(s.equals(""))
        			return;
        		else
        			node_Selected.getAttributes().add((String)s);
        	} 
        	else {
        		//Si on supprime un attribut -> En mettant une chaine de caractère vide dans la case du nom
        		if(s.equals("") && columnIndex == 0){
        			node_Selected.getAttributes().remove(rowIndex);
        			//Maj de l'affichage
        			fireTableDataChanged();
                	CollieModelPanel.getCollieModelPanel().repaint();
                	CollieProjectPanel.rebuildAttributes(node_Selected);
        			return;
            	}
	        	String   s1 = (String) node_Selected.getAttributes().get(rowIndex);
	        	String[] c  = s1.split(" : ");
	        	//Si on a changé la valeur d'un attribut
	        	if(c.length > 1) {
		        	c[columnIndex] = (String) s;
		        	node_Selected.getAttributes().set(rowIndex, c[0]+" : "+c[1]);
	        	} 
	        	//Ou si on change le nom d'un attribut qui n'a pas de valeur
	        	else if(columnIndex == 0)
	        		node_Selected.getAttributes().set(rowIndex, s);
	        	//Ou si on donne une valeur a un attribut qui n'en avait pas
	        	else {
	        		node_Selected.getAttributes().set(rowIndex, c[0]+" : "+s);
	        	}
        	}
        	//Maj de l'affichage
        	fireTableDataChanged();
        	CollieModelPanel.getCollieModelPanel().repaint();
        	CollieProjectPanel.rebuildAttributes(node_Selected);
        } 	
    }
    

    
}	// class ColliePropertyPanel

// ==================================================================
