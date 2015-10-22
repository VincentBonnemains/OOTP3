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
    private NodeElement selected;
    private JTable tableau;
    private JTable tableau_gauche;
    
    protected 				ColliePropertyPanel() {
    	super();
    	this.setLayout(new GridLayout(1,2));
        tableau = new JTable(new modeleAttributs());
        tableau_gauche = new JTable(new modeleRef());
        JScrollPane pleft = new JScrollPane(tableau_gauche);
    	JScrollPane pright = new JScrollPane(tableau);
    	
    	this.add(pleft);
    	this.add(pright);
    	
    }
    
    public void setNode(NodeElement e){
    	selected = e;
    	((AbstractTableModel) tableau.getModel()).fireTableDataChanged();
    	((AbstractTableModel) tableau_gauche.getModel()).fireTableDataChanged();
    }
    
    
// ---------- properties ----------------------------------

    private static ColliePropertyPanel	colliePropertyPanel = new ColliePropertyPanel();
    
// ---------- special class -------------------------------
    private class modeleRef extends AbstractTableModel {
    	private final String[] entetes = {"Reference", "Classe"};
		@Override
		
		public boolean isCellEditable(int row,int column){
			if(selected != null)
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
        	if(selected != null){
        		if(columnIndex == 0)
        			return selected.getName();
        		else
        			return selected.getClassifier();
        	}
        	return "";
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        	if(columnIndex == 0)
        		selected.setName((String)value);
        	else
        		selected.setClassifier((String) value);
        	fireTableDataChanged();
        } 
    
    }
    
    private class modeleAttributs extends AbstractTableModel {
     
        private final String[] entetes = {"Attributes", "Value"};
     
        public modeleAttributs() {
            super();     
        }
     
        @Override
        public boolean isCellEditable(int row, int column) {
        	if(selected == null || row > selected.getAttributes().size()) return false;

        	if(column == 1 && row >= selected.getAttributes().size()) 
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
        	if(selected == null || rowIndex >= selected.getAttributes().size()) return "";
        	String s1 = (String) selected.getAttributes().get(rowIndex);
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
        	
        	if(rowIndex >= selected.getAttributes().size()){
        		selected.getAttributes().add((String)value);
        	} else {
	        	String   s1 = (String) selected.getAttributes().get(rowIndex);
	        	String[] c  = s1.split(" ");
	        	if(c.length > 1) {
		        	c[columnIndex] = (String) value;
		        	selected.getAttributes().set(rowIndex, c[0]+" "+c[1]);
	        	} else if(c.length == 1)
	        		selected.getAttributes().set(rowIndex, c[0]+" "+s);
	        	else {
	        		selected.getAttributes().set(rowIndex, c[0]);
	        	}
        	}
        	fireTableDataChanged();
        }   	
    }
    

    
}	// class ColliePropertyPanel

// ==================================================================
