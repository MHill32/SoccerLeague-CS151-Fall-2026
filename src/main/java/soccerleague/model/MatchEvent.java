package main.java.soccerleague.model;

/**
 * Abstract parent for anything that happens during a match (goals, cards).
 * Each subclass decides how it changes the match in applyEvent().
 * Pratham Patel
 */


import java.util.Objects;
import main.java.soccerleague.exceptions.InvalidMatchOperationException;

public abstract class MatchEvent {

    public static final int MAX_MINUTE = 120;

    private int minute;
    private Player player;
    private Team team;
    private boolean applied;

    public MatchEvent(int minute, Player player, Team team) {
        if (player == null || team == null) {
            throw new IllegalArgumentException("An event needs a player and a team");
        }
        if (minute < 0 || minute > MAX_MINUTE) {
            throw new IllegalArgumentException("Minute must be between 0 and " + MAX_MINUTE);
        }
        this.minute = minute;
        this.player = player;
        this.team = team;
        this.applied = false;
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

    public boolean isApplied() {
        return applied;
    }

    protected void setApplied(boolean applied) {
        this.applied = applied;
    }

    public boolean isValidMinute() {
        return minute >= 0 && minute <= MAX_MINUTE;
    }

    public boolean isFirstHalf() {
        return minute <= 45;
    }

    public boolean occurredBefore(MatchEvent other) {
        if (other == null) {
            throw new IllegalArgumentException("Other event cannot be null");
        }
        return this.minute < other.minute;
    }

    public void changeMinute(int newMinute) {
        if (applied) {
            throw new InvalidMatchOperationException("Cannot change the minute after the event was added to a match");
        }
        if (newMinute < 0 || newMinute > MAX_MINUTE) {
            throw new InvalidMatchOperationException("Minute must be between 0 and " + MAX_MINUTE);
        }
        this.minute = newMinute;
    }

    protected void validateParticipant(Match match) {
        if (match == null) {
            throw new InvalidMatchOperationException("Match cannot be null");
        }
        if (applied) {
            throw new InvalidMatchOperationException("This event was already added to a match");
        }
        if (!match.isStarted() || match.isCompleted()) {
            throw new InvalidMatchOperationException("Events can only be added while the match is in progress");
        }
        if (team != match.getHomeTeam() && team != match.getAwayTeam()) {
            throw new InvalidMatchOperationException(team.getName() + " is not playing in this match");
        }
        if (!team.getPlayers().contains(player)) {
            throw new InvalidMatchOperationException(player.getName() + " does not play for " + team.getName());
        }
    }

    public static boolean isSentOff(Match match, Player player) {
        for (MatchEvent event : match.getEvents()) {
            if (event instanceof CardEvent && event.getPlayer() == player
                    && ((CardEvent) event).isSendingOff()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchEvent other = (MatchEvent) o;
        return minute == other.minute && player == other.player && team == other.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), minute, player, team);
    }

    @Override
    public String toString() {
        return minute + "' " + getEventType() + " - " + player.getName() + " (" + team.getName() + ")";
    }
}