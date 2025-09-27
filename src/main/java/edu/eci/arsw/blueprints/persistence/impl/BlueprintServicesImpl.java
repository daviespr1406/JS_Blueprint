package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BlueprintServicesImpl implements BlueprintServices {

    private final BlueprintsPersistence persistence;
    private static final Logger LOG = Logger.getLogger(BlueprintServicesImpl.class.getName());

    @Autowired
    public BlueprintServicesImpl(BlueprintsPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        return persistence.getAllBlueprints();
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        return persistence.getBlueprintsByAuthor(author);
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return persistence.getBlueprint(author, name);
    }

    @Override
    public void addBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        try {
            persistence.addBlueprint(bp);
        } catch (IllegalStateException e) {
            LOG.log(Level.WARNING, "Blueprint exists: " + bp.getAuthor() + "/" + bp.getName(), e);
            throw new BlueprintPersistenceException("Blueprint already exists: " + bp.getAuthor() + "/" + bp.getName());
        }
    }

    @Override
    public void updateBlueprint(String author, String name, Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException {
        persistence.updateBlueprint(author, name, bp);
    }
}
