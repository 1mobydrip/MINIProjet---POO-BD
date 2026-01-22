package tn.univ.pharmacie.service;

import tn.univ.pharmacie.dao.StockLotDAO;
import tn.univ.pharmacie.model.StockLot;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestionStock {
    private StockLotDAO stockLotDAO = new StockLotDAO();
    private static final int SEUIL_STOCK = 10;


    public void ajouterLotStock(StockLot lot) throws SQLException {
        if (lot == null || lot.getMedicament() == null) {
            throw new IllegalArgumentException("Lot invalide");
        }
        if (lot.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        stockLotDAO.ajouterStockLot(lot);
    }

    public void supprimerLotStock(int id) throws SQLException {
        stockLotDAO.supprimerStockLot(id);
    }

    public List<StockLot> consulterStock() throws SQLException {
        return stockLotDAO.getAllStockLots();
    }

    public int getSeuilStock() {
        return SEUIL_STOCK;
    }

    public void mettreAJourStockParId(int stockId, int quantite, LocalDate dateExpiration) throws SQLException {
        StockLot lot = stockLotDAO.getStockLotById(stockId);
        if (lot == null) {
            throw new IllegalArgumentException("Aucun stock trouvé avec l'ID " + stockId);
        }
        lot.setQuantite(quantite);
        lot.setDateExpiration(dateExpiration);
        stockLotDAO.modifierStockLot(lot);
    }
}