package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Commande;
import tn.univ.pharmacie.model.Commande.StatutCommande;
import tn.univ.pharmacie.service.GestionCommandes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CommandesPanel extends JPanel {
    private GestionCommandes gestionCommandes;
    private JTable commandesTable;
    private DefaultTableModel tableModel;
    private JTextField txtMontant;
    private JComboBox<StatutCommande> cmbStatut;
    private JButton btnValider, btnAnnuler, btnRecevoirCommande, btnRafraichir;
    private JTextArea txtStats;

    public CommandesPanel() {
        gestionCommandes = new GestionCommandes();
        initComponents();
        rafraichirTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Filter/Action
        JPanel actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions sur Commandes"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        cmbStatut = new JComboBox<>(StatutCommande.values());
        btnValider = new JButton("✓ Valider");
        btnAnnuler = new JButton("✗ Annuler");
        btnRecevoirCommande = new JButton("📥 Recevoir");
        btnRafraichir = new JButton("🔄 Rafraîchir");

        btnValider.addActionListener(e -> validerCommande());
        btnAnnuler.addActionListener(e -> annulerCommande());
        btnRecevoirCommande.addActionListener(e -> recevoirCommande());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        gbc.gridx = 0; gbc.gridy = 0;
        actionPanel.add(new JLabel("Filtrer par statut:"), gbc);
        gbc.gridx = 1;
        actionPanel.add(cmbStatut, gbc);
        gbc.gridx = 2;
        actionPanel.add(btnValider, gbc);
        gbc.gridx = 3;
        actionPanel.add(btnAnnuler, gbc);
        gbc.gridx = 4;
        actionPanel.add(btnRecevoirCommande, gbc);
        gbc.gridx = 5;
        actionPanel.add(btnRafraichir, gbc);

        add(actionPanel, BorderLayout.NORTH);

        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Fournisseur", "Montant", "Date", "Statut"}, 0);
        commandesTable = new JTable(tableModel);
        tablePanel.add(new JScrollPane(commandesTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // South panel - Stats
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistiques Commandes"));
        txtStats = new JTextArea(4, 50);
        txtStats.setEditable(false);
        statsPanel.add(new JScrollPane(txtStats), BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
    }

    private void validerCommande() {
        int row = commandesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            gestionCommandes.validerCommande(id);
            JOptionPane.showMessageDialog(this, "Commande validée!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annulerCommande() {
        int row = commandesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Annuler cette commande?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                gestionCommandes.annulerCommande(id);
                JOptionPane.showMessageDialog(this, "Commande annulée!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recevoirCommande() {
        int row = commandesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            gestionCommandes.recevoirCommande(id);
            JOptionPane.showMessageDialog(this, "Commande réceptionnée!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Commande> commandes = gestionCommandes.listerCommandes();

        for (Commande c : commandes) {
            String fournisseur = (c.getFournisseur() != null) ? c.getFournisseur().getNom() : "N/A";
            tableModel.addRow(new Object[]{
                c.getId(),
                fournisseur,
                String.format("%.2f", c.getMontant()),
                c.getDate(),
                c.getStatut()
            });
        }

        // Update stats
        StringBuilder stats = new StringBuilder();
        stats.append("Total Commandes: ").append(gestionCommandes.getTotalCommandes()).append("\n");
        stats.append("Montant Total: ").append(String.format("%.2f", gestionCommandes.calculerMontantTotal())).append(" DT\n");
        stats.append("En Cours: ").append(gestionCommandes.listerCommandesParStatut(StatutCommande.EN_COURS).size()).append(" | ");
        stats.append("Validées: ").append(gestionCommandes.listerCommandesParStatut(StatutCommande.VALIDEE).size()).append(" | ");
        stats.append("Réceptionnées: ").append(gestionCommandes.listerCommandesParStatut(StatutCommande.RECEPTIONNEE).size()).append(" | ");
        stats.append("Annulées: ").append(gestionCommandes.listerCommandesParStatut(StatutCommande.ANNULEE).size());

        txtStats.setText(stats.toString());
    }
}
