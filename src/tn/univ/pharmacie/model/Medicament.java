package tn.univ.pharmacie.model;

public class Medicament {
    private int id;
    private String nom;
    private double prix;
    private String description;
    private TypeMedicament type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeMedicament getType() {
        return type;
    }

    public void setType(TypeMedicament type) {
        this.type = type;
    }
}
