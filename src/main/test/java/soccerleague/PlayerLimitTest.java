package main.test.java.soccerleague;

import main.java.soccerleague.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerLimitTest extends TestSupport {

    @Test
    public void testPlayerLimit() {
        // Accept the first 100 players.
        for (int i = 1; i <= 100; i++) {
            String id = "LIMIT-P" + i;
            assertDoesNotThrow(() -> createPlayer(id));
        }

        // Reject the 101st unique player.
        assertThrows(
                IllegalStateException.class,
                () -> createPlayer("LIMIT-P101")
        );
    }

    private Player createPlayer(String id) {
        return new Player(
                id, "Test Player " + id, "ST", 9, 25,
                80, 85, 88, 75, 82, 40, 78, 85, 70
        );
    }
}