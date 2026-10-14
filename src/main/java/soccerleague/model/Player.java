package main.java.soccerleague.model;

import main.java.soccerleague.interfaces.StatTrack;

public class Player implements StatTrack {

    private String playerID;
    private String name;
    private String position;
    private int jerseyNumber;
    private int age;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;

    public Player(String playerID, String name, String position,
                  int jerseyNumber, int age) {

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

        this.playerID = playerID;
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.age = age;
        this.goals = 0;
        this.assists = 0;
        this.yellowCards = 0;
        this.redCards = 0;
    }

    public void scoreGoal(){
        if (redCards > 0){
            throw new IllegalStateException("Player cannot score while suspended");
        }
        goals++;
    }

    public void recordAssist(){
         if (redCards > 0){
            throw new IllegalStateException("Player cannot assist while suspended");
        }
        assists++;
    }

    public void receieveCard (String cardType) {
        if (cardType.equalsIgnoreCase("yellow")){
            yellowCards++;
        } else if (cardType.equalsIgnoreCase("red")) {
            redCards++;
        } else {
            throw new IllegalArgumentException("Invalid card type");
        }
    }
}