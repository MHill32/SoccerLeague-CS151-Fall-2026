/*
 * Author: Mason Hill 
 */
package main.java.soccerleague.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.java.soccerleague.exceptions.InvalidMatchOperationException;

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
        if (matchId == null || matchId.isBlank()) {
            throw new IllegalArgumentException("Match ID cannot be blank");
        }

        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Both teams must be provided");
        }

        if (homeTeam == awayTeam) {
            throw new IllegalArgumentException("A team connot play against itself");
        }

        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;

        this.homeScore = 0;
        this.awayScore = 0;

        this.started = false;
        this.completed = false;

        this.events = new ArrayList<>();
    }

    //Start scheduled match
    public void startMatch() {
        if (started) {
            throw new IllegalStateException("Match has already started");
        }

        if (completed) {
            throw new IllegalStateException("Match has already ended");
        }
        
        started = true;
    }

    //Add Event during match
    public void addEvent(MatchEvent event) throws InvalidMatchOperationException {
        if (!started || completed) {
            throw new IllegalStateException("Events can only be added to started matches");
        }

        if (event == null) {
            throw new IllegalArgumentException("Events cannot be null");
        }

        if (!event.isValidMinute()) {
            throw new IllegalArgumentException("Invalid match event minute");
        }

        if (event.getTeam() != homeTeam && event.getTeam() != awayTeam) {
            throw new IllegalArgumentException("Event team is not praticipating in the match");
        }

        if (events.contains(event)) {
            throw new IllegalArgumentException("Event already exists in the match");
        }

        event.applyEvent(this);

        events.add(event);
    }

    //Update the score for teams in match
    public void recordGoal(Team scoringTeam) throws InvalidMatchOperationException {
        if (!started || completed) {
            throw new InvalidMatchOperationException("Goals can only be recorded during active matches");
        }

        if (scoringTeam == homeTeam) {
            homeScore++;
        } else if (scoringTeam == awayTeam) {
            awayScore++;
        } else {
            throw new IllegalArgumentException("Scoring team is not part of the match");
        }
    }

    //End an active match 
    public void endMatch() {
        if (!started) {
            throw new IllegalStateException("Match has not started");
        }

        if (completed) {
            throw new IllegalStateException("Match has already ended");
        }

        completed = true;
    }

    //Simulates a 90 minute match
    public void simulateMatch() throws InvalidMatchOperationException {
        simulateMatch(new Random());
    }

    //Chances of a goal or card during match
    public void simulateMatch(Random random) throws InvalidMatchOperationException {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }

        if (started || completed) {
            throw new InvalidMatchOperationException("Match has already started or ended");
        }

        if (homeTeam.getPlayers().isEmpty() || awayTeam.getPlayers().isEmpty()) {
            throw new InvalidMatchOperationException("Both teams must have at least one player");
        }

        startMatch();
        for (int minute = 1; minute <= 90; minute++) {
            Team[] teams = {homeTeam, awayTeam};

            for (Team team : teams) {
                Player player = randomAvailablePlayer(team, random);

                if (player == null) {
                    continue;
                }
                //1.35 expected goals per team per match
                if (random.nextDouble() < 0.015) {
                    addEvent(new GoalEvent(minute, player, team, "REGULAR"));
                }
                //0.02 expected card per team
                if (random.nextDouble() < 0.02) {
                    Player cardPlayer = randomAvailablePlayer(team, random);

                    if (cardPlayer != null) {
                        String cardType = random.nextDouble() < 0.1 ? "RED" : "YELLOW";
                        addEvent(new CardEvent(minute, cardPlayer, team, cardType, "Foul"));
                    }
                }
            }
        }
        endMatch();
    }

    //Helper that prevents players that recieved a red card from playing
    private Player randomAvailablePlayer(Team team, Random random) {
        ArrayList<Player> available = new ArrayList<>();

        for (Player player : team.getPlayers()) {
            if (!MatchEvent.isSentOff(this, player)) {
                available.add(player);
            }
        }
        if (available.isEmpty()) {
            return null;
        }
        return available.get(random.nextInt(available.size()));
    }


    //Determine winner
    public Team determineWinner() {
        if (!completed) {
            throw new IllegalStateException("Match has not ended");
        }
        
        if (homeScore > awayScore) {
            return homeTeam;
        }

        if (awayScore > homeScore) {
            return awayTeam;     
        }

        return null;                //null means a draw
    }

    //Getters
    public String getMatchId() {
        return matchId;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<MatchEvent> getEvents() {
        return List.copyOf(events);
    }

    @Override
    public String toString() {
        return "Match ID: " + matchId
                + ", Home Team: " + homeTeam
                + ", Away Team: " + awayTeam
                + ", Score: " + homeScore
                + " - " + awayScore
                + ", Status: " 
                + (completed ? "Completed" 
                        : started ? "In Progress" : "Scheduled");
    }
}
