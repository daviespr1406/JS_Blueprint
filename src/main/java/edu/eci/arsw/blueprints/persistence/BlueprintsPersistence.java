package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import java.util.Set;

public interface BlueprintsPersistence {
    Set<Blueprint> getAllBlueprints();
    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;
    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;
    void addBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    void updateBlueprint(String author, String name, Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException;
}
