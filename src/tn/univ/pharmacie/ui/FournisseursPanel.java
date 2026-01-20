package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Fournisseur;
import tn.univ.pharmacie.service.GestionFournisseurs;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FournisseursPanel extends JPanel {
    private GestionFournisseurs gestionFournisseurs;
    private JTable fournisseursTable;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtTelephone, txtAdresse;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRafraichir;
    private JTextArea txtPerformance;

    public FournisseursPanel() {
        gestionFournisseurs = new GestionFournisseurs();
        initComponents();
        rafraichirTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Téléphone", "Adresse"}, 0);
        fournisseursTable = new JTable(tableModel);
        tablePanel.add(new JScrollPane(fournisseursTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // South panel - Performance
        JPanel perfPanel = new JPanel(new BorderLayout());
        perfPanel.setBorder(BorderFactory.createTitledBorder("Performance des Fournisseurs"));
        txtPerformance = new JTextArea(5, 50);
        txtPerformance.setEditable(false);
        perfPanel.add(new JScrollPane(txtPerformance), BorderLayout.CENTER);
        add(perfPanel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        btnAjouter = new JButton("➕ Ajouter");
        btnModifier = new JButton("✏️ Modifier");
        btnSupprimer = new JButton("🗑️ Supprimer");
        btnRafraichir = new JButton("🔄 Rafraîchir");

        btnAjouter.addActionListener(e -> ajouterFournisseur());
        btnModifier.addActionListener(e -> modifierFournisseur());
        btnSupprimer.addActionListener(e -> supprimerFournisseur());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRafraichir);

        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter/Modifier Fournisseur"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNom = new JTextField(15);
        txtTelephone = new JTextField(15);
        txtAdresse = new JTextField(25);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNom, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 3;
        panel.add(txtTelephone, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(txtAdresse, gbc);

        return panel;
    }

    private void ajouterFournisseur() {
        try {
            String nom = txtNom.getText();
            int telephone = Integer.parseInt(txtTelephone.getText());
            String adresse = txtAdresse.getText();

            Fournisseur f = new Fournisseur();
            f.setNom(nom);
            f.setTelephone(telephone);
            f.setAdresse(adresse);

            gestionFournisseurs.ajouterFournisseur(f);
            JOptionPane.showMessageDialog(this, "Fournisseur ajouté!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierFournisseur() {
        int row = fournisseursTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fournisseur", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String nom = txtNom.getText();
            int telephone = Integer.parseInt(txtTelephone.getText());
            String adresse = txtAdresse.getText();

            Fournisseur f = new Fournisseur();
            f.setId(id);
            f.setNom(nom);
            f.setTelephone(telephone);
            f.setAdresse(adresse);

            gestionFournisseurs.modifierFournisseur(f);
            JOptionPane.showMessageDialog(this, "Fournisseur modifié!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerFournisseur() {
        int row = fournisseursTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fournisseur", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionFournisseurs.supprimerFournisseur(id);
                JOptionPane.showMessageDialog(this, "Fournisseur supprimé!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Fournisseur> fournisseurs = gestionFournisseurs.listerFournisseurs();
        StringBuilder perf = new StringBuilder();

        for (Fournisseur f : fournisseurs) {
            tableModel.addRow(new Object[]{
                f.getId(),
                f.getNom(),
                f.getTelephone(),
                f.getAdresse()
            });

            double performance = gestionFournisseurs.calculerPerformanceFournisseur(f.getId());
            String status = performance >= 80 ? "✓ Bon" : "⚠️ À surveiller";
            perf.append(f.getNom()).append(": ").append(String.format("%.2f%%", performance)).append(" - ").append(status).append("\n");
        }

        txtPerformance.setText(perf.toString());
    }

    private void clearForm() {
        txtNom.setText("");
        txtTelephone.setText("");
        txtAdresse.setText("");
    }
}
