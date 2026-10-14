package main.java.soccerleague.model;

public abstract class MatchEvent {

    public static final int MAX_MINUTE = 120;

    private int minute;
    private Player player;
    private Team team;
    private boolean reversed;

    public MatchEvent(int minute, Player player, Team team) {
        this.minute = minute;
        this.player = player;
        this.team = team;
        this.reversed = false;
    }

    public abstract void applyEvent(Match match);

    public abstract String getEventType();

    public int getMinute() {
        return minute;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isReversed() {
        return reversed;
    }

    protected void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public boolean isValidMinute() {
        return minute >= 0 && minute <= MAX_MINUTE;
    }

    public boolean occurredBefore(MatchEvent other) {
        return false;
    }

    public void changeMinute(int newMinute) {
    }

    @Override
    public String toString() {
        return minute + "' " + getEventType() + " - " + player;
    }
}
