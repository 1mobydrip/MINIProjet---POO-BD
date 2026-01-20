package tn.univ.pharmacie.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.Commande;
import tn.univ.pharmacie.model.Commande.StatutCommande;


public class GestionCommandes {
    private static List<Commande> commandes = new ArrayList<>();
    private static int nextId = 1;


    public void creerCommande(Commande c) {
        if (c == null || c.getFournisseur() == null) {
            throw new IllegalArgumentException("La commande et le fournisseur sont obligatoires");
        }
        
        if (c.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        
        c.setId(nextId++);
        c.setDate(LocalDate.now());
        c.setStatut(StatutCommande.EN_COURS);
        commandes.add(c);
    }

    public void modifierCommande(Commande c) {
        if (c == null || c.getId() <= 0) {
            throw new IllegalArgumentException("Commande invalide");
        }
        
        Commande existing = consulterCommande(c.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + c.getId() + " non trouvée");
        }
        
        // On ne peut modifier que les commandes EN_COURS
        if (existing.getStatut() != StatutCommande.EN_COURS) {
            throw new IllegalArgumentException("On ne peut modifier que les commandes en cours");
        }
        
        existing.setFournisseur(c.getFournisseur());
        existing.setEmploye(c.getEmploye());
        existing.setMontant(c.getMontant());
        existing.setDetails(c.getDetails());
    }

    public void annulerCommande(int commandeId) {
        Commande commande = consulterCommande(commandeId);
        if (commande == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + commandeId + " non trouvée");
        }
        
        if (commande.getStatut() == StatutCommande.RECEPTIONNEE) {
            throw new IllegalArgumentException("On ne peut pas annuler une commande déjà réceptionnée");
        }
        
        commande.setStatut(StatutCommande.ANNULEE);
    }

    public void recevoirCommande(int commandeId) {
        Commande commande = consulterCommande(commandeId);
        if (commande == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + commandeId + " non trouvée");
        }
        
        if (commande.getStatut() != StatutCommande.VALIDEE) {
            throw new IllegalArgumentException("Seules les commandes validées peuvent être réceptionnées");
        }
        
        commande.setStatut(StatutCommande.RECEPTIONNEE);
    }

    public void validerCommande(int commandeId) {
        Commande commande = consulterCommande(commandeId);
        if (commande == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + commandeId + " non trouvée");
        }
        
        if (commande.getStatut() != StatutCommande.EN_COURS) {
            throw new IllegalArgumentException("Seules les commandes en cours peuvent être validées");
        }
        
        commande.setStatut(StatutCommande.VALIDEE);
    }

    public Commande consulterCommande(int commandeId) {
        for (Commande commande : commandes) {
            if (commande.getId() == commandeId) {
                return commande;
            }
        }
        return null;
    }

    public List<Commande> listerCommandes() {
        return new ArrayList<>(commandes);
    }

    public List<Commande> listerCommandesParStatut(StatutCommande statut) {
        List<Commande> results = new ArrayList<>();
        for (Commande commande : commandes) {
            if (commande.getStatut() == statut) {
                results.add(commande);
            }
        }
        return results;
    }

    public List<Commande> listerCommandesParFournisseur(int fournisseurId) {
        List<Commande> results = new ArrayList<>();
        for (Commande commande : commandes) {
            if (commande.getFournisseur() != null && commande.getFournisseur().getId() == fournisseurId) {
                results.add(commande);
            }
        }
        return results;
    }

    public double calculerMontantTotal() {
        double total = 0;
        for (Commande commande : commandes) {
            total += commande.getMontant();
        }
        return total;
    }

    public int getTotalCommandes() {
        return commandes.size();
    }
}