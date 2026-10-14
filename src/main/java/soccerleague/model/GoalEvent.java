package main.java.soccerleague.model;

public class GoalEvent extends MatchEvent {

    private Player assistPlayer;
    private String goalType;

    public GoalEvent(int minute, Player scorer, Team team, String goalType) {
        super(minute, scorer, team);
        this.goalType = goalType;
        this.assistPlayer = null;
    }

    public GoalEvent(int minute, Player scorer, Team team, String goalType, Player assistPlayer) {
        super(minute, scorer, team);
        this.goalType = goalType;
        this.assistPlayer = assistPlayer;
    }

    public Player getAssistPlayer() {
        return assistPlayer;
    }

    public String getGoalType() {
        return goalType;
    }

    public boolean validateScorer(Match match) {
        return true;
    }

    public boolean validateAssist() {
        return true;
    }

    public void reverseGoal(Match match) {
    }

    @Override
    public void applyEvent(Match match) {
    }

    @Override
    public String getEventType() {
        return "GOAL";
    }

    @Override
    public String toString() {
        return super.toString() + " (" + goalType + ")";
    }
}
