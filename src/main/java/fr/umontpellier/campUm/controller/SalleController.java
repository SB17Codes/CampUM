package fr.umontpellier.campUm.controller;

import fr.umontpellier.campUm.entity.Salle;
import fr.umontpellier.campUm.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
public class SalleController {
    @Autowired
    private SalleService salleService;

    @GetMapping
    public List<Salle> getAllSalles() {
        return salleService.getAllSalles();
    }

    @GetMapping("/{nums}")
    public ResponseEntity<Salle> getSalleByNums(@PathVariable String nums) {
        Salle s = salleService.getSalleByNums(nums);
        if (s != null) return ResponseEntity.ok(s);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/byBatiment")
    public List<Salle> getSallesByBatiment(@RequestParam String codeB) {
        return salleService.getSallesByBatiment(codeB);
    }

    @PostMapping("/create")
    public ResponseEntity<Salle> createSalle(@RequestBody Salle salle) {
        Salle created = salleService.createSalle(salle);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{nums}")
    public ResponseEntity<Salle> updateSalle(@PathVariable String nums, @RequestBody Salle salleDetails) {
        Salle updated = salleService.updateSalle(nums, salleDetails);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{nums}")
    public ResponseEntity<Void> deleteSalle(@PathVariable String nums) {
        salleService.deleteSalle(nums);
        return ResponseEntity.noContent().build();
    }
}