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
    private NodeElement node_Selected;
    private JTable tableau_droite;
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
        	fireTableDataChanged();
        	CollieModelPanel.getCollieModelPanel().repaint();
        }
    
    }
    
    private class modelTableAttributs extends AbstractTableModel {
     
        private final String[] entetes = {"Attributes", "Value"};
     
        public modelTableAttributs() {
            super();     
        }
     
        @Override
        public boolean isCellEditable(int row, int column) {
        	if(node_Selected == null || row > node_Selected.getAttributes().size()) return false;

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
     
        public Object getValueAt(int rowIndex, int columnIndex) {        	
        	if(node_Selected == null || rowIndex >= node_Selected.getAttributes().size()) return "";
        	String s1 = (String) node_Selected.getAttributes().get(rowIndex);
        	String[] ts = s1.split(" ");
        	if(ts.length > 1) 
        		return ts[columnIndex];
        	else if(ts.length == 1 && columnIndex == 0) 
        		return ts[columnIndex];
        	else return "";
        	
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        	String s = ((String)value).replaceAll(" ", "_");
        	if(s.equals("")) return;
        	
        	if(rowIndex >= node_Selected.getAttributes().size()){
        		node_Selected.getAttributes().add((String)value);
        	} else {
	        	String   s1 = (String) node_Selected.getAttributes().get(rowIndex);
	        	String[] c  = s1.split(" ");
	        	if(c.length > 1) {
		        	c[columnIndex] = (String) value;
		        	node_Selected.getAttributes().set(rowIndex, c[0]+" "+c[1]);
	        	} else if(c.length == 1)
	        		node_Selected.getAttributes().set(rowIndex, c[0]+" "+s);
	        	else {
	        		node_Selected.getAttributes().set(rowIndex, c[0]);
	        	}
        	}
        	fireTableDataChanged();
        	CollieModelPanel.getCollieModelPanel().repaint();
        }   	
    }
    

    
}	// class ColliePropertyPanel

// ==================================================================
