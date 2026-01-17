CREATE DATABASE IF NOT EXISTS pharmacie;
USE pharmacie;

-- Table type_medicament
CREATE TABLE type_medicament (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Table fournisseur
CREATE TABLE fournisseur (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    adresse VARCHAR(255)
) ENGINE=InnoDB;

-- Table medicament
CREATE TABLE medicament (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    prix DECIMAL(10,2) NOT NULL,
    type_id INT UNSIGNED NOT NULL,
    fournisseur_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (type_id) REFERENCES type_medicament(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (fournisseur_id) REFERENCES fournisseur(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;
CREATE TABLE stock_lot (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    medicament_id INT UNSIGNED NOT NULL,
    fournisseur_id INT UNSIGNED NOT NULL,
    quantite INT NOT NULL CHECK (quantite >= 0),
    date_expiration DATE NOT NULL,
    
    FOREIGN KEY (medicament_id) REFERENCES medicament(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (fournisseur_id) REFERENCES fournisseur(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;
CREATE TABLE client (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    adresse VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE employe (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'pharmacien', 'caissier') NOT NULL
) ENGINE=InnoDB;

-- Table commande
CREATE TABLE commande (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    fournisseur_id INT UNSIGNED NOT NULL,
    employe_id INT UNSIGNED NOT NULL,
    date_commande DATE NOT NULL,
    statut ENUM('en_attente', 'livree', 'annulee') NOT NULL,
    
    FOREIGN KEY (fournisseur_id) REFERENCES fournisseur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employe_id) REFERENCES employe(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Table commande_detail
CREATE TABLE commande_detail (
    commande_id INT UNSIGNED NOT NULL,
    medicament_id INT UNSIGNED NOT NULL,
    quantite INT NOT NULL,
    
    PRIMARY KEY (commande_id, medicament_id),
    FOREIGN KEY (commande_id) REFERENCES commande(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (medicament_id) REFERENCES medicament(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Table vente
CREATE TABLE vente (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    client_id INT UNSIGNED NOT NULL,
    employe_id INT UNSIGNED NOT NULL,
    date_vente DATE NOT NULL,
    
    FOREIGN KEY (client_id) REFERENCES client(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employe_id) REFERENCES employe(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Table vente_detail
CREATE TABLE vente_detail (
    vente_id INT UNSIGNED NOT NULL,
    medicament_id INT UNSIGNED NOT NULL,
    quantite INT NOT NULL,
    
    PRIMARY KEY (vente_id, medicament_id),
    FOREIGN KEY (vente_id) REFERENCES vente(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (medicament_id) REFERENCES medicament(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


