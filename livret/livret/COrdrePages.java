/*
 * COrdrePages.java
 *
 * Created on 17 mars 2005, 23:14
 */

package livret;
/**
 * Renseigne l'ordre des pages à mettre sur les feuilles. Par exemple, pour un
 * document de 5 pages;
 *  Rectos : {0, 1, 0, 3}
 *  Versos : {2, 0, 4, 5}
 * signifie que :
 *  - la page 1 contiendra :
 *    Recto :
 *     - Une page vide
 *     - La page 1 de l'original
 *    Verso :
 *     - Une page vide
 *     - La page 3
 *  - la page 2 contiendra
 *    Recto :
 *    - la page 2
 *    - une page vide
 *    Verso :
 *    - la page 4
 *    - la page 5
 *
 * Attention ici à la terminologie :
 *     - Mini - Page : Une petite page sur une feuille (donc une demi A4)
 *     - Feuille : La feuille de papier physique (A4)
 */

public class COrdrePages {
    
    // Liste de l'ordre des mini-pages sur les rectos
    // (ne dépend pas de l'imprimante)
    private CListePages m_listePagesRectos;
    // Liste de l'ordre des mini-pages sur les versos
    // (ne dépend pas de l'imprimante)
    private CListePages m_listePagesVersos;
    private int m_nombrePagesInitiales;
    // Nombre de petites pages finales (multiple de 4)
    private int m_nombreMiniPagesFinales;
    // Nombre de feuilles R-V finales
    private int m_nombreFeuillesFinales;
    private CImprimante m_imprimante;
    // Ordre final des mini-pages à imprimer, en fonction de l'imprimante
    private CListePages m_ordreImpression;
    
    /**
     * Crée une nouvelle instance contenant l'ordre des pages des
     * feuilles rectos et versos. 0 correspond à une page vide.
     */
    public COrdrePages(int nombrePages, CImprimante imprimante) {
        m_listePagesRectos = new CListePages();
        m_listePagesVersos = new CListePages();
        m_ordreImpression = new CListePages();
        MAJ(nombrePages, imprimante);
    }
    
    /**
     * Renvoie un couple de mini pages à imprimer sur le recto i en
     * fonction du type de l'imprimante.
     * -> indicé à partir de 1
     */
//    public CFeuille getFeuilleRecto(int i) {
//        return m_listePagesRectos.getFeuille(i);
//    }
    
    /**
     * Renvoie un couple de mini pages à imprimer sur le verso i en
     * fonction du type de l'imprimante.
     * --> indicé à partir de 1
     */
//    public CFeuille getFeuilleVerso(int i) {
//        return m_listePagesVersos.getFeuille(i);
//    }
    
    /**
     * Mets à jour m_listePagesRectos, m_listePagesVersos et m_ordreImpression
     * en fonction du nombre de pages du document initial et du type d'imprimante
     */
    public void MAJ(int nombrePages, CImprimante imprimante) {
        m_nombrePagesInitiales = nombrePages;
        m_imprimante = imprimante;
        MAJListePages();
        MAJOrdreImpression();
    }
    
    /**
     * @return liste des mini-pages à imprimer. Cette liste est mise à jour
     * lors de la création de l'objet ou lors d'un appel à MAJ().
     * - Dans le cas d'une imprimante non recto-verso, donc nécessitant une
     * intervention manuelle au milieu du traitement, la première moitié de la
     * liste (dont la taille est toujours un multiple de 4) donne la première
     * impression à effectuer, qu'on peu appeller les rectos. La deuxième moitié
     * donne la deuxième.
     * - Dans le cas d'une imprimante recto-verso, la liste représente la suite
     * des mini-pages à imprimer.
     */
    public CListePages getOrdreMiniPages() {
        return m_ordreImpression;
    }
    
    /**
     * renvoie le nombre de feuilles nécessaires calculé lors de la création de
     * l'objet ou lors du dernier appel à MAJ
     *
     * @return le nombre de feuilles de papier nécessaires (donc rectos-versos)
     */
    public int getNbFeuilles() {
        return m_nombreFeuillesFinales;
    }
    
    public String toString() {
//    return "Rectos : " + m_listePagesRectos.toString() + "\n" +
//            "Versos : " + m_listePagesVersos.toString();
        return m_ordreImpression.toString();
    }
    
    /**
     * Calcule le nombre de feuilles recto-verso nécessaires pour imprimer
     * un livret de nbPages. Cette fonction est publique pour permettre à l'ihm
     * de donner le nombre de pages sans pour autant calculer l'ordre des pages.
     *
     * @return nombre de feuilles nécessaires pour imprimer les nbPages en livret
     * @param  nbPages nombre de pages du document initial
     */
    static public int nbFeuilles(int nbPages) {
        return nombreMiniPagesTotales(nbPages) / 4;
    }
    
    /**
     * Recalcule les listes des pages rectos et versos.
     */
    private void MAJListePages() {
        m_nombreMiniPagesFinales = nombreMiniPagesTotales(m_nombrePagesInitiales);
        m_nombreFeuillesFinales = m_nombreMiniPagesFinales / 4;
        
        m_listePagesRectos.clear();
        m_listePagesVersos.clear();
        
        // Rectos
        for (int i = 1; i <= m_nombreFeuillesFinales; i++ ) {
            m_listePagesRectos.ajouterFeuille(page(m_nombreMiniPagesFinales - 2*i + 2), page(2*i - 1));
        }
        // Versos
        for (int i = 1; i <= m_nombreFeuillesFinales; i++ ) {
            m_listePagesVersos.ajouterFeuille(page(2*i), page(m_nombreMiniPagesFinales - 2*i + 1));
        }
        
    }
    
    /**
     * Remplie m_ordreImression
     */
    private void MAJOrdreImpression() {
        m_ordreImpression.clear();
        
        int incrementR = m_imprimante.getIncrementR();
        int incrementV = m_imprimante.getIncrementV();
        boolean rectos = m_imprimante.isRectoFirst();
        int periode = (m_imprimante.getPeriode() == 0) ?
            m_nombreFeuillesFinales : m_imprimante.getPeriode();
        int cpt = 0;
        int r;
        int v;
        r = (incrementR == -1) ? m_nombreFeuillesFinales : 1;
        v = (incrementV == -1) ? m_nombreFeuillesFinales : 1;
        
        for (int i = 1; i <= 2 * m_nombreFeuillesFinales; ++i) {
            if (rectos) {
                m_ordreImpression.ajouterFeuille(m_listePagesRectos.getFeuille(r));
                r += incrementR;
            } else {
                m_ordreImpression.ajouterFeuille(m_listePagesVersos.getFeuille(v));
                v += incrementV;
            }
            cpt++;
            if (cpt == periode) {
                cpt = 0;
                rectos = ! rectos;
            }
        }
    }
    
    /**
     * Calcule le nombre de mini-pages finales (4 pages = 1 feuille RV)
     */
    static private int nombreMiniPagesTotales(int nbPages) {
        return (int) ((float) Math.ceil(nbPages / 4f) ) * 4;
    }
    
    private int page(int page) {
        if (page <= m_nombrePagesInitiales) {
            return page;
        } else {
            return 0;
        }
    }
    
}
