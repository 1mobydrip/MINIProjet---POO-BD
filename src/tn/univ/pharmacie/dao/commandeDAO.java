package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.Fournisseur;
import tn.univ.pharmacie.model.Employe;
import tn.univ.pharmacie.model.Commande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class commandeDAO {

    public void ajouterCommande(Commande cmd) throws SQLException {
        String sql = "INSERT INTO commande (fournisseur_id, employe_id, date_commande, statut) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cmd.getFournisseur().getId());
            stmt.setInt(2, cmd.getEmploye().getId());
            stmt.setDate(3, Date.valueOf(cmd.getDate()));
            stmt.setString(4, cmd.getStatut().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cmd.setId(rs.getInt(1));
                }
            }
        }
    }

    public void supprimerCommande(int id) throws SQLException {
        String sql = "DELETE FROM commande WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierCommande(Commande cmd) throws SQLException {
        String sql = "UPDATE commande SET fournisseur_id=?, employe_id=?, date_commande=?, statut=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cmd.getFournisseur().getId());
            stmt.setInt(2, cmd.getEmploye().getId());
            stmt.setDate(3, Date.valueOf(cmd.getDate()));
            stmt.setString(4, cmd.getStatut().name());
            stmt.setInt(5, cmd.getId());

            stmt.executeUpdate();
        }
    }

    public List<Commande> getAllCommandes() throws SQLException {
        List<Commande> liste = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Commande cmd = new Commande();
                cmd.setId(rs.getInt("id"));

                // Créer objets Fournisseur et Employe avec juste l'ID
                Commande cmdF = new Commande();
                cmd.setFournisseur(new Fournisseur());
                cmd.getFournisseur().setId(rs.getInt("fournisseur_id"));

                cmd.setEmploye(new Employe());
                cmd.getEmploye().setId(rs.getInt("employe_id"));

                cmd.setDate(rs.getDate("date_commande").toLocalDate());
                cmd.setStatut(Commande.StatutCommande.valueOf(rs.getString("statut").toUpperCase()));

                liste.add(cmd);
            }
        }
        return liste;
    }

    public Commande getCommandeById(int id) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Commande cmd = new Commande();
                cmd.setId(rs.getInt("id"));

                cmd.setFournisseur(new Fournisseur());
                cmd.getFournisseur().setId(rs.getInt("fournisseur_id"));

                cmd.setEmploye(new Employe());
                cmd.getEmploye().setId(rs.getInt("employe_id"));

                cmd.setDate(rs.getDate("date_commande").toLocalDate());
                cmd.setStatut(Commande.StatutCommande.valueOf(rs.getString("statut").toUpperCase()));

                return cmd;
            }
        }
        return null;
    }
}