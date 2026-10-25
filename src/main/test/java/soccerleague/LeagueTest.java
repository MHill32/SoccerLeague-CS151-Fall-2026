package main.test.java.soccerleague;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.soccerleague.exceptions.DuplicateObjectException;
import main.java.soccerleague.model.League;
import main.java.soccerleague.model.Match;
import main.java.soccerleague.model.Team;

public class LeagueTest extends TestSupport {
    @Test 
    public void testPromoteTeam() throws Exception {
        League league = new League("L01", "Premier League");
        Team team = new Team("T01", "Reds", "Liverpool");
        
        league.promoteTeam(team);

        assertEquals(1, league.getTeams().size());
        assertEquals(1, league.getStandings().size());
        assertSame(team, league.getTeams().get(0));
    }

    @Test 
    public void testDuplicateTeam() throws Exception {
        League league = new League("L02", "Premier League");
        Team team = new Team("T02", "Manchester City", "Manchester");
        league.promoteTeam(team);

        assertThrows(DuplicateObjectException.class, () -> {
            league.promoteTeam(team);
        });

        assertEquals(1, league.getTeams().size());
    }

    @Test
    public void testScheduleMatch() throws Exception {
        League league = new League("L03", "Premier League");
        Team homeTeam = new Team("T04", "Chelsea", "London");
        Team awayTeam = new Team("T05", "Fulham", "Fulham");

        league.promoteTeam(homeTeam);
        league.promoteTeam(awayTeam);

        Match match = new Match("M01", homeTeam, awayTeam);
        league.scheduleMatch(match);

        assertEquals(1, league.getMatches().size());
        assertSame(match, league.getMatches().get(0));
    }

    @Test
    public void testUpdateStandings() throws Exception {
        League league = new League("L04", "Premier League");
        Team homeTeam = new Team("T06", "Spurs", "Tottenham");
        Team awayTeam = new Team("T07", "Hull City", "Hull");

        league.promoteTeam(homeTeam);
        league.promoteTeam(awayTeam);

        Match match = new Match("M02", homeTeam, awayTeam);
        league.scheduleMatch(match);

        match.startMatch();
        match.recordGoal(homeTeam);
        match.recordGoal(homeTeam);
        match.recordGoal(awayTeam);
        match.endMatch();

        league.updateStandings();

        assertEquals(3, league.getRankedStandings().get(0).getPoints());
        assertEquals(0, league.getRankedStandings().get(1).getPoints());
        assertSame(homeTeam, league.getRankedStandings().get(0).getTeam());
    }

    @Test
    public void testNoDuplicateStandingsUpdate() throws Exception {
        League league = new League("L05", "Premier League");
        Team homeTeam = new Team("T08", "Arsenal", "London");
        Team awayTeam = new Team("T09", "Ipswich Town", "Ipswich");

        league.promoteTeam(homeTeam);
        league.promoteTeam(awayTeam);

        Match match = new Match("M03", homeTeam, awayTeam);
        league.scheduleMatch(match);

        match.startMatch();
        match.recordGoal(homeTeam);
        match.endMatch();

        league.updateStandings();
        league.updateStandings();

        assertEquals(3, league.getRankedStandings().get(0).getPoints());
        assertEquals(1, league.getRankedStandings().get(0).getMatchesPlayed());
    }
}
