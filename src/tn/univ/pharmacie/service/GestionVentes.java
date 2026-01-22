package tn.univ.pharmacie.service;

import tn.univ.pharmacie.dao.VenteDAO;
import tn.univ.pharmacie.dao.VenteDetailDAO;
import tn.univ.pharmacie.dao.StockLotDAO;
import tn.univ.pharmacie.model.Vente;
import tn.univ.pharmacie.model.VenteDetail;
import tn.univ.pharmacie.model.StockLot;
import tn.univ.pharmacie.dao.MedicamentDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestionVentes {
    private VenteDAO venteDAO = new VenteDAO();
    private VenteDetailDAO venteDetailDAO = new VenteDetailDAO();
    private StockLotDAO stockLotDAO = new StockLotDAO();
    private MedicamentDAO medicamentDAO = new MedicamentDAO();


    public void enregistrerVente(VenteDetail vd, Vente v) throws SQLException {
        if (vd.getVente() == null || vd.getVente().getClient() == null) {
            throw new IllegalArgumentException("La vente et le client sont obligatoires");
        }
        if (vd.getVente().getEmploye() == null) {
            throw new IllegalArgumentException("L'employé est obligatoire");
        }
        if (vd.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        // Vérifier stock
        StockLot lot = stockLotDAO.getStockLotByMedicamentId(vd.getMedicament().getId());
        if (lot == null) {
            throw new IllegalArgumentException("Produit " + vd.getMedicament().getNom() + " non disponible en stock");
        }
        if (lot.getQuantite() < vd.getQuantite()) {
            throw new IllegalArgumentException("Stock insuffisant pour " + vd.getMedicament().getNom());
        }

        venteDAO.ajouterVente(v);
        venteDetailDAO.ajouterVenteDetail(vd);

        lot.setQuantite(lot.getQuantite() - vd.getQuantite());
        stockLotDAO.modifierStockLot(lot);
    }

    public List<Vente> consulterHistoriqueClient(int clientId) throws SQLException {
        return venteDAO.getAllVentes().stream()
                .filter(v -> v.getClient() != null && v.getClient().getId() == clientId)
                .toList();
    }

    public double calculerMontantTotalVentes() throws SQLException {
        double total = 0;
        for (VenteDetail vd : venteDetailDAO.getAllVenteDetails()) {
            total += medicamentDAO.getPrixById(vd.getMedicament().getId()) *  vd.getQuantite();
        }
        return total;
    }

    public double calculerMontantTotalClientId(int clientId) throws SQLException {
        double total = 0;
        for (VenteDetail vd : venteDetailDAO.getAllVenteDetails()) {
            if (vd.getVente().getClient() != null && vd.getVente().getClient().getId() == clientId) {
                total += vd.getMontantTotal();
            }
        }
        return total;
    }

    public int getTotalVentes() throws SQLException {
        return venteDAO.getAllVentes().size();
    }

    public VenteDetail getDetailByVente(int venteId) {
        VenteDetailDAO venteDetailDAO = new VenteDetailDAO();
        try {
            List<VenteDetail> details = venteDetailDAO.getDetailsByVente(venteId);

            if (!details.isEmpty()) {
                return details.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération du détail de la vente: " + e.getMessage());
        }

        return null;
    }
}