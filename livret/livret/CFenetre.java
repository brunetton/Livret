/*
 * CFenetre.java
 *
 * Created on 18 mars 2005, 11:46
 */

package livret;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


/**
 *
 * @author  bruno DUYE
 */
public class CFenetre extends javax.swing.JFrame {
    
    public enum TypeRV {RV, RpuisV, RpuisVsepares};
    
    private CMiseEnPage m_miseEnPage;
    private COrdrePages m_ordrePages;
    private CAfficheurHtml m_aide;
    private CAfficheurHtml m_aide_imprimantes;
    private SpinnerNumberModel m_spinnerModel = new SpinnerNumberModel(
            new Float(1), new Float(0), new Float(15), new Float(.5f));
    private CPdf m_pdf;
    private String m_nomFichierSource;            // Sans chemin
    private String m_nomFichierDestCommun;        // Sans chemin
    private String m_cheminFichierSource;
    private String m_cheminFichierDest;
    private String m_repertoireCourant;           // Deriner répertoire selectionné
    private Boolean m_fichierSourceOk = false;
    private Boolean m_fichierDestOk = false;
    private URL m_tmpUrl;
    private CImprimante m_imprimante;
    private boolean m_imprimanteEstRV = false;
    private COptionsManager m_options;
    
    /** Creates new form CFenetre */
    public CFenetre(String fileName) {
        m_miseEnPage = new CMiseEnPage(CConfig.hauteurA4, CConfig.largeurA4, 1f,1f,1f,1f);
        initComponents();
        initSpinner(jSpinnerE);
        initSpinner(jSpinnerI);
        initSpinner(jSpinnerB);
        initSpinner(jSpinnerH);
        MAJMiseEnPage();
        m_pdf = new CPdf(m_nomFichierSource);
        m_options = new COptionsManager();
        lireOptions();
//        m_imprimante = new imprimantes.CImprimanteJetEncre();
//        m_ordrePages = new COrdrePages(0, m_imprimante);
        
        
        // DEBUG
//        m_fichierSourceOk = true;
//        m_fichierDestOk = true;
//        MAJBouttonGenerer();
//        m_nomFichierSource = "10pages.pdf";
//        m_cheminFichierSource = "/home/bruno/";
//        m_nomFichierDestCommun = "res";
//        m_pdf.ouvrirFichierSource(m_cheminFichierSource + m_nomFichierSource);
//        jLabelFSource.setText("Nombre de pages : " + m_pdf.getNbPagesSource());
//        nomFSource.setText(m_nomFichierSource);
//        m_cheminFichierDest = m_cheminFichierSource;
//        MAJLabelFDest();
    }
    
    /**
     * Lit et met à jour l'état des sélections des imprimantes
     */
    private void lireOptions() {
        COptions options = new COptions(0,false);
        if (m_options.lireConfig(options)) {
            switch (options.typeImprimante) {
                case 1:
                    buttonGroup1.setSelected(imprimanteJetEncre.getModel(), true);
                    break;
                case 2:
                    buttonGroup1.setSelected(imprimanteLaser.getModel(), true);
                    break;
                case 3:
                    buttonGroup1.setSelected(imprimantePlat.getModel(), true);
                    break;
                case 4:
                    buttonGroup1.setSelected(imprimantePlatDessous.getModel(), true);
                    break;
                case 5:
                    buttonGroup1.setSelected(imprimanteRV.getModel(), true);
                    break;
                case 6:
                    buttonGroup1.setSelected(imprimanteRVPlate.getModel(), true);
                    break;
            }
            checkBoxGenererDeuxFichiers.setSelected(options.rectoVerso);
            
        }
    }
    
    /**
     * Met à jour la mise en page
     */
    private void MAJMiseEnPage() {
        m_miseEnPage.MAJ(
                (Float) jSpinnerE.getValue(), (Float) jSpinnerI.getValue(),
                (Float) jSpinnerH.getValue(), (Float) jSpinnerB.getValue());
        m_widget.repaint();
        lB.setText(affichFloat(m_miseEnPage.getVraieMargeB()));
        lH.setText(affichFloat(m_miseEnPage.getVraieMargeH()));
        lI.setText(affichFloat(m_miseEnPage.getVraieMargeI()));
        lE.setText(affichFloat(m_miseEnPage.getVraieMargeE()));
//        System.out.println("Fenetre : ");
//        System.out.println(m_miseEnPage.toString());
    }
    
