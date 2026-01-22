package tn.univ.pharmacie.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.*;

public class EmployeDAO {

    public static Employe authentifier(String username, String password) {
        String sql = "SELECT * FROM employe WHERE username=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Comparer le hash du mot de passe entré avec le hash stocké en base
                String passwordInDB = rs.getString("password");
                if (password.equals(passwordInDB)) {
                    Employe emp = new Employe();
                    emp.setId(rs.getInt("id"));
                    emp.setNom(rs.getString("nom"));
                    emp.setPrenom(rs.getString("prenom"));
                    emp.setUsername(rs.getString("username"));
                    emp.setPassword(passwordInDB);
                    String roleStr = rs.getString("role").trim();
                    roleStr = roleStr.substring(0,1).toUpperCase() + roleStr.substring(1).toLowerCase();
                    emp.setRole(Employe.EmployeRole.valueOf(roleStr));

                    return emp;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ajouterEmploye(Employe emp) throws SQLException {
        String sql = "INSERT INTO employe (nom, prenom, username, password, role) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emp.getNom());
            stmt.setString(2, emp.getPrenom());
            stmt.setString(3, emp.getUsername());

            stmt.setString(4, emp.getPassword());

            stmt.setString(5, emp.getRole().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emp.setId(rs.getInt(1));
                }
            }
        }
    }

    public void supprimerEmploye(int id) throws SQLException {
        String sql = "DELETE FROM employe WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Employe> getAllEmployes() throws SQLException {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employe";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employe emp = new Employe();
                emp.setId(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                String roleStr = rs.getString("role").trim();
                roleStr = roleStr.substring(0,1).toUpperCase() + roleStr.substring(1).toLowerCase();
                emp.setRole(Employe.EmployeRole.valueOf(roleStr));
                employes.add(emp);
            }
        }
        return employes;
    }

    public Employe getEById(int id) throws SQLException {
        String sql = "SELECT * FROM employe WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employe emp = new Employe();
                emp.setId(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                String roleStr = rs.getString("role").trim();
                roleStr = roleStr.substring(0,1).toUpperCase() + roleStr.substring(1).toLowerCase();
                emp.setRole(Employe.EmployeRole.valueOf(roleStr));
                return emp;
            }
        }
        return null;
    }


}