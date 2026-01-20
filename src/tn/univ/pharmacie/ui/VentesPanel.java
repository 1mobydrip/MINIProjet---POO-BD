package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Vente;
import tn.univ.pharmacie.service.GestionVentes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentesPanel extends JPanel {
    private GestionVentes gestionVentes;
    private JTable ventesTable;
    private DefaultTableModel tableModel;
    private JTextField txtClientId, txtEmployeId;
    private JButton btnRafraichir, btnHistoriqueClient;
    private JTextArea txtStats;

    public VentesPanel() {
        gestionVentes = new GestionVentes();
        initComponents();
        rafraichirTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Search
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        txtClientId = new JTextField(10);
        txtEmployeId = new JTextField(10);
        btnHistoriqueClient = new JButton("🔍 Historique Client");
        btnRafraichir = new JButton("🔄 Rafraîchir");

        btnHistoriqueClient.addActionListener(e -> afficherHistoriqueClient());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("ID Client:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(txtClientId, gbc);
        gbc.gridx = 2;
        searchPanel.add(btnHistoriqueClient, gbc);
        gbc.gridx = 3;
        searchPanel.add(btnRafraichir, gbc);

        add(searchPanel, BorderLayout.NORTH);

        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Client", "Employé", "Date", "Montant Total"}, 0);
        ventesTable = new JTable(tableModel);
        tablePanel.add(new JScrollPane(ventesTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // South panel - Stats
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistiques Ventes"));
        txtStats = new JTextArea(4, 50);
        txtStats.setEditable(false);
        statsPanel.add(new JScrollPane(txtStats), BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
    }

    private void afficherHistoriqueClient() {
        try {
            int clientId = Integer.parseInt(txtClientId.getText());
            tableModel.setRowCount(0);
            List<Vente> historique = gestionVentes.consulterHistoriqueClient(clientId);

            if (historique.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune vente pour ce client", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Vente v : historique) {
                String client = (v.getClien() != null) ? v.getClien().getNom() + " " + v.getClien().getPrenom() : "N/A";
                String employe = (v.getEmploye() != null) ? v.getEmploye().getNom() : "N/A";
                tableModel.addRow(new Object[]{
                    v.getId(),
                    client,
                    employe,
                    v.getDateVente(),
                    String.format("%.2f", v.getMontantTotal())
                });
            }

            double total = gestionVentes.calculerMontantTotalClientId(clientId);
            JOptionPane.showMessageDialog(this, "Montant total pour ce client: " + String.format("%.2f", total) + " DT", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Vente> ventes = gestionVentes.listerVentes();

        for (Vente v : ventes) {
            String client = (v.getClien() != null) ? v.getClien().getNom() + " " + v.getClien().getPrenom() : "N/A";
            String employe = (v.getEmploye() != null) ? v.getEmploye().getNom() : "N/A";
            tableModel.addRow(new Object[]{
                v.getId(),
                client,
                employe,
                v.getDateVente(),
                String.format("%.2f", v.getMontantTotal())
            });
        }

        // Update stats
        StringBuilder stats = new StringBuilder();
        double totalVentes = gestionVentes.calculerMontantTotalVentes();
        stats.append("Total Ventes: ").append(gestionVentes.getTotalVentes()).append("\n");
        stats.append("Chiffre d'Affaires: ").append(String.format("%.2f", totalVentes)).append(" DT\n");
        stats.append("Moyenne par Vente: ").append(String.format("%.2f", gestionVentes.getTotalVentes() > 0 ? totalVentes / gestionVentes.getTotalVentes() : 0)).append(" DT");

        txtStats.setText(stats.toString());
        txtClientId.setText("");
    }
}
