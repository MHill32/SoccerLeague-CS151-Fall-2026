package main.java.soccerleague.model;

/**
 * A goal scored during a match.
 * Pratham Patel
 */


import main.java.soccerleague.exceptions.InvalidMatchOperationException;

public class GoalEvent extends MatchEvent {

    private static final String[] GOAL_TYPES = {"REGULAR", "HEADER", "PENALTY", "FREE_KICK", "OWN_GOAL"};

    private Player assistPlayer;
    private String goalType;

    public GoalEvent(int minute, Player scorer, Team team, String goalType) {
        this(minute, scorer, team, goalType, null);
    }

    public GoalEvent(int minute, Player scorer, Team team, String goalType, Player assistPlayer) {
        super(minute, scorer, team);
        if (!isValidGoalType(goalType)) {
            throw new IllegalArgumentException("Goal type must be REGULAR, HEADER, PENALTY, FREE_KICK, or OWN_GOAL");
        }
        this.goalType = goalType.trim().toUpperCase();
        this.assistPlayer = assistPlayer;
    }

    public Player getAssistPlayer() {
        return assistPlayer;
    }

    public String getGoalType() {
        return goalType;
    }

    public boolean isOwnGoal() {
        return goalType.equals("OWN_GOAL");
    }

    private static boolean isValidGoalType(String type) {
        if (type == null) {
            return false;
        }
        for (String valid : GOAL_TYPES) {
            if (valid.equalsIgnoreCase(type.trim())) {
                return true;
            }
        }
        return false;
    }

    public void validateScorer(Match match) {
        validateParticipant(match);
        if (isSentOff(match, getPlayer())) {
            throw new InvalidMatchOperationException(getPlayer().getName() + " was sent off and cannot score");
        }
    }

    public void validateAssist(Match match) {
        if (assistPlayer == null) {
            return;
        }
        if (isOwnGoal() || goalType.equals("PENALTY")) {
            throw new InvalidMatchOperationException("A " + goalType + " cannot have an assist");
        }
        if (assistPlayer == getPlayer()) {
            throw new InvalidMatchOperationException("A player cannot assist their own goal");
        }
        if (!getTeam().getPlayers().contains(assistPlayer)) {
            throw new InvalidMatchOperationException(assistPlayer.getName() + " is not a teammate of " + getPlayer().getName());
        }
        if (isSentOff(match, assistPlayer)) {
            throw new InvalidMatchOperationException(assistPlayer.getName() + " was sent off and cannot assist");
        }
    }

    @Override
    public void applyEvent(Match match) {
    }

    @Override
    public String getEventType() {
        if (goalType.equals("REGULAR")) {
            return "GOAL";
        }
        return "GOAL (" + goalType + ")";
    }

    @Override
    public String toString() {
        if (assistPlayer == null) {
            return super.toString();
        }
        return super.toString() + " assist: " + assistPlayer.getName();
    }
}