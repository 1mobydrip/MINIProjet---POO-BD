package tn.univ.pharmacie.model;

import java.time.LocalDate;
import java.util.List;

public class Vente {
    private int id;
    private Client clien;
    private Employe employe;
    private LocalDate dateVente;
    private double montantTotal;
    private List<VenteDetail> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClien() {
        return clien;
    }

    public void setClien(Client clien) {
        this.clien = clien;
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

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<VenteDetail> getDetails() {
        return details;
    }

    public void setDetails(List<VenteDetail> details) {
        this.details = details;
    }
}
