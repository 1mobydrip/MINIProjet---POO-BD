package service.interfaces;

import model.Client;
import model.Employe;
import model.Medicament;
import model.Vente;
import service.GestionStock;

import java.util.List;

public interface IGestionVente {
    void creerVente(Client client, Employe employe);
    void ajouterMedicamentVente(Vente vente, Medicament medicament, int quantite, GestionStock gestionStock);
    void validerVente(Vente vente,GestionStock gestionStock);
    void calculerTotalVente(Vente vente);
    List<Vente> historiqueVentesClient(Client client);
}
