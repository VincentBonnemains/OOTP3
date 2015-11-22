/**
 * Here, we establish a specialised frame that will operate as the main
 *	application screen. A window listener handles the correct closing
 *	procedure.
 *
 * The frame is fully populated with a panel which ultimately carries three
 *	subcomponents.
 *
 * The frame also sports a menubar with File and Help menus. The corresponding
 *	menu items are associated with specific listener objects.
 *
 *  This class implements the singleton design pattern.
 *
 *  The class now implements the PropertyChangeListener interface. It awaits
 *	a change event advising that the user has changed the model and that
 *	the save/save as buttons be enabled.
 *
 * @author	K Barclay
 */

package window;



import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;

import collie.Collie;
import diagram.CollaborationDiagram;
import diagram.ModelElement;
import diagram.NodeElement;
import dialog.JAttributesDialog;
import dialog.JInstanceDialog;
import dialog.JOpenDialog;
import dialog.JSaveAsDialog;



public class CollieFrame	extends JFrame implements PropertyChangeListener {
	//On retient le nom du fichier en cours d'édition
	public static File fichierSauvegarde;
	//firstSave == true si le fichier n'a jamais été sauvé
	public static boolean firstSave;

    public static CollieFrame		getCollieFrame()	{ return collieFrame; }
    
    
    
//
//  Source:	CollieModelPanel
//  Event:	MODEL_DIRTIED_CHANGED_PROPERTY
//
//  If the model has been dirtied, then we enable the save and save as menu items
//	and enable the save toolbar button.
//
    public void				propertyChange(PropertyChangeEvent event) {
    	if(event.getPropertyName().equals(CollieModelPanel.MODEL_DIRTIED_CHANGED_PROPERTY)) {
    	    fileSaveAction.setEnabled(true);
    	    fileSaveAsAction.setEnabled(true);
    	    //Maj de l'arbre
    	    CollieProjectPanel.rebuild(fichierSauvegarde);
    	}
    }
    
    
    
    protected				CollieFrame(String captionTitle) {
        super(captionTitle);
        firstSave = true;

	Container contentPane = this.getContentPane();
	contentPane.setLayout(new BorderLayout());
	
	ColliePanel colliePanel = ColliePanel.getColliePanel(); //new ColliePanel();
	contentPane.add(colliePanel, BorderLayout.CENTER);

	assembleMenuAndToolBar();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width * 3 / 4;
        int height = screen.height * 3 / 4;
        this.setBounds(screen.width / 8, screen.height / 8, width, height);
        this.setVisible(true);
        
        colliePanel.setDivider();
        
    this.addWindowListener(new WListener());
        
    }
    
    //Mise à disposition des boutons de sauvegarde
    public  void saveOpen(){
    	fileSaveAction.setEnabled(true);
	    fileSaveAsAction.setEnabled(true);
    }



// ---------- statics -------------------------------------

    private static final Boolean FALSE		= new Boolean(false);
    private static final Boolean TRUE		= new Boolean(true);
    
    
    
// ---------- properties ----------------------------------

    private static CollieFrame	collieFrame	= new CollieFrame("COLLIE: Collaboration diagrammer");
    
    private JMenuBar		menuBar	= new JMenuBar();
    private JToolBar		toolBar	= new JToolBar();



// ---------- private -------------------------------------
    
    //
    // Menu items listener objects
    //
    private FileNewAction		fileNewAction 		= new FileNewAction("New",	new ImageIcon(Collie.class.getResource("/images/newButton_large.gif")));
    private FileOpenAction		fileOpenAction 		= new FileOpenAction("Open...",	new ImageIcon(Collie.class.getResource("/images/openButton_large.gif")));
    private FileSaveAction		fileSaveAction 		= new FileSaveAction("Save",	new ImageIcon(Collie.class.getResource("/images/saveButton_large.gif")));
    private FileSaveAsAction	fileSaveAsAction	= new FileSaveAsAction("Save As...");
    private FileExitAction		fileExitAction 		= new FileExitAction("Exit");

    private HelpAboutAction		helpAboutAction 	= new HelpAboutAction("About...");

    //
    //  Menu tables:
    //
    //	menu name		menu mnemonic					(first row)
    //	menu item action	menu item mnemonic	has 	enabled? 	(subsequent rows)
    //							toolbar
    //							button?
    //
    private Object[][] fileMenus = {
    	{ "File",		new Character('F') 			},
    	{ fileNewAction,	new Character('N'),	TRUE,	TRUE	},
    	{ fileOpenAction,	new Character('O'),	TRUE,	TRUE	},
    	{ fileSaveAction,	new Character('S'),	TRUE,	FALSE	},
    	{ fileSaveAsAction,	new Character('a'),	FALSE,	FALSE	},
    	{ null								},
    	{ fileExitAction,	new Character('x'),	FALSE,	TRUE	}
    };
    
