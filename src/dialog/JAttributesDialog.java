package dialog;

import window.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import window.ColliePropertyPanel;

public class JAttributesDialog extends JDialog {
  private ArrayList<String> baseAttributes = new ArrayList<String>();
  private boolean sendData;
  private JLabel nom,val;

  public JAttributesDialog(JFrame parent, String title, boolean modal,ArrayList<String> l){
    super(parent, title, modal);
    //La liste des attributs est initialisée aux valeurs du noeud avant modification
    baseAttributes = l;
    this.setSize(370, 210);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    this.initComponent();
  }

  //On récupère la liste des attributs du noeud, modifée ou non selon le bouton cliqué
  public ArrayList<String> showDialog(){
    this.sendData = false;
    this.setVisible(true);      
    return this.baseAttributes;      
  }

  private void initComponent(){
    nom = new JLabel("Nom");
    val = new JLabel("Valeur");
	JPanel panel = new JPanel();
	JPanel title = new JPanel();
	title.setLayout(new GridLayout(1,2,2,0));
	title.add(nom);
	title.add(val);
    JTable tableau = new JTable(new modelAttributes());
    
    JPanel control = new JPanel();
    
    JButton okBouton = new JButton("confirm");
    okBouton.addActionListener(new ActionListener(){
      //Si on clique sur "ok", on applique les modifications effectuées dans le tableau
      public void actionPerformed(ActionEvent arg0) {        
        baseAttributes = ((modelAttributes)tableau.getModel()).getAttributes();
        setVisible(false);
      }
    });

    JButton cancelBouton = new JButton("cancel");
    cancelBouton.addActionListener(new ActionListener(){
      //Si on clique sur annuler, on n'applique pas les modifications effectuées dans le tableau
      public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
      }      
    });

    control.add(okBouton);
    control.add(cancelBouton);

    
    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    panel.add(title);
    panel.add(tableau);
    panel.add(control);
    this.getContentPane().add(panel);
  }
    
  //------------------------------Classes privées------------------------------
  	 //Model du tableau utilisant la liste des attributs
     private class modelAttributes extends AbstractTableModel{
    	 ArrayList attributes = new ArrayList();
         private final String[] entetes = {"Attributes", "Value"};
         
         public ArrayList getAttributes(){
          	return attributes;
          }
         
         public boolean isCellEditable(int row, int column) {
          	if(row > attributes.size()) return false;

          	if(column == 1 && row >= attributes.size()) 
          		return false;
          	
          	return true;
          }
         
         @Override
		 public int getColumnCount() {
			// TODO Auto-generated method stub
        	 return entetes.length;
         }
	
         @Override
         public int getRowCount() {
         // TODO Auto-generated method stub
        	 return 8;
         }
         
         public String getColumnName(int columnIndex) {
             return entetes[columnIndex];
         }
	
         @Override
         public Object getValueAt(int rowIndex, int columnIndex) {        	
          	if(rowIndex >= attributes.size()) return "";
          	String s1 = (String) attributes.get(rowIndex);
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
          	
          	if(rowIndex >= attributes.size()){
          		attributes.add((String)value);
          	} else {
              	String   s1 = (String) attributes.get(rowIndex);
              	String[] c  = s1.split(" ");
              	if(c.length > 1) {
      	        	c[columnIndex] = (String) value;
      	        	attributes.set(rowIndex, c[0]+" "+c[1]);
              	} else if(c.length == 1)
              		attributes.set(rowIndex, c[0]+" "+s);
              	else {
              		attributes.set(rowIndex, c[0]);
              	}
          	}
          	fireTableDataChanged();
          }
	    	 
    }
    //-----------classe modelAttributs-------------
      
         
}//---------------classe JAttributesDialog---------------------