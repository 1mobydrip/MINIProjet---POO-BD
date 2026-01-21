package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.service.GestionProduits;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProduitsPanel extends JPanel {
    private GestionProduits gestionProduits;
    private JTable produitsTable;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrix, txtDescription, txtFournisseurId, txtTypeMedId;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRafraichir, btnRechercher;
    private JTextField txtRecherche;

    public ProduitsPanel() {
        gestionProduits = new GestionProduits();
        initComponents();
        rafraichirTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Center panel - Table + Search
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        txtRecherche = new JTextField(20);
        searchPanel.add(txtRecherche);
        btnRechercher = new JButton("🔍 Chercher");
        btnRechercher.addActionListener(e -> rechercher());
        searchPanel.add(btnRechercher);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Table with new columns
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nom", "Prix", "Description", "Fournisseur ID", "Type Médicament ID"}, 0);
        produitsTable = new JTable(tableModel);
        centerPanel.add(new JScrollPane(produitsTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel - Buttons
        JPanel buttonPanel = new JPanel();
        btnAjouter = new JButton("➕ Ajouter");
        btnModifier = new JButton("✏️ Modifier");
        btnSupprimer = new JButton("🗑️ Supprimer");
        btnRafraichir = new JButton("🔄 Rafraîchir");

        btnAjouter.addActionListener(e -> ajouterProduit());
        btnModifier.addActionListener(e -> modifierProduit());
        btnSupprimer.addActionListener(e -> supprimerProduit());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRafraichir);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter/Modifier Produit"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNom = new JTextField(20);
        txtPrix = new JTextField(15);
        txtDescription = new JTextField(30);
        txtFournisseurId = new JTextField(10);
        txtTypeMedId = new JTextField(10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNom, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Prix:"), gbc);
        gbc.gridx = 3;
        panel.add(txtPrix, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(txtDescription, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Fournisseur ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtFournisseurId, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Type Médicament ID:"), gbc);
        gbc.gridx = 3;
        panel.add(txtTypeMedId, gbc);

        return panel;
    }

    private void ajouterProduit() {
        try {
            String nom = txtNom.getText();
            double prix = Double.parseDouble(txtPrix.getText());
            String description = txtDescription.getText();
            int fournisseurId = Integer.parseInt(txtFournisseurId.getText());
            int typeMedId = Integer.parseInt(txtTypeMedId.getText());

            Medicament med = new Medicament();
            med.setNom(nom);
            med.setPrix(prix);
            med.setDescription(description);
            med.setFournisseurId(fournisseurId);
            med.setTypeId(typeMedId);

            gestionProduits.ajouterProduit(med);
            JOptionPane.showMessageDialog(this, "Produit ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierProduit() {
        int row = produitsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String nom = txtNom.getText();
            double prix = Double.parseDouble(txtPrix.getText());
            String description = txtDescription.getText();
            int fournisseurId = Integer.parseInt(txtFournisseurId.getText());
            int typeMedId = Integer.parseInt(txtTypeMedId.getText());

            Medicament med = new Medicament();
            med.setId(id);
            med.setNom(nom);
            med.setPrix(prix);
            med.setDescription(description);
            med.setFournisseurId(fournisseurId);
            med.setTypeId(typeMedId);

            gestionProduits.modifierProduit(med);
            JOptionPane.showMessageDialog(this, "Produit modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerProduit() {
        int row = produitsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionProduits.supprimerProduit(id);
                JOptionPane.showMessageDialog(this, "Produit supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rechercher() {
        String nom = txtRecherche.getText();
        tableModel.setRowCount(0);
        List<Medicament> results = null;
        try {
            results = gestionProduits.rechercherProduitParNom(nom);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Medicament med : results) {
            tableModel.addRow(new Object[]{
                    med.getId(),
                    med.getNom(),
                    med.getPrix(),
                    med.getDescription(),
                    med.getFournisseurId(),
                    med.getTypeId()
            });
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Medicament> produits = null;
        try {
            produits = gestionProduits.listerProduits();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Medicament med : produits) {
            tableModel.addRow(new Object[]{
                    med.getId(),
                    med.getNom(),
                    med.getPrix(),
                    med.getDescription(),
                    med.getFournisseurId(),
                    med.getTypeId()
            });
        }
    }

    private void clearForm() {
        txtNom.setText("");
        txtPrix.setText("");
        txtDescription.setText("");
        txtFournisseurId.setText("");
        txtTypeMedId.setText("");
    }
}