    private Object[][] helpMenus = {
    	{ "Help",		new Character('H')			},
    	{ helpAboutAction,	new Character('a'),	FALSE,	TRUE	}
    };
    
    private Object[] menus = {
    	fileMenus,
    	helpMenus
    };




    //
    //  Assemble the various menus and for some of the menu items prepare
    //	  matching toolbar buttons. Attach the menu and the toolbar to the
    //    frame.
    //
    private void			assembleMenuAndToolBar() {
    	menuBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    	
    	for(int k = 0; k < menus.length; k++)
    	    assembleMenu((Object[][])menus[k]);
    	    
    	this.setJMenuBar(menuBar);
	
	toolBar.setFloatable(false);
	this.getContentPane().add(toolBar, BorderLayout.NORTH);
    }
    
    
    
    //
    //  Extract the menu name and mnemonic from the first row. Create a
    //	  menu, add it to the menubar, and add a toolbar separator. For
    //	  each subsequent row, prepare a menu item and, if necessary, a
    //	  toolbar button. Add each to their respective container.
    //
    private void	assembleMenu(Object[][] menuData) {
    	String menuName = (String)menuData[0][0];
    	Character menuMnemonic = (Character)menuData[0][1];
    	JMenu menu = new JMenu(menuName);
    	menu.setMnemonic(menuMnemonic.charValue());
    	
    	for(int k = 1; k < menuData.length; k++) {
    	    if(menuData[k][0] == null)
    	    	menu.addSeparator();
    	    else {
    	    	Action menuItemAction = (Action)menuData[k][0];
    	    	Character menuItemMnemonic = (Character)menuData[k][1];
    	    	Boolean menuItemHasButton = (Boolean)menuData[k][2];
    	    	Boolean menuItemEnabled = (Boolean)menuData[k][3];
    	    	menuItemAction.setEnabled(menuItemEnabled.booleanValue());
    	    	JMenuItem menuItem = menu.add(menuItemAction);
    	    	menuItem.setMnemonic(menuItemMnemonic.charValue());
    	    	if(menuItemHasButton.booleanValue() == true) {
    	    	    JButton button = toolBar.add(menuItemAction);
    	    	    button.setActionCommand((String)menuItemAction.getValue(Action.NAME));
    	    	    button.setToolTipText((String)menuItemAction.getValue(Action.NAME));
    	    	}
    	    }
    	}
    	
    	menuBar.add(menu);
    	toolBar.addSeparator();
    }


    class WListener extends WindowAdapter{
    	public void windowClosing(WindowEvent e){
    		//Si l'application n'a pas été sauvegardée, on propose à l'utilisateur de le faire
    		safeSave("Fermeture de l'application","Attention : Des modifications n'ont pas été enregistrées, Enregistrer avant de quitter?");
    		System.out.println("close propre");
    		System.exit(0);
    	}
    }
    
