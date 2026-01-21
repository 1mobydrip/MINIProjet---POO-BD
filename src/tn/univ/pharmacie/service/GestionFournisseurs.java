package tn.univ.pharmacie.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tn.univ.pharmacie.dao.FournisseurDAO;
import tn.univ.pharmacie.model.Fournisseur;

public class GestionFournisseurs {
    private static List<Fournisseur> fournisseurs = new ArrayList<>();
    private static int nextId = 1;
    private static Map<Integer, Integer> livraisonsReussies = new HashMap<>();
    private static Map<Integer, Integer> livraisonsEchouees = new HashMap<>();

    public void ajouterFournisseur(Fournisseur f) {
        if (f == null || f.getNom() == null || f.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fournisseur est obligatoire");
        }
        
        if (f.getTelephone() <= 0) {
            throw new IllegalArgumentException("Le téléphone doit être valide");
        }

        FournisseurDAO dao = new FournisseurDAO();
        try {
            dao.ajouterFournisseur(f);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        livraisonsReussies.put(f.getId(), 0);
        livraisonsEchouees.put(f.getId(), 0);
    }

    public void modifierFournisseur(Fournisseur f) {
        if (f == null || f.getId() <= 0) {
            throw new IllegalArgumentException("Fournisseur invalide");
        }
        
        Fournisseur existing = consulterFournisseur(f.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Fournisseur avec l'ID " + f.getId() + " non trouvé");
        }
        
        existing.setNom(f.getNom());
        existing.setTelephone(f.getTelephone());
        existing.setAdresse(f.getAdresse());
    }

    public void supprimerFournisseur(int fournisseurId) {
        Fournisseur fournisseur = consulterFournisseur(fournisseurId);
        if (fournisseur == null) {
            throw new IllegalArgumentException("Fournisseur avec l'ID " + fournisseurId + " non trouvé");
        }

        FournisseurDAO dao = new FournisseurDAO();
        try {
            dao.supprimerFournisseur(fournisseurId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        livraisonsReussies.remove(fournisseurId);
        livraisonsEchouees.remove(fournisseurId);
    }

    public Fournisseur consulterFournisseur(int fournisseurId) {
        for (Fournisseur fournisseur : fournisseurs) {
            if (fournisseur.getId() == fournisseurId) {
                return fournisseur;
            }
        }
        return null;
    }

    public List<Fournisseur> listerFournisseurs() {
        FournisseurDAO dao = new FournisseurDAO();
        try {
            fournisseurs = dao.getAllFournisseurs();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fournisseurs;
    }

    public double calculerPerformanceFournisseur(int fournisseurId) {
        if (consulterFournisseur(fournisseurId) == null) {
            throw new IllegalArgumentException("Fournisseur avec l'ID " + fournisseurId + " non trouvé");
        }
        
        int reussies = livraisonsReussies.getOrDefault(fournisseurId, 0);
        int echouees = livraisonsEchouees.getOrDefault(fournisseurId, 0);
        int total = reussies + echouees;
        
        if (total == 0) {
            return 0.0;
        }
        
        return (reussies * 100.0) / total;
    }

    public void enregistrerLivraisonReussie(int fournisseurId) {
        if (consulterFournisseur(fournisseurId) == null) {
            throw new IllegalArgumentException("Fournisseur avec l'ID " + fournisseurId + " non trouvé");
        }
        
        livraisonsReussies.put(fournisseurId, livraisonsReussies.getOrDefault(fournisseurId, 0) + 1);
    }

    public void enregistrerLivraisonEchouee(int fournisseurId) {
        if (consulterFournisseur(fournisseurId) == null) {
            throw new IllegalArgumentException("Fournisseur avec l'ID " + fournisseurId + " non trouvé");
        }
        
        livraisonsEchouees.put(fournisseurId, livraisonsEchouees.getOrDefault(fournisseurId, 0) + 1);
    }

    public List<Fournisseur> getMeilleursFournisseurs() {
        List<Fournisseur> meilleurs = new ArrayList<>();
        for (Fournisseur f : fournisseurs) {
            if (calculerPerformanceFournisseur(f.getId()) >= 80.0) {
                meilleurs.add(f);
            }
        }
        return meilleurs;
    }

    public int getTotalFournisseurs() {
        FournisseurDAO dao = new FournisseurDAO();
        try {
            fournisseurs = dao.getAllFournisseurs();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fournisseurs.size();
    }
}
