package tn.univ.pharmacie.ui;

import tn.univ.pharmacie.model.Employe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Employe currentUser;

    public MainFrame(Employe employe) {
        this.currentUser = employe;
        setTitle("Pharmacy Management System - " + employe.getNom() + " " + employe.getPrenom());
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponents();
    }

    private void initComponents() {
        // Create menu bar
        createMenuBar();

        // Create main panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add all panels
        contentPanel.add(new DashboardPanel(), "DASHBOARD");
        contentPanel.add(new ClientsPanel(), "CLIENTS");
        contentPanel.add(new ProduitsPanel(), "PRODUITS");
        contentPanel.add(new StockPanel(), "STOCK");
        contentPanel.add(new FournisseursPanel(), "FOURNISSEURS");
        contentPanel.add(new CommandesPanel(), "COMMANDES");
        contentPanel.add(new VentesPanel(), "VENTES");
        contentPanel.add(new RapportsPanel(), "RAPPORTS");

        add(contentPanel);
        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Gestion Menu
        JMenu gestionMenu = new JMenu("Gestion");

        JMenuItem clientsItem = new JMenuItem("👥 Clients");
        clientsItem.addActionListener(e -> cardLayout.show(contentPanel, "CLIENTS"));
        gestionMenu.add(clientsItem);

        JMenuItem produitsItem = new JMenuItem("💊 Produits");
        produitsItem.addActionListener(e -> cardLayout.show(contentPanel, "PRODUITS"));
        gestionMenu.add(produitsItem);

        JMenuItem stockItem = new JMenuItem("📦 Stock");
        stockItem.addActionListener(e -> cardLayout.show(contentPanel, "STOCK"));
        gestionMenu.add(stockItem);

        JMenuItem fournisseursItem = new JMenuItem("🏭 Fournisseurs");
        fournisseursItem.addActionListener(e -> cardLayout.show(contentPanel, "FOURNISSEURS"));
        gestionMenu.add(fournisseursItem);

        JMenuItem commandesItem = new JMenuItem("📑 Commandes");
        commandesItem.addActionListener(e -> cardLayout.show(contentPanel, "COMMANDES"));
        gestionMenu.add(commandesItem);

        JMenuItem ventesItem = new JMenuItem("💳 Ventes");
        ventesItem.addActionListener(e -> cardLayout.show(contentPanel, "VENTES"));
        gestionMenu.add(ventesItem);

        // Reports Menu
        JMenu rapportsMenu = new JMenu("Rapports");
        JMenuItem rapportsItem = new JMenuItem("📊 Rapports & Analyses");
        rapportsItem.addActionListener(e -> cardLayout.show(contentPanel, "RAPPORTS"));
        rapportsMenu.add(rapportsItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Aide");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Pharmacy Management System\nVersion 1.0\n\nUtilisateur: " + currentUser.getNom() + " " + currentUser.getPrenom(),
            "À propos", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(gestionMenu);
        menuBar.add(rapportsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing, create a dummy employee
            Employe employe = new Employe();
            employe.setId(1);
            employe.setNom("Admin");
            employe.setPrenom("User");
            MainFrame frame = new MainFrame(employe);
            frame.setVisible(true);
        });
    }
}
