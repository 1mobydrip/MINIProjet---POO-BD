package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.*;
import tn.univ.pharmacie.service.GestionCommandes;
import tn.univ.pharmacie.dao.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CommandesPanel extends JPanel {
    private GestionCommandes gestionCommandes = new GestionCommandes();
    private JTable commandesTable;
    private DefaultTableModel tableModel;
    private JTextField txtFournisseurId, txtEmployeId, txtMedicamentId, txtQuantite;
    private JComboBox<Commande.StatutCommande> cmbStatut;
    private JButton btnValider, btnAnnuler, btnRecevoirCommande, btnRafraichir, btnAjouterCommande;
    private JTextArea txtStats;

    public CommandesPanel() {
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

        txtFournisseurId = new JTextField(5);
        txtEmployeId = new JTextField(5);
        txtMedicamentId = new JTextField(5);
        txtQuantite = new JTextField(5);

        cmbStatut = new JComboBox<>(Commande.StatutCommande.values());
        btnValider = new JButton("✓ Valider");
        btnAnnuler = new JButton("✗ Annuler");
        btnRecevoirCommande = new JButton("📥 Recevoir");
        btnRafraichir = new JButton("🔄 Rafraîchir");
        btnAjouterCommande = new JButton("➕ Ajouter Commande");

        btnValider.addActionListener(e -> validerCommande());
        btnAnnuler.addActionListener(e -> annulerCommande());
        btnRecevoirCommande.addActionListener(e -> recevoirCommande());
        btnRafraichir.addActionListener(e -> rafraichirTable());
        btnAjouterCommande.addActionListener(e -> ajouterCommande());

        gbc.gridx = 0; gbc.gridy = 0;
        actionPanel.add(new JLabel("Fournisseur ID:"), gbc);
        gbc.gridx = 1; actionPanel.add(txtFournisseurId, gbc);

        gbc.gridx = 2; actionPanel.add(new JLabel("Employé ID:"), gbc);
        gbc.gridx = 3; actionPanel.add(txtEmployeId, gbc);

        gbc.gridx = 4; actionPanel.add(new JLabel("Médicament ID:"), gbc);
        gbc.gridx = 5; actionPanel.add(txtMedicamentId, gbc);

        gbc.gridx = 6; actionPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 7; actionPanel.add(txtQuantite, gbc);

        gbc.gridx = 8; actionPanel.add(btnAjouterCommande, gbc);
        gbc.gridx = 9; actionPanel.add(btnValider, gbc);
        gbc.gridx = 10; actionPanel.add(btnAnnuler, gbc);
        gbc.gridx = 11; actionPanel.add(btnRecevoirCommande, gbc);
        gbc.gridx = 12; actionPanel.add(btnRafraichir, gbc);

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

    private void ajouterCommande() {
        try {
            int fournisseurId = Integer.parseInt(txtFournisseurId.getText());
            int employeId = Integer.parseInt(txtEmployeId.getText());
            int medicamentId = Integer.parseInt(txtMedicamentId.getText());
            int quantite = Integer.parseInt(txtQuantite.getText());

            FournisseurDAO fdao = new FournisseurDAO();
            Fournisseur f = fdao.getFById(fournisseurId);
            EmployeDAO edao = new EmployeDAO();
            Employe e = edao.getEById(employeId);
            MedicamentDAO mdao = new MedicamentDAO();
            Medicament m = mdao.getMedicamentById(medicamentId);


            CommandeDetail cd = new CommandeDetail();
            Commande c = new Commande();
            c.setFournisseur(f);
            c.setDate(LocalDate.now());
            c.setEmploye(e);
            c.setStatut(Commande.StatutCommande.en_attente);

            cd.setCommande(c);
            cd.setMedicament(m);
            cd.setQuantite(quantite);

            gestionCommandes.creerCommande(cd);

            JOptionPane.showMessageDialog(this, "Commande ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            rafraichirTable();

            txtFournisseurId.setText("");
            txtEmployeId.setText("");
            txtMedicamentId.setText("");
            txtQuantite.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
        List<CommandeDetail> commandes = gestionCommandes.listerCommandes();

        for (CommandeDetail c : commandes) {
            FournisseurDAO f =  new FournisseurDAO();
            commandeDAO dao = new commandeDAO();
            MedicamentDAO mdao = new MedicamentDAO();
            try {
                tableModel.addRow(new Object[]{
                    c.getCommande().getId(),
                    f.getFById(dao.getFidById(c.getCommande().getId())).getNom(),
                    String.format("%.2f", c.getQuantite() * mdao.getPrixById(c.getMedicament().getId())),
                    dao.getDateById(c.getCommande().getId()),
                    dao.getStatusById(c.getCommande().getId()),
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Update stats
        StringBuilder stats = new StringBuilder();
        stats.append("Total Commandes: ").append(gestionCommandes.getTotalCommandes()).append("\n");
        stats.append("Montant Total: ").append(String.format("%.2f", gestionCommandes.calculerMontantTotal())).append(" DT\n");
        stats.append("En Cours: ").append(gestionCommandes.listerCommandesParStatut(Commande.StatutCommande.en_attente).size()).append(" | ");
        stats.append("Validées: ").append(gestionCommandes.listerCommandesParStatut(Commande.StatutCommande.livree).size()).append(" | ");
        stats.append("Réceptionnées: ").append(gestionCommandes.listerCommandesParStatut(Commande.StatutCommande.livree).size()).append(" | ");
        stats.append("Annulées: ").append(gestionCommandes.listerCommandesParStatut(Commande.StatutCommande.annulee).size());

        txtStats.setText(stats.toString());
    }
}