    /**
     * Mets à jour l'état du boutton générer
     */
    private void MAJBouttonGenerer() {
        if (m_fichierDestOk && m_fichierSourceOk) {
            bouttonGenerer.setEnabled(true);
        } else {
            bouttonGenerer.setEnabled(false);
        }
    }
    
    private String affichFloat(float f) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(f);
    }
    
    private void initSpinner(JSpinner s) {
        s.setValue(new Float(1));
    }
    
    private String enleverExtension(String s) {
        return s.split("\\.")[0];
    }
    
    private void selectionFichierSource() {
        JFileChooser fileChooser;
//        if (m_repertoireCourant == "") {
//            fileChooser = new javax.swing.JFileChooser(CConfig.cheminHome);
//        } else {
//            fileChooser = new JFileChooser(m_repertoireCourant);
//        }
        fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Fichier source ...");
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            m_repertoireCourant = fichier.getParent() + "/";
            String nomFichierSource = fichier.getAbsolutePath();
            ouvrirFichierSource(fichier.getName());
        }
    }
    
    private void ouvrirFichierSource(String nomFichierSource) {
        if (m_pdf.ouvrirFichierSource(m_repertoireCourant + nomFichierSource) == true) {
            m_nomFichierSource = nomFichierSource;
            m_cheminFichierSource = m_repertoireCourant;
            jLabelFSource.setText("Nombre de pages : " + m_pdf.getNbPagesSource());
            nomFSource.setText(nomFichierSource);
            m_fichierSourceOk = true;
            m_fichierDestOk = true;
            MAJBouttonGenerer();
            m_cheminFichierDest = m_repertoireCourant;
            m_nomFichierDestCommun =  enleverExtension(m_nomFichierSource);
            MAJLabelFDest();
        } else {
            // Erreur ouverture
            jLabelFSource.setText("Erreur de chargment !");
            nomFSource.setText("");
            m_fichierSourceOk = false;
            MAJLabelFDest();
            MAJBouttonGenerer();
        }
    }
    
    /**
     * Mets à jour l'affichage du nom et du nombre de pages du fichier destination
     * ainsi que le boutton de sélection du fichier destination
     * en fonction du type de recto verso demandé
     */
    private void MAJLabelFDest() {
        if (m_fichierSourceOk) {
            bouttonChangerNomDest.setEnabled(true);
            if (m_imprimanteEstRV || ! checkBoxGenererDeuxFichiers.isSelected()) {
                if (m_nomFichierDestCommun.length() < 30) {
                    jLabelFDest.setText("fichier destination : "
                            + m_nomFichierDestCommun + CConfig.suffixeRV + " ("
                            + COrdrePages.nbFeuilles(m_pdf.getNbPagesSource())
                            + " feuilles recto-verso)");
                } else {
                    jLabelFDest.setText("fichier destination : [...]-livret.pdf" + " ("
                            + COrdrePages.nbFeuilles(m_pdf.getNbPagesSource())
                            + " feuilles recto-verso)");
                }
            } else {
                if (m_nomFichierDestCommun.length() < 12) {
                    jLabelFDest.setText("fichiers destinations : "
                            + m_nomFichierDestCommun + CConfig.suffixeRectos + " et "
                            + m_nomFichierDestCommun + CConfig.suffixeVersos + " ("
                            + COrdrePages.nbFeuilles(m_pdf.getNbPagesSource())
                            + " pages chacun)");
                } else {
                    jLabelFDest.setText("fichiers destinations : [...]"
                            + CConfig.suffixeRectos + " et [....]"
                            + CConfig.suffixeVersos + " ("
                            + COrdrePages.nbFeuilles(m_pdf.getNbPagesSource())
                            + " pages chacun)");
                }
            }
        } else {
            jLabelFDest.setText("fichier destination : non sélectionné");
            bouttonChangerNomDest.setEnabled(false);
        }
    }
    
    private void selectionFichierDest() {
        JFileChooser fileChooser;
        if (m_repertoireCourant == "") {
            fileChooser = new JFileChooser(CConfig.cheminHome);
        } else {
            fileChooser = new JFileChooser(m_repertoireCourant);
        }
        fileChooser.setDialogTitle("Fichier destination ...");
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichierDest = fileChooser.getSelectedFile();
            m_repertoireCourant = fichierDest.getParent() + "/";
            m_cheminFichierDest = m_repertoireCourant;
            m_nomFichierDestCommun = fichierDest.getName();
            m_fichierDestOk = true;
            MAJLabelFDest();
            MAJBouttonGenerer();
        } else {
            // User annule
            m_fichierDestOk = false;
            MAJBouttonGenerer();
        }
        
    }
    
    private void generer() {
        
        CImprimante imprimanteTmp = null;
        
        if (buttonGroup1.isSelected(imprimanteJetEncre.getModel())) {
            imprimanteTmp = new livret.imprimantes.CImprimanteJetEncre();
        }
        if (buttonGroup1.isSelected(imprimanteLaser.getModel())) {
            imprimanteTmp = new livret.imprimantes.CImprimanteLaser();
        }
        if (buttonGroup1.isSelected(imprimantePlat.getModel())) {
            imprimanteTmp = new livret.imprimantes.CImprimantePlate();
        }
        if (buttonGroup1.isSelected(imprimantePlatDessous.getModel())) {
            imprimanteTmp = new livret.imprimantes.CImprimantePlateDessous();
        }
        if (buttonGroup1.isSelected(imprimanteRV.getModel())) {
            imprimanteTmp = new livret.imprimantes.CImprimanteRV();
        }
        if (buttonGroup1.isSelected(imprimanteRVPlate.getModel())) {
            imprimanteTmp = new livret.imprimantes.CImprimanteRVsansRetourn();
        }
        
        
        if (m_imprimanteEstRV || ! checkBoxGenererDeuxFichiers.isSelected()) {
            System.out.println("UN SEUL");
            m_pdf.generer(m_miseEnPage,
                    new COrdrePages(m_pdf.getNbPagesSource(), imprimanteTmp).getOrdreMiniPages(),
                    m_cheminFichierDest + m_nomFichierDestCommun + CConfig.suffixeRV);
        } else {
            System.out.println("DEUX");
            m_pdf.generer(m_miseEnPage,
                    new COrdrePages(m_pdf.getNbPagesSource(), imprimanteTmp).getOrdreMiniPages(),
                    m_cheminFichierDest + m_nomFichierDestCommun + CConfig.suffixeRectos,
                    m_cheminFichierDest + m_nomFichierDestCommun + CConfig.suffixeVersos);
        }
    }
    
    private void changerSpinnerValue(JSpinner spinner, java.awt.event.MouseWheelEvent evt) {
        if ((evt.getWheelRotation() < 0)) {
            if (spinner.getModel().getNextValue() != null)
                spinner.setValue(spinner.getNextValue());
        } else if ((Float) spinner.getValue() > 0) {
            spinner.setValue(spinner.getPreviousValue());
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        panelHaut = new javax.swing.JPanel();
        panelMarges = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerH = new javax.swing.JSpinner();
        jSpinnerE = new javax.swing.JSpinner();
        jSpinnerI = new javax.swing.JSpinner();
        jSpinnerB = new javax.swing.JSpinner();
        lH = new javax.swing.JLabel();
        lB = new javax.swing.JLabel();
        lI = new javax.swing.JLabel();
        lE = new javax.swing.JLabel();
        m_widget = new CWPreviewMEP(m_miseEnPage);
        panelFSource = new javax.swing.JPanel();
        nomFSource = new javax.swing.JTextField();
        bouttonParcourirSource = new javax.swing.JButton();
        jLabelFSource = new javax.swing.JLabel();
        panelOptions = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bouttonChangerNomDest = new javax.swing.JButton();
        jLabelFDest = new javax.swing.JLabel();
        imprimanteJetEncre = new javax.swing.JRadioButton();
        imprimanteLaser = new javax.swing.JRadioButton();
        imprimantePlat = new javax.swing.JRadioButton();
        jLabelFDest1 = new javax.swing.JLabel();
        jLabelFDest2 = new javax.swing.JLabel();
        imprimantePlatDessous = new javax.swing.JRadioButton();
        checkBoxGenererDeuxFichiers = new javax.swing.JCheckBox();
        imprimanteRVPlate = new javax.swing.JRadioButton();
        imprimanteRV = new javax.swing.JRadioButton();
        bouttonEnregistrerConfig = new javax.swing.JButton();
        bouttonAideImprimantes = new javax.swing.JButton();
        panelBas = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        bouttonGenerer = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Livret");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelHaut.setLayout(new java.awt.GridBagLayout());

        panelMarges.setLayout(new java.awt.GridBagLayout());

        panelMarges.setBorder(javax.swing.BorderFactory.createTitledBorder("Marges"));
        panelMarges.setMinimumSize(new java.awt.Dimension(160, 96));
        panelMarges.setPreferredSize(new java.awt.Dimension(180, 104));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel1.setText("Haut");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel2.setText("Bas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel3.setText("Interne");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel4.setText("Externe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(jLabel4, gridBagConstraints);

        jSpinnerH.setFont(new java.awt.Font("Dialog", 0, 10));
        jSpinnerH.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerH.setName("");
        jSpinnerH.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerHStateChanged(evt);
            }
        });
        jSpinnerH.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jSpinnerHMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        panelMarges.add(jSpinnerH, gridBagConstraints);

        jSpinnerE.setFont(new java.awt.Font("Dialog", 0, 10));
        jSpinnerE.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerE.setName("");
        jSpinnerE.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerE.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerEStateChanged(evt);
            }
        });
        jSpinnerE.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jSpinnerEMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        panelMarges.add(jSpinnerE, gridBagConstraints);

        jSpinnerI.setFont(new java.awt.Font("Dialog", 0, 10));
        jSpinnerI.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerI.setName("");
        jSpinnerI.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerI.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerIStateChanged(evt);
            }
        });
        jSpinnerI.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jSpinnerIMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        panelMarges.add(jSpinnerI, gridBagConstraints);

        jSpinnerB.setFont(new java.awt.Font("Dialog", 0, 10));
        jSpinnerB.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerB.setName("");
        jSpinnerB.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerBStateChanged(evt);
            }
        });
        jSpinnerB.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jSpinnerBMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        panelMarges.add(jSpinnerB, gridBagConstraints);

        lH.setFont(new java.awt.Font("Dialog", 0, 10));
        lH.setText("Haut");
        lH.setMinimumSize(new java.awt.Dimension(30, 13));
        lH.setPreferredSize(new java.awt.Dimension(80, 13));
        lH.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                lHMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(lH, gridBagConstraints);

        lB.setFont(new java.awt.Font("Dialog", 0, 10));
        lB.setText("Bas");
        lB.setMinimumSize(new java.awt.Dimension(30, 13));
        lB.setPreferredSize(new java.awt.Dimension(80, 13));
        lB.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                lBMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(lB, gridBagConstraints);

        lI.setFont(new java.awt.Font("Dialog", 0, 10));
        lI.setText("Interne");
        lI.setMinimumSize(new java.awt.Dimension(30, 13));
        lI.setPreferredSize(new java.awt.Dimension(80, 13));
        lI.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                lIMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(lI, gridBagConstraints);

        lE.setFont(new java.awt.Font("Dialog", 0, 10));
        lE.setText("Externe");
        lE.setMinimumSize(new java.awt.Dimension(30, 13));
        lE.setPreferredSize(new java.awt.Dimension(80, 13));
        lE.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                lEMouseWheelMoved(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelMarges.add(lE, gridBagConstraints);

        panelHaut.add(panelMarges, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelHaut.add(m_widget, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(panelHaut, gridBagConstraints);

        panelFSource.setLayout(new java.awt.GridBagLayout());

        panelFSource.setBorder(javax.swing.BorderFactory.createTitledBorder("Fichier source"));
        panelFSource.setMinimumSize(new java.awt.Dimension(500, 80));
        panelFSource.setPreferredSize(new java.awt.Dimension(500, 80));
        nomFSource.setEditable(false);
        nomFSource.setFont(new java.awt.Font("Dialog", 0, 10));
        nomFSource.setMinimumSize(new java.awt.Dimension(200, 19));
        nomFSource.setPreferredSize(new java.awt.Dimension(400, 19));
        panelFSource.add(nomFSource, new java.awt.GridBagConstraints());

        bouttonParcourirSource.setText("...");
        bouttonParcourirSource.setBorderPainted(false);
        bouttonParcourirSource.setContentAreaFilled(false);
        bouttonParcourirSource.setMaximumSize(new java.awt.Dimension(19, 19));
        bouttonParcourirSource.setMinimumSize(new java.awt.Dimension(19, 19));
        bouttonParcourirSource.setPreferredSize(new java.awt.Dimension(19, 19));
        m_tmpUrl = getClass().getResource("icones/open.png");
        if (m_tmpUrl != null) {
            bouttonParcourirSource.setText("");
            bouttonParcourirSource.setIcon(new ImageIcon(m_tmpUrl));
        }

        bouttonParcourirSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonParcourirSourceActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panelFSource.add(bouttonParcourirSource, gridBagConstraints);

        jLabelFSource.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabelFSource.setText("Fichier non charg\u00e9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        panelFSource.add(jLabelFSource, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(panelFSource, gridBagConstraints);

        panelOptions.setLayout(new java.awt.GridBagLayout());

        panelOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Options"))));
        setTitle("Livret " + CConfig.version);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        bouttonChangerNomDest.setText("...");
        bouttonChangerNomDest.setBorderPainted(false);
        bouttonChangerNomDest.setContentAreaFilled(false);
        bouttonChangerNomDest.setEnabled(false);
        bouttonChangerNomDest.setMaximumSize(new java.awt.Dimension(19, 19));
        bouttonChangerNomDest.setMinimumSize(new java.awt.Dimension(19, 19));
        bouttonChangerNomDest.setPreferredSize(new java.awt.Dimension(19, 19));
        m_tmpUrl = getClass().getResource("icones/open.png");
        if (m_tmpUrl != null) {
            bouttonChangerNomDest.setText("");
            bouttonChangerNomDest.setIcon(new ImageIcon(m_tmpUrl));
        }

        bouttonChangerNomDest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonChangerNomDestActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 5, 0);
        jPanel2.add(bouttonChangerNomDest, gridBagConstraints);

        jLabelFDest.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabelFDest.setText("fichier destination : non s\u00e9lectionn\u00e9");
        jLabelFDest.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabelFDest.setMaximumSize(new java.awt.Dimension(580, 50));
        jLabelFDest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelFDestMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel2.add(jLabelFDest, gridBagConstraints);

        buttonGroup1.add(imprimanteJetEncre);
        imprimanteJetEncre.setFont(new java.awt.Font("Dialog", 0, 10));
        imprimanteJetEncre.setSelected(true);
        imprimanteJetEncre.setText("jet d'encre");
        imprimanteJetEncre.setToolTipText("");
        imprimanteJetEncre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imprimanteJetEncre.setMargin(new java.awt.Insets(0, 0, 0, 0));
        imprimanteJetEncre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimanteJetEncreActionPerformed(evt);
            }
        });
        imprimanteJetEncre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                imprimanteJetEncreStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel2.add(imprimanteJetEncre, gridBagConstraints);

        buttonGroup1.add(imprimanteLaser);
        imprimanteLaser.setFont(new java.awt.Font("Dialog", 0, 10));
        imprimanteLaser.setText("laser");
        imprimanteLaser.setToolTipText("");
        imprimanteLaser.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imprimanteLaser.setMargin(new java.awt.Insets(0, 0, 0, 0));
        imprimanteLaser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimanteLaserActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        jPanel2.add(imprimanteLaser, gridBagConstraints);

        buttonGroup1.add(imprimantePlat);
        imprimantePlat.setFont(new java.awt.Font("Dialog", 0, 10));
        imprimantePlat.setText("imprimante \u00e0 plat");
        imprimantePlat.setToolTipText("");
        imprimantePlat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imprimantePlat.setMargin(new java.awt.Insets(0, 0, 0, 0));
        imprimantePlat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimantePlatActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        jPanel2.add(imprimantePlat, gridBagConstraints);

        jLabelFDest1.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabelFDest1.setText("imprimante non recto-verso");
        jLabelFDest1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabelFDest1.setMaximumSize(new java.awt.Dimension(580, 50));
        jLabelFDest1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelFDest1MouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel2.add(jLabelFDest1, gridBagConstraints);

        jLabelFDest2.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabelFDest2.setText("imprimante recto-verso");
        jLabelFDest2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabelFDest2.setMaximumSize(new java.awt.Dimension(580, 50));
        jLabelFDest2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelFDest2MouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 4, 0);
        jPanel2.add(jLabelFDest2, gridBagConstraints);

        buttonGroup1.add(imprimantePlatDessous);
        imprimantePlatDessous.setFont(new java.awt.Font("Dialog", 0, 10));
        imprimantePlatDessous.setText("imprimante \u00e0 plat avec t\u00eates d'impression sous la feuille");
        imprimantePlatDessous.setToolTipText("");
        imprimantePlatDessous.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imprimantePlatDessous.setMargin(new java.awt.Insets(0, 0, 0, 0));
        imprimantePlatDessous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimantePlatDessousActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        jPanel2.add(imprimantePlatDessous, gridBagConstraints);

        checkBoxGenererDeuxFichiers.setFont(new java.awt.Font("Dialog", 0, 10));
        checkBoxGenererDeuxFichiers.setSelected(true);
        checkBoxGenererDeuxFichiers.setText("g\u00e9n\u00e9rer deux fichiers s\u00e9par\u00e9s");
        checkBoxGenererDeuxFichiers.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBoxGenererDeuxFichiers.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkBoxGenererDeuxFichiers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxGenererDeuxFichiersActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        jPanel2.add(checkBoxGenererDeuxFichiers, gridBagConstraints);

        buttonGroup1.add(imprimanteRVPlate);
        imprimanteRVPlate.setFont(new java.awt.Font("Dialog", 0, 10));
        imprimanteRVPlate.setText("imprimante recto-verso ne retournant pas les feuilles");
        imprimanteRVPlate.setToolTipText("");
        imprimanteRVPlate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imprimanteRVPlate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        imprimanteRVPlate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimanteRVPlateActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        jPanel2.add(imprimanteRVPlate, gridBagConstraints);

        buttonGroup1.add(imprimanteRV);
        imprimanteRV.setFont(new java.awt.Font("Dialog", 0, 10));
        imprimanteRV.setText("imprimante recto-verso retournant les feuilles");
        imprimanteRV.setToolTipText("");
        imprimanteRV.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imprimanteRV.setMargin(new java.awt.Insets(0, 0, 0, 0));
        imprimanteRV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimanteRVActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        jPanel2.add(imprimanteRV, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panelOptions.add(jPanel2, gridBagConstraints);

        bouttonEnregistrerConfig.setFont(new java.awt.Font("Dialog", 0, 10));
        bouttonEnregistrerConfig.setText("Sauver config r/v");
        bouttonEnregistrerConfig.setAlignmentX(0.5F);
        bouttonEnregistrerConfig.setIconTextGap(8);
        bouttonEnregistrerConfig.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bouttonEnregistrerConfig.setMaximumSize(new java.awt.Dimension(200, 19));
        bouttonEnregistrerConfig.setMinimumSize(new java.awt.Dimension(19, 19));
        bouttonEnregistrerConfig.setPreferredSize(new java.awt.Dimension(120, 19));
        m_tmpUrl = getClass().getResource("icones/save.png");
        if (m_tmpUrl != null) {
            bouttonEnregistrerConfig.setIcon(new ImageIcon(m_tmpUrl));
        }

        bouttonEnregistrerConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonEnregistrerConfigActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panelOptions.add(bouttonEnregistrerConfig, gridBagConstraints);

        bouttonAideImprimantes.setFont(new java.awt.Font("Dialog", 0, 10));
        bouttonAideImprimantes.setText("Aide sur les imprimantes");
        bouttonAideImprimantes.setAlignmentX(0.5F);
        bouttonAideImprimantes.setIconTextGap(8);
        bouttonAideImprimantes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bouttonAideImprimantes.setMaximumSize(new java.awt.Dimension(300, 19));
        bouttonAideImprimantes.setMinimumSize(new java.awt.Dimension(19, 19));
        bouttonAideImprimantes.setPreferredSize(new java.awt.Dimension(180, 19));
        m_tmpUrl = getClass().getResource("icones/help.png");
        if (m_tmpUrl != null) {
            bouttonAideImprimantes.setIcon(new ImageIcon(m_tmpUrl));
        }

        bouttonAideImprimantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonAideImprimantesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 31, 0, 0);
        panelOptions.add(bouttonAideImprimantes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(panelOptions, gridBagConstraints);

        panelBas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        jButton1.setFont(new java.awt.Font("Dialog", 0, 12));
        jButton1.setText("\u00c0 propos ...");
        m_tmpUrl = getClass().getResource("icones/about.png");
        if (m_tmpUrl != null) {
            jButton1.setIcon(new ImageIcon(m_tmpUrl));
        }

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        panelBas.add(jButton1);

        bouttonGenerer.setText("Generer !");
        bouttonGenerer.setEnabled(false);
        m_tmpUrl = getClass().getResource("icones/ok.png");
        if (m_tmpUrl != null) {
            bouttonGenerer.setIcon(new ImageIcon(m_tmpUrl));
        }

        bouttonGenerer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonGenererActionPerformed(evt);
            }
        });

        panelBas.add(bouttonGenerer);

        jButton2.setFont(new java.awt.Font("Dialog", 0, 12));
        jButton2.setText("Quitter");
        m_tmpUrl = getClass().getResource("icones/about.png");
        if (m_tmpUrl != null) {
            jButton1.setIcon(new ImageIcon(m_tmpUrl));
        }

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        panelBas.add(jButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(panelBas, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void lEMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_lEMouseWheelMoved
        changerSpinnerValue(jSpinnerE, evt);
    }//GEN-LAST:event_lEMouseWheelMoved
    
    private void lIMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_lIMouseWheelMoved
        changerSpinnerValue(jSpinnerI, evt);
    }//GEN-LAST:event_lIMouseWheelMoved
    
    private void lBMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_lBMouseWheelMoved
        changerSpinnerValue(jSpinnerB, evt);
    }//GEN-LAST:event_lBMouseWheelMoved
    
    private void lHMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_lHMouseWheelMoved
        changerSpinnerValue(jSpinnerH, evt);
    }//GEN-LAST:event_lHMouseWheelMoved
    
    private void jSpinnerEMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jSpinnerEMouseWheelMoved
        changerSpinnerValue(jSpinnerE, evt);
    }//GEN-LAST:event_jSpinnerEMouseWheelMoved
    
    private void jSpinnerIMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jSpinnerIMouseWheelMoved
        changerSpinnerValue(jSpinnerI, evt);
    }//GEN-LAST:event_jSpinnerIMouseWheelMoved
    
    private void jSpinnerBMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jSpinnerBMouseWheelMoved
        changerSpinnerValue(jSpinnerB, evt);
    }//GEN-LAST:event_jSpinnerBMouseWheelMoved
    
    private void jSpinnerHMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jSpinnerHMouseWheelMoved
        changerSpinnerValue(jSpinnerH, evt);
    }//GEN-LAST:event_jSpinnerHMouseWheelMoved
    
    private void checkBoxGenererDeuxFichiersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxGenererDeuxFichiersActionPerformed
        MAJLabelFDest();
    }//GEN-LAST:event_checkBoxGenererDeuxFichiersActionPerformed
    
    private void bouttonAideImprimantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonAideImprimantesActionPerformed
        if (m_aide_imprimantes == null) {
            m_aide_imprimantes = new CAfficheurHtml(
                    CConfig.cheminRessourceAide + CConfig.fichierAideImprimantes,
                    600, 700);
        }
        
        m_aide_imprimantes.setVisible(true);
        
    }//GEN-LAST:event_bouttonAideImprimantesActionPerformed
    
    private void bouttonEnregistrerConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonEnregistrerConfigActionPerformed
        int typeImprimanteTmp = 1;
        
        if (buttonGroup1.isSelected(imprimanteJetEncre.getModel())) {
            typeImprimanteTmp = 1;
        }
        if (buttonGroup1.isSelected(imprimanteLaser.getModel())) {
            typeImprimanteTmp = 2;
        }
        if (buttonGroup1.isSelected(imprimantePlat.getModel())) {
            typeImprimanteTmp = 3;
        }
        if (buttonGroup1.isSelected(imprimantePlatDessous.getModel())) {
            typeImprimanteTmp = 4;
        }
        if (buttonGroup1.isSelected(imprimanteRV.getModel())) {
            typeImprimanteTmp = 5;
        }
        if (buttonGroup1.isSelected(imprimanteRVPlate.getModel())) {
            typeImprimanteTmp = 6;
        }
        
        m_options.enregistrerConfig(new COptions(typeImprimanteTmp,
                checkBoxGenererDeuxFichiers.isSelected()));
        
    }//GEN-LAST:event_bouttonEnregistrerConfigActionPerformed
    
    private void imprimanteRVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimanteRVActionPerformed
        m_imprimanteEstRV = true;
        MAJLabelFDest();
    }//GEN-LAST:event_imprimanteRVActionPerformed
    
    private void imprimanteRVPlateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimanteRVPlateActionPerformed
        m_imprimanteEstRV = true;
        MAJLabelFDest();
    }//GEN-LAST:event_imprimanteRVPlateActionPerformed
    
    private void imprimantePlatDessousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimantePlatDessousActionPerformed
        m_imprimanteEstRV = false;
        MAJLabelFDest();
    }//GEN-LAST:event_imprimantePlatDessousActionPerformed
    
    private void jLabelFDest2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelFDest2MouseClicked
