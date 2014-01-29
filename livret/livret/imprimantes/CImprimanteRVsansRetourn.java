/*
 * CImprimanteLaser.java
 *
 * Created on 16 novembre 2006, 19:05
 *
 */

package livret.imprimantes;

import livret.*;

/**
 * Représente une imprimante recto verso qui ne retourne pas la feuille en sortie.
 */
public class CImprimanteRVsansRetourn extends CImprimante {
    
    public CImprimanteRVsansRetourn() {
        super(-1, -1, true, 1, false);
    }
    
}
