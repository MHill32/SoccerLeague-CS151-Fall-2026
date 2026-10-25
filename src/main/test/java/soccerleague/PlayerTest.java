package main.test.java.soccerleague;

import main.java.soccerleague.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest extends TestSupport {

    private Player createPlayer(String id) {
        return new Player(
                id, "Test Player", "ST", 9, 25,
                80, 85, 88, 75, 82, 40, 78, 85, 70
        );
    }

    @Test
    public void testScoreGoal() {
        Player player = createPlayer("PLAYER-TEST-GOAL");

        player.scoreGoal();

        assertEquals(1, player.getGoals());
    }

    @Test
    public void testRecordAssist() {
        Player player = createPlayer("PLAYER-TEST-ASSIST");

        player.recordAssist();

        assertEquals(1, player.getAssists());
    }

    @Test
    public void testYellowCard() {
        Player player = createPlayer("PLAYER-TEST-YELLOW");

        player.receiveCard(" YELLOW ");

        assertEquals(1, player.getYellowCards());
        assertEquals(0, player.getRedCards());
    }

    @Test
    public void testRedCard() {
        Player player = createPlayer("PLAYER-TEST-RED");

        player.receiveCard("red");

        assertEquals(1, player.getRedCards());
        assertEquals(0, player.getYellowCards());
    }

    @Test
    public void testInvalidCardPreservesStatistics() {
        Player player = createPlayer("PLAYER-TEST-INVALID-CARD");
        player.receiveCard("yellow");
        player.receiveCard("red");

        assertThrows(
                IllegalArgumentException.class,
                () -> player.receiveCard("blue")
        );

        assertEquals(1, player.getYellowCards());
        assertEquals(1, player.getRedCards());
    }

    @Test
    public void testDuplicateTrimmedID() {
        createPlayer("PLAYER-TEST-DUPLICATE");

        assertThrows(
                IllegalArgumentException.class,
                () -> createPlayer(" PLAYER-TEST-DUPLICATE ")
        );
    }

    @Test
    public void testInvalidOverallRating() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Player(
                        "PLAYER-TEST-INVALID-RATING", "Test Player",
                        "ST", 9, 25,
                        100, 85, 88, 75, 82, 40, 78, 85, 70
                )
        );
    }
}