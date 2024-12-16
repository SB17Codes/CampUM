package fr.umontpellier.campUm.service;

import fr.umontpellier.campUm.entity.Batiment;
import fr.umontpellier.campUm.entity.Campus;
import fr.umontpellier.campUm.exception.ResourceNotFoundException;
import fr.umontpellier.campUm.repository.BatimentRepository;
import fr.umontpellier.campUm.repository.CampusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BatimentService {
    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private CampusRepository campusRepository;

    public List<Batiment> getAllBatiments() {
        return batimentRepository.findAll();
    }

    public Batiment getBatimentByCodeB(String codeB) {
        return batimentRepository.findByCodeB(codeB)
                .orElseThrow(() -> new ResourceNotFoundException("Batiment with code '" + codeB + "' not found."));
    }

    public Batiment createBatiment(Batiment batiment) {
        if (batiment.getCampus() != null) {
            Campus campus = campusRepository.findByNomC(batiment.getCampus())
                    .orElseThrow(() -> new ResourceNotFoundException("Campus with name '" + batiment.getCampus() + "' not found."));
            batiment.setCampusC(campus);
        } else {
            throw new ResourceNotFoundException("Campus information is missing.");
        }
        return batimentRepository.save(batiment);
    }

    public Batiment updateBatiment(String codeB, Batiment updates) {
        Batiment existing = batimentRepository.findByCodeB(codeB)
                .orElseThrow(() -> new ResourceNotFoundException("Batiment with code '" + codeB + "' not found."));
        existing.setAnneeC(updates.getAnneeC());
        return batimentRepository.save(existing);
    }

    public void deleteBatimentByCodeB(String codeB) {
        Batiment existing = batimentRepository.findByCodeB(codeB)
                .orElseThrow(() -> new ResourceNotFoundException("Batiment with code '" + codeB + "' not found."));
        batimentRepository.deleteByCodeB(codeB);
    }

    public List<Batiment> getBatimentsByCampus(String nomc) {
        return batimentRepository.getBatimentsByCampusC_nomC(nomc);
    }
}