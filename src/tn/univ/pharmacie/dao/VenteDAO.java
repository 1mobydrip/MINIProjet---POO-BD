package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.Vente;
import tn.univ.pharmacie.model.Client;
import tn.univ.pharmacie.model.Employe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {


    public void ajouterVente(Vente vente) throws SQLException {
        String sql = "INSERT INTO vente (client_id, employe_id, date_vente) VALUES (?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vente.getClient().getId());
            stmt.setInt(2, vente.getEmploye().getId());
            stmt.setDate(3, Date.valueOf(vente.getDateVente()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vente.setId(rs.getInt(1));
                }
            }
        }
    }

    public void supprimerVente(int id) throws SQLException {
        String sql = "DELETE FROM vente WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierVente(Vente vente) throws SQLException {
        String sql = "UPDATE vente SET client_id=?, employe_id=?, date_vente=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vente.getClient().getId());
            stmt.setInt(2, vente.getEmploye().getId());
            stmt.setDate(3, Date.valueOf(vente.getDateVente()));
            stmt.setInt(4, vente.getId());

            stmt.executeUpdate();
        }
    }

    public List<Vente> getAllVentes() throws SQLException {
        List<Vente> liste = new ArrayList<>();
        String sql = "SELECT * FROM vente";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vente vente = new Vente();
                vente.setId(rs.getInt("id"));


                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                vente.setClient(client);

                Employe employe = new Employe();
                employe.setId(rs.getInt("employe_id"));
                vente.setEmploye(employe);

                vente.setDateVente(rs.getDate("date_vente").toLocalDate());
                liste.add(vente);
            }
        }
        return liste;
    }

    public Vente getVenteById(int id) throws SQLException {
        String sql = "SELECT * FROM vente WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Vente vente = new Vente();
                vente.setId(rs.getInt("id"));

                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                vente.setClient(client);

                Employe employe = new Employe();
                employe.setId(rs.getInt("employe_id"));
                vente.setEmploye(employe);

                vente.setDateVente(rs.getDate("date_vente").toLocalDate());
                return vente;
            }
        }
        return null;
    }
}
