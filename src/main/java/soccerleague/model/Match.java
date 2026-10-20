/*
 * Author: Mason Hill 
 */
package main.java.soccerleague.model;

import java.util.ArrayList;

public class Match {
    private String matchId;

    private Team homeTeam;
    private Team awayTeam;

    private int homeScore;
    private int awayScore;

    private boolean started;
    private boolean completed;

    private ArrayList<MatchEvent> events;

    //Match Constructor
    public Match(String matchId, Team homeTeam, Team awayTeam) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;

        this.events = new ArrayList<>();
    }

    //Methods
    public void startMatch() {
        started = true;
    }

    public void addEvent(MatchEvent event) {
        if (!started || completed) {
            throw new IllegalStateException("Events can only be added to started matches");
        }

        if (event == null) {
            throw new IllegalArgumentException("Events cannot be null");
        }
        
        events.add(event);
    }

    public void endMatch() {
        completed = true;
    }

    public Team determineWinner() {
        if (homeScore > awayScore) {
            return homeTeam;
        }
        if (awayScore > homeScore) {
            return awayTeam;     
        }
        return null;
    }
}
