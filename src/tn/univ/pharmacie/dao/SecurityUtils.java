package tn.univ.pharmacie.dao;

import java.security.MessageDigest;

public class SecurityUtils {

    // pour gérer les fonctions liées à la sécurité, comme le hash des mots de passe
    //hash:suite de caractères (apres la fonction de hachage) qui représente le mot de passe, mais qu’on ne peut pas facilement inverser
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}