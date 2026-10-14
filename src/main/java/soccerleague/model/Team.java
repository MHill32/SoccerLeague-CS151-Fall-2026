package main.java.soccerleague.model;

import main.java.soccerleague.interfaces.StatTrack;

import java.util.ArrayList;
public class Team implements StatTrack {
    private String teamID;
    private String name;
    private String city;
    private ArrayList<Player> players;

    public Team(String teamID, String name, String city) {
        if (teamID == null || teamID.isBlank()){
            throw new IllegalArgumentException("Team ID cannot be empty");
        }
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        if (city == null || city.isBlank()){
            throw new IllegalArgumentException("City cannot be empty");
        }

        this.teamID = teamID;
        this.name = name;
        this.city = city;
        this.players = new ArrayList<>();
    }
}
