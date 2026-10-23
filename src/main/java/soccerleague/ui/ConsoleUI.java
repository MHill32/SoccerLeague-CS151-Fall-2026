package main.java.soccerleague.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import main.java.soccerleague.data.PlayerDataLoader;
import main.java.soccerleague.exceptions.DuplicateObjectException;
import main.java.soccerleague.exceptions.MaximumInstancesException;
import main.java.soccerleague.model.League;
import main.java.soccerleague.model.Team;
import main.java.soccerleague.model.Standing;

public class ConsoleUI {
    
    public static void main(String[] args) {
        League league = new League("L001", "Soccer League");

        // Load the starting teams and players only once
        try{
            for (Team team : PlayerDataLoader.loadTeams (Path.of("data", "players.csv"))){
                league.promoteTeam(team);
            }
            System.out.println("Loaded 8 teams and 96 players.");

        } catch (IOException | DuplicateObjectException | MaximumInstancesException exception){
            System.out.println("Could not load player data: " + exception.getMessage());
            return;
        }

        Scanner scanner = new Scanner (System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== SOCCER LEAGUE MANAGER =====");
            System.out.println("1. View teams");
            System.out.println("2. Players");
            System.out.println("3. Coaches and lineups");
            System.out.println("4. Matches");
            System.out.println("5. League standings");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");   

            if (!scanner.hasNextLine()) {
                break;
            }

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayTeams(league);
                    break;
                case "2":
                    System.out.println("This menu is not implemented yet.");
                    break;
                case "3":
                    System.out.println("This menu is not implemented yet.");
                    break;
                case "4":
                    System.out.println("This menu is not implemented yet.");
                    break;
                case "5":
                    displayStandings(league);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    if (choice.equalsIgnoreCase("EXIT")) {
                        running = false;
                    } else {
                        System.out.println("Invalid option. Choose bewtween 0 and 5.");
                    }
                    break;
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    private static void displayTeams(League league) {
    System.out.println();
    System.out.println("===== TEAMS =====");
    
    if (league.getTeams().isEmpty()) {
        System.out.println("No teams in the league.");
        return;
    }

    for (Team team : league.getTeams()) {
        System.out.printf(
            "%s | %s | Players: %d%n",
            team.getName(),
            team.getCity(),
            team.getRosterSize()
            );
        }
    }
    
    private static void displayStandings(League league) {
        league.updateStandings();

        System.out.println();
        System.out.println("===== LEAGUE STANDINGS =====");

        System.out.printf(
                "%-24s %3s %3s %3s %3s %4s %4s %4s %4s%n",
                "Team", "P", "W", "D", "L", "GF", "GA", "GD", "PTS"
        );

        for (Standing standing : league.getRankedStandings()) {
            System.out.printf(
                    "%-24s %3d %3d %3d %3d %4d %4d %4d %4d%n",
                    standing.getTeam().getName(),
                    standing.getMatchesPlayed(),
                    standing.getWins(),
                    standing.getDraws(),
                    standing.getLosses(),
                    standing.getGoalsFor(),
                    standing.getGoalsAgainst(),
                    standing.getGoalDifference(),
                    standing.getPoints()
            );
        }

        System.out.println();
        System.out.println("P: Played | W: Wins | D: Draws | L: Losses");
        System.out.println("GF: Goals for | GA: Goals against");
        System.out.println("GD: Goal difference | PTS: Points");
    }
}
