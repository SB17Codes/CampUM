package fr.umontpellier.campUm.service;

import fr.umontpellier.campUm.entity.Salle;
import fr.umontpellier.campUm.entity.Batiment;
import fr.umontpellier.campUm.exception.ResourceNotFoundException;
import fr.umontpellier.campUm.repository.SalleRepository;
import fr.umontpellier.campUm.repository.BatimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleService {
    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    public Salle getSalleByNums(String nums) {
        return salleRepository.findById(nums).orElse(null);
    }

    public List<Salle> getSallesByBatiment(String codeB) {
        return salleRepository.findByBatimentB_CodeB(codeB);
    }

    public Salle createSalle(Salle salle) {
        if (salle.getBatiment() != null) {
            Batiment batiment = batimentRepository.findByCodeB(salle.getBatiment())
                    .orElseThrow(() -> new ResourceNotFoundException("Batiment with code '" + salle.getBatiment() + "' not found."));
            salle.setBatimentB(batiment);
        } else {
            throw new ResourceNotFoundException("Batiment information is missing.");
        }
        return salleRepository.save(salle);
    }

    public Salle updateSalle(String nums, Salle salleDetails) {
        Salle salle = salleRepository.findById(nums).orElse(null);
        if (salle != null) {
            salle.setCapacite(salleDetails.getCapacite());
            salle.setTypes(salleDetails.getTypes());
            salle.setAcces(salleDetails.getAcces());
            salle.setBatimentB(salleDetails.getBatimentB());
            return salleRepository.save(salle);
        }
        return null;
    }

    public void deleteSalle(String nums) {
        salleRepository.deleteById(nums);
    }
}