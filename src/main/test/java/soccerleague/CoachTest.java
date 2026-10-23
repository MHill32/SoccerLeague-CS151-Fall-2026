package main.test.java.soccerleague;

import java.util.ArrayList;
import main.java.soccerleague.model.Coach;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;
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

    private Team assignTeam(Coach coach, ArrayList<Player> players) {
        Team team = new Team("TEAM-" + coach.getCoachID(), "Test Team", "Test City");
        team.addCoach(coach);
        for (Player player : players) {
            team.addPlayer(player);
        }
        return team;
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
        Team team = assignTeam(coach, players);
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
        Team team = assignTeam(coach, valid);
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
        Player outfield = player("COACH-NO-GK", "ST");
        team.addPlayer(outfield);
        noGoalkeeper.set(0, outfield);
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(noGoalkeeper));

        ArrayList<Player> twoGoalkeepers = new ArrayList<>(valid);
        Player goalkeeper = player("COACH-SECOND-GK", "GK");
        team.addPlayer(goalkeeper);
        twoGoalkeepers.set(1, goalkeeper);
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(twoGoalkeepers));
        ArrayList<Player> outsideLineup = new ArrayList<>(valid);
        outsideLineup.set(1, player("COACH-OUTSIDE-LINEUP", "ST"));
        assertThrows(IllegalArgumentException.class,
                () -> coach.selectStartingLineup(outsideLineup));
        assertEquals(valid, coach.getStartingLineup());
    }

    @Test
    public void testSubstitution() {
        Coach coach = new Coach("COACH-SUB", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("COACH-SUB-P");
        Team team = assignTeam(coach, players);
        coach.selectStartingLineup(players);
        Player incoming = player("COACH-SUB-IN", "ST");
        team.addPlayer(incoming);
        coach.substitutePlayer(players.get(1), incoming);
        assertEquals(11, coach.getStartingLineup().size());
        assertSame(incoming, coach.getStartingLineup().get(1));
        assertFalse(coach.getStartingLineup().contains(players.get(1)));
    }

    @Test
    public void testInvalidSubstitutionsPreserveLineup() {
        Coach coach = new Coach("COACH-INVALID-SUB", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("COACH-INVALID-SUB-P");
        Team team = assignTeam(coach, players);
        coach.selectStartingLineup(players);
        Player outsider = player("COACH-OUTSIDER", "ST");
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(1), outsider));
        Player bench = player("COACH-INVALID-SUB-BENCH", "ST");
        team.addPlayer(bench);
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(null, outsider));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(outsider, players.get(1)));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(1), players.get(1)));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(1), players.get(2)));
        assertThrows(IllegalArgumentException.class,
                () -> coach.substitutePlayer(players.get(0), bench));
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

    @Test
    public void testUnassignedCoachCannotSelectLineup() {
        Coach coach = new Coach("COACH-UNASSIGNED", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("UNASSIGNED-P");
        assertThrows(IllegalStateException.class, () -> coach.selectStartingLineup(players));
        assertTrue(coach.getStartingLineup().isEmpty());
    }

    @Test
    public void testStarterRemovalClearsLineupAndBenchRemovalPreservesIt() {
        Coach coach = new Coach("COACH-REMOVE", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("REMOVE-P");
        Team team = assignTeam(coach, players);
        Player bench = player("REMOVE-BENCH", "ST");
        team.addPlayer(bench);
        coach.selectStartingLineup(players);

        team.removePlayer(bench);
        assertEquals(players, coach.getStartingLineup());
        team.removePlayer(players.get(1));
        assertTrue(coach.getStartingLineup().isEmpty());
        assertFalse(team.getPlayers().contains(players.get(1)));
    }

    @Test
    public void testStarterTransferClearsOnlySourceLineup() {
        Coach coach = new Coach("COACH-TRANSFER", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("TRANSFER-P");
        Team source = assignTeam(coach, players);
        Team destination = new Team("COACH-TRANSFER-DEST", "Destination", "City");
        coach.selectStartingLineup(players);

        source.transferPlayer(players.get(1), destination);
        assertTrue(coach.getStartingLineup().isEmpty());
        assertFalse(source.getPlayers().contains(players.get(1)));
        assertTrue(destination.getPlayers().contains(players.get(1)));
    }

    @Test
    public void testCoachRemovalClearsLineupAndAllowsReassignment() {
        Coach coach = new Coach("COACH-LEAVE", "Alex", "4-3-3");
        ArrayList<Player> players = lineup("LEAVE-P");
        Team source = assignTeam(coach, players);
        coach.selectStartingLineup(players);
        source.removeCoach();

        assertNull(source.getCoach());
        assertTrue(coach.getStartingLineup().isEmpty());
        assertThrows(IllegalStateException.class, () -> coach.selectStartingLineup(players));

        Team destination = new Team("COACH-LEAVE-DEST", "Destination", "City");
        destination.addCoach(coach);
        assertSame(coach, destination.getCoach());
        assertThrows(IllegalArgumentException.class, () -> coach.selectStartingLineup(players));
        assertTrue(coach.getStartingLineup().isEmpty());
    }
}
