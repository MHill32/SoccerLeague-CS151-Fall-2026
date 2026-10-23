package main.java.soccerleague.model;

import main.java.soccerleague.interfaces.StatTrack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Team implements StatTrack {
    private static final HashSet<String> registeredTeamIDs = new HashSet<>();
    private String teamID;
    private String name;
    private String city;
    private ArrayList<Player> players;
    private Coach coach;
    private static final HashMap<String, Team> playerTeams = new HashMap<>();
    private static final HashMap<String, Team> coachTeams = new HashMap<>();

    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
    
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

        String cleanedTeamID = teamID.trim();

        // Prevents 2 teams having the same ID
        if (registeredTeamIDs.contains(cleanedTeamID)){
            throw new IllegalArgumentException( "Team ID already exists: " + cleanedTeamID);
        }
        if (registeredTeamIDs.size() >= 100) {
            throw new IllegalStateException("Cannot create more than 100 teams");
        }

        this.teamID = cleanedTeamID;
        this.name = name.trim();
        this.city = city.trim();
        this.players = new ArrayList<>();
        this.coach = null;

        registeredTeamIDs.add(cleanedTeamID);
    }
    // Adds a player to the team
    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        String playerID = player.getPlayerID();
        
        if (playerTeams.containsKey(playerID)) {
            throw new IllegalArgumentException("Player already belongs to a team. Use transferPlayer instead");
        }
        players.add(player);
        playerTeams.put(playerID, this);
    }

    // Remove a player from the team
    public void removePlayer(Player player){
        if (player == null){
            throw new IllegalArgumentException("Player cannot be null");
        }

        if(!players.remove(player)){
            throw new IllegalArgumentException("Player not found on the team");
        }

        playerTeams.remove(player.getPlayerID());
        if (coach != null) {
            coach.clearLineupIfStarting(player);
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

        players.remove(player);
        newTeam.players.add(player);
        playerTeams.put(player.getPlayerID(), newTeam);
        if (coach != null) {
            coach.clearLineupIfStarting(player);
        }

    }
    
    // Adds a coach to the team
    public void addCoach(Coach coach){
        
        if (coach == null) {
            throw new IllegalArgumentException("Coach cannot be null");
        }
        if (this.coach != null) {
            throw new IllegalStateException("Team already has a coach");
        }

        if (coachTeams.containsKey(coach.getCoachID())) {
            throw new IllegalStateException("Coach already belongs to a team");
        }

        this.coach = coach;
        coachTeams.put(coach.getCoachID(), this);
    }
    
    // Removes the coach from the team
    public void removeCoach(){
        
        if (coach == null) {
            throw new IllegalStateException("Team does not have a coach");
        }

        coach.clearStartingLineup();
        coachTeams.remove(coach.getCoachID());
        coach = null;
    }

    // Lets Coach find its assigned team within this package.
    static Team getTeamForCoach(Coach coach) {
        return coachTeams.get(coach.getCoachID());
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
        public double calculateDefenseRating() {
        if(players.isEmpty()){
            return 0.0;
        }
        double total = 0.0;

        for (Player player : players){
            total += player.getDefending();
        }

        return total / players.size();
    }

    // Records a goal scored for this team
    public void recordGoal() {
        goals++;
    }

    // Records an assist made for this team
    public void recordAssist() {
        assists++;
    }

    // Records a card received while playing for this team.
    public void recordCard(String cardType) {
    if (cardType == null || cardType.isBlank()) {
        throw new IllegalArgumentException("Card type cannot be empty");
    }

    String cleanedCardType = cardType.trim();

    if (cleanedCardType.equalsIgnoreCase("yellow")) {
        yellowCards++;
    } else if (cleanedCardType.equalsIgnoreCase("red")) {
        redCards++;
    } else {
        throw new IllegalArgumentException("Invalid card type");
    }
}

    @Override 
    public String toString() {
        return "Team{"
        + "teamID='" + teamID + '\''
        + ", name='" + name + '\''
        + ", city='" + city + '\''
        + ", rosterSize=" + players.size()
        + ", hasCoach=" + (coach != null)
        + '}'
        + ", goals=" + goals
        + ", assists=" + assists
        + ", yellowCards=" + yellowCards
        + ", redCards=" + redCards;
    }

    @Override
    public int getGoals() {
        return goals;
    }

    @Override
    public int getAssists() {
        return assists;
    }

    @Override
    public int getYellowCards() {
        return yellowCards;
    }

    @Override
    public int getRedCards() {
        return redCards;
    }
}
