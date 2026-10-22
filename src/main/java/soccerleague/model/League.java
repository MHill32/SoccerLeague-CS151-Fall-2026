/*
 * Author: Mason Hill
*/
package main.java.soccerleague.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Comparator;

public class League {
    private String leagueId;
    private String leagueName;

    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    private ArrayList<Standing> standings;

    private HashSet<Match> processedMatches;    //Keeps track of matches that have been processed in standings 

    private static final int MAX_INSTANCES = 100;

    //League Constructor
    public League(String leagueID, String leagueName) {
        if (leagueId == null || leagueId.isBlank()) {
            throw new IllegalArgumentException("League ID cannot be blank");
        }

        if (leagueName == null || leagueName.isBlank()) {
            throw new IllegalArgumentException("League name cannot be blank");
        }
        
        this.leagueId = leagueID;
        this.leagueName = leagueName;
        
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.standings = new ArrayList<>();
        this.processedMatches = new HashSet<>();
    }
    
    //Adds a team into the league
    public void promoteTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }

        for (Team existingTeam : teams) {
            if (existingTeam.getTeamID().equals(team.getTeamID())) {
                throw new IllegalArgumentException("Team already in league");
            }
        }

        if (teams.size() >= MAX_INSTANCES) {
            throw new IllegalStateException("Maximum number of teams reached");
        }

        teams.add(team);
        standings.add(new Standing(team));
    }

    //Removes a team from the league
    public void relegateTeam(Team team) {
        if (team == null || !teams.contains(team)) {
            throw new IllegalArgumentException("Team is not in this league");
        }

        for (Match match: matches) {
            if (match.getHomeTeam() == team || match.getAwayTeam() == team) {
                throw new IllegalStateException("Can't remove team with active matches");
            }
        }

        teams.remove(team);
        for (int i = 0; i < standings.size(); i++) {
            if (standings.get(i).getTeam() == team) {
                standings.remove(i);
                break;
            }
        }
    }

    //Schedules a match between two teams in the league
    public void scheduleMatch(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }

        if (matches.size() >= MAX_INSTANCES) {
            throw new IllegalStateException("Maximum number of matches reached");
        }

        if (!teams.contains(match.getHomeTeam()) || !teams.contains(match.getAwayTeam())) {
            throw new IllegalArgumentException("One or both teams are not in this league");
        }

        if (match.getHomeTeam() == match.getAwayTeam()) {
            throw new IllegalArgumentException("Teams must be different");
        }

        for (Match existing : matches) {
            if (existing.getMatchId().equals(match.getMatchId())) {
                throw new IllegalArgumentException("Match already scheduled");
            }
        }

        if (match.isStarted() || match.isCompleted()) {
            throw new IllegalStateException("Match already started or completed");
        }

        matches.add(match);
    }

    //Updates match results to the league standings
    public void updateStandings() {
        for (Match match : matches) {
            if (!match.isCompleted() || processedMatches.contains(match)) {
                continue;
            }

            Standing homeStanding = findStanding(match.getHomeTeam());
            Standing awayStanding = findStanding(match.getAwayTeam());

            int homeScore = match.getHomeScore();
            int awayScore = match.getAwayScore();

            if (homeScore > awayScore) {
                homeStanding.recordWin(awayScore, homeScore);
                awayStanding.recordLoss(awayScore, homeScore);
            } else if (homeScore < awayScore) {
                awayStanding.recordWin(awayScore, homeScore);
                homeStanding.recordLoss(awayScore, homeScore);
            } else {
                homeStanding.recordDraw(awayScore, homeScore);
                awayStanding.recordDraw(awayScore, homeScore);
            }

            processedMatches.add(match);
        }
    }

    //Finds a teams standing in the league
    private Standing findStanding(Team team) {
        for (Standing standing : standings) {
            if (standing.getTeam() == team) {
                return standing;
            }
        }
        throw new IllegalArgumentException("No standing found for team");
    }

    //Return standings sorted by points and goal difference
    public List<Standing> getRankedStandings() {
        ArrayList<Standing> ranked = new ArrayList<>(standings);

        ranked.sort(new Comparator<Standing>() {
            @Override
            public int compare(Standing a, Standing b) {
                //First compare points
                int pointsComparison = Integer.compare(b.getPoints(), a.getPoints());

                if (pointsComparison != 0) {
                    return pointsComparison;
            }
            //If points are equal, compare goal difference
            return Integer.compare(b.getGoalDifference(), a.getGoalDifference());
            }
        });

        return ranked;
    }

    //Getters
    public String getLeagueId() {
        return leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public List<Team> getTeams() {
        return List.copyOf(teams);
    }

    public List<Match> getMatches() {
        return List.copyOf(matches);
    }

    public List<Standing> getStandings() {
        return List.copyOf(standings);
    }

    @Override
    public String toString() {
        return "League: " + leagueName
            + ", ID: " + leagueId
            + ", Teams: " + teams.size()
            + ", Matches: " + matches.size();
    }
}
