package main.test.java.soccerleague;

import main.java.soccerleague.model.Coach;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest extends TestSupport {
    private Player createPlayer(String id, int rating) {
        return new Player(id, "Player " + id, "ST", 9, 25,
                rating, rating, rating, rating, rating,
                rating, rating, rating, rating);
    }

    @Test
    public void testAddPlayer() {
        Team team = new Team("TEAM-ADD", "Arsenal", "London");
        Player player = createPlayer("TEAM-P-ADD", 80);
        team.addPlayer(player);
        assertEquals(1, team.getRosterSize());
        assertTrue(team.getPlayers().contains(player));
    }

    @Test
    public void testPlayerCannotJoinTwoTeams() {
        Team first = new Team("TEAM-MEMBER-A", "Arsenal", "London");
        Team second = new Team("TEAM-MEMBER-B", "Liverpool", "Liverpool");
        Player player = createPlayer("TEAM-P-MEMBER", 80);
        first.addPlayer(player);
        assertThrows(IllegalArgumentException.class, () -> second.addPlayer(player));
        assertTrue(first.getPlayers().contains(player));
        assertEquals(0, second.getRosterSize());
    }

    @Test
    public void testDuplicateRosterAddition() {
        Team team = new Team("TEAM-DUP-ROSTER", "Arsenal", "London");
        Player player = createPlayer("TEAM-P-DUP", 80);
        team.addPlayer(player);
        assertThrows(IllegalArgumentException.class, () -> team.addPlayer(player));
        assertEquals(1, team.getRosterSize());
    }

    @Test
    public void testRemovePlayerClearsMembership() {
        Team first = new Team("TEAM-REMOVE-A", "Arsenal", "London");
        Team second = new Team("TEAM-REMOVE-B", "Liverpool", "Liverpool");
        Player player = createPlayer("TEAM-P-REMOVE", 80);
        first.addPlayer(player);
        first.removePlayer(player);
        second.addPlayer(player);
        assertEquals(0, first.getRosterSize());
        assertTrue(second.getPlayers().contains(player));
    }

    @Test
    public void testTransferPlayer() {
        Team first = new Team("TEAM-TRANSFER-A", "Arsenal", "London");
        Team second = new Team("TEAM-TRANSFER-B", "Liverpool", "Liverpool");
        Player player = createPlayer("TEAM-P-TRANSFER", 80);
        first.addPlayer(player);
        first.transferPlayer(player, second);
        assertEquals(0, first.getRosterSize());
        assertTrue(second.getPlayers().contains(player));
        assertThrows(IllegalArgumentException.class, () -> first.addPlayer(player));
    }

    @Test
    public void testFailedTransferPreservesRoster() {
        Team team = new Team("TEAM-FAILED-TRANSFER", "Arsenal", "London");
        Player player = createPlayer("TEAM-P-FAILED-TRANSFER", 80);
        team.addPlayer(player);
        assertThrows(IllegalArgumentException.class, () -> team.transferPlayer(player, team));
        assertThrows(IllegalArgumentException.class, () -> team.transferPlayer(player, null));
        assertEquals(1, team.getRosterSize());
        assertTrue(team.getPlayers().contains(player));
    }

    @Test
    public void testRosterGetterReturnsCopy() {
        Team team = new Team("TEAM-COPY", "Arsenal", "London");
        Player player = createPlayer("TEAM-P-COPY", 80);
        team.addPlayer(player);
        team.getPlayers().clear();
        assertEquals(1, team.getRosterSize());
        assertTrue(team.getPlayers().contains(player));
    }

    @Test
    public void testRatingAverages() {
        Team team = new Team("TEAM-RATINGS", "Arsenal", "London");
        team.addPlayer(createPlayer("TEAM-P-RATING-A", 70));
        team.addPlayer(createPlayer("TEAM-P-RATING-B", 91));
        assertEquals(80.5, team.calculateOverallRating(), 0.001);
        assertEquals(80.5, team.calculateAttackRating(), 0.001);
        assertEquals(80.5, team.calculateDefenseRating(), 0.001);
    }

    @Test
    public void testEmptyTeamRatings() {
        Team team = new Team("TEAM-EMPTY", "Arsenal", "London");
        assertEquals(0.0, team.calculateOverallRating(), 0.001);
        assertEquals(0.0, team.calculateAttackRating(), 0.001);
        assertEquals(0.0, team.calculateDefenseRating(), 0.001);
    }

    @Test
    public void testStatisticsStayWithTeamAfterTransfer() {
        Team first = new Team("TEAM-STATS-A", "Arsenal", "London");
        Team second = new Team("TEAM-STATS-B", "Liverpool", "Liverpool");
        Player player = createPlayer("TEAM-P-STATS", 80);
        first.addPlayer(player);
        first.recordGoal();
        first.recordAssist();
        first.recordCard(" YELLOW ");
        first.recordCard("red");
        first.transferPlayer(player, second);
        assertEquals(1, first.getGoals());
        assertEquals(1, first.getAssists());
        assertEquals(1, first.getYellowCards());
        assertEquals(1, first.getRedCards());
        assertEquals(0, second.getGoals());
        assertEquals(0, second.getAssists());
        assertEquals(0, second.getYellowCards());
        assertEquals(0, second.getRedCards());
    }

    @Test
    public void testInvalidCardPreservesStatistics() {
        Team team = new Team("TEAM-INVALID-CARD", "Arsenal", "London");
        team.recordCard("yellow");
        assertThrows(IllegalArgumentException.class, () -> team.recordCard("blue"));
        assertEquals(1, team.getYellowCards());
        assertEquals(0, team.getRedCards());
    }

    @Test
    public void testDuplicateTrimmedTeamID() {
        new Team("TEAM-DUP-ID", "Arsenal", "London");
        assertThrows(IllegalArgumentException.class,
                () -> new Team(" TEAM-DUP-ID ", "Liverpool", "Liverpool"));
    }

    @Test
    public void testCoachCannotJoinTwoTeams() {
        Team first = new Team("COACH-OWNER-A", "First", "City");
        Team second = new Team("COACH-OWNER-B", "Second", "City");
        Coach coach = new Coach("COACH-OWNER-C", "Alex", "4-3-3");
        first.addCoach(coach);

        assertThrows(IllegalStateException.class, () -> second.addCoach(coach));
        assertSame(coach, first.getCoach());
        assertNull(second.getCoach());
    }

    @Test
    public void testCoachCanJoinAnotherTeamAfterRemoval() {
        Team first = new Team("COACH-REASSIGN-A", "First", "City");
        Team second = new Team("COACH-REASSIGN-B", "Second", "City");
        Coach coach = new Coach("COACH-REASSIGN-C", "Alex", "4-3-3");
        first.addCoach(coach);
        first.removeCoach();
        second.addCoach(coach);

        assertNull(first.getCoach());
        assertSame(coach, second.getCoach());
        assertThrows(IllegalStateException.class, () -> first.addCoach(coach));
    }

    @Test
    public void testFailedCoachAssignmentDoesNotReserveCoach() {
        Team first = new Team("COACH-FAIL-A", "First", "City");
        Team second = new Team("COACH-FAIL-B", "Second", "City");
        Coach current = new Coach("COACH-FAIL-CURRENT", "Alex", "4-3-3");
        Coach incoming = new Coach("COACH-FAIL-INCOMING", "Sam", "4-4-2");
        first.addCoach(current);

        assertThrows(IllegalStateException.class, () -> first.addCoach(incoming));
        second.addCoach(incoming);
        assertSame(current, first.getCoach());
        assertSame(incoming, second.getCoach());
    }

    @Test
    public void testInvalidCoachOperationsPreserveAssignment() {
        Team team = new Team("COACH-INVALID-TEAM", "First", "City");
        assertThrows(IllegalStateException.class, team::removeCoach);
        assertThrows(IllegalArgumentException.class, () -> team.addCoach(null));
        Coach coach = new Coach("COACH-INVALID-ASSIGN", "Alex", "4-3-3");
        team.addCoach(coach);
        assertThrows(IllegalStateException.class, () -> team.addCoach(coach));
        assertSame(coach, team.getCoach());
    }
}
