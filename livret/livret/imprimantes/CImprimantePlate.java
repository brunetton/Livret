/*
 * Created on 16 novembre 2006, 19:05
 *
 */

package livret.imprimantes;

import livret.*;

/**
 * Repr�sente une imprimante dans laquelle le papier ressort comme il est entr�.
 * La t�te s'impression est situ�e au dessus de la feuille. 
 * Ce type d'imprimante n�cessite un retournement manuel du paquet de feuilles 
 * entre les deux impressions.
 *
 */
public class CImprimantePlate extends CImprimante {
    
    public CImprimantePlate() {
        super(-1, -1, false, 0, true);
    }
    
}
