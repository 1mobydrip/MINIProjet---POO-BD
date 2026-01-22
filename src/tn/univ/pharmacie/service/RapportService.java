package tn.univ.pharmacie.service;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tn.univ.pharmacie.model.Fournisseur;
import tn.univ.pharmacie.model.Medicament;
import tn.univ.pharmacie.model.StockLot;


public class RapportService {
    private GestionStock gestionStock = new GestionStock();
    private GestionVentes gestionVentes = new GestionVentes();
    private GestionFournisseurs gestionFournisseurs = new GestionFournisseurs();


    public String genererEtatStock() {
        List<StockLot> stocks = null;
        try {
            stocks = gestionStock.consulterStock();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        sb.append("Etat du stock:\n");

        if (stocks.isEmpty()) {
            sb.append("Aucun stock disponible.\n");
            return sb.toString();
        }

        for (StockLot lot : stocks) {
            Medicament m = lot.getMedicament();
            String nom = (m != null) ? m.getNom() : "<inconnu>";
            String exp = (lot.getDateExpiration() == null) ? "N/A" : lot.getDateExpiration().format(fmt);
            sb.append(String.format("Medicament: %s (ID:%d) - Qté: %d - Exp: %s", nom,
                    (m != null ? m.getId() : 0), lot.getQuantite(), exp));
            if (lot.getQuantite() < gestionStock.getSeuilStock()) {
                sb.append(" - ALERTE: faible stock");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    public double genererChiffreAffaires() {
        try {
            return gestionVentes.calculerMontantTotalVentes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<Fournisseur, Double> genererPerformanceFournisseurs() {
        List<Fournisseur> fournisseurs = gestionFournisseurs.listerFournisseurs();
        Map<Fournisseur, Double> performance = new HashMap<>();

        for (Fournisseur f : fournisseurs) {
            double perf = gestionFournisseurs.calculerPerformanceFournisseur(f.getId());
            performance.put(f, perf);
        }

        return performance;
    }


    public String genererRapportPerformanceFournisseursTexte() {
        Map<Fournisseur, Double> perf = genererPerformanceFournisseurs();
        StringBuilder sb = new StringBuilder();
        sb.append("Performance des fournisseurs (pourcentage de livraisons réussies):\n");
        for (Map.Entry<Fournisseur, Double> e : perf.entrySet()) {
            Fournisseur f = e.getKey();
            sb.append(String.format("%s (ID:%d) : %.2f%%\n", f.getNom(), f.getId(), e.getValue()));
        }
        return sb.toString();
    }
}
