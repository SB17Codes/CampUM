// src/main/java/fr/umontpellier/campUm/controller/CampusController.java
package fr.umontpellier.campUm.controller;

import fr.umontpellier.campUm.entity.Campus;
import fr.umontpellier.campUm.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class CampusController {

    @Autowired
    private CampusService campusService;

    @GetMapping("/api/campuses")
    @ResponseBody
    public List<Campus> getAllCampuses() {
        return campusService.getAllCampuses();
    }

    @GetMapping("/api/campuses/byName")
    @ResponseBody
    public Campus getCampusesByName(@RequestParam String name) {
        return campusService.findCampusesByName(name);
    }


    @GetMapping("/api/campuses/byCity")
    @ResponseBody
    public List<Campus> getCampusesByCity(@RequestParam String city) {
        return campusService.findCampusesByCity(city);
    }

    @GetMapping("/api/campuses/uniqueCities")
    @ResponseBody
    public List<String> getUniqueCities() {
        return campusService.getUniqueCities();
    }

    @Value("${google.api.key}")
    private String googleApiKey;

    @GetMapping("/api/itinerary")
    public ResponseEntity<String> getItinerary(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String mode) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin +
                "&destination=" + destination + "&mode=" + mode + "&key=" + googleApiKey;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/campus")
    public String campusPage(Model model) {
        List<Campus> campuses = campusService.getAllCampuses();
        List<String> uniqueCities = campusService.getUniqueCities();
        List<String> campusesNames = campusService.getCampusesNames();
        model.addAttribute("campuses", campuses);
        model.addAttribute("uniqueCities", uniqueCities);
        model.addAttribute("campusesNames", campusesNames);
        model.addAttribute("googleApiKey", googleApiKey); // Add this line
        return "campus";

    }
}