package main.test.java.soccerleague;

import main.java.soccerleague.model.Coach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoachLimitTest extends TestSupport {

    @Test
    public void testCoachLimit() {
        for (int i = 1; i <= 100; i++) {
            String id = "LIMIT-C" + i;

            assertDoesNotThrow(
                    () -> new Coach(id, "Coach " + id, "4-3-3")
            );
        }

        assertThrows(
                IllegalStateException.class,
                () -> new Coach("LIMIT-C101", "Coach 101", "4-3-3")
        );
    }
}
