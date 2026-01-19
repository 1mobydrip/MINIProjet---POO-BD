package service;

import exception.CommandeInvalideException;
import exception.QuantiteInvalideException;
import exception.StockInsuffisantException;
import model.*;
import service.interfaces.IGestionCommande;
import exception.MedicamentInexistantException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class GestionCommandes implements IGestionCommande {
    public Commande creeCommandeFournisseur(Fournisseur fournisseur, Employe employe) {
        if (fournisseur == null) {
            throw new MedicamentInexistantException("Fournisseur invalide !");
        }
        if (employe == null) {
            throw new CommandeInvalideException("Employe invalide !");
        }
        Commande commande = new Commande();
        commande.setFournisseur(fournisseur);
        commande.setEmploye(employe);
        commande.setDate(LocalDate.now());
        commande.setMontant(0);
        commande.setDetails(null);
        return commande;
    }
    public void ajouterMedicamentCommande(Commande commande,Medicament medicament,int quantite){
        if(medicament==null)
            throw new MedicamentInexistantException("Medicament invalide !");
        if (quantite<=0)
            throw new QuantiteInvalideException("Quantite invalide !");
        CommandeDetail commandeDetail=new CommandeDetail();
        commandeDetail.setMedicament(medicament);
        commandeDetail.setQuantite(quantite);
        commandeDetail.setCommande(commande);
        commande.getDetails().add(commandeDetail);
    }
    public void modifierCommande(Commande commande,Medicament medicament,int nouvQuantite){
        if(commande==null)
            throw new CommandeInvalideException("Commande invalide !");
        if (commande.getStatut()== Commande.StatutCommande.VALIDEE ||
            commande.getStatut()== Commande.StatutCommande.RECEPTIONNEE ||
            commande.getStatut()== Commande.StatutCommande.ANNULEE)
            throw new CommandeInvalideException("La commande ne peut pas etre modifiee ");
        if (nouvQuantite <= 0)
            throw new QuantiteInvalideException("Quantite invalide");
        boolean trouve = false;
        for (CommandeDetail c : commande.getDetails()){
            if (c.getMedicament()==(medicament)){
                c.setQuantite(nouvQuantite);
                trouve=true;
                break;
            }
        }
        if(!trouve)
            throw new MedicamentInexistantException("Medicament non present dans la commande");
    }
    public void annulerCommande(Commande commande){
        if(commande==null)
            throw new CommandeInvalideException("Commande invalide ");
        if (commande.getStatut()!= Commande.StatutCommande.EN_COURS)
            throw new CommandeInvalideException("Seule une commande en cours peut être annulee");
        commande.setStatut(Commande.StatutCommande.ANNULEE);
    }
    public void validerCommande(Commande commande){
        if(commande==null)
            throw new CommandeInvalideException("Commande invalide ");
        if (commande.getStatut()!= Commande.StatutCommande.EN_COURS)
            throw new CommandeInvalideException("Seule une commande en cours peut être validee");
        commande.setStatut(Commande.StatutCommande.VALIDEE);
    }
    public void receptionnerCommande(Commande commande, GestionStock gestionStock,Map<Medicament,LocalDate> datesExpiration){
        if (commande == null)
            throw new CommandeInvalideException("Commande invalide");
        if (commande.getStatut() != Commande.StatutCommande.VALIDEE)
            throw new CommandeInvalideException("Seule une commande validee peut être réceptionnee");
        for (CommandeDetail c : commande.getDetails()) {
            Medicament medicament=c.getMedicament();
            int quantite=c.getQuantite();
            LocalDate dateExpiration=datesExpiration.get(medicament);
            gestionStock.ajouterMedicament(medicament,commande.getFournisseur(),quantite,dateExpiration);
        }
        commande.setStatut(Commande.StatutCommande.RECEPTIONNEE);
    }
}
