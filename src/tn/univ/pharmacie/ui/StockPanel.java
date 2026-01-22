package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.StockLot;
import tn.univ.pharmacie.service.GestionStock;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StockPanel extends JPanel {
    private GestionStock gestionStock;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JTextField txtStockId, txtQuantite, txtDateExp;
    private JButton btnMettreAJour, btnRafraichir;
    private JTextArea txtAlerts;

    public StockPanel() {
        gestionStock = new GestionStock();
        initComponents();
        rafraichirTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Update form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Mettre à Jour le Stock"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        txtStockId = new JTextField(10);
        txtQuantite = new JTextField(10);
        txtDateExp = new JTextField(10); // format YYYY-MM-DD
        btnMettreAJour = new JButton("🔄 Mettre à Jour");
        btnRafraichir = new JButton("🔃 Rafraîchir");

        btnMettreAJour.addActionListener(e -> mettreAJourStock());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Stock:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtStockId, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtQuantite, gbc);

        gbc.gridx = 4; gbc.gridy = 0;
        formPanel.add(new JLabel("Date Expiration (YYYY-MM-DD):"), gbc);
        gbc.gridx = 5;
        formPanel.add(txtDateExp, gbc);

        gbc.gridx = 6; gbc.gridy = 0;
        formPanel.add(btnMettreAJour, gbc);
        gbc.gridx = 7;
        formPanel.add(btnRafraichir, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Médicament", "Quantité", "Expiration", "Statut"}, 0);
        stockTable = new JTable(tableModel);
        tablePanel.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // South panel - Alerts
        JPanel alertPanel = new JPanel(new BorderLayout());
        alertPanel.setBorder(BorderFactory.createTitledBorder("Alertes Stock"));
        txtAlerts = new JTextArea(5, 50);
        txtAlerts.setEditable(false);
        alertPanel.add(new JScrollPane(txtAlerts), BorderLayout.CENTER);
        add(alertPanel, BorderLayout.SOUTH);
    }

    private void mettreAJourStock() {
        try {
            int stockId = Integer.parseInt(txtStockId.getText());
            int quantite = Integer.parseInt(txtQuantite.getText());
            LocalDate dateExp = LocalDate.parse(txtDateExp.getText());

            gestionStock.mettreAJourStockParId(stockId, quantite, dateExp);
            JOptionPane.showMessageDialog(this, "Stock mis à jour!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            txtStockId.setText("");
            txtQuantite.setText("");
            txtDateExp.setText("");
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<StockLot> stocks = null;
        try {
            stocks = gestionStock.consulterStock();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        StringBuilder alerts = new StringBuilder();

        for (StockLot lot : stocks) {
            String medicament = (lot.getMedicament() != null) ? lot.getMedicament().getNom() : "N/A";
            String exp = (lot.getDateExpiration() != null) ? lot.getDateExpiration().toString() : "N/A";
            String statut = lot.getQuantite() < gestionStock.getSeuilStock() ? "⚠️ ALERTE" : "✓ OK";

            tableModel.addRow(new Object[]{
                    lot.getId(),
                    medicament,
                    lot.getQuantite(),
                    exp,
                    statut
            });

            if (lot.getQuantite() < gestionStock.getSeuilStock()) {
                alerts.append("⚠️ ").append(medicament).append(" - Quantité: ").append(lot.getQuantite()).append("\n");
            }
        }

        if (alerts.length() == 0) {
            alerts.append("✓ Aucune alerte\n");
        }
        txtAlerts.setText(alerts.toString());
    }
}