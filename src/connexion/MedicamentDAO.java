package connexion;

import model.Medicament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MedicamentDAO {
	/**
	 * Ajoute un médicament dans la base.
	 * 
	 * On doit fournir :
	 *  - med : l’objet Medicament avec nom, description, prix
	 *  - typeId : l’ID du type de médicament existant en base (clé étrangère)
	 *  - fournisseurId : l’ID du fournisseur existant en base (clé étrangère)
	 * 
	 * Cette méthode remplit la table `medicament` avec toutes ces informations
	 * et récupère automatiquement l’ID généré pour le stocker dans l’objet med.
	 */

	public void ajouterMedicament(Medicament med, int typeId, int fournisseurId) throws SQLException {
	    String sql = "INSERT INTO medicament (nom, description, prix, type_id, fournisseur_id) VALUES (?,?,?,?,?)";
	    try (Connection conn = ConnexionBD.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        stmt.setString(1, med.getNom());
	        stmt.setString(2, med.getDescription());
	        stmt.setBigDecimal(3, BigDecimal.valueOf(med.getPrix()));
	        stmt.setInt(4, typeId);          
	        stmt.setInt(5, fournisseurId);  

	        stmt.executeUpdate();

	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                med.setId(rs.getInt(1));
	            }
	        }
	    }
	}

    public void supprimerMedicament(int id) throws SQLException {
        String sql = "DELETE FROM medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierMedicament(Medicament med) throws SQLException {
        String sql = "UPDATE medicament SET nom=?, description=?, prix=?,WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, med.getNom());
            stmt.setString(2, med.getDescription());
            stmt.setBigDecimal(3, BigDecimal.valueOf(med.getPrix()));
          
            stmt.setInt(6, med.getId());

            stmt.executeUpdate();
        }
    }

    public List<Medicament> getAllMedicaments() throws SQLException {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM medicament";
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicament med = new Medicament();
                med.setId(rs.getInt("id"));
                med.setNom(rs.getString("nom"));
                med.setDescription(rs.getString("description"));
                med.setPrix(rs.getBigDecimal("prix").doubleValue());

                // Créer l'objet TypeMedicament minimal avec ID
                med.setType(new model.TypeMedicament());
                med.getType().setId(rs.getInt("type_id"));

               // med.setFournisseurId(rs.getInt("fournisseur_id"));
                liste.add(med);
            }
        }
        return liste;
    }

    public Medicament getMedicamentById(int id) throws SQLException {
        String sql = "SELECT * FROM medicament WHERE id=?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Medicament med = new Medicament();
                med.setId(rs.getInt("id"));
                med.setNom(rs.getString("nom"));
                med.setDescription(rs.getString("description"));
                med.setPrix(rs.getBigDecimal("prix").doubleValue());

                med.setType(new model.TypeMedicament());
               
                return med;
            }
        }
        return null;
    }
}
