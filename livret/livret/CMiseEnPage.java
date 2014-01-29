/*
 * CMiseEnPage.java
 *
 * Created on 17 mars 2005, 20:20
 */

package livret;

import java.awt.Point;

/**
 * Contient une mise en page totale d'une page double.
 */
public final class CMiseEnPage {
    
    // Taille de la feuille
    public float m_largeurTotale;
    public float m_hauteurTotale;
    // Marges
    public float m_margeExt;
    public float m_margeInt;
    public float m_margeHaut;
    public float m_margeBas;
    // Largeur hauteur des petites pages
    public float m_largeur;
    public float m_hauteur;
    // Marges "internes" à l'interieur de l'espace dispo pour les petites pages.
    // Une des deux est normalement à 0 !
    public float m_dx;
    public float m_dy;
    
    // Coordonnees des points bas gauche des 2 petites pages.
    private CCoord m_A;
    private CCoord m_B;
    
    // Taux de réduction des petites pages
    private float m_taux;
    
    
    /**
     * Construit ue mise en page à partir des données de marges.
     * Calcule donc les "marges internes".
     */
    public CMiseEnPage(float largeurTot, float hauteurTot, float margeExt,
            float margeInt, float margeHaut, float margeBas) {
        
        initValeurs(largeurTot, hauteurTot, margeExt, margeInt, margeHaut, margeBas);
        recalcule();
        
    }
    
    public void recalcule() {
        float largeurA4 = CConfig.largeurA4;
        float hauteurA4 = CConfig.hauteurA4;
        
        m_taux = min(
                (m_largeurTotale / 2f - m_margeInt - m_margeExt) / largeurA4,
                (m_hauteurTotale - m_margeHaut - m_margeBas) / hauteurA4);
        
        m_largeur = m_taux * largeurA4;
        m_hauteur = m_taux * hauteurA4;
        
        m_dy = (m_hauteurTotale - m_margeHaut - m_margeBas - m_hauteur) / 2;
        m_dx = (m_largeurTotale / 2 - m_margeInt - m_margeExt - m_largeur) / 2;
        
        m_A = new CCoord(m_margeExt + m_dx, m_margeBas + m_dy);
        m_B = new CCoord(m_largeurTotale / 2 + m_margeInt + m_dx, m_A.gety());
        
    }
    
    
    public float getZoom() {
        return m_taux;
    }
    
    /**
     *  Renvoie le point bas Gauche de la page 1
     */
    public CCoord getBG1() {
        return m_A;
    }
    
    /**
     *  Renvoie le point bas Gauche de la page 2
     */
    public CCoord getBG2() {
        return m_B;
    }
    
    public float getHauteurPage() {
        return m_hauteur;
    }
    
    public float getLargeurPage() {
        return m_largeur;
    }
    
    /**
     * Renvoie la marge effective
     */
    public float getVraieMargeH() {
        return m_margeHaut + m_dy;
    }
    
    /**
     * Renvoie la marge effective
     */
    public float getVraieMargeB() {
        return m_margeBas + m_dy;
    }
    
    /**
     * Renvoie la marge effective
     */
    public float getVraieMargeE() {
        return m_margeExt + m_dx;
    }
    
    /**
     * Renvoie la marge effective
     */
    public float getVraieMargeI() {
        return m_margeInt + m_dx;
    }
    
    /**
     * Fixe les donnees mambres de la classe.
     */
    private void initValeurs(float largeurTot, float hauteurTot, float margeExt,
            float margeInt, float margeHaut, float margeBas) {
        
        m_largeurTotale = largeurTot;
        m_hauteurTotale = hauteurTot;
        m_margeExt = margeExt;
        m_margeInt = margeInt;
        m_margeHaut = margeHaut;
        m_margeBas = margeBas;
        
    }
    
    
    public void MAJ(float margeExt, float margeInt, float margeHaut, float margeBas) {
        
        initValeurs(m_largeurTotale, m_hauteurTotale, margeExt, margeInt, margeHaut, margeBas);
        recalcule();
        
    }
    
    
    public void MAJ(float largeurTot, float hauteurTot, float margeExt,
            float margeInt, float margeHaut, float margeBas) {
        
        initValeurs(largeurTot, hauteurTot, margeExt, margeInt, margeHaut, margeBas);
        recalcule();
        
    }
    
    
    public String toString() {
        String s="MEP";
        s += " Feuille : (" + m_hauteurTotale + ", " + m_largeurTotale  + ")" + "\n"
                + " Marges : " + m_margeExt + " " + m_margeInt + " " +
                m_margeHaut + " " + m_margeBas + "\n";
        s += " A : " + m_A.toString() + "\n";
        s += " B : " + m_B.toString() + "\n";
        s += " zoom : " + m_taux + "\n";
        return s;
    }
    
    private float min(float x, float y) {
        if (x < y)
            return x;
        else
            return y;
    }
    
    
}
