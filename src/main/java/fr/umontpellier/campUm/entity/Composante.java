package fr.umontpellier.campUm.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "composante", schema = "CampUm")
public class Composante {

    @Id
    @Column(name = "acronyme", nullable = false, length = 16)
    private String acronyme;

    @Column(name = "nom")
    private String nom;

    @ManyToMany(mappedBy = "composantes", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Batiment> batiments;

    // Getters and Setters

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Batiment> getBatiments() {
        return batiments;
    }

    public void setBatiments(List<Batiment> batiments) {
        this.batiments = batiments;
    }

    public void addBatiment(Batiment batiment) {
        if (batiments == null) {
            batiments = new ArrayList<>();
        }
        if (!batiments.contains(batiment)) {
            batiments.add(batiment);
            batiment.getComposantes().add(this);
        }
    }

    public void removeBatiment(Batiment batiment) {
        if (batiments != null && batiments.contains(batiment)) {
            batiments.remove(batiment);
            batiment.getComposantes().remove(this);
        }
    }


}