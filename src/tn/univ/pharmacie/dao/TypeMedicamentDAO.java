package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeMedicamentDAO {

    public void ajouterType(TypeMedicament type) throws SQLException {
        String sql = "INSERT INTO type_medicament (libelle) VALUES (?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, type.getLibelle());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    type.setMedicamentId(rs.getInt(1));
                }
            }
        }
    }
    public TypeMedicament getTypeByLibelle(String libelle) throws SQLException {
        String sql = "SELECT * FROM type_medicament WHERE libelle=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TypeMedicament type = new TypeMedicament();
                type.setMedicamentId(rs.getInt("id"));
                type.setLibelle(rs.getString("libelle"));
                return type;
            }
        }
        return null; // pas trouvé
    }
    public void supprimerType(int id) throws SQLException {
        String sql = "DELETE FROM type_medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierType(TypeMedicament type) throws SQLException {
        String sql = "UPDATE type_medicament SET libelle=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.getLibelle());
            stmt.setInt(2, type.getMedicamentId());
            stmt.executeUpdate();
        }
    }

    public List<TypeMedicament> getAllTypes() throws SQLException {
        List<TypeMedicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM type_medicament";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TypeMedicament type = new TypeMedicament();
                type.setMedicamentId(rs.getInt("id"));
                type.setLibelle(rs.getString("libelle"));
                liste.add(type);
            }
        }
        return liste;
    }
    // On utilise getTypeById pour récupérer l'objet TyprMedicamant depuis la base
    public TypeMedicament getTypeById(int id) throws SQLException {
        String sql = "SELECT * FROM type_medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TypeMedicament type = new TypeMedicament();
                type.setMedicamentId(rs.getInt("id"));
                type.setLibelle(rs.getString("libelle"));
                return type;
            }
        }
        return null;
    }
}
