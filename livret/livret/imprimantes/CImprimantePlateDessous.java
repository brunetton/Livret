/*
 * Created on 16 novembre 2006, 19:05
 *
 */

package livret.imprimantes;

import livret.*;

/**
 * Repr�sente une imprimante dans laquelle le papier ressort comme il est entr�.
 * La t�te d'impression est situ�e au dessous de la feuille. 
 * Ce type d'imprimante n�cessite un retournement manuel du paquet de feuilles 
 * entre les deux impressions.
 * @author bruno
 */
public class CImprimantePlateDessous extends CImprimante {
    
    public CImprimantePlateDessous() {
        super(-1, -1, true, 0, true);
    }
    
}
