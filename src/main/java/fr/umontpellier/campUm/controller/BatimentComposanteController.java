package fr.umontpellier.campUm.controller;

import fr.umontpellier.campUm.service.BatimentComposanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for managing the association between Batiment and Composante.
 */
@RestController
@RequestMapping("/api/batiments")
public class BatimentComposanteController {

    @Autowired
    private BatimentComposanteService service;

    /**
     * Endpoint to add a Composante to a Batiment.
     *
     * @param batimentCode        The code of the Batiment.
     * @param composanteAcronyme  The acronym of the Composante.
     * @return ResponseEntity with appropriate HTTP status.
     */
    @PostMapping("/{batimentCode}/composantes/{composanteAcronyme}")
    public ResponseEntity<Map<String, String>> addComposanteToBatiment(
            @PathVariable String batimentCode,
            @PathVariable String composanteAcronyme) {
        service.addComposanteToBatiment(batimentCode, composanteAcronyme);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Composante added to Batiment successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to remove a Composante from a Batiment.
     *
     * @param batimentCode        The code of the Batiment.
     * @param composanteAcronyme  The acronym of the Composante.
     * @return ResponseEntity with appropriate HTTP status.
     */
    @DeleteMapping("/{batimentCode}/composantes/{composanteAcronyme}")
    public ResponseEntity<Map<String, String>> removeComposanteFromBatiment(
            @PathVariable String batimentCode,
            @PathVariable String composanteAcronyme) {
        service.removeComposanteFromBatiment(batimentCode, composanteAcronyme);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Composante removed from Batiment successfully.");
        return ResponseEntity.ok(response);
    }
}