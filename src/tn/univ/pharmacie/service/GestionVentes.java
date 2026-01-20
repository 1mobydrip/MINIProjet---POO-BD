package tn.univ.pharmacie.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.Vente;

public class GestionVentes {
    private static List<Vente> ventes = new ArrayList<>();
    private static int nextId = 1;

    public void enregistrerVente(Vente v) {
        if (v == null || v.getClien() == null) {
            throw new IllegalArgumentException("La vente et le client sont obligatoires");
        }
        
        if (v.getEmploye() == null) {
            throw new IllegalArgumentException("L'employé est obligatoire");
        }
        
        if (v.getMontantTotal() <= 0) {
            throw new IllegalArgumentException("Le montant total doit être positif");
        }
        
        if (v.getDetails() == null || v.getDetails().isEmpty()) {
            throw new IllegalArgumentException("La vente doit contenir au moins un détail");
        }
        
        v.setId(nextId++);
        v.setDateVente(LocalDate.now());
        ventes.add(v);
    }

    public Vente consulterVente(int venteId) {
        for (Vente vente : ventes) {
            if (vente.getId() == venteId) {
                return vente;
            }
        }
        return null;
    }

    public List<Vente> consulterHistoriqueClient(int clientId) {
        List<Vente> historique = new ArrayList<>();
        for (Vente vente : ventes) {
            if (vente.getClien() != null && vente.getClien().getId() == clientId) {
                historique.add(vente);
            }
        }
        return historique;
    }

    public List<Vente> listerVentes() {
        return new ArrayList<>(ventes);
    }

    public List<Vente> listerVentesParEmploye(int employeId) {
        List<Vente> results = new ArrayList<>();
        for (Vente vente : ventes) {
            if (vente.getEmploye() != null && vente.getEmploye().getId() == employeId) {
                results.add(vente);
            }
        }
        return results;
    }

    public List<Vente> listerVentesParDate(LocalDate date) {
        List<Vente> results = new ArrayList<>();
        for (Vente vente : ventes) {
            if (vente.getDateVente().equals(date)) {
                results.add(vente);
            }
        }
        return results;
    }

    public double calculerMontantTotalVentes() {
        double total = 0;
        for (Vente vente : ventes) {
            total += vente.getMontantTotal();
        }
        return total;
    }

    public double calculerMontantTotalClientId(int clientId) {
        double total = 0;
        for (Vente vente : ventes) {
            if (vente.getClien() != null && vente.getClien().getId() == clientId) {
                total += vente.getMontantTotal();
            }
        }
        return total;
    }

    public int getTotalVentes() {
        return ventes.size();
    }

    public int getNombreVentesClient(int clientId) {
        return consulterHistoriqueClient(clientId).size();
    }
}