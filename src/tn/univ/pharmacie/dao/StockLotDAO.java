package tn.univ.pharmacie.dao;

import tn.univ.pharmacie.model.StockLot;
import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.model.Fournisseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockLotDAO {

    /**
     * Ajouter un lot de stock
     */
    public void ajouterStockLot(StockLot lot) throws SQLException {
        String sql = "INSERT INTO stock_lot (medicament_id, fournisseur_id, quantite, date_expiration) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lot.getMedicament().getId());
            stmt.setInt(2, lot.getFournisseur().getId());
            stmt.setInt(3, lot.getQuantite());
            stmt.setDate(4, Date.valueOf(lot.getDateExpiration()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lot.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Supprimer un lot de stock par ID
     */
    public void supprimerStockLot(int id) throws SQLException {
        String sql = "DELETE FROM stock_lot WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Modifier un lot de stock (quantité ou date d'expiration)
     */
    public void modifierStockLot(StockLot lot) throws SQLException {
        String sql = "UPDATE stock_lot SET medicament_id=?, fournisseur_id=?, quantite=?, date_expiration=? WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lot.getMedicament().getId());
            stmt.setInt(2, lot.getFournisseur().getId());
            stmt.setInt(3, lot.getQuantite());
            stmt.setDate(4, Date.valueOf(lot.getDateExpiration()));
            stmt.setInt(5, lot.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Lister tous les lots de stock
     */
    public List<StockLot> getAllStockLots() throws SQLException {
        List<StockLot> liste = new ArrayList<>();
        String sql = "SELECT * FROM stock_lot";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                StockLot lot = new StockLot();
                lot.setId(rs.getInt("id"));

                // Créer les objets Medicament et Fournisseur avec juste l'ID
                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                lot.setMedicament(med);

                Fournisseur fou = new Fournisseur();
                fou.setId(rs.getInt("fournisseur_id"));
                lot.setFournisseur(fou);

                lot.setQuantite(rs.getInt("quantite"));
                lot.setDateExpiration(rs.getDate("date_expiration").toLocalDate());

                liste.add(lot);
            }
        }
        return liste;
    }

    /**
     * Récupérer un lot par ID
     */
    public StockLot getStockLotById(int id) throws SQLException {
        String sql = "SELECT * FROM stock_lot WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StockLot lot = new StockLot();
                lot.setId(rs.getInt("id"));

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                lot.setMedicament(med);

                Fournisseur fou = new Fournisseur();
                fou.setId(rs.getInt("fournisseur_id"));
                lot.setFournisseur(fou);

                lot.setQuantite(rs.getInt("quantite"));
                lot.setDateExpiration(rs.getDate("date_expiration").toLocalDate());
                return lot;
            }
        }
        return null;
    }
}
