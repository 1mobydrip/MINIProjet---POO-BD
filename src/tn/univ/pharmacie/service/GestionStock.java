package tn.univ.pharmacie.service;

import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.StockLot;


public class GestionStock {
    private static List<StockLot> stocks = new ArrayList<>();
    private static int nextId = 1;
    private static final int SEUIL_STOCK = 10; // Seuil minimum de stock

    public void mettreAJourStock(int medicamentId, int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        
        for (StockLot stock : stocks) {
            if (stock.getMedicament() != null && stock.getMedicament().getId() == medicamentId) {
                stock.setQuantite(quantite);
                return;
            }
        }
        
        throw new IllegalArgumentException("Aucun stock trouvé pour le médicament avec l'ID " + medicamentId);
    }

    public void ajouterQuantiteStock(int medicamentId, int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité à ajouter doit être positive");
        }
        
        for (StockLot stock : stocks) {
            if (stock.getMedicament() != null && stock.getMedicament().getId() == medicamentId) {
                stock.setQuantite(stock.getQuantite() + quantite);
                return;
            }
        }
        
        throw new IllegalArgumentException("Aucun stock trouvé pour le médicament avec l'ID " + medicamentId);
    }

    public void retirerQuantiteStock(int medicamentId, int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité à retirer doit être positive");
        }
        
        for (StockLot stock : stocks) {
            if (stock.getMedicament() != null && stock.getMedicament().getId() == medicamentId) {
                if (stock.getQuantite() < quantite) {
                    throw new IllegalArgumentException("Stock insuffisant pour le médicament avec l'ID " + medicamentId);
                }
                stock.setQuantite(stock.getQuantite() - quantite);
                return;
            }
        }
        
        throw new IllegalArgumentException("Aucun stock trouvé pour le médicament avec l'ID " + medicamentId);
    }

    public boolean verifierSeuilStock(int medicamentId) {
        for (StockLot stock : stocks) {
            if (stock.getMedicament() != null && stock.getMedicament().getId() == medicamentId) {
                return stock.getQuantite() < SEUIL_STOCK;
            }
        }
        return false;
    }

    public List<StockLot> consulterStock() {
        return new ArrayList<>(stocks);
    }

    public void ajouterLotStock(StockLot lot) {
        if (lot == null || lot.getMedicament() == null) {
            throw new IllegalArgumentException("Le lot de stock est invalide");
        }
        
        if (lot.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité du lot doit être positive");
        }
        
        lot.setId(nextId++);
        stocks.add(lot);
    }

    public int getQuantiteMedicament(int medicamentId) {
        for (StockLot stock : stocks) {
            if (stock.getMedicament() != null && stock.getMedicament().getId() == medicamentId) {
                return stock.getQuantite();
            }
        }
        return 0;
    }

    public int getSeuilStock() {
        return SEUIL_STOCK;
    }
}