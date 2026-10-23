package main.test.java.soccerleague;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.soccerleague.exceptions.InvalidMatchOperationException;
import main.java.soccerleague.model.GoalEvent;
import main.java.soccerleague.model.Match;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;

public class GoalEventTest {

    private static int idCounter = 0;

    private Player newPlayer(String name) {
        idCounter++;
        return new Player("GE-P" + idCounter, name, "FWD", 9, 24, 80, 80, 80, 80, 80, 50, 70, 80, 70);
    }

    private Team newTeam(String name) {
        idCounter++;
        return new Team("GE-T" + idCounter, name, "San Jose");
    }

    @Test
    public void testGoalUpdatesScoreAndStats() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player scorer = newPlayer("Leo");
        Player assister = newPlayer("Sam");
        home.addPlayer(scorer);
        home.addPlayer(assister);

        Match match = new Match("GE-M1", home, away);
        match.startMatch();
        match.addEvent(new GoalEvent(12, scorer, home, "REGULAR", assister));

        assertEquals(1, match.getHomeScore());
        assertEquals(1, scorer.getGoals());
        assertEquals(1, assister.getAssists());
        assertEquals(1, home.getGoals());
        assertEquals(1, home.getAssists());
    }

    @Test
    public void testOwnGoalCountsForOtherTeam() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player defender = newPlayer("Kai");
        away.addPlayer(defender);

        Match match = new Match("GE-M2", home, away);
        match.startMatch();
        match.addEvent(new GoalEvent(30, defender, away, "OWN_GOAL"));

        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertEquals(0, defender.getGoals());
        assertEquals(1, home.getGoals());
        assertEquals(0, away.getGoals());
    }

    @Test
    public void testSelfAssistIsBlocked() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player scorer = newPlayer("Leo");
        home.addPlayer(scorer);

        Match match = new Match("GE-M3", home, away);
        match.startMatch();

        assertThrows(InvalidMatchOperationException.class, () -> {
            match.addEvent(new GoalEvent(40, scorer, home, "REGULAR", scorer));
        });
        assertEquals(0, match.getHomeScore());
    }

    @Test
    public void testPenaltyCannotHaveAssist() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player scorer = newPlayer("Leo");
        Player teammate = newPlayer("Sam");
        home.addPlayer(scorer);
        home.addPlayer(teammate);

        Match match = new Match("GE-M4", home, away);
        match.startMatch();

        assertThrows(InvalidMatchOperationException.class, () -> {
            match.addEvent(new GoalEvent(50, scorer, home, "PENALTY", teammate));
        });
    }

    @Test
    public void testScorerMustBeOnTeam() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player outsider = newPlayer("Max");

        Match match = new Match("GE-M5", home, away);
        match.startMatch();

        assertThrows(InvalidMatchOperationException.class, () -> {
            match.addEvent(new GoalEvent(60, outsider, home, "REGULAR"));
        });
    }

    @Test
    public void testInvalidGoalTypeAndMinute() throws Exception {
        Team home = newTeam("Sharks");
        Player scorer = newPlayer("Leo");

        assertThrows(IllegalArgumentException.class, () -> new GoalEvent(10, scorer, home, "BICYCLE"));
        assertThrows(IllegalArgumentException.class, () -> new GoalEvent(130, scorer, home, "REGULAR"));
    }
}