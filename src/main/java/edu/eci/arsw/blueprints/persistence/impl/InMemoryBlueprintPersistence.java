package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final ConcurrentMap<String, Set<Blueprint>> blueprintsByAuthor = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Blueprint bp1 = new Blueprint("juan", "plano-casa", Arrays.asList(new Point(0, 0), new Point(1, 1), new Point(2, 2)).toArray(new Point[0]));
        Blueprint bp2 = new Blueprint("juan", "plano-oficina", Arrays.asList(new Point(0, 0), new Point(0, 10)).toArray(new Point[0]));
        Blueprint bp3 = new Blueprint("maria", "plano-puente", Arrays.asList(new Point(5, 5), new Point(10, 10)).toArray(new Point[0]));

        try {
            addBlueprint(bp1);
            addBlueprint(bp2);
            addBlueprint(bp3);
        } catch (BlueprintPersistenceException e) {
            // No debería ocurrir en init
            e.printStackTrace();
        }
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        Set<Blueprint> all = new HashSet<>();
        for (Set<Blueprint> s : blueprintsByAuthor.values()) {
            all.addAll(s);
        }
        return all;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> set = blueprintsByAuthor.get(author);
        if (set == null || set.isEmpty()) {
            throw new BlueprintNotFoundException("Author not found: " + author);
        }
        return new HashSet<>(set);
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Set<Blueprint> set = blueprintsByAuthor.get(author);
        if (set == null) throw new BlueprintNotFoundException("Author not found: " + author);
        for (Blueprint bp : set) {
            if (bp.getName().equals(name)) return bp;
        }
        throw new BlueprintNotFoundException("Blueprint not found: " + author + "/" + name);
    }

    @Override
    public void addBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        blueprintsByAuthor.compute(bp.getAuthor(), (author, existingSet) -> {
            if (existingSet == null) {
                Set<Blueprint> newSet = ConcurrentHashMap.newKeySet();
                newSet.add(bp);
                return newSet;
            } else {
                boolean exists = existingSet.stream().anyMatch(p -> p.getName().equals(bp.getName()));
                if (exists) {
                    throw new IllegalStateException("Blueprint already exists for " + bp.getAuthor() + "/" + bp.getName());
                }
                existingSet.add(bp);
                return existingSet;
            }
        });
    }

    @Override
    public void updateBlueprint(String author, String name, Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException {
        boolean updated = blueprintsByAuthor.computeIfPresent(author, (a, set) -> {
            boolean found = false;
            for (Iterator<Blueprint> it = set.iterator(); it.hasNext();) {
                Blueprint cur = it.next();
                if (cur.getName().equals(name)) {
                    it.remove();
                    found = true;
                    break;
                }
            }
            if (!found) {
                return set;
            }
            set.add(bp);
            return set;
        }) != null;


        Set<Blueprint> set = blueprintsByAuthor.get(author);
        if (set == null) throw new BlueprintNotFoundException("Author not found: " + author);
        boolean existsNow = set.stream().anyMatch(b -> b.getName().equals(bp.getName()));
        if (!existsNow) throw new BlueprintPersistenceException("No blueprint replaced; maybe it didn't exist: " + author + "/" + name);
    }
}
