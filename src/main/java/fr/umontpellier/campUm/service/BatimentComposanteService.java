package fr.umontpellier.campUm.service;

import fr.umontpellier.campUm.entity.Batiment;
import fr.umontpellier.campUm.entity.Composante;
import fr.umontpellier.campUm.repository.BatimentRepository;
import fr.umontpellier.campUm.repository.ComposanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatimentComposanteService {

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private ComposanteRepository composanteRepository;

    @Transactional
    public void addComposanteToBatiment(String batimentCode, String composanteAcronyme) {
        // Fetch Batiment
        Batiment batiment = batimentRepository.findById(batimentCode)
                .orElseThrow(() -> new RuntimeException("Batiment not found with code: " + batimentCode));

        // Fetch Composante
        Composante composante = composanteRepository.findById(composanteAcronyme)
                .orElseThrow(() -> new RuntimeException("Composante not found with acronym: " + composanteAcronyme));

        // Add Composante to Batiment
        batiment.addComposante(composante);

        // Save Batiment (Cascading will handle Composante if necessary)
        batimentRepository.save(batiment);
    }

    @Transactional
    public void removeComposanteFromBatiment(String batimentCode, String composanteAcronyme) {
        // Fetch Batiment
        Batiment batiment = batimentRepository.findById(batimentCode)
                .orElseThrow(() -> new RuntimeException("Batiment not found with code: " + batimentCode));

        // Fetch Composante
        Composante composante = composanteRepository.findById(composanteAcronyme)
                .orElseThrow(() -> new RuntimeException("Composante not found with acronym: " + composanteAcronyme));

        // Remove Composante from Batiment
        batiment.removeComposante(composante);

        // Save Batiment
        batimentRepository.save(batiment);
    }
}