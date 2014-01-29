/*
 * Created on 16 novembre 2006, 19:05
 *
 */

package livret.imprimantes;

import livret.*;

/**
 * Représente une imprimante dans laquelle le papier ressort comme il est entré.
 * La tête s'impression est située au dessus de la feuille. 
 * Ce type d'imprimante nécessite un retournement manuel du paquet de feuilles 
 * entre les deux impressions.
 *
 */
public class CImprimantePlate extends CImprimante {
    
    public CImprimantePlate() {
        super(-1, -1, false, 0, true);
    }
    
}
