package fr.umontpellier.campUm.controller;

import fr.umontpellier.campUm.entity.Batiment;
import fr.umontpellier.campUm.service.BatimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batiments")
public class BatimentController {
    @Autowired
    private BatimentService batimentService;

    @GetMapping
    public List<Batiment> getAllBatiments() {
        return batimentService.getAllBatiments();
    }

    @GetMapping("/{codeB}")
    public ResponseEntity<Batiment> getBatimentByCodeB(@PathVariable String codeB) {
        Batiment b = batimentService.getBatimentByCodeB(codeB);
        if (b != null) return ResponseEntity.ok(b);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/byCampus")
    public List<Batiment> getBatimentsByCampus(@RequestParam("name") String nomc) {
        return batimentService.getBatimentsByCampus(nomc);
    }

    @PostMapping("/create")
    public ResponseEntity<Batiment> createBatiment(@RequestBody Batiment batiment) {
        Batiment created = batimentService.createBatiment(batiment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{codeB}")
    public ResponseEntity<Batiment> updateBatiment(@PathVariable String codeB, @RequestBody Batiment batimentDetails) {
        Batiment updated = batimentService.updateBatiment(codeB, batimentDetails);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{codeB}")
    public ResponseEntity<Void> deleteBatiment(@PathVariable String codeB) {
        batimentService.deleteBatimentByCodeB(codeB);
        return ResponseEntity.noContent().build();
    }
}