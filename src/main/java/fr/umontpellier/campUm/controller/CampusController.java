package fr.umontpellier.campUm.controller;

import fr.umontpellier.campUm.entity.Campus;
import fr.umontpellier.campUm.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campuses")
public class CampusController {

    @Autowired
    private CampusService campusService;

    @GetMapping
    public List<Campus> getAllCampuses() {
        return campusService.getAllCampuses();
    }

    @GetMapping("/byName")
    public ResponseEntity<Campus> getCampusesByName(@RequestParam String name) {
        Campus c = campusService.findCampusesByName(name);
        if (c != null) return ResponseEntity.ok(c);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/byCity")
    public List<Campus> getCampusesByCity(@RequestParam String city) {
        return campusService.findCampusesByCity(city);
    }

    @GetMapping("/uniqueCities")
    public List<String> getUniqueCities() {
        return campusService.getUniqueCities();
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Campus> createCampus(@RequestBody Campus campus) {
        Campus createdCampus = campusService.createCampus(campus);
        return new ResponseEntity<>(createdCampus, HttpStatus.CREATED);
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<Campus> updateCampus(@PathVariable String name, @RequestBody Campus campusDetails) {
        Campus updated = campusService.updateCampus(name, campusDetails);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteCampus(@PathVariable String name) {
        campusService.deleteCampus(name);
        return ResponseEntity.noContent().build();
    }
}