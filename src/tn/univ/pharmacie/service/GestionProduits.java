package tn.univ.pharmacie.service;

import tn.univ.pharmacie.dao.StockLotDAO;
import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.dao.MedicamentDAO;
import tn.univ.pharmacie.model.StockLot;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestionProduits {
    private MedicamentDAO medicamentDAO = new MedicamentDAO();
    private GestionStock gestionStock = new GestionStock();

    public void ajouterProduit(Medicament p) throws SQLException {
        if (p == null || p.getNom() == null || p.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire");
        }
        if (p.getPrix() <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        medicamentDAO.ajouterMedicament(p);

        StockLot l = new StockLot();
        l.setMedicament(p);
        l.setFournisseur(p.getFournisseur());
        l.setQuantite(1);
        l.setDateExpiration(LocalDate.of(2027, 1, 1));

        gestionStock.ajouterLotStock(l);
    }

    public void modifierProduit(Medicament p) throws SQLException {
        if (p == null || p.getId() <= 0) {
            throw new IllegalArgumentException("Produit invalide");
        }
        medicamentDAO.modifierMedicament(p);
    }

    public void supprimerProduit(int produitId) throws SQLException {
        medicamentDAO.supprimerMedicament(produitId);

        StockLotDAO sl = new StockLotDAO();
        GestionStock l = new GestionStock();
        StockLot lot = sl.getStockLotByMedicamentId(produitId);
        if (lot != null) {
            l.supprimerLotStock(lot.getId());
        }

    }

    public List<Medicament> listerProduits() throws SQLException {
        return medicamentDAO.getAllMedicaments();
    }

    public List<Medicament> rechercherProduitParNom(String nom) throws SQLException {

        return listerProduits().stream()
                .filter(m -> m.getNom().toLowerCase().contains(nom.toLowerCase()))
                .toList();
    }

}