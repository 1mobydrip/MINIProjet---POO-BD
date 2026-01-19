package service.interfaces;

import model.Commande;
import model.Employe;
import model.Fournisseur;
import model.Medicament;
import service.GestionStock;

import java.time.LocalDate;
import java.util.Map;

public interface IGestionCommande {
    Commande creeCommandeFournisseur(Fournisseur fournisseur, Employe employe);
    void ajouterMedicamentCommande(Commande commande, Medicament medicament, int quantite);
    void modifierCommande(Commande commande,Medicament medicament,int nouvQuantite);
    void annulerCommande(Commande commande);
    void validerCommande(Commande commande);
    void receptionnerCommande(Commande commande, GestionStock gestionStock, Map<Medicament, LocalDate> datesExpiration);
}
