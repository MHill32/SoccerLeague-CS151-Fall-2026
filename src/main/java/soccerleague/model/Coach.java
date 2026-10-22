package main.java.soccerleague.model;
import java.util.ArrayList;
import java.util.HashSet;

public class Coach {
    private static final int MAX_INSTANCES = 100;
    private static int instanceCount = 0;
    private static final HashSet<String> registeredCoachIDs = new HashSet<>();

    private String coachID;
    private String name;
    private String formation;
    private ArrayList<Player> startingLineup;

    public Coach(String coachID, String name, String formation) {
        if (coachID == null || coachID.isBlank()) {
            throw new IllegalArgumentException("Coach ID cannot be empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Coach name cannot be empty");
        }

        validateFormation(formation);

        String cleanedCoachID = coachID.trim();

    
        if (registeredCoachIDs.contains(cleanedCoachID)){
            throw new IllegalArgumentException("Coach ID already exists: " + cleanedCoachID);
        }

        if(instanceCount >= MAX_INSTANCES){
            throw new IllegalStateException("Cannot create more than 100 coaches");
        }

        this.coachID = cleanedCoachID;
        this.name = name.trim();
        this.formation = formation.trim();
        this.startingLineup = new ArrayList<>();
        registeredCoachIDs.add(cleanedCoachID);

        instanceCount++;
    }

    // Receives the 11 players who are starting
    public void selectStartingLineup(ArrayList<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        if (players.size() != 11) {
            throw new IllegalArgumentException("Starting lineup must contain exactly 11 players");
        }

        ArrayList<Player> newLineup = new ArrayList<>();
        int goalkeeperCount = 0;

        for (Player player : players) {
            if (player == null) {
                throw new IllegalArgumentException("Starting lineup cannot contain null players");
            }

            for (Player existingPlayer : newLineup) {
                if (existingPlayer.getPlayerID().equals(player.getPlayerID())) {
                    throw new IllegalArgumentException("Starting lineup cannot contain duplicate players");
                }
            }

            if (player.getPosition().equalsIgnoreCase("GK")) {
                goalkeeperCount++;
            }

            newLineup.add(player);
        }

        if (goalkeeperCount != 1) {
            throw new IllegalArgumentException("Starting lineup must contain exactly one goalkeeper");
        }

        // Only swap the lineup once everything checks out
        startingLineup = newLineup;
    }    

    // Checks the number of outfield players in the formation
    private static void validateFormation(String formation) {
        if (formation == null || formation.isBlank()) {
            throw new IllegalArgumentException("Formation cannot be empty");
        }

        String cleanedFormation = formation.trim();

        if (!cleanedFormation.matches("[1-9](-[1-9]){2,3}")) {
            throw new IllegalArgumentException("Formation must use three or four positive numbers, "
                    + "such as 4-3-3 or 4-2-3-1"
            );
        }

        String[] positions = cleanedFormation.split("-");
        int totalPlayers = 0;

        for (String position : positions) {
            totalPlayers += Integer.parseInt(position);
        }

        if (totalPlayers != 10) {
            throw new IllegalArgumentException("Formation must total 10 outfield players"
            );
        }
    }

    // Changes a selected starter before kickoff
    public void substitutePlayer(Player playerOut, Player playerIn) {
        if (playerOut == null || playerIn == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        if (startingLineup.isEmpty()) {
            throw new IllegalStateException("Select a starting lineup first");
        }

        ArrayList<Player> updatedLineup = new ArrayList<>(startingLineup);

        int outgoingIndex = -1;

        for (int i = 0; i < updatedLineup.size(); i++) {
            if (updatedLineup.get(i).getPlayerID().equals(playerOut.getPlayerID())) {
                outgoingIndex = i;
                break;
            }
        }

        if (outgoingIndex == -1) {
            throw new IllegalArgumentException("Outgoing player is not in the starting lineup");
        }
        if (playerOut.getPlayerID().equals(playerIn.getPlayerID())) {
            throw new IllegalArgumentException("Choose a different incoming player");
        }

        updatedLineup.set(outgoingIndex, playerIn);

        // Reuse the lineup checks for duplicates and goalkeeper count
        selectStartingLineup(updatedLineup);
    }

    public void changeFormation(String newFormation){
        validateFormation(newFormation);
        formation = newFormation.trim();
    }

    public String getName(){
        return name;
    }

    public String getFormation(){
        return formation;
    }
    
    public String getCoachID() {
        return coachID;
    }

    // Returns a copy so callers don't change the lineup
    public ArrayList<Player> getStartingLineup(){
        return new ArrayList<>(startingLineup);
    }

    @Override
    public String toString() {
        return "Coach{" + "coachID=" + coachID + '\''
        + ", name='" + name + '\''
        + ", formation='" + formation + '\''
        + ", startingLineupSize=" + startingLineup.size() + '}';
    }
}
