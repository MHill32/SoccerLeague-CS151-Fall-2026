package main.java.soccerleague.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.List;

import main.java.soccerleague.data.PlayerDataLoader;
import main.java.soccerleague.exceptions.DuplicateObjectException;
import main.java.soccerleague.exceptions.MaximumInstancesException;
import main.java.soccerleague.model.League;
import main.java.soccerleague.model.Team;
import main.java.soccerleague.model.Standing;
import main.java.soccerleague.model.Match;
import main.java.soccerleague.model.MatchEvent;
import main.java.soccerleague.exceptions.InvalidMatchOperationException;

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
                    matchesMenu(league, scanner);
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
        System.out.println(team); // uses toString()
        }
    }
    
    private static void displayStandings(League league) {
        league.updateStandings();

        System.out.println();
        System.out.println("===== LEAGUE STANDINGS =====");

        if (league.getRankedStandings().isEmpty()){
            System.out.println("No standings available.");
            return;
        }
        
        System.out.printf(
            "%-21s %3s %3s %3s %3s %4s %4s %4s %4s%n",
            "Team", "P", "W", "D", "L", "GF", "GA", "GD", "PTS"
        );

        for (Standing standing : league.getRankedStandings()) {
            System.out.println(standing); // uses toString()
        }

        System.out.println();
        System.out.println("P: Played | W: Wins | D: Draws | L: Losses");
        System.out.println("GF: Goals for | GA: Goals against");
        System.out.println("GD: Goal difference | PTS: Points");
    }

    private static void matchesMenu(League league, Scanner scanner) {
    while (true) {
        System.out.println();
        System.out.println("===== MATCHES =====");
        System.out.println("1. View matches");
        System.out.println("2. Schedule a match");
        System.out.println("3. Simulate a scheduled match");
        System.out.println("4. View match events");
        System.out.println("0. Back");

        String choice = readInput(scanner, "Choose an option: ");

        if (choice == null) {
            return;
        }

        try {
            switch (choice) {
                case "1":
                    if (league.getMatches().isEmpty()) {
                        System.out.println("No matches scheduled.");
                    }

                    for (Match match : league.getMatches()) {
                        System.out.println(match);
                    }
                    break;

                case "2":
                    scheduleMatch(league, scanner);
                    break;

                case "3":
                    simulateSelectedMatch(league, scanner);
                    break;

                case "4":
                    showMatchEvents(league, scanner);
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Choose 0 through 4.");
                    break;
            }
        } catch (InvalidMatchOperationException | IllegalArgumentException | IllegalStateException exception) {
            System.out.println("Could not complete action: " + exception.getMessage());
        }
    }
}

private static void scheduleMatch(League league, Scanner scanner) {
    List<Team> teams = league.getTeams();

    if (teams.size() < 2) {
        System.out.println("At least two teams are needed.");
        return;
    }

    Team home = selectItem(teams, scanner, "Choose the home team:");
    if (home == null) {
        return;
    }

    Team away = selectItem(teams, scanner, "Choose the away team:");
    if (away == null) {
        return;
    }

    if (home == away) {
        System.out.println("A team cannot play against itself.");
        return;
    }

    String id = readInput(scanner, "Match ID, or 0 to cancel: ");
    if (id == null || id.equals("0")) {
        return;
    }

    Match match = new Match(id, home, away);
    league.scheduleMatch(match);

    System.out.println("Scheduled:");
    System.out.println(match);
}

private static void simulateSelectedMatch(
        League league, Scanner scanner)
        throws InvalidMatchOperationException {

    List<Match> scheduled = new java.util.ArrayList<>();

    for (Match match : league.getMatches()) {
        if (!match.isStarted() && !match.isCompleted()) {
            scheduled.add(match);
        }
    }

    if (scheduled.isEmpty()) {
        System.out.println("No scheduled matches available to simulate.");
        return;
    }

    Match match = selectItem(
            scheduled, scanner, "Choose a match to simulate:"
    );

    if (match == null) {
        return;
    }

    match.simulateMatch();
    league.updateStandings();

    System.out.println("===== FINAL RESULT =====");
    System.out.println(match);

    for (MatchEvent event : match.getEvents()) {
        System.out.println(event);
    }

    System.out.println("League standings updated.");
}

private static void showMatchEvents(League league, Scanner scanner) {
    Match match = selectItem(
            league.getMatches(), scanner, "Choose a match:"
    );

    if (match == null) {
        return;
    }

    System.out.println(match);

    if (match.getEvents().isEmpty()) {
        System.out.println("No events recorded.");
        return;
    }

    for (MatchEvent event : match.getEvents()) {
        System.out.println(event);
    }
}

// Reusable numbered selection for teams and matches.
private static <T> T selectItem(
        List<T> items, Scanner scanner, String prompt) {

    if (items.isEmpty()) {
        System.out.println("No items available.");
        return null;
    }

    while (true) {
        System.out.println();
        System.out.println(prompt);

        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }

        System.out.println("0. Back");

        String input = readInput(scanner, "Choose a number: ");

        if (input == null || input.equals("0")) {
            return null;
        }

        try {
            int number = Integer.parseInt(input);

            if (number >= 1 && number <= items.size()) {
                return items.get(number - 1);
            }
        } catch (NumberFormatException exception) {
            // Retry with the message below.
        }

        System.out.println(
                "Enter a number from 0 through " + items.size() + "."
        );
    }
}

private static String readInput(Scanner scanner, String prompt) {
    System.out.print(prompt);

    if (!scanner.hasNextLine()) {
        return null;
    }

    return scanner.nextLine().trim();
}
}
