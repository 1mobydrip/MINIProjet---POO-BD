package connexion;

import model.*;
import java.time.LocalDate;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class main {  
    public static void main(String[] args) {
        try (Connection conn = ConnexionBD.getConnection()) {
            if (conn != null) {
                System.out.println("Connexion réussie !");
            }
           //instanciation des classes DAO
            TypeMedicamentDAO typeDAO = new TypeMedicamentDAO();
            MedicamentDAO medDAO = new MedicamentDAO();
            FournisseurDAO fouDAO = new FournisseurDAO();
            EmployeDAO empDAO = new EmployeDAO();
            ClientDAO clientDAO = new ClientDAO();
            commandeDAO cmdDAO = new commandeDAO();
            CommandeDetailDAO cmdDetailDAO = new CommandeDetailDAO();
            VenteDAO venteDAO = new VenteDAO();
            VenteDetailDAO venteDetailDAO = new VenteDetailDAO();

            TypeMedicament type = typeDAO.getTypeByLibelle("Antibiotique");
            if (type == null) {  // si le type n'existe pas, on l'ajoute
                type = new TypeMedicament();
                type.setLibelle("Antibiotique");
                typeDAO.ajouterType(type);
                System.out.println("TypeMedicament ajouté, ID: " + type.getId());
            } else {
                System.out.println("TypeMedicament déjà existant, ID: " + type.getId());
            }

            //ajouter un fournisseur
            Fournisseur fou = new Fournisseur();
            fou.setNom("PharmaPlus");
            fou.setAdresse("Tunis");
            fou.setTelephone(12345678);
            fouDAO.ajouterFournisseur(fou);
            System.out.println("Fournisseur ID: " + fou.getId());

            // Ajouter un employé
            Employe emp = empDAO.getEmployeByUsername("ali");
            if (emp == null) { // si l'employé n'existe pas, on l'ajoute
                emp = new Employe();
                emp.setNom("Ali");
                emp.setPrenom("Ben");
                emp.setUsername("ali");
                emp.setPassword("1234");
                emp.setRole(Employe.EmployeRole.Admin);
                empDAO.ajouterEmploye(emp);
                System.out.println("Employe ajouté, ID: " + emp.getId());
            } else {
                System.out.println("Employe déjà existant, ID: " + emp.getId());
            }

            // Ajouter un client
            Client cl = new Client();
            cl.setNom("Sarra");
            cl.setPrenom("Khaled");
            cl.setTelephone(98765432);
            clientDAO.ajouterClient(cl);
            System.out.println("Client ID: " + cl.getId());

            // Ajouter un médicament
            Medicament med = new Medicament();
            med.setNom("Amoxicilline");
            med.setDescription("Antibiotique");
            med.setPrix(12.5);
            medDAO.ajouterMedicament(med, 1, 1);
            System.out.println("Medicament ID: " + med.getId());

            

        } catch (SQLException e) {
            System.out.println("Erreur de connexion ou DAO : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