// TODO add your handling code here:
    }//GEN-LAST:event_jLabelFDest2MouseClicked
    
    private void jLabelFDest1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelFDest1MouseClicked
// TODO add your handling code here:
    }//GEN-LAST:event_jLabelFDest1MouseClicked
    
    private void jLabelFDestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelFDestMouseClicked
        if ((evt.getClickCount() == 2) && m_fichierSourceOk) {
            selectionFichierDest();
        }
    }//GEN-LAST:event_jLabelFDestMouseClicked
    
    private void imprimanteJetEncreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_imprimanteJetEncreStateChanged
        
    }//GEN-LAST:event_imprimanteJetEncreStateChanged
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void imprimantePlatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimantePlatActionPerformed
        m_imprimanteEstRV = false;
        MAJLabelFDest();
    }//GEN-LAST:event_imprimantePlatActionPerformed
    
    private void imprimanteLaserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimanteLaserActionPerformed
        m_imprimanteEstRV = false;
        MAJLabelFDest();
    }//GEN-LAST:event_imprimanteLaserActionPerformed
    
    private void imprimanteJetEncreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimanteJetEncreActionPerformed
        m_imprimanteEstRV = false;
        MAJLabelFDest();
    }//GEN-LAST:event_imprimanteJetEncreActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (m_aide == null) {
            m_aide = new CAfficheurHtml(CConfig.cheminRessourceAide 
                    + CConfig.fichierAPropos);
        }
        
        m_aide.setVisible(true);
        
