package tn.univ.pharmacie.model;

import java.time.LocalDate;

public class Vente {
    private int id;
    private Client client;
    private Employe employe;
    private LocalDate dateVente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client clien) {
        this.client = clien;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }


}
