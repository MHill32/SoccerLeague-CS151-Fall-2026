/*
 * Author: Mason Hill
 */
package main.java.soccerleague.model;

import java.util.ArrayList;

public class League {
    private String leagueId;
    private String leagueName;

    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    private ArrayList<Standing> standings;

    //League Constructor
    public League(String leagueID, String leagueName) {
        this.leagueId = leagueID;
        this.leagueName = leagueName;
        
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.standings = new ArrayList<>();
    }
    
    //Methods
    public void addTeam(Team team) {

    }

    public void relegateTeam(Team team) {

    }

    public void schduleMatch(Match match) {

    }

    public void updateStandings() {

    }
}