//        try {
//            JOptionPane.showMessageDialog(null,
//                    new URL("file:" + CConfig.nomFichierAide).getUserInfo(), "Aide",
//                            JOptionPane.INFORMATION_MESSAGE);
//        } catch (Exception e) { System.err.println(e);}
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void bouttonGenererActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonGenererActionPerformed
        generer();
    }//GEN-LAST:event_bouttonGenererActionPerformed
    
    private void bouttonChangerNomDestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonChangerNomDestActionPerformed
        selectionFichierDest();
    }//GEN-LAST:event_bouttonChangerNomDestActionPerformed
    
    private void bouttonParcourirSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonParcourirSourceActionPerformed
        selectionFichierSource();
    }//GEN-LAST:event_bouttonParcourirSourceActionPerformed
    
    private void jSpinnerEStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerEStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerEStateChanged
    
    private void jSpinnerIStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerIStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerIStateChanged
    
    private void jSpinnerBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerBStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerBStateChanged
    
    private void jSpinnerHStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerHStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerHStateChanged
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bouttonAideImprimantes;
    private javax.swing.JButton bouttonChangerNomDest;
    private javax.swing.JButton bouttonEnregistrerConfig;
    private javax.swing.JButton bouttonGenerer;
    private javax.swing.JButton bouttonParcourirSource;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox checkBoxGenererDeuxFichiers;
    private javax.swing.JRadioButton imprimanteJetEncre;
    private javax.swing.JRadioButton imprimanteLaser;
    private javax.swing.JRadioButton imprimantePlat;
    private javax.swing.JRadioButton imprimantePlatDessous;
    private javax.swing.JRadioButton imprimanteRV;
    private javax.swing.JRadioButton imprimanteRVPlate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelFDest;
    private javax.swing.JLabel jLabelFDest1;
    private javax.swing.JLabel jLabelFDest2;
    private javax.swing.JLabel jLabelFSource;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinnerB;
    private javax.swing.JSpinner jSpinnerE;
    private javax.swing.JSpinner jSpinnerH;
    private javax.swing.JSpinner jSpinnerI;
    private javax.swing.JLabel lB;
    private javax.swing.JLabel lE;
    private javax.swing.JLabel lH;
    private javax.swing.JLabel lI;
    private livret.CWPreviewMEP m_widget;
    private javax.swing.JTextField nomFSource;
    private javax.swing.JPanel panelBas;
    private javax.swing.JPanel panelFSource;
    private javax.swing.JPanel panelHaut;
    private javax.swing.JPanel panelMarges;
    private javax.swing.JPanel panelOptions;
    // End of variables declaration//GEN-END:variables
    
}
