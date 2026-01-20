package connexion;

import model.Employe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    /**
     * Ajouter un employé dans la base.
     * Le mot de passe est hashé avant d'être stocké.
     */
    public void ajouterEmploye(Employe emp) throws SQLException {
        String sql = "INSERT INTO employe (nom, prenom, username, password, role) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emp.getNom());
            stmt.setString(2, emp.getPrenom());
            stmt.setString(3, emp.getUsername());
            
            // Hash du mot de passe avant stockage
            stmt.setString(4, SecurityUtils.hashPassword(emp.getPassword()));
            //.name() convertit l’enum en chaîne compatible SQL
            stmt.setString(5, emp.getRole().name());

            stmt.executeUpdate();

            // Récupérer l'ID généré automatiquement
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emp.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Supprimer un employé par ID
     */
    public void supprimerEmploye(int id) throws SQLException {
        String sql = "DELETE FROM employe WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Compter le nombre d'employés
     */
    public int compterEmployes() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM employe";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Récupérer tous les employés
     */
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
                //valueOf convertit la chaîne de la base en enum
                //il faut assurer que la premier letre de role est seulement en maj (valueof sensible a la case) 
                String roleStr = rs.getString("role").trim();
                roleStr = roleStr.substring(0,1).toUpperCase() + roleStr.substring(1).toLowerCase();
                emp.setRole(Employe.EmployeRole.valueOf(roleStr));
                employes.add(emp);
            }
        }
        return employes;
    }

    /**
     * Authentifier un employé par username et mot de passe
     */
    public static Employe authentifier(String username, String password) {
        String sql = "SELECT * FROM employe WHERE username=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Comparer le hash du mot de passe entré avec le hash stocké en base
                String passwordHashInDB = rs.getString("password");
                if (SecurityUtils.hashPassword(password).equals(passwordHashInDB)) {
                    Employe emp = new Employe();
                    emp.setId(rs.getInt("id"));
                    emp.setNom(rs.getString("nom"));
                    emp.setPrenom(rs.getString("prenom"));
                    emp.setUsername(rs.getString("username"));
                    emp.setPassword(passwordHashInDB);
                    String roleStr = rs.getString("role").trim();
                    roleStr = roleStr.substring(0,1).toUpperCase() + roleStr.substring(1).toLowerCase();
                    emp.setRole(Employe.EmployeRole.valueOf(roleStr));
                    
                    return emp;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // authentification échouée
    }
 // On utilise getEmployeByUsername pour récupérer l'objet Employe depuis la base
 // afin de pouvoir travailler avec ses données en Java (nom, rôle, etc.)
    public Employe getEmployeByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM employe WHERE username=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employe emp = new Employe();
                emp.setId(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));

                // Normaliser le rôle
                String roleStr = rs.getString("role").trim();
                roleStr = roleStr.substring(0,1).toUpperCase() + roleStr.substring(1).toLowerCase();
                emp.setRole(Employe.EmployeRole.valueOf(roleStr));

                return emp;
            }
        }
        return null; // pas trouvé
    }


}
