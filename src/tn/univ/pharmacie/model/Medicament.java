package tn.univ.pharmacie.model;

import java.math.BigDecimal;

public class Medicament {
    private int id;
    private String nom;
    private String description;
    private double prix;
    private TypeMedicament type;
    private Fournisseur fournisseur;

    public Medicament() {
        this.type = new TypeMedicament();
        this.fournisseur = new Fournisseur();
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public TypeMedicament getType() { return type; }
    public void setType(TypeMedicament type) { this.type = type; }

    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public int getFournisseurId() {
        return fournisseur.getId();
    }
    public void setFournisseurId(int fournisseurId) {
        this.fournisseur.setId(fournisseurId);
    }
    public int getTypeId () {
        return type.getId();
    }
    public void setTypeId(int typeId) {
        this.type.setId(typeId);
    }
}
