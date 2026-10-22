package main.java.soccerleague.model;
import java.util.ArrayList;

public class Coach {
    private String name;
    private String formation;
    private ArrayList<Player> startingLineup;

    public Coach(String name, String formation) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Coach name cannot be empty");
        }

        validateFormation(formation);

        this.name = name.trim();
        this.formation = formation.trim();
        this.startingLineup = new ArrayList<>();
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
}
