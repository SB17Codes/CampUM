package fr.umontpellier.campUm.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "salle", schema = "CampUm")
public class Salle {

    @Id
    @Column(name = "nums", nullable = false)
    private String nums;

    @Column(name = "capacite")
    private Integer capacite;

    @Column(name = "types")
    private String types;

    @Column(name = "acces")
    private String acces;

    @Column(name = "etage")
    private String etage;

    @Transient
    private String batiment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batiment", referencedColumnName = "codeB")
    @JsonBackReference
    private Batiment batimentB;

    // Getters and Setters

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAcces() {
        return acces;
    }

    public void setAcces(String acces) {
        this.acces = acces;
    }

    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    public String getBatiment() {
        return batiment;
    }

    public void setBatiment(String batiment) {
        this.batiment = batiment;
    }

    public Batiment getBatimentB() {
        return batimentB;
    }

    public void setBatimentB(Batiment batimentB) {
        this.batimentB = batimentB;
    }
}