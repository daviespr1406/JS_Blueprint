package edu.eci.arsw.blueprints.test.services;

import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.impl.BlueprintServicesImpl;
import edu.eci.arsw.blueprints.services.BlueprintServices;
import edu.eci.arsw.blueprints.model.Blueprint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationServicesTests {

    @Autowired
    private BlueprintServicesImpl blueprintsServices;


    @Test
    void contextLoads() {
        assertNotNull(blueprintsServices, "BlueprintsServices should be loaded in Spring context");
    }

    @Test
    void shouldRegisterBlueprint() throws Exception, BlueprintNotFoundException {
        Blueprint bp = new Blueprint("david", "testBlueprint", null);
        blueprintsServices.addBlueprint(bp);

        Set<Blueprint> byAuthor = blueprintsServices.getBlueprintsByAuthor("david");
        assertTrue(byAuthor.stream().anyMatch(b -> b.getName().equals("testBlueprint")));
    }

    /**
     * Verifica que buscar un blueprint inexistente lance excepción.
     */
    @Test
    void shouldThrowWhenBlueprintNotFound() {
        assertThrows(Exception.class, () -> {
            blueprintsServices.getBlueprint("ghostAuthor", "ghostName");
        });
    }
}