    //Fonction qui demande a sauvegarder en cas de fermeture du fichier, en mettant "title" comme titre de la fenetre et "message" comme message d'information
    public void safeSave(String title,String message){
    	int option;
    	//Si le fichier n'a pas été sauvegardé
		if(fileSaveAction.isEnabled() == true){
			JOptionPane jop = new JOptionPane();
			option = jop.showConfirmDialog(null, 
			        message,
			        title, 
			        JOptionPane.YES_NO_OPTION, 
			        JOptionPane.WARNING_MESSAGE);
			//Si l'utilisateur décide de sauvegarder
			if(option == 0){
				//Si c'est la premiere sauvegarde, on lui demande de choisir le nom du fichier
				if(firstSave){
	       		 	 JSaveAsDialog jsad = new JSaveAsDialog();
	       		 	 //On vérifie que l'extension de fichier est bien la bonne
		       		 String[] filt = new String[1];
		       		 filt[0] = "col";
		       		 jsad.setFileFilter(new Filtre(filt));
		       		 int returnVal = jsad.showSaveDialog(null);
		       		 //Si l'utilisateur n'a pas annulé
		   			 if ( returnVal == JFileChooser.APPROVE_OPTION ) 
		   			 {
		   				fichierSauvegarde = jsad.getSelectedFile();
		   				firstSave = false;
		   				String path = fichierSauvegarde.getAbsolutePath();
		   				//Si l'utilisateur n'a pas mis l'extension en fin de nom, on la rajoute
						if(!path.endsWith(".col")) {
						    File f = new File(path + ".col");
						    fichierSauvegarde.delete();
						    fichierSauvegarde = f;
						}
		   			 }
		   			 else
		   				 return;
	    		}
				System.out.println(fichierSauvegarde.getAbsolutePath());
				//Ecriture dans le fichier
				write(fichierSauvegarde);
				//Bouton de sauvegarde plus disponible
				fileSaveAction.setEnabled(false);
			}
		}
    }
    
    
    //Fonction de recuperation des données dans le fichier f
    public void read(File f){
    	ObjectInputStream flotLecture;
        Object lu = null;
		try {
			flotLecture = new ObjectInputStream(new FileInputStream(f));
			lu = flotLecture.readObject();
			flotLecture.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        CollaborationDiagram diagram;
        if (lu instanceof CollaborationDiagram)
        	diagram = (CollaborationDiagram)lu;
        else
        {
        	JOptionPane jop = new JOptionPane();
        	jop.showMessageDialog(null, "Message d'erreur", "Impossible d'ouvrir le fichier", JOptionPane.ERROR_MESSAGE);
        	System.out.println("Erreur de lecture");
        	return;
        }
        //Mise à jour de l'affichage
        CollieModelPanel.getCollieModelPanel().setDiagram(diagram);
        CollieProjectPanel.getCollieProjectPanel().rebuild(fichierSauvegarde);
        ColliePropertyPanel.getColliePropertyPanel().setNode(null);
    }
    
    //Fonction d'écriture des données dans le fichier f
    public void write(File f){
    	CollaborationDiagram diagram = CollieModelPanel.getCollieModelPanel().getDiagram();
    	ObjectOutputStream flotEcriture;
		try {
			flotEcriture = new ObjectOutputStream(new FileOutputStream(f));
			flotEcriture.writeObject(diagram);
			flotEcriture.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Afficher message d'erreur
			JOptionPane jop = new JOptionPane();
        	jop.showMessageDialog(null, "Message d'erreur", "Erreur d'écriture", JOptionPane.ERROR_MESSAGE);
			System.out.println("Erreur d'écriture");
			e.printStackTrace();
		}
    }
    
// ---------- inner class ---------------------------------

    class HelpAboutAction extends AbstractAction {

    	public				HelpAboutAction(String label) {
	    super(label);
    	}



    	public void				actionPerformed(ActionEvent event) {
	    String message = "COLLIE\n";
	    message += "Author  : A GUSTIN et V BONNEMAINS\n";
	    message += "Version : 2\n";
	    message += "Date    : Novembre 2015";
	    JOptionPane.showMessageDialog(CollieFrame.this, message, "COLLIE", JOptionPane.INFORMATION_MESSAGE);
    	}
    	
    }	// class HelpAboutAction



// ---------- action classes ------------------------------
//METTRE VOS CLASSES ACTION ICI
    class FileExitAction extends AbstractAction {

    	public				FileExitAction(String label) {
	    super(label);
    	}



    	public void				actionPerformed(ActionEvent event) {
    		System.out.println(event.getActionCommand());
    		CollieFrame c = CollieFrame.getCollieFrame();
    		c.dispatchEvent(new WindowEvent(c, WindowEvent.WINDOW_CLOSING));
    		
    	}
    	
    }
    
    class FileSaveAsAction extends AbstractAction {

    	public				FileSaveAsAction(String label) {
	    super(label);
    	}



    	public void				actionPerformed(ActionEvent event) {
    		 System.out.println(event.getActionCommand());
    		 JSaveAsDialog jsad = new JSaveAsDialog();
    		 String[] filt = new String[1];
    		 filt[0] = "col";
    		 jsad.setFileFilter(new Filtre(filt));
    		 int returnVal = jsad.showSaveDialog(null);
    		 //Si on a choisi un fichier valide
			 if ( returnVal == JFileChooser.APPROVE_OPTION ) 
			 {
				fichierSauvegarde = jsad.getSelectedFile();	
				firstSave = false;
				String path = fichierSauvegarde.getAbsolutePath();
				//Si l'utilisateur n'a pas spécifié l'extension, on la rajoute
				if(!path.endsWith(".col")) {
				    File f = new File(path + ".col");
				    fichierSauvegarde.delete();
				    fichierSauvegarde = f;
				}
				//Mise à jour de l'affichage
				CollieProjectPanel.rename(fichierSauvegarde);
				System.out.println(fichierSauvegarde.getAbsolutePath());
				write(fichierSauvegarde);
				//Bouton de sauvegarde plus disponible
				fileSaveAction.setEnabled(false);
			 }
    	}
    	
    }
    
    class FileNewAction extends AbstractAction {

    	public FileNewAction(String label,ImageIcon image) {
			super(label);
    	}

    	public void actionPerformed(ActionEvent event) {
    		System.out.println(event.getActionCommand());
    		//Avertissement à l'utilisateur d'ouverture d'un nouveau projet
    		int option;
    		JOptionPane jop = new JOptionPane();
        	option = jop.showConfirmDialog(null, "Vous allez ouvrir un nouveau projet, continuer?", "New", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        	if(option == 0){
        		//Si sauvegarde du précédent projet n'a pas été effectuée, on la propose
	    		safeSave("Creation d'un nouveau projet","Attention : Des modifications n'ont pas été enregistrées, Enregistrer avant de quitter?");
	    		//Remise à zero des données et de l'affichage
	    		fichierSauvegarde = null;
	    		firstSave = true;
	    		fileSaveAction.setEnabled(false);
	    	    CollieModelPanel.getCollieModelPanel().raz();
	    		ColliePropertyPanel.getColliePropertyPanel().setNode(null);
	    		CollieProjectPanel.getCollieProjectPanel().rebuild(fichierSauvegarde);
        	}
    	}
    	
    }
    
    class FileOpenAction extends AbstractAction {

    	public				FileOpenAction(String label,ImageIcon image) {
    		super(label);
    	}



    	public void				actionPerformed(ActionEvent event) {
    		System.out.println(event.getActionCommand());
    		//Boolean servant à savoir s'il faut afficher la confirmation de la sauvegarde
    		boolean confirmation = false;
    		if(fileSaveAction.isEnabled() == true)
    			confirmation = true;
    		 //Si l'utilisateur n'a pas sauvegardé, on lui propose de le faire
    		 safeSave("Ouverture d'un projet","Attention : Des modifications n'ont pas été enregistrées, Enregistrer avant de quitter?");
    		 //Si il accepte, on confirme la sauvegarde avant choix du projet à ouvrir
    		 if(fileSaveAction.isEnabled() == false && confirmation == true){
    			 JOptionPane info = new JOptionPane();
    			 info.showMessageDialog(null, "Sauvegarde effectuée", "Information", JOptionPane.INFORMATION_MESSAGE);
    		 }
    		 //Choix du fichier à ouvrir
    		 JOpenDialog jod = new JOpenDialog();
    		 String[] filt = new String[1];
    		 filt[0] = "col";
    		 jod.setFileFilter(new Filtre(filt));
    		 int returnVal = jod.showOpenDialog(null);
    		 //Si fichier valide
			 if ( returnVal == JFileChooser.APPROVE_OPTION ) 
			 {
				fichierSauvegarde = jod.getSelectedFile();				
				firstSave = false;
				System.out.println(fichierSauvegarde.getAbsolutePath());
				read(fichierSauvegarde);
				fileSaveAction.setEnabled(false);
			 }
		 }
    	
    }
    
    class FileSaveAction extends AbstractAction {

    	public				FileSaveAction(String label,ImageIcon image) {
	    super(label);
    	}



    	public void				actionPerformed(ActionEvent event) {
    		System.out.println(event.getActionCommand());
    		//Si c'est la premiere fois qu'on sauvegarde, on demande à l'utilisateur de choisir le nom du fichier
    		if(firstSave){
       		 	 JSaveAsDialog jsad = new JSaveAsDialog();
	       		 String[] filt = new String[1];
	       		 filt[0] = "col";
	       		 jsad.setFileFilter(new Filtre(filt));
	       		 int returnVal = jsad.showSaveDialog(null);
	       		 //Si l'utilisateur n'a pas annulé
	   			 if ( returnVal == JFileChooser.APPROVE_OPTION ) 
	   			 {
	   				fichierSauvegarde = jsad.getSelectedFile();
	   				firstSave = false;
	   				String path = fichierSauvegarde.getAbsolutePath();
	   				//On ajoute l'extension valide au besoin
					if(!path.endsWith(".col")) {
					    File f = new File(path + ".col");
					    fichierSauvegarde.delete();
					    fichierSauvegarde = f;
					}
					//Maj de l'arbre
					CollieProjectPanel.rename(fichierSauvegarde);
	   			 }
	   			 else
	   				 return;
    		}
    		//Ecriture dans le fichier
			System.out.println(fichierSauvegarde.getAbsolutePath());
			write(fichierSauvegarde);
			//Bouton save plus disponible
			fileSaveAction.setEnabled(false);
		 }
    	
    }
    

}	// class CollieFrame