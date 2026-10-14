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

    }

    public void recordDraw(int goalsScored, int goalsAllowed) {

    }

    public void recordLoss(int goalsScored, int goalsAllowed) {

    }

    public int getGoalDifference() {
        return 0; //FIX
    }
}
