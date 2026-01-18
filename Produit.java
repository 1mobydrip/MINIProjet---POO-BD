package git_test;

public class Produit {

 private String nom;
private double prix;
private int stock;

// Constructeur
public Produit(String nom, double prix, int stock) {
    this.nom = nom;
    this.prix = prix;
    this.stock = stock;
}

// Méthodes
public void afficher() {
    System.out.println("Produit : " + nom);
    System.out.println("Prix : " + prix + " DT");
    System.out.println("Stock : " + stock);
}

public void ajouterStock(int quantite) {
    stock += quantite;
}

public void vendre(int quantite) {
    if (quantite <= stock) {
        stock -= quantite;
        System.out.println("Vente réussie !");
    } else {
        System.out.println("Stock insuffisant !");
    }
}
}
