package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.service.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {
    private GestionClients gestionClients;
    private GestionProduits gestionProduits;
    private GestionStock gestionStock;
    private GestionFournisseurs gestionFournisseurs;
    private GestionCommandes gestionCommandes;
    private GestionVentes gestionVentes;

    public DashboardPanel() throws SQLException {
        gestionClients = new GestionClients();
        gestionProduits = new GestionProduits();
        gestionStock = new GestionStock();
        gestionFournisseurs = new GestionFournisseurs();
        gestionCommandes = new GestionCommandes();
        gestionVentes = new GestionVentes();

        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new GridLayout(3, 2, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245));

        add(createStatsCard("👥 Clients", String.valueOf(gestionClients.listerClients().size()), new Color(52, 168, 224)));
        add(createStatsCard("💊 Produits", String.valueOf(gestionProduits.listerProduits().size()), new Color(46, 204, 113)));
        add(createStatsCard("📦 Lots en Stock", String.valueOf(gestionStock.consulterStock().size()), new Color(241, 196, 15)));
        add(createStatsCard("🏭 Fournisseurs", String.valueOf(gestionFournisseurs.listerFournisseurs().size()), new Color(155, 89, 182)));
        add(createStatsCard("📑 Commandes", String.valueOf(gestionCommandes.getTotalCommandes()), new Color(230, 126, 34)));
        add(createStatsCard("💳 Ventes", String.valueOf(gestionVentes.getTotalVentes()), new Color(52, 73, 94)));
    }

    private JPanel createStatsCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createRaisedBevelBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(titleLabel, gbc);

        gbc.gridy = 1;
        card.add(valueLabel, gbc);

        return card;
    }
}
