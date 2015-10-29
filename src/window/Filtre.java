package window;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*Filtre permettant de ne garder que les fichiers ayant la bonne extension dans le JFileChooser*/
public class Filtre extends FileFilter {
	 
    String []lesSuffixes;
 
   public Filtre(String []lesSuffixes){
        this.lesSuffixes = lesSuffixes;
   }
 
   boolean appartient( String suffixe ){
      for( int i = 0; i<lesSuffixes.length; ++i){
          if(suffixe.equals(lesSuffixes[i])) return true;
      }
      return false;
   }
 
   //On accepte un fichier si c'est un dossier ou s'il a la bonne extension
   public boolean accept(File f) {
     if (f.isDirectory()) {
         return true;
     }
     String suffixe = null;
     String s = f.getName();
     int i = s.lastIndexOf('.');
    if (i > 0 &&  i < s.length() - 1) {
         suffixe = s.substring(i+1).toLowerCase();
     }
     return suffixe != null && appartient(suffixe);
   }

   
	public String getDescription() {
		return "*.col";
	}

}
