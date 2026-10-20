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
        this.team = team;
    }

    //Methods
    public void recordWin(int goalsScored, int goalsAllowed) {
        if (goalsScored < 0 || goalsAllowed < 0) {
            throw new IllegalArgumentException("Goals cannot be negative");
        }
        
        if (goalsScored <= goalsAllowed) {
            throw new IllegalArgumentException("A win requires more goals scored than allowed");
        }

        matchesPlayed++;
        wins++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
        points += 3;
    }

    public void recordDraw(int goalsScored, int goalsAllowed) {
        matchesPlayed++;
        draws++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
        points += 1;
    }

    public void recordLoss(int goalsScored, int goalsAllowed) {
        matchesPlayed++;
        losses++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }
}
