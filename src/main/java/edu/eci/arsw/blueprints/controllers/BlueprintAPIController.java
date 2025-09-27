package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/blueprints")
public class BlueprintAPIController {

    private static final Logger LOG = Logger.getLogger(BlueprintAPIController.class.getName());

    private final BlueprintServices blueprintServices;

    @Autowired
    public BlueprintAPIController(BlueprintServices blueprintServices) {
        this.blueprintServices = blueprintServices;
    }

    @GetMapping
    public ResponseEntity<?> getAllBlueprints() {
        try {
            Set<Blueprint> all = blueprintServices.getAllBlueprints();
            return new ResponseEntity<>(all, HttpStatus.ACCEPTED); // 202 por requerimiento del enunciado
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error obteniendo planos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable("author") String author) {
        try {
            Set<Blueprint> set = blueprintServices.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(set, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            LOG.log(Level.WARNING, null, ex);
            return new ResponseEntity<>("Autor no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error procesando petición", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintByAuthorAndName(@PathVariable("author") String author,
                                                         @PathVariable("bpname") String bpname) {
        try {
            Blueprint bp = blueprintServices.getBlueprint(author, bpname);
            return new ResponseEntity<>(bp, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            LOG.log(Level.WARNING, null, ex);
            return new ResponseEntity<>("Plano no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error procesando petición", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlueprint(@RequestBody Blueprint blueprint) {
        try {
            blueprintServices.addBlueprint(blueprint);
            // Por semántica HTTP, CREATED es 201; el enunciado pide 202 en general.
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException ex) {
            LOG.log(Level.WARNING, null, ex);
            return new ResponseEntity<>("No se pudo crear el plano: " + ex.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(@PathVariable("author") String author,
                                             @PathVariable("bpname") String bpname,
                                             @RequestBody Blueprint blueprint) {
        try {
            blueprintServices.updateBlueprint(author, bpname, blueprint);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            LOG.log(Level.WARNING, null, ex);
            return new ResponseEntity<>("Plano no encontrado", HttpStatus.NOT_FOUND);
        } catch (BlueprintPersistenceException ex) {
            LOG.log(Level.WARNING, null, ex);
            return new ResponseEntity<>("No se pudo actualizar el plano", HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
