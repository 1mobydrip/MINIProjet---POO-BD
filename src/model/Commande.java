package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private Fournisseur fournisseur;
    private Employe employe;
    private double montant;
    private LocalDate date;
    private StatutCommande statut;
    private List<CommandeDetail> details;

    public enum StatutCommande{
        EN_COURS,
        VALIDEE,
        RECEPTIONNEE,
        ANNULEE
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

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
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

    public List<CommandeDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CommandeDetail> details) {
        this.details = details;
    }
}
