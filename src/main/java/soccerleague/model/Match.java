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

    }

    public void addEvent(MatchEvent event) {

    }

    public void endMatch() {
    
    }

    public Team determineWinner() {
        return null;
    }
}
