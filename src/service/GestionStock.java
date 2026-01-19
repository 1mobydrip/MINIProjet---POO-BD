package service;

import exception.CommandeInvalideException;
import exception.MedicamentInexistantException;
import exception.QuantiteInvalideException;
import exception.StockInsuffisantException;
import model.Fournisseur;
import model.Medicament;
import model.StockLot;
import service.interfaces.IGestionStock;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestionStock implements IGestionStock {
    private List<StockLot> stockLots = new ArrayList<>();
    private static final int SEUIL_MINIMAL_GLOBAL = 10;
    public void ajouterMedicament(Medicament medicament,Fournisseur fournisseur,int quantite,LocalDate dateExpiration) {
        if (medicament == null)
            throw new MedicamentInexistantException("Médicament invalide");
        if (fournisseur == null)
            throw new CommandeInvalideException("Fournisseur invalide");
        if (quantite <= 0)
            throw new QuantiteInvalideException("Quantité invalide");
        if (dateExpiration == null || dateExpiration.isBefore(LocalDate.now())) {
            throw new CommandeInvalideException("Date d'expiration invalide");
        }
        StockLot lot = new StockLot();
        lot.setMedicament(medicament);
        lot.setFournisseur(fournisseur);
        lot.setQuantite(quantite);
        lot.setDateExpiration(dateExpiration);

        stockLots.add(lot);
    }
    public void retirerMedicament(Medicament medicament, int quantite) {
        if (medicament == null)
            throw new MedicamentInexistantException("Médicament invalide");
        if (quantite <= 0)
            throw new QuantiteInvalideException("Quantité invalide");
        // Récupérer les lots du médicament, triés par date d'expiration (FEFO)
        List<StockLot> lots = stockLots.stream().filter(lot -> lot.getMedicament().equals(medicament))
                .sorted(Comparator.comparing(StockLot::getDateExpiration)).toList();
        int quantiteRestante = quantite;
        for (StockLot lot : lots) {
            if (quantiteRestante <= 0)
                break;
            int qteLot = lot.getQuantite();
            if (qteLot <= quantiteRestante) {
                quantiteRestante -= qteLot;
                lot.setQuantite(0);
            } else{
                lot.setQuantite(qteLot - quantiteRestante);
                quantiteRestante = 0;
            }
        }
        if (quantiteRestante > 0)
            throw new StockInsuffisantException("Stock insuffisant pour ce medicament");
        // Supprimer les lots vides
        stockLots.removeIf(lot -> lot.getQuantite() == 0);
    }
    private List<StockLot> trouverLotsParMedicament(Medicament medicament) {
        List<StockLot> lotsMedicaments = new ArrayList<>();
        for (StockLot lot : stockLots) {
            if (lot.getMedicament().equals(medicament))
                lotsMedicaments.add(lot);
        }
        return lotsMedicaments;
    }
    public int getQuantiteDisponible(Medicament medicament){
        if (medicament == null)
            throw new MedicamentInexistantException("Medicament invalide");
        List<StockLot> stock = trouverLotsParMedicament(medicament);
        if (stock.isEmpty())
            throw new MedicamentInexistantException("Medicament inexistant");
        int q=0;
        for(StockLot lot : stock){
            q+=lot.getQuantite();
        }
        return q;
    }
    public boolean verifierDisponibile(Medicament medicament,int quantite){
        if (medicament == null)
            throw new MedicamentInexistantException("Medicament invalide");
        if (quantite <= 0)
            throw new QuantiteInvalideException("Quantite invalide");
        List<StockLot> stock = trouverLotsParMedicament(medicament);
        if(stock.isEmpty())
            throw new MedicamentInexistantException("Medicament inexistant");
        if(getQuantiteDisponible(medicament)>0)
            return true;
        else
            return false;
    }
    public void verifierSeuilStock(Medicament medicament){
        if (medicament == null)
            throw new MedicamentInexistantException("Medicament invalide");
        List<StockLot> stock = trouverLotsParMedicament(medicament);
        if(stock.isEmpty())
            throw new MedicamentInexistantException("Medicament inexistant");
        if(getQuantiteDisponible(medicament)<=SEUIL_MINIMAL_GLOBAL)
            System.out.println("Seuil minimal atteint !!");
    }
}
