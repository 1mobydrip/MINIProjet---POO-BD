package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.Fournisseur;
import tn.univ.pharmacie.model.Employe;
import tn.univ.pharmacie.model.Commande;
import java.sql.*;
import java.time.LocalDate;
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

    public int getFidById(int id) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int f;
                f = rs.getInt("fournisseur_id");

                return f;
            }
        }
        return 0;
    }

    public LocalDate getDateById(int id) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate f;
                f = rs.getDate("date_commande").toLocalDate();

                return f;
            }
        }
        return null;
    }

    public Commande.StatutCommande getStatusById(int id) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Commande.StatutCommande f;
                f = Commande.StatutCommande.valueOf(rs.getString("statut"));

                return f;
            }
        }
        return null;
    }

    public void setStatusById(int id, Commande.StatutCommande statut) throws SQLException {
        String sql = "UPDATE commande SET statut=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, statut.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}

