package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Fournisseur;
import tn.univ.pharmacie.service.RapportService;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RapportsPanel extends JPanel {
    private RapportService rapportService;
    private JTextArea txtRapportStock;
    private JTextArea txtRapportPerformance;
    private JLabel lblChiffreAffaires;
    private JButton btnGenererRapports, btnExporter;

    public RapportsPanel() {
        rapportService = new RapportService();
        initComponents();
        genererRapports();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Chiffre d'Affaires & Buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        lblChiffreAffaires = new JLabel("Chiffre d'Affaires: 0.00 DT");
        lblChiffreAffaires.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(lblChiffreAffaires);

        JPanel buttonPanel = new JPanel();
        btnGenererRapports = new JButton("📊 Générer Rapports");
        btnExporter = new JButton("💾 Exporter");
        btnGenererRapports.addActionListener(e -> genererRapports());
        btnExporter.addActionListener(e -> exporterRapports());
        buttonPanel.add(btnGenererRapports);
        buttonPanel.add(btnExporter);

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center panel - Split pane with two reports
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel stockPanel = new JPanel(new BorderLayout());
        stockPanel.setBorder(BorderFactory.createTitledBorder("📦 État du Stock"));
        txtRapportStock = new JTextArea();
        txtRapportStock.setEditable(false);
        txtRapportStock.setFont(new Font("Monospaced", Font.PLAIN, 11));
        stockPanel.add(new JScrollPane(txtRapportStock), BorderLayout.CENTER);

        JPanel performancePanel = new JPanel(new BorderLayout());
        performancePanel.setBorder(BorderFactory.createTitledBorder("🏭 Performance des Fournisseurs"));
        txtRapportPerformance = new JTextArea();
        txtRapportPerformance.setEditable(false);
        txtRapportPerformance.setFont(new Font("Monospaced", Font.PLAIN, 11));
        performancePanel.add(new JScrollPane(txtRapportPerformance), BorderLayout.CENTER);

        splitPane.setLeftComponent(stockPanel);
        splitPane.setRightComponent(performancePanel);
        splitPane.setDividerLocation(0.5);

        add(splitPane, BorderLayout.CENTER);
    }

    private void genererRapports() {
        try {
            // Etat du stock
            String etatStock = rapportService.genererEtatStock();
            txtRapportStock.setText(etatStock);

            // Chiffre d'affaires
            double ca = rapportService.genererChiffreAffaires();
            lblChiffreAffaires.setText("💰 Chiffre d'Affaires: " + String.format("%.2f", ca) + " DT");

            // Performance des fournisseurs
            String rapportPerf = rapportService.genererRapportPerformanceFournisseursTexte();
            txtRapportPerformance.setText(rapportPerf);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exporterRapports() {
        try {
            StringBuilder rapport = new StringBuilder();
            rapport.append("================== RAPPORT PHARMACIE ==================\n\n");
            rapport.append(lblChiffreAffaires.getText()).append("\n\n");
            rapport.append(txtRapportStock.getText()).append("\n\n");
            rapport.append(txtRapportPerformance.getText()).append("\n");
            rapport.append("======================================================\n");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("rapport_pharmacie.txt"));
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.FileWriter fw = new java.io.FileWriter(file);
                fw.write(rapport.toString());
                fw.close();
                JOptionPane.showMessageDialog(this, "Rapport exporté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
