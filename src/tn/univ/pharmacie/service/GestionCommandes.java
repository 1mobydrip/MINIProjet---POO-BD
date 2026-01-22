package tn.univ.pharmacie.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tn.univ.pharmacie.dao.MedicamentDAO;
import tn.univ.pharmacie.model.Commande.StatutCommande;
import tn.univ.pharmacie.model.CommandeDetail;
import tn.univ.pharmacie.dao.commandeDAO;
import tn.univ.pharmacie.dao.CommandeDetailDAO;


public class GestionCommandes {
    private static CommandeDetailDAO cdDAO = new CommandeDetailDAO();
    private static List<CommandeDetail> commandes;

    static {
        try {
            commandes = cdDAO.getAllCommandeDetails();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void creerCommande(CommandeDetail cd) {
        if (cd == null || cd.getMedicament() == null || cd.getCommande() == null || cd.getQuantite() <= 0) {
            throw new IllegalArgumentException("Erreur de Commande");
        }

        if (cd.getCommande() == null || cd.getCommande().getFournisseur() == null) {
            throw new IllegalArgumentException("La commande et le fournisseur sont obligatoires");
        }

        CommandeDetailDAO cdDAO = new CommandeDetailDAO();
        commandeDAO cDAO = new commandeDAO();
        try {
            cDAO.ajouterCommande(cd.getCommande());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            cdDAO.ajouterCommandeDetail(cd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public void annulerCommande(int commandeId) throws SQLException {
        commandeDAO cDAO = new commandeDAO();
        CommandeDetail cd = consulterCommande(commandeId);
        if (cd == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + commandeId + " non trouvée");
        }
        
        cDAO.setStatusById(cd.getCommande().getId(), StatutCommande.annulee);
    }

    public void recevoirCommande(int commandeId) throws SQLException {
        CommandeDetail cd = consulterCommande(commandeId);
        commandeDAO cDAO = new commandeDAO();
        if (cd == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + commandeId + " non trouvée");
        }

        cDAO.setStatusById(cd.getCommande().getId(), StatutCommande.livree);
    }

    public void validerCommande(int commandeId) throws SQLException {
        CommandeDetail cd = consulterCommande(commandeId);
        commandeDAO cDAO = new commandeDAO();
        if (cd == null) {
            throw new IllegalArgumentException("Commande avec l'ID " + commandeId + " non trouvée");
        }
        cDAO.setStatusById(cd.getCommande().getId(), StatutCommande.livree);
    }

    public CommandeDetail consulterCommande(int commandeId) {
        for (CommandeDetail commande : commandes) {
            if (commande.getCommande().getId() == commandeId) {
                return commande;
            }
        }
        return null;
    }

    public List<CommandeDetail> listerCommandes() {
        return new ArrayList<>(commandes);
    }

    public List<CommandeDetail> listerCommandesParStatut(StatutCommande statut) {
        List<CommandeDetail> results = new ArrayList<>();
        commandeDAO cDAO = new commandeDAO();

        for (CommandeDetail cd : commandes) {
            try {
                if (cDAO.getStatusById(cd.getCommande().getId()) == statut) {
                    results.add(cd);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    public double calculerMontantTotal() {
        double total = 0;
        CommandeDetailDAO cdDAO = new CommandeDetailDAO();
        MedicamentDAO mdDAO = new MedicamentDAO();
        try {
            commandes = cdDAO.getAllCommandeDetails();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (CommandeDetail commande : commandes) {
            try {
                total += mdDAO.getPrixById(commande.getMedicament().getId()) * commande.getQuantite();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return total;
    }

    public int getTotalCommandes() {
        return commandes.size();
    }
}