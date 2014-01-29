/*
 * CPdf.java
 *
 * Created on 18 mars 2005, 11:37
 */

package livret;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CPdf {
    
    private PdfReader m_reader;
    private String m_nomFichierSource;
    private boolean m_fichiersSepares;
    private String m_nomFichierResultatRectos;
    private String m_nomFichierResultatVersos;
    
    public CPdf(String nomFichierSource) {
        m_nomFichierSource = nomFichierSource;
    }
    
    public Boolean ouvrirFichierSource() {
        return ouvrirFichierSource(m_nomFichierSource);
    }
    
    public int getNbPagesSource() {
        return m_reader.getNumberOfPages();
    }
    
    public Boolean ouvrirFichierSource(String nomFichier) {
        m_nomFichierSource = nomFichier;
        Boolean erreur = false;
        // Ouverture du fichier source
        try {
            m_reader = new PdfReader(m_nomFichierSource);
        } catch (IOException e) {
            System.out.println("Erreur : impossible d'ouvrir le fichier " + m_nomFichierSource);
            erreur = true;
        }
        return ! erreur;
    }
    
    /**
     * Génere le livret en un seul fichier
     */
    public void generer(CMiseEnPage miseEnPage,
            CListePages ordrePages, String nomFichierCOmmun) {
        
        m_fichiersSepares = false;
        m_nomFichierResultatRectos = nomFichierCOmmun;
        m_nomFichierResultatVersos = null;
        
        genererLivret(miseEnPage, ordrePages);
    }
    
    /**
     * Génere le livret en deux fichiers séparés
     */
    public void generer(CMiseEnPage miseEnPage,
            CListePages ordrePages, String nomFichierRectos,
            String nomFichiersVersos) {
        
        m_fichiersSepares = true;
        m_nomFichierResultatRectos = nomFichierRectos;
        m_nomFichierResultatVersos = nomFichiersVersos;
        
        genererLivret(miseEnPage, ordrePages);
    }
    
    private void genererLivret(CMiseEnPage miseEnPage,
            CListePages ordrePages) {
        
//        System.out.println("Nom fichier rectos : " + m_nomFichierResultatRectos);
//        System.out.println("Nom fichier rectos : " + m_nomFichierResultatVersos);
        
        Document resultat;
        PdfWriter writer;
        PdfContentByte cb;
        float tauxCmToPixels;
        
//        System.out.println("Génération");
//        System.out.println("Mise en page : " + miseEnPage.toString());
//        System.out.println("Ordre : " + ordrePages.toString());
        
        resultat = new Document((m_reader.getPageSize(1)).rotate());
        writer = null;
        try {
            writer = PdfWriter.getInstance(resultat,
                    new FileOutputStream(m_nomFichierResultatRectos));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
        
        resultat.open();
        cb = writer.getDirectContent();
        tauxCmToPixels = resultat.getPageSize().width() / CConfig.hauteurA4;
        
        ecrirePages(ordrePages, miseEnPage, resultat, cb, writer, tauxCmToPixels,
                1, ordrePages.getTaille() / 4);
        
        // Si il faut générer deux fichiers, on ferme le premier et on ouvre
        // le deuxième
        if (m_fichiersSepares) {
            resultat.close();
            resultat = new Document((m_reader.getPageSize(1)).rotate());
            
            try {
                writer = PdfWriter.getInstance(resultat,
                        new FileOutputStream(m_nomFichierResultatVersos));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-2);
            }
            
            resultat.open();
            cb = writer.getDirectContent();
        }
        
//        System.out.println("- Versos");
        
        ecrirePages(ordrePages, miseEnPage, resultat, cb, writer, tauxCmToPixels,
                ordrePages.getTaille() / 4 + 1, ordrePages.getTaille() / 2);
        
        resultat.close();
        
        
        // TODO : renvoyer une exception au lieu d'afficher un message'
        String s = (m_fichiersSepares) ? "s" : "";
        JOptionPane.showMessageDialog(null, "Fichier" + s + " écrit" + s + ".",
                "Fini", JOptionPane.INFORMATION_MESSAGE);
        
    }
    
    private void ecrirePages(CListePages ordrePages, CMiseEnPage miseEnPage,
            Document resultat, PdfContentByte cb, PdfWriter writer,
            float tauxCmToPixels, int firstPage, int lastPage) {
        
        try {
            CFeuille feuille;
            PdfImportedPage page;
            
            for (int i = firstPage; i <= lastPage; i++) {
//                System.out.println("-  " + Integer.toString(i));
                resultat.newPage();
                // petite page 1
//                System.out.println("feuille : " + ordrePages.getFeuille(i));
                feuille = ordrePages.getFeuille(i);
                if (feuille.getPage1() != 0) {
                    page = writer.getImportedPage(m_reader, feuille.getPage1());
                    cb.addTemplate(page, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
                            miseEnPage.getBG1().getx() * tauxCmToPixels,
                            miseEnPage.getBG1().gety() * tauxCmToPixels);
                }
                // petite page 2
                if (feuille.getPage2() != 0) {
                    page = writer.getImportedPage(m_reader, feuille.getPage2());
                    cb.addTemplate(page, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
                            miseEnPage.getBG2().getx() * tauxCmToPixels,
                            miseEnPage.getBG2().gety() * tauxCmToPixels);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            resultat.close();
            System.exit(-3);
        }
    }
    
    
    public void tests() {
        int pagenumber = 1;
        
        // step 1: creation of a document-object
        Document resultat = new Document((m_reader.getPageSize(1)).rotate());
        //   step 2: we create a writer that listens to the document
        PdfWriter writer = null;
        try {
//            writer = PdfWriter.getInstance(resultat, new FileOutputStream(m_nomFichierResultat));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
        //   step 3: we open the document
        resultat.open();
        PdfContentByte cb1 = writer.getDirectContent();
        PdfImportedPage page;
        System.out.println(resultat.getPageSize().width());
//        m_tauxCmToPixels = resultat.getPageSize().width() / m_largeurReelle;
        try {
            resultat.newPage();
            page = writer.getImportedPage(m_reader, 1);
            float facteur = .5f;
//            cb1.addTemplate(page, facteur, 0, 0, facteur,
//                    5 * m_tauxCmToPixels, 5 * m_tauxCmToPixels);
            
            resultat.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-3);
        }
        
        
        
//        int rotation;
//        int i = 0;
//        // step 4: we add content
//        while (i < pagenumber - 1) {
//            i++;
//            document1.setPageSize(reader.getPageSizeWithRotation(i));
//            document1.newPage();
////                page = writer1.getImportedPage(reader, i);
//            rotation = reader.getPageRotation(i);
//            if (rotation == 90 || rotation == 270) {
////                    cb1.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
//            } else {
////                    cb1.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
//            }
//        }
//        while (i < n) {
//            i++;
//            document2.setPageSize(reader.getPageSizeWithRotation(i));
//            document2.newPage();
////                page = writer2.getImportedPage(reader, i);
//            rotation = reader.getPageRotation(i);
//            if (rotation == 90 || rotation == 270) {
////                    cb2.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
//            } else {
////                    cb2.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
//            }
//            System.out.println("Processed page " + i);
//        }
//        // step 5: we close the document
//        document1.close();
//        document2.close();
    }
    
}
