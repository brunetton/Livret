/*
 * CFeuille.java
 *
 * Created on 17 mars 2005, 23:56
 */

package livret;

public class CFeuille {
    
    private Integer m_page1;
    private Integer m_page2;
    
    public CFeuille(int page1, int page2) {
        m_page1 = page1;
        m_page2 = page2;
    }
    
    public Integer getPage1() {
        return m_page1;
    }
    
    public Integer getPage2() {
        return m_page2;
    }
    
    public String toString() {
        return "[" + m_page1.toString() + ", " + m_page2.toString() + "]";
    }
    
}
