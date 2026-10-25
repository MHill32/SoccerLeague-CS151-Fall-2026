package main.test.java.soccerleague;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import main.java.soccerleague.model.Coach;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Gives each test fresh model registries without adding reset methods to the app.
 * Run these tests sequentially because the registries are shared static state.
 */
public abstract class TestSupport {
    @BeforeEach
    @AfterEach
    void resetModelRegistries() throws ReflectiveOperationException {
        ((Set<?>) staticField(Player.class, "registeredPlayerIDs").get(null)).clear();
        ((Set<?>) staticField(Team.class, "registeredTeamIDs").get(null)).clear();
        ((Map<?, ?>) staticField(Team.class, "playerTeams").get(null)).clear();
        ((Map<?, ?>) staticField(Team.class, "coachTeams").get(null)).clear();
        ((Set<?>) staticField(Coach.class, "registeredCoachIDs").get(null)).clear();
        staticField(Coach.class, "instanceCount").setInt(null, 0);
    }

    private static Field staticField(Class<?> owner, String name)
            throws NoSuchFieldException {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}
