/*
 * CImprimante.java
 *
 * Created on 16 novembre 2006
 */

package livret;

/**
 *
 * @author bruno
 */
public class CImprimante {
    
    private int m_incrementR;
    private int m_incrementV;
    private boolean m_rectoFirst;
    private int m_periode;
    // Est ce qu'il faut retourner manuellement le paquet de feuilles entre 
    // l'impression des rectos et celle des versos ?
    private boolean m_retournerManuellement;
    
    /**
     * Représente les caractéristiques d'une imprimante.
     * Les quadruplets
     * (int incrementR, int incrementV, boolean rectoFirst, int periode)
     * définissent l'ordre d'impression des feuilles.
     *
     * exemple : (-1, 1, false, 0)  (imprimante jet d'encre classique)
     * Pour imprimer 8 pages de façon à ce que l'utilisateur n'ai pas à changer
     * l'ordre des pages, il faut pour cette imprimante imprimer les
     * feuilles dans cet ordre :
     *          V1, V2, V3, V4, R4, R3, R2, R1
     * avec Vi : le verso numéro i calculé par COrdrePages
     *
     * @param incrementR sens de parcours des rectos : {-1, 1}
     * @param incrementV sens de parcours des versos : {-1, 1}
     * @param rectoFirst indique qu'il faut traiter les rectos en premier; ou pas
     * @param periode nombre de pages recto ou verso d'affilées.
     * 1 -> Type RVRVRV, 0 -> Type RRRVVV
     */
    public CImprimante(int _incrementR, int _incrementV, boolean _rectoFirst,
            int _periode, boolean _retournerManuellement) {
        m_incrementR = _incrementR;
        m_incrementV = _incrementV;
        m_rectoFirst = _rectoFirst;
        m_periode = _periode;
        m_retournerManuellement = _retournerManuellement;
    }
    
    public CImprimante(int _incrementR, int _incrementV, boolean _rectoFirst,
            int _periode) {
        this(_incrementR, _incrementV, _rectoFirst, _periode, false);
    }
    
    
    // DEBUG
    public CImprimante() {
        
    }
    
    public int getIncrementR() {
        return m_incrementR;
    }
    
    public int getIncrementV() {
        return m_incrementV;
    }
    
    public boolean isRectoFirst() {
        return m_rectoFirst;
    }
    
    public int getPeriode() {
        return m_periode;
    }
    
    public boolean isManuelRetournement() {
        return m_retournerManuellement;
    }
    
    
}
