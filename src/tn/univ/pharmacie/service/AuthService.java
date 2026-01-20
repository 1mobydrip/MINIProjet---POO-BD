package tn.univ.pharmacie.service;

import java.util.ArrayList;
import java.util.List;
import tn.univ.pharmacie.model.Employe;


public class AuthService {
    private GestionEmploye gestionEmploye = new GestionEmploye();
    private List<Integer> sessionActive = new ArrayList<>();

    public boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        
        List<Employe> employes = gestionEmploye.listerEmployes();
        for (Employe employe : employes) {
            if (employe.getUsername().equals(username) && employe.getPassword().equals(password)) {
                if (!sessionActive.contains(employe.getId())) {
                    sessionActive.add(employe.getId());
                }
                return true;
            }
        }
        return false;
    }

    public void logout(int employeId) {
        Employe employe = gestionEmploye.consulterEmploye(employeId);
        if (employe == null) {
            throw new IllegalArgumentException("Employé avec l'ID " + employeId + " non trouvé");
        }
        
        if (sessionActive.contains(employeId)) {
            sessionActive.remove(Integer.valueOf(employeId));
        }
    }

    public void changePassword(int employeId, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Le nouveau password ne peut pas être vide");
        }
        
        Employe employe = gestionEmploye.consulterEmploye(employeId);
        if (employe == null) {
            throw new IllegalArgumentException("Employé avec l'ID " + employeId + " non trouvé");
        }
        
        employe.setPassword(newPassword);
    }

    public boolean isSessionActive(int employeId) {
        return sessionActive.contains(employeId);
    }

    public List<Integer> getActiveSessions() {
        return new ArrayList<>(sessionActive);
    }
}
