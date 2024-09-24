package fr.umontpellier.campUm.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Salle")
public class Salle {
    @Id
    private String numS;
    private int capacite;
    private String typeS;
    private String acces;
    private String etage;

    @ManyToOne
    private Batiment batiment;

    // Getters and Setters
}
