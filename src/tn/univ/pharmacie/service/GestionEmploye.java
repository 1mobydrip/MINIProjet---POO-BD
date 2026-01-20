package tn.univ.pharmacie.service;

import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.Employe;

public class GestionEmploye {
    private static List<Employe> employes = new ArrayList<>();
    private static int nextId = 1;

    public void ajouterEmploye(Employe e) {
        if (e == null || e.getNom() == null || e.getNom().isEmpty() || 
            e.getPrenom() == null || e.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires");
        }
        
        if (e.getUsername() == null || e.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Le username est obligatoire");
        }
        
        // Vérifier que le username n'existe pas déjà
        for (Employe emp : employes) {
            if (emp.getUsername().equals(e.getUsername())) {
                throw new IllegalArgumentException("Le username " + e.getUsername() + " existe déjà");
            }
        }
        
        e.setId(nextId++);
        employes.add(e);
    }

    public void modifierEmploye(Employe e) {
        if (e == null || e.getId() <= 0) {
            throw new IllegalArgumentException("Employé invalide");
        }
        
        Employe existing = consulterEmploye(e.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Employé avec l'ID " + e.getId() + " non trouvé");
        }
        
        existing.setNom(e.getNom());
        existing.setPrenom(e.getPrenom());
        existing.setUsername(e.getUsername());
        existing.setPassword(e.getPassword());
    }

    public void supprimerEmploye(int employeId) {
        Employe employe = consulterEmploye(employeId);
        if (employe == null) {
            throw new IllegalArgumentException("Employé avec l'ID " + employeId + " non trouvé");
        }
        employes.remove(employe);
    }

    public Employe consulterEmploye(int employeId) {
        for (Employe employe : employes) {
            if (employe.getId() == employeId) {
                return employe;
            }
        }
        return null;
    }

    public List<Employe> listerEmployes() {
        return new ArrayList<>(employes);
    }
}
