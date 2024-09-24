package fr.umontpellier.campUm.service;

import fr.umontpellier.campUm.entity.Campus;
import fr.umontpellier.campUm.repository.CampusRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampusService {

    @Autowired
    private CampusRepository campusRepository;

    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }

    // Find campuses by name

    public Campus findCampusesByName(String name) {
        return campusRepository.findByNomC(name);
    }


    // Find campuses by city

    public List<Campus> findCampusesByCity(String city) {
        return campusRepository.findByVille(city);
    }

    // get unique cities'

    public List<String> getUniqueCities() {
        return campusRepository.findAll().stream()
                .map(Campus::getVille)
                .distinct()
                .collect(Collectors.toList());
    }

    // get campuses names

    public List<String> getCampusesNames() {
        return campusRepository.findAll().stream()
                .map(Campus::getNomC)
                .collect(Collectors.toList());
    }
}



