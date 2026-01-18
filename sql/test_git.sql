CREATE DATABASE universite;
use universite;
CREATE TABLE departements (
	id_dept INT PRIMARY KEY,
    nom_dept VARCHAR(50)
    );



CREATE TABLE Etudiants(
id_etudiant INT PRIMARY KEY,
nom VARCHAR(50),
prenom VARCHAR(50),
age INT,
id_dept INT,
moyenne DECIMAL(4,2),
email VARCHAR(100),
FOREIGN KEY (id_dept) REFERENCES departements(id_dept)
);

CREATE TABLE Modules(
id_module INT PRIMARY KEY,
nom_module VARCHAR(50),
id_dept INT,
FOREIGN KEY (id_dept) REFERENCES departements(id_dept)
);
INSERT INTO departements(id_dept,nom_dept) 
VALUES
(1, 'GL'),
(2, 'IIA'),
(3, 'RT');
INSERT INTO Etudiants (id_etudiant, nom, prenom, age, id_dept, moyenne, email) VALUES
(1, 'Ali', 'Karim', 21, 1, 12.5, 'ali.karim@univ.tn'),
(2, 'Meriem', 'Saadi', 22, 2, 15.3, 'meriem.saadi@univ.tn'),
(3, 'Sami', 'Ghorbel', 20, 1, 9.8, 'sami.ghorbel@univ.tn'),
(4, 'Amina', 'Ben Salah', 23, 3, 13.4, 'amina.bensalah@univ.tn'),
(5, 'Youssef', 'Trabelsi', 21, 1, 16.0, 'youssef.trabelsi@univ.tn'),
(6, 'Nour', 'Zayani', 22, 2, 10.0, 'nour.zayani@univ.tn');

INSERT INTO Modules (id_module, nom_module, id_dept) VALUES
(1, 'Programmation', 1),
(2, 'Réseaux', 3),
(3, 'Base de données', 1),
(4, 'IA', 2),
(5, 'Systèmes', 3);

SELECT E.nom, E.prenom, D.nom_dept
FROM Etudiants E
JOIN departements D ON D.id_dept = E.id_dept;

SELECT E.nom, E.prenom, D.nom_dept
FROM Etudiants E
JOIN departements D ON D.id_dept = E.id_dept
WHERE E.moyenne >= 12;

Select M.nom_module 
from Modules M
join departements D on D.id_dept=M.id_dept
where D.id_dept=1;

SELECT *
FROM Etudiants E
ORDER BY E.moyenne DESC;

select nom
from Etudiants
where nom like'A%';

select D.id_dept, AVG(E.moyenne) as moy
from Etudiants E
join departements D on D.id_dept=E.id_dept
group by id_dept;

select D.id_dept,COUNT(E.id_etudiant) as nbr_etudiants
from Etudiants E
join departements D on D.id_dept=E.id_dept
group by id_dept;

SELECT E.nom,E.prenom
from Etudiants E 
where E.moyenne>=all(select moyenne from etudiants);

SELECT D.nom_dept, AVG(E.moyenne) AS moy_generale
FROM Etudiants E
JOIN Departements D ON D.id_dept = E.id_dept
GROUP BY D.nom_dept
ORDER BY moy_generale DESC
limit 1;
ALTER TABLE etudiants
ADD CONSTRAINT chk_moyenne
check(moyenne BETWEEN 0 AND 20);



 




