package livret;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class COptionsManager {
    
    private String m_urlFichierConfig =
            CConfig.cheminLivret + CConfig.fichierOptions;
    private char m_tmpBuff[] = new char[2];
    
    /**
     * Remplie les options passées
     * @return true s'il est impossible de lire le fichier de config.
     */
    public boolean lireConfig(COptions options) {
        FileReader fr = null;
        boolean erreurLecture = false;
        try {
            fr = new FileReader(m_urlFichierConfig);
        } catch (FileNotFoundException ex) {
            System.out.println("COptionsManager: Aucun fichier de configuration trouvé ("
                    + m_urlFichierConfig + ")");
            return true;
        }
        
        try {
            if (fr.read(m_tmpBuff) != 2) {
                erreurLecture = true;
            } else {
                options.typeImprimante = m_tmpBuff[0];
                options.rectoVerso = (m_tmpBuff[1] == 0) ? false : true;
            }
        } catch (IOException ex) {
            System.out.println("COptionsManager: impossible de lire depuis "
                    + fr.toString());
            erreurLecture = true;
        }
        
        try {
            fr.close();
        } catch (IOException ex) {
            System.out.println("COptionsManager: impossible de fermer le fichier de config ...");
        }
        
        System.out.println("lu : " + options);
        return ! erreurLecture;
    }
    
    public void enregistrerConfig(COptions options) {
        System.out.println("écriture de " + options);
        
        FileWriter fw = null;
        try {
            fw = new FileWriter(m_urlFichierConfig);
        } catch (IOException ex) {
            System.out.println("COptionsManager: impossible de créer un writer sur "
                    + m_urlFichierConfig);
        }
        m_tmpBuff[0] = (char) options.typeImprimante;
        m_tmpBuff[1] = options.rectoVerso ? (char) 1 : (char) 0;
        try {
            fw.write(m_tmpBuff);
        } catch (IOException ex) {
            System.out.println("COptionsManager: impossible d'écrire dans " + fw.toString());
        }
        try {
            fw.close();
        } catch (IOException ex) {
            System.out.println("COptionsManager: impossible de fermer le fichier de config ...");
        }
    }
}
