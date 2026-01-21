package tn.univ.pharmacie.model;

import java.time.LocalDate;

public class StockLot {
    private int id;
    private Medicament medicament;
    private Fournisseur fournisseur;
    private int quantite;
    private LocalDate dateExpiration;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Medicament getMedicament() {
        return medicament;
    }
    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }
    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
}