package fr.umontpellier.campUm.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "batiment", schema = "CampUm")
public class Batiment {

    @Id
    @Column(name = "codeB", nullable = false, length = 16)
    private String codeB;

    @Column(name = "anneeC")
    private Integer anneeC;

    @Transient
    private String campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus", referencedColumnName = "nomC")
    @JsonBackReference
    private Campus campusC;

    @OneToMany(mappedBy = "batimentB", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Salle> salles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exploite",
            joinColumns = @JoinColumn(name = "building", referencedColumnName = "codeB"),
            inverseJoinColumns = @JoinColumn(name = "team", referencedColumnName = "acronyme")
    )
    private List<Composante> composantes;

    // Getters and Setters

    public String getCodeB() {
        return codeB;
    }

    public void setCodeB(String codeB) {
        this.codeB = codeB;
    }

    public Integer getAnneeC() {
        return anneeC;
    }

    public void setAnneeC(Integer anneeC) {
        this.anneeC = anneeC;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Campus getCampusC() {
        return campusC;
    }

    public void setCampusC(Campus campusC) {
        this.campusC = campusC;
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

    public List<Composante> getComposantes() {
        return composantes;
    }

    public void setComposantes(List<Composante> composantes) {
        this.composantes = composantes;
    }

    public void addComposante(Composante composante) {
        if (composantes == null) {
            composantes = new ArrayList<>();
        }
        if (!composantes.contains(composante)) {
            composantes.add(composante);
            composante.getBatiments().add(this);
        }
    }

    public void removeComposante(Composante composante) {
        if (composantes != null && composantes.contains(composante)) {
            composantes.remove(composante);
            composante.getBatiments().remove(this);
        }
    }
}