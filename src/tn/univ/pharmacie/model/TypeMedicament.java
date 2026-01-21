package tn.univ.pharmacie.model;

public class TypeMedicament {
    private Medicament medicament;
    private int id;
    private String libelle;

    public  int getId () {
        return id;
    }
    public void setId (int id) {
        this.id = id;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMedicamentId(int id) {
        this.medicament.setId(id);
    }
    public int getMedicamentId() {
        return medicament.getId();
    }
}
