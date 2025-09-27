package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public interface BlueprintServices {
    Set<Blueprint> getAllBlueprints();
    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;
    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;
    void addBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    void updateBlueprint(String author, String name, Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException;
}
