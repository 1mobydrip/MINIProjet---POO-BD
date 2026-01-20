package connexion;

import model.CommandeDetail;
import model.Commande;
import model.Medicament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDetailDAO {

    // Ajouter un détail de commande
    public void ajouterCommandeDetail(CommandeDetail cd) throws SQLException {
        String sql = "INSERT INTO commande_detail (commande_id, medicament_id, quantite) VALUES (?,?,?)";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cd.getCommande().getId());
            stmt.setInt(2, cd.getMedicament().getId());
            stmt.setInt(3, cd.getQuantite());

            stmt.executeUpdate();
        }
    }

    // Supprimer un détail de commande par commande_id et medicament_id
    public void supprimerCommandeDetail(int commandeId, int medicamentId) throws SQLException {
        String sql = "DELETE FROM commande_detail WHERE commande_id=? AND medicament_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, commandeId);
            stmt.setInt(2, medicamentId);

            stmt.executeUpdate();
        }
    }

    // Mettre à jour la quantité d'un détail de commande
    public void modifierQuantite(int commandeId, int medicamentId, int nouvelleQuantite) throws SQLException {
        String sql = "UPDATE commande_detail SET quantite=? WHERE commande_id=? AND medicament_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, commandeId);
            stmt.setInt(3, medicamentId);

            stmt.executeUpdate();
        }
    }

    // Lister tous les détails d'une commande
    public List<CommandeDetail> getDetailsByCommande(int commandeId) throws SQLException {
        List<CommandeDetail> liste = new ArrayList<>();
        String sql = "SELECT * FROM commande_detail WHERE commande_id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CommandeDetail cd = new CommandeDetail();

                // Créer les objets avec seulement l'ID
                Commande commande = new Commande();
                commande.setId(rs.getInt("commande_id"));
                cd.setCommande(commande);

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                cd.setMedicament(med);

                cd.setQuantite(rs.getInt("quantite"));
                liste.add(cd);
            }
        }
        return liste;
    }

    // Lister tous les détails de toutes les commandes
    public List<CommandeDetail> getAllCommandeDetails() throws SQLException {
        List<CommandeDetail> liste = new ArrayList<>();
        String sql = "SELECT * FROM commande_detail";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CommandeDetail cd = new CommandeDetail();

                Commande commande = new Commande();
                commande.setId(rs.getInt("commande_id"));
                cd.setCommande(commande);

                Medicament med = new Medicament();
                med.setId(rs.getInt("medicament_id"));
                cd.setMedicament(med);

                cd.setQuantite(rs.getInt("quantite"));
                liste.add(cd);
            }
        }
        return liste;
    }
}
