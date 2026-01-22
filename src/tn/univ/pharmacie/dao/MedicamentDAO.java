package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.Medicament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MedicamentDAO {

    public void ajouterMedicament(Medicament med) throws SQLException {
        String sql = "INSERT INTO medicament (nom, description, prix, type_id, fournisseur_id) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, med.getNom());
            stmt.setString(2, med.getDescription());
            stmt.setBigDecimal(3, BigDecimal.valueOf(med.getPrix()));
            stmt.setInt(4, med.getTypeId());
            stmt.setInt(5, med.getFournisseurId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    med.setId(rs.getInt(1));
                }
            }
        }
    }

    public void supprimerMedicament(int id) throws SQLException {
        String sql = "DELETE FROM medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierMedicament(Medicament med) throws SQLException {
        String sql = "UPDATE medicament SET nom=?, description=?, prix=?, type_id=?, fournisseur_id=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, med.getNom());
            stmt.setString(2, med.getDescription());
            stmt.setBigDecimal(3, BigDecimal.valueOf(med.getPrix()));
            stmt.setInt(4, med.getTypeId());
            stmt.setInt(5, med.getFournisseurId());
            stmt.setInt(6, med.getId());

            stmt.executeUpdate();
        }
    }

    public List<Medicament> getAllMedicaments() throws SQLException {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM medicament";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicament med = new Medicament();
                med.setId(rs.getInt("id"));
                med.setNom(rs.getString("nom"));
                med.setDescription(rs.getString("description"));
                med.setPrix(rs.getBigDecimal("prix").doubleValue());
                med.setTypeId(rs.getInt("type_id"));
                med.setFournisseurId(rs.getInt("fournisseur_id"));
                liste.add(med);
            }
        }
        return liste;
    }

    public Medicament getMedicamentById(int id) throws SQLException {
        String sql = "SELECT * FROM medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Medicament med = new Medicament();
                med.setId(rs.getInt("id"));
                med.setNom(rs.getString("nom"));
                med.setDescription(rs.getString("description"));
                med.setPrix(rs.getBigDecimal("prix").doubleValue());
                med.setTypeId(rs.getInt("type_id"));
                med.setFournisseurId(rs.getInt("fournisseur_id"));
                return med;
            }
        }
        return null;
    }

    public double getPrixById(int id) throws SQLException {
        String sql = "SELECT prix FROM medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getBigDecimal("prix").doubleValue();
                }
            }
        return 0;
    }
}