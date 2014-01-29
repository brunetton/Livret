/*
 * CCoord.java
 *
 * Created on 17 mars 2005, 22:28
 */

package livret;

import java.awt.Dimension;

/**
 *
 * @author bruno DUYE
 */
public class CCoord {
    
    private float m_x;
    private float m_y;
    
    public CCoord(float x, float y) {
        m_x = x;
        m_y = y;
    }
    
    public float getx() {
        return m_x;
    }
    
    public float gety() {
        return m_y;
    }
    
    public void set(float x, float y) {
        m_x = x;
        m_y = y;
    }
    
    public String toString() {
        return "(" + m_x + ", " + m_y + ")";
    }
    
    /**
     * Renvoie une nouvelle instance de CCoord contenant l'addition des deux
     */
    public CCoord plus(CCoord c) {
        return new CCoord(this.getx() + c.getx(), this.gety() + c.gety());
    }
    
    /**
     * Renvoie une nouvelle instance de CCoord contenant l'addition des deux
     */
    public CCoord plus(Dimension d) {
        return new CCoord(this.getx() + (float) d.width, 
                this.gety() + (float) d.height);
    }
    
}
