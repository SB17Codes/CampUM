package fr.umontpellier.campUm.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Composante")
public class Composante {
    @Id
    private String acronyme;
    private String nom;
    private String responsable;

    // Getters and Setters
}