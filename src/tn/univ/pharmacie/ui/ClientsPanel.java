package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Client;
import tn.univ.pharmacie.service.GestionClients;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientsPanel extends JPanel {
    private GestionClients gestionClients;
    private JTable clientsTable;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrenom, txtTelephone, txtAdresse;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRafraichir;

    public ClientsPanel() {
        gestionClients = new GestionClients();
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
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Prénom", "Téléphone", "Adresse"}, 0);
        clientsTable = new JTable(tableModel);
        tablePanel.add(new JScrollPane(clientsTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        btnAjouter = new JButton("➕ Ajouter");
        btnModifier = new JButton("✏️ Modifier");
        btnSupprimer = new JButton("🗑️ Supprimer");
        btnRafraichir = new JButton("🔄 Rafraîchir");

        btnAjouter.addActionListener(e -> ajouterClient());
        btnModifier.addActionListener(e -> modifierClient());
        btnSupprimer.addActionListener(e -> supprimerClient());
        btnRafraichir.addActionListener(e -> rafraichirTable());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRafraichir);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter/Modifier Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNom = new JTextField(15);
        txtPrenom = new JTextField(15);
        txtTelephone = new JTextField(15);
        txtAdresse = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNom, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 3;
        panel.add(txtPrenom, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTelephone, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 3;
        panel.add(txtAdresse, gbc);

        return panel;
    }

    private void ajouterClient() {
        try {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            int telephone = Integer.parseInt(txtTelephone.getText());
            String adresse = txtAdresse.getText();

            Client client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setTelephone(telephone);
            client.setAdresse(adresse);

            gestionClients.ajouterClient(client);
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierClient() {
        int row = clientsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            int telephone = Integer.parseInt(txtTelephone.getText());
            String adresse = txtAdresse.getText();

            Client client = new Client();
            client.setId(id);
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setTelephone(telephone);
            client.setAdresse(adresse);

            gestionClients.modifierClient(client);
            JOptionPane.showMessageDialog(this, "Client modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            rafraichirTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerClient() {
        int row = clientsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionClients.supprimerClient(id);
                JOptionPane.showMessageDialog(this, "Client supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Client> clients = gestionClients.listerClients();
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getTelephone(),
                client.getAdresse()
            });
        }
    }

    private void clearForm() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtTelephone.setText("");
        txtAdresse.setText("");
    }
}
