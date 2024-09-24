package fr.umontpellier.campUm.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Batiment")
public class Batiment {
    @Id
    private String codeB;
    private int anneeC;

    @ManyToOne
    private Campus campus;

    // Getters and Setters
}