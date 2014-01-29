package livret;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/*
 * Main.java
 *
 * Created on 9 mars 2005, 23:31
 */

/**
 *
 * @author Bruno Duyé
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String fileName = "";
        
        if (args.length != 0) {
            File f = new File(args[0]);
            if (f.exists()) {
                fileName = args[0];
            } else {
                System.out.println("Erreur : le fichier " + f.getAbsoluteFile() +
                        "n'existe pas ...");
                System.exit(1);
            }
        }
        
        new CFenetre(fileName).setVisible(true);
        
    }
    
}
