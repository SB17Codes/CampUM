package fr.umontpellier.campUm.controller;

import fr.umontpellier.campUm.entity.Composante;
import fr.umontpellier.campUm.service.ComposanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/composantes")
public class ComposanteController {
    @Autowired
    private ComposanteService composanteService;

    @GetMapping
    public List<Composante> getAllComposantes() {
        return composanteService.getAllComposantes();
    }

    @GetMapping("/{acronyme}")
    public ResponseEntity<Composante> getComposanteByAcronyme(@PathVariable String acronyme) {
        Composante c = composanteService.getComposanteByAcronyme(acronyme);
        if (c != null) return ResponseEntity.ok(c);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Composante> createComposante(@RequestBody Composante composante) {
        Composante created = composanteService.createComposante(composante);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{acronyme}")
    public ResponseEntity<Composante> updateComposante(@PathVariable String acronyme, @RequestBody Composante details) {
        Composante updated = composanteService.updateComposante(acronyme, details);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{acronyme}")
    public ResponseEntity<Void> deleteComposante(@PathVariable String acronyme) {
        composanteService.deleteComposante(acronyme);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{acronyme}/addToBatiment/{codeB}")
    public ResponseEntity<Composante> addComposanteToBatiment(@PathVariable String acronyme, @PathVariable String codeB) {
        Composante updated = composanteService.addComposanteToBatiment(acronyme, codeB);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }
}