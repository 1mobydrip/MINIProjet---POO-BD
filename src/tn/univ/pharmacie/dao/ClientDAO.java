package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void ajouterClient(Client client) throws SQLException {
        String sql = "INSERT INTO client (nom, prenom, telephone, adresse) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setInt(3, client.getTelephone());
            stmt.setString(4, client.getAdresse());

            stmt.executeUpdate();
        }
    }

    public void supprimerClient(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE client SET nom = ?, prenom = ?, telephone = ?, adresse = ? WHERE id = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setInt(3, client.getTelephone());
            stmt.setString(4, client.getAdresse());
            stmt.setInt(5, client.getId());

            stmt.executeUpdate();
        }
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setTelephone(rs.getInt("telephone"));
                c.setAdresse(rs.getString("adresse"));
                clients.add(c);
            }
        }
        return clients;
    }


}
