package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.dao.VenteDAO;
import tn.univ.pharmacie.dao.VenteDetailDAO;
import tn.univ.pharmacie.model.Vente;
import tn.univ.pharmacie.model.VenteDetail;
import tn.univ.pharmacie.model.Client;
import tn.univ.pharmacie.model.Employe;
import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.service.GestionVentes;
import tn.univ.pharmacie.dao.MedicamentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VentesPanel extends JPanel {
    private GestionVentes gestionVentes;
    private VenteDAO venteDAO = new VenteDAO();
    private VenteDetailDAO venteDetailDAO = new VenteDetailDAO();
    private JTable ventesTable;
    private DefaultTableModel tableModel;
    private JTextField txtClientId, txtEmployeId, txtMedicamentId, txtQuantite;
    private JButton btnRafraichir, btnHistoriqueClient, btnAjouterVente;
    private JTextArea txtStats;
    private MedicamentDAO medicamentDAO = new MedicamentDAO();

    public VentesPanel() {
        gestionVentes = new GestionVentes();
        initComponents();
        rafraichirTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Nouvelle Vente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        txtClientId = new JTextField(5);
        txtEmployeId = new JTextField(5);
        txtMedicamentId = new JTextField(5);
        txtQuantite = new JTextField(5);

        btnAjouterVente = new JButton("➕ Ajouter Vente");
        btnHistoriqueClient = new JButton("🔍 Historique Client");
        btnRafraichir = new JButton("🔄 Rafraîchir");

        btnAjouterVente.addActionListener(e -> ajouterVente());
        btnHistoriqueClient.addActionListener(e -> afficherHistoriqueClient());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Client:"), gbc);
        gbc.gridx = 1; formPanel.add(txtClientId, gbc);

        gbc.gridx = 2; formPanel.add(new JLabel("ID Employé:"), gbc);
        gbc.gridx = 3; formPanel.add(txtEmployeId, gbc);

        gbc.gridx = 4; formPanel.add(new JLabel("ID Médicament:"), gbc);
        gbc.gridx = 5; formPanel.add(txtMedicamentId, gbc);

        gbc.gridx = 6; formPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 7; formPanel.add(txtQuantite, gbc);

        gbc.gridx = 8; formPanel.add(btnAjouterVente, gbc);
        gbc.gridx = 9; formPanel.add(btnHistoriqueClient, gbc);
        gbc.gridx = 10; formPanel.add(btnRafraichir, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{
                "ID Vente", "Client", "Employé", "Date", "Médicament", "Quantité", "Montant Total"
        }, 0);
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

    private void ajouterVente() {
        try {
            int clientId = Integer.parseInt(txtClientId.getText());
            int employeId = Integer.parseInt(txtEmployeId.getText());
            int medicamentId = Integer.parseInt(txtMedicamentId.getText());
            int quantite = Integer.parseInt(txtQuantite.getText());

            // Créer la vente
            Vente vente = new Vente();
            Client client = new Client(); client.setId(clientId);
            Employe employe = new Employe(); employe.setId(employeId);
            vente.setClient(client);
            vente.setEmploye(employe);
            vente.setDateVente(LocalDate.now());
            // Créer le détail
            Medicament med = new Medicament(); med.setId(medicamentId);
            VenteDetail vd = new VenteDetail();
            vd.setVente(vente);
            vd.setMedicament(med);
            vd.setQuantite(quantite);

            gestionVentes.enregistrerVente(vd, vente);

            JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            rafraichirTable();

            txtClientId.setText("");
            txtEmployeId.setText("");
            txtMedicamentId.setText("");
            txtQuantite.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
                String client = (v.getClient() != null) ? v.getClient().getNom() + " " + v.getClient().getPrenom() : "N/A";
                String employe = (v.getEmploye() != null) ? v.getEmploye().getNom() : "N/A";

                VenteDetail vd = gestionVentes.getDetailByVente(v.getId());

                tableModel.addRow(new Object[]{
                        v.getId(),
                        client,
                        employe,
                        v.getDateVente(),
                        (vd != null && vd.getMedicament() != null) ? vd.getMedicament().getNom() : "N/A",
                        (vd != null) ? vd.getQuantite() : "N/A",
                        (vd != null) ? String.format("%.2f", vd.getMontantTotal()) : "N/A"
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
        List<Vente> ventes = null;
        List<VenteDetail> details = null;
        try {
            ventes = venteDAO.getAllVentes();
            details = venteDetailDAO.getAllVenteDetails();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (VenteDetail v : details) {
            Vente vente;
            try {
                vente = venteDAO.getVenteById(v.getVente().getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                tableModel.addRow(new Object[]{
                        vente.getId(),
                        vente.getClient().getId(),
                        vente.getEmploye().getId(),
                        vente.getDateVente().toString(),
                        v.getMedicament().getId(),
                        v.getQuantite(),
                        medicamentDAO.getPrixById(v.getMedicament().getId()) * v.getQuantite()
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        StringBuilder stats = new StringBuilder();
        try {
            double totalVentes = gestionVentes.calculerMontantTotalVentes();
            stats.append("Total Ventes: ").append(gestionVentes.getTotalVentes()).append("\n");
            stats.append("Chiffre d'Affaires: ").append(String.format("%.2f", totalVentes)).append(" DT\n");
            stats.append("Moyenne par Vente: ").append(String.format("%.2f",
                    gestionVentes.getTotalVentes() > 0 ? totalVentes / gestionVentes.getTotalVentes() : 0)).append(" DT");
        } catch (SQLException e) {
            stats.append("Erreur calcul stats: ").append(e.getMessage());
        }

        txtStats.setText(stats.toString());
        txtClientId.setText("");
    }
}