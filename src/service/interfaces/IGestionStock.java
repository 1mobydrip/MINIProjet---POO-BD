package service.interfaces;

import model.Fournisseur;
import model.Medicament;

import java.time.LocalDate;

public interface IGestionStock {
    void ajouterMedicament(Medicament medicament, Fournisseur fournisseur, int quantite, LocalDate dateExpiration);
    void retirerMedicament(Medicament medicament, int quantite);
    int getQuantiteDisponible(Medicament medicament);
    boolean verifierDisponibile(Medicament medicament,int quantite);
    void verifierSeuilStock(Medicament medicament);

}
