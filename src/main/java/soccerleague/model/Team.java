package main.java.soccerleague.model;

import main.java.soccerleague.interfaces.StatTrack;
import java.util.ArrayList;

public class Team implements StatTrack {
    private String teamID;
    private String name;
    private String city;
    private ArrayList<Player> players;
    private Coach coach;

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
        this.coach = null;
    }
    // Adds a player to the team
    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        for (Player existingPlayer : players) {
            if (existingPlayer == player) {
                throw new IllegalArgumentException("Player already on team");
            }
        }
        players.add(player);
    }

    // Remove a player from the team
    public void removePlayer(Player player){
        if (player == null){
            throw new IllegalArgumentException("Player cannot be null");
        }

        if(!players.remove(player)){
            throw new IllegalArgumentException("Player not found on the team");
        }
    }

    // Transfers a player to a new team
    public void transferPlayer (Player player, Team newTeam){
        
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (newTeam == null) {
            throw new IllegalArgumentException("New team cannot be null");
        }
        if (newTeam == this) {
            throw new IllegalArgumentException("Cannot transfer a player to the same team");
        }
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Player not found on team");
        }

        newTeam.addPlayer(player);
        players.remove(player);
    }
    
    // Adds a coach to the team
    public void addCoach(Coach coach){
        
        if (coach == null) {
            throw new IllegalArgumentException("Coach cannot be null");
        }
        if (this.coach != null) {
            throw new IllegalStateException("Team already has a coach");
        }

        this.coach = coach;

    }
    
    // Removes the coach from the team
    public void removeCoach(){
        
        if (coach == null) {
            throw new IllegalStateException("Team does not have a coach");
        }

        coach = null;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    // Returns a copy so callers cannot directly change the team's roster
    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    public Coach getCoach(){
        return coach;
    }

    public int getRosterSize(){
        return players.size();
    }

    // Average OVR of team (based off the team's roster)
    public double calculateOverallRating() {
        if (players.isEmpty()){
            return 0.0;
        }

        double total = 0.0;

        for (Player player : players){
            total += player.getOverall();
        }

        return total / players.size();
    }

    // Average simulated attack of all players in the team's roster
    public double calculateAttackRating() {
        if(players.isEmpty()){
            return 0.0;
        }
        double total = 0.0;

        for (Player player : players){
            total += player.getSimAttack();
        }

        return total / players.size();
    }

    // Average defense of all players on team's roster
        public double calculateDefRating() {
        if(players.isEmpty()){
            return 0.0;
        }
        double total = 0.0;

        for (Player player : players){
            total += player.getDefending();
        }

        return total / players.size();
    }

    @Override 
    public String toString() {
        return "Team{"
        + "teamID='" + teamID + '\''
        + ", name='" + name + '\''
        + ", city='" + city + '\''
        + ", rosterSize=" + players.size()
        + ", hasCoach=" + (coach != null)
        + '}';
    }

}
