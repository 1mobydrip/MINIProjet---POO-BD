package service;

import exception.CommandeInvalideException;
import exception.MedicamentInexistantException;
import exception.QuantiteInvalideException;
import exception.VenteInvalideException;
import model.*;
import service.interfaces.IGestionVente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionVentes implements IGestionVente {
    private List<Vente> ventes;
    public void ajouterVente(Vente vente) {
        ventes.add(vente); // chaque vente validée est ajoutée
    }
    public List<Vente> getToutesVentes() {
        return ventes;
    }
    public void creerVente(Client client, Employe employe){
        if (client==null)
            throw new VenteInvalideException("vente invalide");
        if (employe==null)
            throw new VenteInvalideException("vente invalide");
        Vente vente = new Vente();
        vente.setClien(client);
        vente.setEmploye(employe);
        vente.setDateVente(LocalDate.now());
        vente.setMontantTotal(0);
        vente.setDetails(null);
    }
    public void ajouterMedicamentVente(Vente vente,Medicament medicament,int quantite,GestionStock gestionStock){
        if (vente == null)
            throw new CommandeInvalideException("Vente invalide");
        if (medicament == null)
            throw new MedicamentInexistantException("Médicament invalide");
        if (quantite <= 0)
            throw new QuantiteInvalideException("Quantité invalide");
        if(!gestionStock.verifierDisponibile(medicament,quantite))
            throw new QuantiteInvalideException("quantite insuffisante");
        VenteDetail venteDetail = new VenteDetail();
        venteDetail.setVente(vente);
        venteDetail.setMedicament(medicament);
        venteDetail.setQuantite(quantite);
        vente.getDetails().add(venteDetail);
    }
    public void validerVente(Vente vente,GestionStock gestionStock){
        if (vente == null)
            throw new VenteInvalideException("Vente invalide");
        for(VenteDetail v : vente.getDetails()){
            Medicament med=v.getMedicament();
            int q=v.getQuantite();
            if(!gestionStock.verifierDisponibile(med,q))
                throw new QuantiteInvalideException("Quantite insuffisante pour "+med.getNom());
        }
        for(VenteDetail v : vente.getDetails()) {
            gestionStock.retirerMedicament(v.getMedicament(),v.getQuantite());
        }
        ajouterVente(vente);
    }
    public void calculerTotalVente(Vente vente){
        if (vente == null)
            throw new VenteInvalideException("Vente invalide");
        double p=0;
        for(VenteDetail v : vente.getDetails()){
            p+=v.getMedicament().getPrix()*v.getQuantite();
        }
        vente.setMontantTotal(p);
    }
    public List<Vente> historiqueVentesClient(Client client){
        if(client==null)
            throw new VenteInvalideException("Client invalide");
        List<Vente> venteClient=new ArrayList<>();
        for(Vente v : ventes){
            if(v.getClien().getId()==client.getId())
                venteClient.add(v);
        }
        return venteClient;
    }
}
