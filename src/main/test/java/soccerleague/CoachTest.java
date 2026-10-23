package main.test.java.soccerleague;

import java.util.ArrayList;
import main.java.soccerleague.model.Coach;
import main.java.soccerleague.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoachTest {
    private Player player(String id, String position) {
        return new Player(id, "Player " + id, position, 9, 25,
                80, 80, 80, 80, 80, 80, 80, 80, 80);
    }

    private ArrayList<Player> lineup(String prefix) {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            players.add(player(prefix + i, i == 0 ? "GK" : "ST"));
        }
        return players;
    }

    @Test
    public void testConstructorTrimsInput() {
        Coach coach = new Coach(" COACH-TRIM ", " Alex Smith ", " 4-3-3 ");
        assertEquals("COACH-TRIM", coach.getCoachID());
        assertEquals("Alex Smith", coach.getName());
        assertEquals("4-3-3", coach.getFormation());
        assertTrue(coach.getStartingLineup().isEmpty());
    }

    @Test
    public void testDuplicateTrimmedID() {
        new Coach("COACH-DUP", "Alex", "4-3-3");
        assertThrows(IllegalArgumentException.class,
                () -> new Coach(" COACH-DUP ", "Other", "4-4-2"));
    }

    @Test
    public void testInvalidConstructorInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coach(null, "Alex", "4-3-3"));
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("COACH-BLANK", " ", "4-3-3"));
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("COACH-FORMAT", "Alex", "four-three-three"));
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("COACH-TOTAL", "Alex", "4-4-3"));
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("COACH-NULL-FORM", "Alex", null));
    }

    @Test
    public void testChangeFormationAndRejectInvalidChange() {
        Coach coach = new Coach("COACH-CHANGE", "Alex", "4-3-3");
        coach.changeFormation(" 4-2-3-1 ");
        assertEquals("4-2-3-1", coach.getFormation());
        assertThrows(IllegalArgumentException.class, () -> coach.changeFormation("4-4-3"));
        assertEquals("4-2-3-1", coach.getFormation());
    }

    @Test
    public void testSelectLineupAndProtectCopies() {
        Coach coach = new Coach("COACH-SELECT", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("COACH-SELECT-P");
        coach.selectStartingLineup(players);
        ArrayList<Player> expected = new ArrayList<>(players);
        players.clear();
        coach.getStartingLineup().clear();
        assertEquals(expected, coach.getStartingLineup());
    }

    @Test
    public void testInvalidLineupsPreservePreviousSelection() {
        Coach coach = new Coach("COACH-INVALID", "Alex", "4-3-3");
        ArrayList<Player> valid = lineup("COACH-INVALID-P");
        coach.selectStartingLineup(valid);
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(null));
        assertThrows(IllegalArgumentException.class,
                () -> coach.selectStartingLineup(new ArrayList<>()));

        ArrayList<Player> duplicate = new ArrayList<>(valid);
        duplicate.set(2, valid.get(1));
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(duplicate));

        ArrayList<Player> withNull = new ArrayList<>(valid);
        withNull.set(2, null);
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(withNull));

        ArrayList<Player> noGoalkeeper = new ArrayList<>(valid);
        noGoalkeeper.set(0, player("COACH-NO-GK", "ST"));
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(noGoalkeeper));

        ArrayList<Player> twoGoalkeepers = new ArrayList<>(valid);
        twoGoalkeepers.set(1, player("COACH-SECOND-GK", "GK"));
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(twoGoalkeepers));
        assertEquals(valid, coach.getStartingLineup());
    }

    @Test
    public void testSubstitution() {
        Coach coach = new Coach("COACH-SUB", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("COACH-SUB-P");
        coach.selectStartingLineup(players);
        Player incoming = player("COACH-SUB-IN", "ST");
        coach.substitutePlayer(players.get(1), incoming);
        assertEquals(11, coach.getStartingLineup().size());
        assertSame(incoming, coach.getStartingLineup().get(1));
        assertFalse(coach.getStartingLineup().contains(players.get(1)));
    }

    @Test
    public void testInvalidSubstitutionsPreserveLineup() {
        Coach coach = new Coach("COACH-INVALID-SUB", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("COACH-INVALID-SUB-P");
        coach.selectStartingLineup(players);
        Player outsider = player("COACH-OUTSIDER", "ST");
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(null, outsider));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(outsider, players.get(1)));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(1), players.get(1)));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(1), players.get(2)));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(0), outsider));
        assertEquals(players, coach.getStartingLineup());
    }

    @Test
    public void testSubstitutionRequiresLineup() {
        Coach coach = new Coach("COACH-NO-LINEUP", "Alex", "4-3-3");
        Player outgoing = player("COACH-NO-LINEUP-OUT", "ST");
        Player incoming = player("COACH-NO-LINEUP-IN", "ST");
        assertThrows(IllegalStateException.class,
                () -> coach.substitutePlayer(outgoing, incoming));
    }
}
