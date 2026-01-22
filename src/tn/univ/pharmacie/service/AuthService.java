package tn.univ.pharmacie.service;

import tn.univ.pharmacie.dao.EmployeDAO;
import tn.univ.pharmacie.model.Employe;

public class AuthService {

    public Employe login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return EmployeDAO.authentifier(username, password);
    }

    public void logout(int employeId) {
        // todo
    }

    public void changePassword(int employeId, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être vide");
        }
        //todo
    }
}