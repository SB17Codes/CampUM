package fr.umontpellier.campUm.service;

import fr.umontpellier.campUm.entity.Composante;
import fr.umontpellier.campUm.entity.Batiment;
import fr.umontpellier.campUm.exception.ResourceNotFoundException;
import fr.umontpellier.campUm.repository.ComposanteRepository;
import fr.umontpellier.campUm.repository.BatimentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComposanteService {
    @Autowired
    private ComposanteRepository composanteRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    public List<Composante> getAllComposantes() {
        return composanteRepository.findAll();
    }

    public Composante getComposanteByAcronyme(String acronyme) {
        return composanteRepository.findById(acronyme)
                .orElseThrow(() -> new ResourceNotFoundException("Composante with acronym '" + acronyme + "' not found."));
    }

    public Composante createComposante(Composante composante) {
        return composanteRepository.save(composante);
    }

    public Composante updateComposante(String acronyme, Composante composanteDetails) {
        Composante composante = getComposanteByAcronyme(acronyme);
        composante.setNom(composanteDetails.getNom());
        return composanteRepository.save(composante);
    }

    public void deleteComposante(String acronyme) {
        Composante composante = getComposanteByAcronyme(acronyme);
        composanteRepository.delete(composante);
    }

    @Transactional
    public Composante addComposanteToBatiment(String acronyme, String codeB) {
        Composante composante = composanteRepository.findById(acronyme)
                .orElseThrow(() -> new ResourceNotFoundException("Composante with acronyme '" + acronyme + "' not found."));
        Batiment batiment = batimentRepository.findById(codeB)
                .orElseThrow(() -> new ResourceNotFoundException("Batiment with code '" + codeB + "' not found."));
        composante.getBatiments().add(batiment);
        return composanteRepository.save(composante);
    }}