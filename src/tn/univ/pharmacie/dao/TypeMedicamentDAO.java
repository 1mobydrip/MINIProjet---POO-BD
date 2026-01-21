package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.TypeMedicament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeMedicamentDAO {

    /**
     * Ajouter un type de médicament
     */
    public void ajouterType(TypeMedicament type) throws SQLException {
        String sql = "INSERT INTO type_medicament (libelle) VALUES (?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, type.getLibelle());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    type.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Récupérer un type par libellé
     */
    public TypeMedicament getTypeByLibelle(String libelle) throws SQLException {
        String sql = "SELECT * FROM type_medicament WHERE libelle=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TypeMedicament type = new TypeMedicament();
                type.setId(rs.getInt("id"));
                type.setLibelle(rs.getString("libelle"));
                return type;
            }
        }
        return null;
    }

    /**
     * Supprimer un type par ID
     */
    public void supprimerType(int id) throws SQLException {
        String sql = "DELETE FROM type_medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Modifier un type existant
     */
    public void modifierType(TypeMedicament type) throws SQLException {
        String sql = "UPDATE type_medicament SET libelle=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.getLibelle());
            stmt.setInt(2, type.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Récupérer tous les types
     */
    public List<TypeMedicament> getAllTypes() throws SQLException {
        List<TypeMedicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM type_medicament";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TypeMedicament type = new TypeMedicament();
                type.setId(rs.getInt("id"));
                type.setLibelle(rs.getString("libelle"));
                liste.add(type);
            }
        }
        return liste;
    }

    /**
     * Récupérer un type par ID
     */
    public TypeMedicament getTypeById(int id) throws SQLException {
        String sql = "SELECT * FROM type_medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TypeMedicament type = new TypeMedicament();
                type.setId(rs.getInt("id"));
                type.setLibelle(rs.getString("libelle"));
                return type;
            }
        }
        return null;
    }
}