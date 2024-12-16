package fr.umontpellier.campUm.service;

import fr.umontpellier.campUm.entity.Campus;
import fr.umontpellier.campUm.exception.ResourceNotFoundException;
import fr.umontpellier.campUm.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampusService {

    @Autowired
    private CampusRepository campusRepository;

    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }

    public Campus findCampusesByName(String name) {
        return campusRepository.findByNomC(name)
                .orElseThrow(() -> new ResourceNotFoundException("Campus with name '" + name + "' not found."));
    }

    public List<Campus> findCampusesByCity(String city) {
        return campusRepository.findByVille(city);
    }

    public List<String> getUniqueCities() {
        return campusRepository.findAll().stream()
                .map(Campus::getVille)
                .distinct()
                .collect(Collectors.toList());
    }

    public Campus createCampus(Campus campus) {
        return campusRepository.save(campus);
    }

    public Campus updateCampus(String name, Campus campusDetails) {
        Campus campus = campusRepository.findByNomC(name)
                .orElseThrow(() -> new ResourceNotFoundException("Campus with name '" + name + "' not found."));
        campus.setVille(campusDetails.getVille());
        campus.setAdresse(campusDetails.getAdresse());
        campus.setLatitude(campusDetails.getLatitude());
        campus.setLongitude(campusDetails.getLongitude());
        campus.setType(campusDetails.getType());
        campus.setPhone(campusDetails.getPhone());
        campus.setWebsite(campusDetails.getWebsite());
        return campusRepository.save(campus);
    }

    public void deleteCampus(String name) {
        Campus campus = campusRepository.findByNomC(name)
                .orElseThrow(() -> new ResourceNotFoundException("Campus with name '" + name + "' not found."));
        campusRepository.delete(campus);
    }
}