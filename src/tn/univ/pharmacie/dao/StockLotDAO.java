package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.StockLot;
import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.model.Fournisseur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockLotDAO {

    public void ajouterStockLot(StockLot lot) throws SQLException {
        String sql = "INSERT INTO stock_lot (medicament_id, fournisseur_id, quantite, date_expiration) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lot.getMedicament().getId());
            stmt.setInt(2, lot.getFournisseur().getId());
            stmt.setInt(3, lot.getQuantite());
            if (lot.getDateExpiration() != null) {
                stmt.setDate(4, Date.valueOf(lot.getDateExpiration()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lot.setId(rs.getInt(1));
                }
            }
        }
    }

    public void supprimerStockLot(int id) throws SQLException {
        String sql = "DELETE FROM stock_lot WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierStockLot(StockLot lot) throws SQLException {
        String sql = "UPDATE stock_lot SET medicament_id=?, fournisseur_id=?, quantite=?, date_expiration=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lot.getMedicament().getId());
            stmt.setInt(2, lot.getFournisseur().getId());
            stmt.setInt(3, lot.getQuantite());
            if (lot.getDateExpiration() != null) {
                stmt.setDate(4, Date.valueOf(lot.getDateExpiration()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, lot.getId());

            stmt.executeUpdate();
        }
    }

    public List<StockLot> getAllStockLots() throws SQLException {
        List<StockLot> liste = new ArrayList<>();
        String sql = "SELECT s.*, m.nom AS med_nom, f.nom AS fou_nom " +
                "FROM stock_lot s " +
                "JOIN medicament m ON s.medicament_id = m.id " +
                "JOIN fournisseur f ON s.fournisseur_id = f.id";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                StockLot lot = new StockLot();
                lot.setId(rs.getInt("id"));

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                med.setNom(rs.getString("med_nom"));   // ✅ name set
                lot.setMedicament(med);

                Fournisseur fou = new Fournisseur();
                fou.setId(rs.getInt("fournisseur_id"));
                fou.setNom(rs.getString("fou_nom"));   // ✅ name set
                lot.setFournisseur(fou);

                lot.setQuantite(rs.getInt("quantite"));
                Date expDate = rs.getDate("date_expiration");
                lot.setDateExpiration(expDate != null ? expDate.toLocalDate() : null);

                liste.add(lot);
            }
        }
        return liste;
    }

    public StockLot getStockLotById(int id) throws SQLException {
        String sql = "SELECT s.*, m.nom AS med_nom, f.nom AS fou_nom " +
                "FROM stock_lot s " +
                "JOIN medicament m ON s.medicament_id = m.id " +
                "JOIN fournisseur f ON s.fournisseur_id = f.id " +
                "WHERE s.id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StockLot lot = new StockLot();
                lot.setId(rs.getInt("id"));

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                med.setNom(rs.getString("med_nom"));
                lot.setMedicament(med);

                Fournisseur fou = new Fournisseur();
                fou.setId(rs.getInt("fournisseur_id"));
                fou.setNom(rs.getString("fou_nom"));
                lot.setFournisseur(fou);

                lot.setQuantite(rs.getInt("quantite"));
                Date expDate = rs.getDate("date_expiration");
                lot.setDateExpiration(expDate != null ? expDate.toLocalDate() : null);

                return lot;
            }
        }
        return null;
    }

    public StockLot getStockLotByMedicamentId(int medicamentId) throws SQLException {
        String sql = "SELECT s.*, m.nom AS med_nom, f.nom AS fou_nom " +
                "FROM stock_lot s " +
                "JOIN medicament m ON s.medicament_id = m.id " +
                "JOIN fournisseur f ON s.fournisseur_id = f.id " +
                "WHERE s.medicament_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicamentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StockLot lot = new StockLot();
                lot.setId(rs.getInt("id"));

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                med.setNom(rs.getString("med_nom"));
                lot.setMedicament(med);

                Fournisseur fou = new Fournisseur();
                fou.setId(rs.getInt("fournisseur_id"));
                fou.setNom(rs.getString("fou_nom"));
                lot.setFournisseur(fou);

                lot.setQuantite(rs.getInt("quantite"));
                Date expDate = rs.getDate("date_expiration");
                lot.setDateExpiration(expDate != null ? expDate.toLocalDate() : null);

                return lot;
            }
        }
        return null;
    }
}