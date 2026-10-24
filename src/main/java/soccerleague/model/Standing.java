/*
 * Author: Mason Hill
*/
package main.java.soccerleague.model;

public class Standing {
    private Team team;

    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
    
    //Standing Constructor
    public Standing(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Standing must be associated with a team");
        }

        this.team = team;
    }

    // Win Method (+3 points)
    public void recordWin(int goalsScored, int goalsAllowed) {
        validateGoals(goalsScored, goalsAllowed);

        if (goalsScored <= goalsAllowed) {
            throw new IllegalArgumentException("A win requires more goals scored than allowed");
        }

        matchesPlayed++;
        wins++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
        points += 3;
    }

    // Draw Method (+1 point)
    public void recordDraw(int goalsScored, int goalsAllowed) {
        validateGoals(goalsScored, goalsAllowed);

        if (goalsScored != goalsAllowed) {
            throw new IllegalArgumentException("A draw requires the score to be equal");
        }

        matchesPlayed++;
        draws++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
        points += 1;
    }

    // Loss Method (0 points)
    public void recordLoss(int goalsScored, int goalsAllowed) {
        validateGoals(goalsScored, goalsAllowed);

        if (goalsScored >= goalsAllowed) {
            throw new IllegalArgumentException("A loss requires less goals scored than allowed");
        }

        matchesPlayed++;
        losses++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
    }

    // Helper Method that ensures goalsScored and goalsAllowed are valid
    private void validateGoals(int goalsScored, int goalsAllowed) {
        if (goalsScored < 0 || goalsAllowed < 0) {
            throw new IllegalArgumentException("Goals cannot be negative");
        }
    }

    // Calculates goal difference
    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    // Getters
    public Team getTeam() {
        return team;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getPoints() {
        return points;
    }

    // Display standing/stats
    @Override
    public String toString() {
        return String.format(
            "%-21s %3d %3d %3d %3d %4d %4d %4d %4d", 
            team.getName(),
            matchesPlayed,
            wins,
            draws,
            losses,
            goalsFor,
            goalsAgainst,
            getGoalDifference(),
            points
        );
    }
}
