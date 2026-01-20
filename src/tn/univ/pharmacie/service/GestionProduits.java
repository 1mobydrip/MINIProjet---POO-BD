package tn.univ.pharmacie.service;

import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.Medicament;

public class GestionProduits {
    private static List<Medicament> produits = new ArrayList<>();
    private static int nextId = 1;

    public void ajouterProduit(Medicament p) {
        if (p == null || p.getNom() == null || p.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire");
        }
        
        if (p.getPrix() <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        
        p.setId(nextId++);
        produits.add(p);
    }

    public void modifierProduit(Medicament p) {
        if (p == null || p.getId() <= 0) {
            throw new IllegalArgumentException("Produit invalide");
        }
        
        Medicament existing = consulterProduit(p.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Produit avec l'ID " + p.getId() + " non trouvé");
        }
        
        existing.setNom(p.getNom());
        existing.setPrix(p.getPrix());
        existing.setDescription(p.getDescription());
        existing.setType(p.getType());
    }

    public void supprimerProduit(int produitId) {
        Medicament produit = consulterProduit(produitId);
        if (produit == null) {
            throw new IllegalArgumentException("Produit avec l'ID " + produitId + " non trouvé");
        }
        produits.remove(produit);
    }

    public Medicament consulterProduit(int produitId) {
        for (Medicament produit : produits) {
            if (produit.getId() == produitId) {
                return produit;
            }
        }
        return null;
    }

    public List<Medicament> listerProduits() {
        return new ArrayList<>(produits);
    }

    public List<Medicament> rechercherProduitParNom(String nom) {
        List<Medicament> results = new ArrayList<>();
        for (Medicament produit : produits) {
            if (produit.getNom().toLowerCase().contains(nom.toLowerCase())) {
                results.add(produit);
            }
        }
        return results;
    }

    public int getTotalProduits() {
        return produits.size();
    }
}
