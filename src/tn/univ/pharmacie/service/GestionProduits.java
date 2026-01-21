package tn.univ.pharmacie.service;

import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.dao.MedicamentDAO;

import java.sql.SQLException;
import java.util.List;

public class GestionProduits {
    private MedicamentDAO medicamentDAO = new MedicamentDAO();

    public void ajouterProduit(Medicament p) throws SQLException {
        if (p == null || p.getNom() == null || p.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire");
        }
        if (p.getPrix() <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        medicamentDAO.ajouterMedicament(p);
    }

    public void modifierProduit(Medicament p) throws SQLException {
        if (p == null || p.getId() <= 0) {
            throw new IllegalArgumentException("Produit invalide");
        }
        medicamentDAO.modifierMedicament(p);
    }

    public void supprimerProduit(int produitId) throws SQLException {
        medicamentDAO.supprimerMedicament(produitId);
    }

    public Medicament consulterProduit(int produitId) throws SQLException {
        return medicamentDAO.getMedicamentById(produitId);
    }

    public List<Medicament> listerProduits() throws SQLException {
        return medicamentDAO.getAllMedicaments();
    }

    public List<Medicament> rechercherProduitParNom(String nom) throws SQLException {
        // Simple filter on all products
        return listerProduits().stream()
                .filter(m -> m.getNom().toLowerCase().contains(nom.toLowerCase()))
                .toList();
    }

    public int getTotalProduits() throws SQLException {
        return listerProduits().size();
    }
}