package main.test.java.soccerleague;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.soccerleague.model.Standing;
import main.java.soccerleague.model.Team;

public class StandingTest extends TestSupport {
    @Test
    public void testRecordWin() {
        Team team = new Team("T01", "Reds", "Liverpool");
        Standing standing = new Standing(team);

        standing.recordWin(3, 1);

        assertEquals(1, standing.getWins());
        assertEquals(1, standing.getMatchesPlayed());
        assertEquals(3, standing.getPoints());
        assertEquals(2, standing.getGoalDifference());
    }

    @Test
    public void testRecordDraw() {
        Team team = new Team("T02", "Spurs", "Tottenham");
        Standing standing = new Standing(team);

        standing.recordDraw(2, 2);

        assertEquals(1, standing.getDraws());
        assertEquals(1, standing.getMatchesPlayed());
        assertEquals(1, standing.getPoints());
        assertEquals(0, standing.getGoalDifference());
    }

    @Test
    public void testRecordLoss() {
        Team team = new Team("T03", "Chelsea", "London");
        Standing standing = new Standing(team);

        standing.recordLoss(1, 4);

        assertEquals(1, standing.getLosses());
        assertEquals(1, standing.getMatchesPlayed());
        assertEquals(0, standing.getPoints());
        assertEquals(-3, standing.getGoalDifference());
    }

    @Test
    public void testInvalidWin() {
        Team team = new Team("T04", "Fulham", "Fulham");
        Standing standing = new Standing(team);

        assertThrows(IllegalArgumentException.class, () -> {
            standing.recordWin(1, 3);
        });

        assertEquals(0, standing.getMatchesPlayed());
    }

    @Test
    public void testNegativeGoals() {
        Team team = new Team("T05", "Manchester United", "Manchester");
        Standing standing = new Standing(team);

        assertThrows(IllegalArgumentException.class, () -> {
            standing.recordWin(-1, 2);
        });
    }

    @Test
    public void testMultipleMatches() {
        Team team = new Team("T06", "Leeds United", "Leeds");
        Standing standing = new Standing(team);

        standing.recordWin(3, 1);
        standing.recordDraw(1, 1);
        standing.recordLoss(0, 2);

        assertEquals(3, standing.getMatchesPlayed());
        assertEquals(1, standing.getWins());
        assertEquals(1, standing.getDraws());
        assertEquals(1, standing.getLosses());
        assertEquals(4, standing.getPoints());
        assertEquals(0, standing.getGoalDifference());
    }
}
