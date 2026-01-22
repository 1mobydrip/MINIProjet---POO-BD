package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.Fournisseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO {

    public void ajouterFournisseur(Fournisseur f) throws SQLException {
        String sql = "INSERT INTO fournisseur (nom, telephone, adresse) VALUES (?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, f.getNom());
            stmt.setInt(2, f.getTelephone());
            stmt.setString(3, f.getAdresse());

            stmt.executeUpdate();


            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setId(rs.getInt(1));
                }}}}

    public void supprimerFournisseur(int id) throws SQLException {
        String sql = "DELETE FROM fournisseur WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int compterFournisseurs() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM fournisseur";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }


    public List<Fournisseur> getAllFournisseurs() throws SQLException {
        List<Fournisseur> liste = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Fournisseur f = new Fournisseur();
                f.setId(rs.getInt("id"));
                f.setNom(rs.getString("nom"));
                f.setTelephone(rs.getInt("telephone"));
                f.setAdresse(rs.getString("adresse"));
                liste.add(f);
            }
        }
        return liste;

    }

    public void modifierFournisseur(Fournisseur f) throws SQLException {
        String sql = "UPDATE fournisseur SET nom=?, telephone=?, adresse=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNom());
            stmt.setInt(2, f.getTelephone());
            stmt.setString(3, f.getAdresse());
            stmt.setInt(4, f.getId());

            stmt.executeUpdate();
        }
    }

    public Fournisseur getFById(int id) throws SQLException {
        String sql = "SELECT * FROM fournisseur WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Fournisseur f = new Fournisseur();
                f.setId(rs.getInt("id"));
                f.setNom(rs.getString("nom"));
                f.setTelephone(rs.getInt("telephone"));
                f.setAdresse(rs.getString("adresse"));
                return f;
            }
        }
        return null;
    }
}

