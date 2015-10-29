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

import diagram.NodeElement;
import window.ColliePropertyPanel;

public class JInstanceDialog extends JDialog {
  private String tabRef,tabClasse;
  private JLabel classe,ref;
  private boolean sendData;
  private NodeElement node;

  public JInstanceDialog(JFrame parent, String title, boolean modal,NodeElement n){
    super(parent, title, modal);
    //On récupère le nom et la classe du noeud avant modification
    node = n;
    tabRef= n.getName();
    tabClasse = n.getClassifier();
    this.setSize(370, 150);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    this.initComponent();
  }

  public NodeElement showDialog(){
    this.sendData = false;
    this.setVisible(true);
    //On retourne le node modifié ou non
    return node;      
  }

  private void initComponent(){
    classe = new JLabel("Classe");
    ref = new JLabel("Reference");
	JPanel panel = new JPanel();
	JPanel title = new JPanel();
	title.setLayout(new GridLayout(1,2,2,0));
	title.add(ref);
	title.add(classe);
    JTable tableau = new JTable(new modelRef());
    
    JPanel control = new JPanel();
    
    
    //Si on clique sur ok, on modifie les valeurs du noeud
    JButton okBouton = new JButton("confirm");
    okBouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {        
        node.setClassifier(tabClasse);
        node.setName(tabRef);
        setVisible(false);
      }
    });
    
    //Si on clique sur annuler, on n'applique pas les modifications
    JButton cancelBouton = new JButton("cancel");
    cancelBouton.addActionListener(new ActionListener(){
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
  //Model du tableau de données
  private class modelRef extends AbstractTableModel {
  	private final String[] entetes = {"Reference", "Classe"};
		@Override
		
		public boolean isCellEditable(int row,int column){
			return true;
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
      		if(columnIndex == 0)
      			return tabRef;
      		else
      			return tabClasse;
      }
      
      public void setValueAt(Object value, int rowIndex, int columnIndex) {
      	if(columnIndex == 0)
      		tabRef = (String)value;
      	else
      		tabClasse = (String) value;
      	fireTableDataChanged();
      	CollieModelPanel.getCollieModelPanel().repaint();
      }
  
  }//-------------------classe modelRef-----------------------
    
      
         
}//------------------------------classe JInstanceDialog---------------------