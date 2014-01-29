/*
 * CListePages.java
 *
 * Created on 17 mars 2005, 23:50
 */

package livret;

import java.util.ArrayList;
import java.util.List;


/**
 * Stoke une liste des pages des feuilles � raison de deux pages par feuille.
 * Une feuille est compos�e de deux pages.
 * Attention : La liste est indic�e � partir de 1.
 */
public class CListePages {
    
    private List <Integer> m_liste;
    
    public CListePages() {
        m_liste = new ArrayList();
    }
    
    /**
     * Ajoute une feuille � la liste, c'est � dire deux pages.
     */
    public void ajouterFeuille(Integer page1, Integer page2) {
        m_liste.add(page1);
        m_liste.add(page2);
    }
    
    /**
     * Ajoute une feuille � la liste.
     */
    public void ajouterFeuille(CFeuille feuille) {
        m_liste.add(feuille.getPage1());
        m_liste.add(feuille.getPage2());
    }
    
    public void clear() {
        m_liste.clear();
    }
    
    /**
     * Indic� � partir de 1
     */
    public CFeuille getFeuille(int numFeuille) {
        return new CFeuille(
                m_liste.get(2 * numFeuille - 2),
                m_liste.get(2 * numFeuille - 1));
    }
    
    
    public Integer getTaille() {
        return m_liste.size();
    }
   
    
    public List <Integer> getListe() {
        return  m_liste;
    }
   
            
    public String toString() {
        String s = "[";
        
        for (int i = 0; i < m_liste.size() - 1; i++) {
            s = s + m_liste.get(i).toString() + ", ";
        }
        if (m_liste.size() > 0) {
            s = s + m_liste.get(m_liste.size() - 1).toString();
        }
        s = s + "]";
        
        return s;
    }
}
