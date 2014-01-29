/*
 * CConfig.java
 *
 * Created on 17 mars 2005, 21:05
 */

package livret;

import java.io.File;

/**
 *
 * @author bruno DUYE
 */
public class CConfig {
    
    static final String suffixeRectos = "-rectos.pdf";
    static final String suffixeVersos = "-versos.pdf";
    static final String suffixeRV = "-livret.pdf";
    static final String cheminHome = System.getenv("HOME");
    static final String cheminLivret = new File(".").getAbsolutePath() + "/";
    static final String cheminRessourceAide = "doc/";
    static final String fichierAPropos = "a_propos.html";
    static final String fichierAideImprimantes = "aide_imprimantes.html";
    static final String fichierOptions = "config";
    static final String version = "0.2";
    static final float largeurA4 = 21;
    static final float hauteurA4 = 29.7f;
    static final float rapportHL = hauteurA4 / largeurA4;
    
}
