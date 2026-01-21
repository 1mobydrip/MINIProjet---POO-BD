package tn.univ.pharmacie.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.Client;
import tn.univ.pharmacie.dao.ClientDAO;

public class GestionClients {
    private static List<Client> clients = new ArrayList<>();
    private static int nextId = 1;

    public void ajouterClient(Client c) throws SQLException {
        if (c == null || c.getNom() == null || c.getNom().isEmpty() || 
            c.getPrenom() == null || c.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires");
        }
        
        ClientDAO dao = new ClientDAO();
        dao.ajouterClient(c);
    }

    public void modifierClient(Client c) {
        if (c == null || c.getId() <= 0) {
            throw new IllegalArgumentException("Client invalide");
        }
        
        Client existing = consulterClient(c.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Client avec l'ID " + c.getId() + " non trouvé");
        }
        
        existing.setNom(c.getNom());
        existing.setPrenom(c.getPrenom());
        existing.setTelephone(c.getTelephone());
        existing.setAdresse(c.getAdresse());
    }

    public void supprimerClient(int clientId) throws SQLException {
        Client client = consulterClient(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client avec l'ID " + clientId + " non trouvé");
        }
        ClientDAO dao = new ClientDAO();
        dao.supprimerClient(clientId);
    }

    public Client consulterClient(int clientId) {
        for (Client client : clients) {
            if (client.getId() == clientId) {
                return client;
            }
        }
        return null;
    }

    public List<Client> listerClients() throws SQLException {
        ClientDAO dao = new ClientDAO();
        clients = dao.getAllClients();
        return clients;
    }
}
