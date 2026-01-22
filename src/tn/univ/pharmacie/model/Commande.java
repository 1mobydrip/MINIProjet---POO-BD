package tn.univ.pharmacie.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private Fournisseur fournisseur;
    private Employe employe;
    private LocalDate date;
    private StatutCommande statut;

    public enum StatutCommande {
        en_attente,
        livree,
        annulee
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public StatutCommande getStatut() {
        return statut;
    }

}
