CREATE DATABASE IF NOT EXISTS pharmacie;
USE pharmacie;

CREATE TABLE type_medicament (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE fournisseur (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    adresse VARCHAR(255)
) ENGINE=InnoDB;

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
    role ENUM('Admin', 'Pharmacien', 'Caissier') NOT NULL
) ENGINE=InnoDB;

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


CREATE USER 'pharm_app'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT, INSERT, UPDATE, DELETE ON pharmacie.* TO 'pharm_app'@'localhost';
FLUSH PRIVILEGES;

SELECT * FROM type_medicament;
SELECT * FROM fournisseur;
SELECT * FROM employe;
SELECT * FROM client;
SELECT * FROM medicament;
select * from stock_lot;

INSERT INTO client (nom, prenom, telephone, adresse) VALUES
('Ben Ali', 'Mohamed', '98123456', 'Tunis'),
('Trabelsi', 'Amine', '22114567', 'Ariana'),
('Gharbi', 'Youssef', '53127894', 'Sfax'),
('Jebali', 'Sami', '99112233', 'Sousse'),
('Ayari', 'Houssem', '25478961', 'Ben Arous'),
('Khlifi', 'Walid', '97334455', 'Bizerte'),
('Mansouri', 'Karim', '21456789', 'Nabeul'),
('Saidi', 'Nour', '55667788', 'Kairouan'),
('Mejri', 'Rim', '29998877', 'Monastir'),
('Bouzid', 'Faten', '44775566', 'Gabes'),
('Haddad', 'Anis', '98665544', 'Mahdia'),
('Cherni', 'Asma', '52778899', 'Zaghouan'),
('Slimani', 'Omar', '93336699', 'Gafsa'),
('Ferchichi', 'Ines', '20669988', 'Beja'),
('Lahmar', 'Hatem', '98771234', 'Kef');

INSERT INTO fournisseur (nom, telephone, adresse) VALUES
('PharmaTunisie', '71324567', 'Tunis'),
('Medika Plus', '70211223', 'Ariana'),
('SanteDistrib', '74455667', 'Sfax'),
('BioPharm', '73123456', 'Sousse'),
('Nord Medical', '72444556', 'Bizerte'),
('Central Pharma', '71223344', 'Ben Arous'),
('Maghreb Medic', '74556677', 'Nabeul'),
('Sahel Distribution', '73445566', 'Monastir'),
('Sud Pharma', '75667788', 'Gabes'),
('Atlas Medical', '73559900', 'Kairouan'),
('Ifriqiya Health', '71887766', 'Mahdia'),
('El Amal Pharma', '72668899', 'Zaghouan');


INSERT INTO type_medicament (libelle) VALUES
('Antalgique'),
('Antibiotique'),
('Anti-inflammatoire'),
('Antipyrétique'),
('Antiseptique'),
('Vitamine');


INSERT INTO medicament (nom, description, prix, type_id, fournisseur_id) VALUES
('Paracétamol 500mg', 'Douleur et fièvre', 2.50, 1, 1),
('Amoxicilline 500mg', 'Antibiotique à large spectre', 7.80, 2, 2),
('Ibuprofène 400mg', 'Anti-inflammatoire non stéroïdien', 4.20, 3, 3),
('Aspirine 500mg', 'Antalgique et antipyrétique', 3.10, 4, 4),
('Bétadine', 'Solution antiseptique', 6.90, 5, 5),
('Vitamine C', 'Complément vitaminique', 5.00, 6, 6),
('Doliprane 1000mg', 'Traitement de la douleur', 3.80, 1, 1),
('Augmentin', 'Antibiotique combiné', 12.50, 2, 2),
('Nifluril', 'Anti-inflammatoire', 9.30, 3, 7),
('Hextril', 'Bain de bouche antiseptique', 6.40, 5, 8);

INSERT INTO stock_lot (medicament_id, fournisseur_id, quantite, date_expiration) VALUES
(1, 1, 120, '2026-05-15'),
(1, 1, 80,  '2026-11-20'),
(2, 2, 60,  '2025-09-10'),
(3, 3, 200, '2027-01-30'),
(4, 4, 150, '2026-03-18'),
(5, 5, 90,  '2025-12-05'),
(6, 6, 300, '2027-06-25'),
(7, 1, 110, '2026-08-14'),
(8, 2, 40,  '2025-10-22'),
(9, 7, 75,  '2026-04-09'),
(10,8, 50,  '2026-12-31');





