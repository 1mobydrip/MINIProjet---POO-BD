package connexion;

import model.VenteDetail;
import model.Vente;
import model.Medicament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDetailDAO {

    // Ajouter un détail de vente
    public void ajouterVenteDetail(VenteDetail vd) throws SQLException {
        String sql = "INSERT INTO vente_detail (vente_id, medicament_id, quantite) VALUES (?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vd.getVente().getId());
            stmt.setInt(2, vd.getMedicament().getId());
            stmt.setInt(3, vd.getQuantite());

            stmt.executeUpdate();
        }
    }

    // Supprimer un détail de vente par vente_id et medicament_id
    public void supprimerVenteDetail(int venteId, int medicamentId) throws SQLException {
        String sql = "DELETE FROM vente_detail WHERE vente_id=? AND medicament_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venteId);
            stmt.setInt(2, medicamentId);

            stmt.executeUpdate();
        }
    }

    // Mettre à jour la quantité d'un détail de vente
    public void modifierQuantite(int venteId, int medicamentId, int nouvelleQuantite) throws SQLException {
        String sql = "UPDATE vente_detail SET quantite=? WHERE vente_id=? AND medicament_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, venteId);
            stmt.setInt(3, medicamentId);

            stmt.executeUpdate();
        }
    }

    // Lister tous les détails d'une vente
    public List<VenteDetail> getDetailsByVente(int venteId) throws SQLException {
        List<VenteDetail> liste = new ArrayList<>();
        String sql = "SELECT * FROM vente_detail WHERE vente_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VenteDetail vd = new VenteDetail();

                // Créer les objets Vente et Medicament avec juste l'ID
                Vente vente = new Vente();
                vente.setId(rs.getInt("vente_id"));
                vd.setVente(vente);

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                vd.setMedicament(med);

                vd.setQuantite(rs.getInt("quantite"));
                liste.add(vd);
            }
        }
        return liste;
    }

    // Lister tous les détails de toutes les ventes
    public List<VenteDetail> getAllVenteDetails() throws SQLException {
        List<VenteDetail> liste = new ArrayList<>();
        String sql = "SELECT * FROM vente_detail";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VenteDetail vd = new VenteDetail();

                Vente vente = new Vente();
                vente.setId(rs.getInt("vente_id"));
                vd.setVente(vente);

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                vd.setMedicament(med);

                vd.setQuantite(rs.getInt("quantite"));
                liste.add(vd);
            }
        }
        return liste;
    }
}
