/*
 * CWPreviewMEP.java
 *
 * Created on 18 mars 2005, 11:48
 */

package livret;

import com.lowagie.text.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public class CWPreviewMEP extends JComponent {
    
    private CMiseEnPage m_miseEnPage;
    private CCoord m_tailleA4 = new CCoord(CConfig.largeurA4, CConfig.hauteurA4);
    // Décalage
    private Dimension m_decalage;
    private float m_zoom;
    
    int m_longeurWidget;
    int m_hauteurWidget;
    
    /**
     * @deprecated Pour en faire un bean.
     */
    public CWPreviewMEP() {
        this(null);
    }
    
    public CWPreviewMEP(CMiseEnPage miseEnPage) {
        m_miseEnPage = miseEnPage;
        setPreferredSize(new Dimension(200,170));
    }
    
    protected void paintComponent(java.awt.Graphics g) {
        
        m_longeurWidget = getWidth() - 1;
        m_hauteurWidget = getHeight() - 1;
        
        // Pour le bean ...
        if (m_miseEnPage == null) {
            g.setColor(new Color(.3f,.7f,0));
            g.drawRect(1, 1, m_longeurWidget - 1, m_hauteurWidget - 1);
            return;
        }
        // Calcul du décalage et du facteur de zoom
        {
            int longeur;
            int hauteur;
            // Attention : la page est retournée, d'où le longeurWidget / y !
            m_zoom = Math.min(m_longeurWidget / m_tailleA4.gety(),
                    m_hauteurWidget / m_tailleA4.getx());
            longeur = (int) (m_zoom * m_tailleA4.gety());
            hauteur = (int) (m_zoom * m_tailleA4.getx());
            m_decalage = new Dimension((int) (m_longeurWidget - longeur) / 2,
                    (int) (m_hauteurWidget - hauteur) / 2);
        }
        
        // Dessine la feuille
        g.setColor(new Color(.3f,.7f,0));
        g.drawRect((int) m_decalage.getWidth(), (int) m_decalage.getHeight(),
                (int) (m_tailleA4.gety() * m_zoom), (int) (m_tailleA4.getx() * m_zoom));
        g.setColor(new Color(0,0,0));
        
        dessinerPage(g, m_miseEnPage.getBG1());
        dessinerPage(g, m_miseEnPage.getBG2());
        
        g.dispose();
    }
    
    
    /**
     * Dessine une feuille donné à bg=(x,y) cm (bas gauche) avec le zoom f
     */
    private void dessinerPage(Graphics g, CCoord bg) {
        int x = m_decalage.width + (int) (bg.getx() * m_zoom);
        int y =  (int) m_hauteurWidget - m_decalage.height
                - (int) (bg.gety() * m_zoom)
                - (int) (m_miseEnPage.getHauteurPage() * m_zoom) - 1;
        int largeur = (int) (m_miseEnPage.getLargeurPage() * m_zoom);
        int hauteur = (int) (m_miseEnPage.getHauteurPage() * m_zoom);
        
        g.drawRect(x, y, largeur, hauteur);
    }
    
}
