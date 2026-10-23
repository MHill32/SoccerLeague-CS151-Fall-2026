package main.test.java.soccerleague;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.soccerleague.exceptions.InvalidMatchOperationException;
import main.java.soccerleague.model.CardEvent;
import main.java.soccerleague.model.GoalEvent;
import main.java.soccerleague.model.Match;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;

public class CardEventTest {

    private static int idCounter = 0;

    private Player newPlayer(String name) {
        idCounter++;
        return new Player("CE-P" + idCounter, name, "DEF", 4, 26, 75, 70, 50, 70, 65, 80, 80, 60, 50);
    }

    private Team newTeam(String name) {
        idCounter++;
        return new Team("CE-T" + idCounter, name, "San Jose");
    }

    @Test
    public void testYellowCardUpdatesPlayer() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player player = newPlayer("Kai");
        home.addPlayer(player);

        Match match = new Match("CE-M1", home, away);
        match.startMatch();
        match.addEvent(new CardEvent(20, player, home, "YELLOW", "foul"));

        assertEquals(1, player.getYellowCards());
        assertEquals(0, player.getRedCards());
        assertEquals(1, home.getYellowCards());
    }

    @Test
    public void testSecondYellowSendsOff() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player player = newPlayer("Kai");
        home.addPlayer(player);

        Match match = new Match("CE-M2", home, away);
        match.startMatch();
        match.addEvent(new CardEvent(20, player, home, "YELLOW", "foul"));
        CardEvent second = new CardEvent(70, player, home, "YELLOW", "dissent");
        match.addEvent(second);

        assertTrue(second.isSecondYellow());
        assertTrue(second.isSendingOff());
        assertEquals(2, player.getYellowCards());
        assertEquals(1, player.getRedCards());
        assertEquals(2, home.getYellowCards());
        assertEquals(1, home.getRedCards());
    }

    @Test
    public void testNoCardAfterSentOff() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player player = newPlayer("Kai");
        home.addPlayer(player);

        Match match = new Match("CE-M3", home, away);
        match.startMatch();
        match.addEvent(new CardEvent(30, player, home, "RED", "violent conduct"));

        assertThrows(InvalidMatchOperationException.class, () -> {
            match.addEvent(new CardEvent(80, player, home, "YELLOW", "dissent"));
        });
    }

    @Test
    public void testSentOffPlayerCannotScore() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player player = newPlayer("Kai");
        home.addPlayer(player);

        Match match = new Match("CE-M4", home, away);
        match.startMatch();
        match.addEvent(new CardEvent(30, player, home, "RED", "violent conduct"));

        assertThrows(InvalidMatchOperationException.class, () -> {
            match.addEvent(new GoalEvent(85, player, home, "REGULAR"));
        });
        assertEquals(0, match.getHomeScore());
    }

    @Test
    public void testInvalidCardTypeAndReason() throws Exception {
        Team home = newTeam("Sharks");
        Player player = newPlayer("Kai");

        assertThrows(IllegalArgumentException.class, () -> new CardEvent(10, player, home, "BLUE", "foul"));
        assertThrows(IllegalArgumentException.class, () -> new CardEvent(10, player, home, "YELLOW", " "));
    }

    @Test
    public void testDuplicateEventIsBlocked() throws Exception {
        Team home = newTeam("Sharks");
        Team away = newTeam("Eagles");
        Player player = newPlayer("Kai");
        home.addPlayer(player);

        Match match = new Match("CE-M5", home, away);
        match.startMatch();
        match.addEvent(new CardEvent(25, player, home, "YELLOW", "foul"));

        assertThrows(IllegalArgumentException.class, () -> {
            match.addEvent(new CardEvent(25, player, home, "YELLOW", "foul"));
        });
    }
}