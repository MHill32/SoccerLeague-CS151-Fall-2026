package main.java.soccerleague.model;

import main.java.soccerleague.interfaces.StatTrack;
import java.util.HashSet;

public class Player implements StatTrack {
    private static final HashSet<String> registeredPlayerIDs = new HashSet<>();

    private String playerID;
    private String name;
    private String position;
    private int jerseyNumber;
    private int age;

    // Stats in matches
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;

    // Attributes loaded from the CSV to run simulation
    private int overall;
    private int pace;
    private int shooting;
    private int passing;
    private int dribbling;
    private int defending;
    private int physicality;
    private int simAttack;
    private int simDiscipline;

    public Player(String playerID, String name, String position,
                  int jerseyNumber, int age, int overall, int pace, int shooting,
                   int passing, int dribbling, int defending, int physicality,
                    int simAttack, int simDiscipline) {

        if (playerID == null || playerID.isBlank()) {
            throw new IllegalArgumentException("Player ID cannot be empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }

        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Position cannot be empty");
        }

        if (jerseyNumber < 1 || jerseyNumber > 99) {
            throw new IllegalArgumentException("Jersey number must be between 1 and 99");
        }

        if (age < 16 || age > 50) {
            throw new IllegalArgumentException("Age must be between 16 and 50");
        }

        validateRating(overall, "Overall");
        validateRating(pace, "Pace");
        validateRating(shooting, "Shooting");
        validateRating(passing, "Passing");
        validateRating(dribbling, "Dribbling");
        validateRating(defending, "Defending");
        validateRating(physicality, "Physicality");
        validateRating(simAttack, "Simulation attack");
        validateRating(simDiscipline, "Simulation discipline");

        String cleanedPlayerID = playerID.trim();
        
        // Prevents 2 players having the same ID
        if(registeredPlayerIDs.contains(cleanedPlayerID)){
            throw new IllegalArgumentException("Player ID already exists: " + cleanedPlayerID);
        }

        // Prevents creating more than 100 players across the program.
        if (registeredPlayerIDs.size() >= 100) {
            throw new IllegalStateException("Cannot create more than 100 players");
        }

    
        this.playerID = cleanedPlayerID;
        this.name = name.trim();
        this.position = position.trim();
        this.jerseyNumber = jerseyNumber;
        this.age = age;
        this.goals = 0;
        this.assists = 0;
        this.yellowCards = 0;
        this.redCards = 0;

        this.overall = overall;
        this.pace = pace;
        this.shooting = shooting;
        this.passing = passing;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physicality = physicality;
        this.simAttack = simAttack;
        this.simDiscipline = simDiscipline;

        // Remembers the ID after the player is constructed
        registeredPlayerIDs.add(cleanedPlayerID); 
    }

    private static void validateRating (int rating, String attribute){
        if (rating < 0 || rating > 99){
            throw new IllegalArgumentException(attribute + " must be between 0 and 99");
        }
    }

    public void scoreGoal(){
        goals++;
    }

    public void recordAssist(){
        assists++;
    }

    public void receiveCard (String cardType) {
        if (cardType == null || cardType.isBlank()){
            throw new IllegalArgumentException("Card type cannot be empty");
        }

        cardType = cardType.trim();

        if (cardType.equalsIgnoreCase("yellow")){
            yellowCards++;
        } else if (cardType.equalsIgnoreCase("red")) {
            redCards++;
        } else {
            throw new IllegalArgumentException("Invalid card type");
        }
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public int getAge() {
        return age;
    }

    public int getOverall() {
        return overall;
    }

    public int getPace() {
        return pace;
    }

    public int getShooting() {
        return shooting;
    }

    public int getPassing() {
        return passing;
    }

    public int getDribbling() {
        return dribbling;
    }

    public int getDefending() {
        return defending;
    }

    public int getPhysicality() {
        return physicality;
    }

    public int getSimAttack() {
        return simAttack;
    }

    public int getSimDiscipline() {
        return simDiscipline;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public int getRedCards() {
        return redCards;
    } 

    @Override
    public String toString() {
        return "Player{"
                + "playerID='" + playerID + '\''
                + ", name='" + name + '\''
                + ", position='" + position + '\''
                + ", jerseyNumber=" + jerseyNumber
                + ", age=" + age
                + ", overall=" + overall
                + ", pace=" + pace
                + ", shooting=" + shooting
                + ", passing=" + passing
                + ", dribbling=" + dribbling
                + ", defending=" + defending
                + ", physicality=" + physicality
                + ", simAttack=" + simAttack
                + ", simDiscipline=" + simDiscipline
                + ", goals=" + goals
                + ", assists=" + assists
                + ", yellowCards=" + yellowCards
                + ", redCards=" + redCards
                + '}';
    }
}