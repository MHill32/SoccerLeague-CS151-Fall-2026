package main.test.java.soccerleague;

import main.java.soccerleague.model.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeamLimitTest extends TestSupport {

    @Test
    public void testTeamLimit() {
        for (int i = 1; i <= 100; i++) {
            String id = "LIMIT-T" + i;

            assertDoesNotThrow(
                    () -> new Team(id, "Team " + id, "Test City")
            );
        }

        assertThrows(
                IllegalStateException.class,
                () -> new Team("LIMIT-T101", "Team 101", "Test City")
        );
    }
}
