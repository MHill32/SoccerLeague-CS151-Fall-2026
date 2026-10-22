package main.test.java.soccerleague;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.soccerleague.model.Match;
import main.java.soccerleague.model.Team;

public class MatchTest {
    @Test 
    public void testStartMatch() {
        Team homeTeam = new Team("T01", "Arsenal", "London");
        Team awayTeam = new Team("T02", "Spurs", "Tottenham");
        
        Match match = new Match("M01", homeTeam, awayTeam);
        match.startMatch();

        assertTrue(match.isStarted());
        assertFalse(match.isCompleted());
    }

    @Test
    public void testRecordGoals() {
        Team homeTeam = new Team("T03", "Chelsea", "London");
        Team awayTeam = new Team("T04", "Fulham", "Fulham");

        Match match = new Match("M02", homeTeam, awayTeam);
        match.startMatch();

        match.recordGoal(homeTeam);
        match.recordGoal(homeTeam);
        match.recordGoal(awayTeam);

        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    public void testEndMatch() {
        Team homeTeam = new Team("T05", "Man City", "Mancester");
        Team awayTeam = new Team("T06", "Man United", "Mancester");

        Match match = new Match("M03", homeTeam, awayTeam);
        match.startMatch();
        match.endMatch();

        assertTrue(match.isCompleted());
    }

    @Test
    public void testDetermineWinner() {
        Team homeTeam = new Team("T07", "Reds", "Liverpool");
        Team awayTeam = new Team("T08", "Hull City", "Hull");

        Match match = new Match("M04", homeTeam, awayTeam);
        match.startMatch();
        match.recordGoal(awayTeam);
        match.recordGoal(awayTeam);
        match.recordGoal(homeTeam);
        match.endMatch();

        assertEquals(awayTeam, match.determineWinner());
    }

    @Test
    public void testGoalBeforeStart() {
        Team homeTeam = new Team("T09", "Ipswich Town", "Ipswich");
        Team awayTeam = new Team("T10", "Leeds United", "Leeds");

        Match match = new Match("M05", homeTeam, awayTeam);

        assertThrows(IllegalStateException.class, () -> {
            match.recordGoal(homeTeam);
        });

        assertEquals(0, match.getHomeScore());
    }
}
