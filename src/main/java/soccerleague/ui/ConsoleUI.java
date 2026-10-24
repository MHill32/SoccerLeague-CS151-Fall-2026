package main.java.soccerleague.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import main.java.soccerleague.data.PlayerDataLoader;
import main.java.soccerleague.exceptions.DuplicateObjectException;
import main.java.soccerleague.exceptions.InvalidMatchOperationException;
import main.java.soccerleague.exceptions.MaximumInstancesException;
import main.java.soccerleague.model.League;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;
import main.java.soccerleague.model.Standing;
import main.java.soccerleague.model.Match;
import main.java.soccerleague.model.MatchEvent;
import main.java.soccerleague.model.Coach;

public class ConsoleUI {
    
    public static void main(String[] args) {
        League league = new League("L001", "Soccer League");

        // Load the starting teams and players only once
        try{
            for (Team team : PlayerDataLoader.loadTeams (Path.of("data", "players.csv"), 4)){
                league.promoteTeam(team);
            }
            System.out.println("Loaded 4 teams and their players.");

        } catch (IOException | DuplicateObjectException | MaximumInstancesException exception){
            System.out.println("Could not load player data: " + exception.getMessage());
            return;
        }

        Scanner scanner = new Scanner (System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== SOCCER LEAGUE MANAGER =====");
            System.out.println("1. View Teams");
            System.out.println("2. Manage Teams and Players");
            System.out.println("3. Manage Matches");
            System.out.println("4. View League standings");
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
                    manageTeams(league, scanner);
                    break;
                case "3":
                    matchesMenu(league, scanner);
                    break;
                case "4":
                    displayStandings(league);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    if (choice.equalsIgnoreCase("EXIT")) {
                        running = false;
                    } else {
                        System.out.println("Invalid option. Choose bewtween 0 and 4.");
                    }
                    break;
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    //1. Teams menu
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

    //2. Team and Player Management menu
    private static void manageTeams(League league, Scanner scanner) {
        System.out.println();
        System.out.println("===== TEAM MANAGEMENT =====");
        System.out.println("1. Create Team");
        System.out.println("2. Add Players to Team");
        System.out.println("3. Assign/View Team Coach");
        System.out.println("4. Set/View Starting Lineup");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                createTeam(league, scanner);
                break;
            case "2":
                createPlayer(league, scanner);
                break;
            case "3":
                manageCoach(league, scanner);
                break;
            case "4":
                manageLineup(league, scanner);
                break;
            case "0":
                return;
            default:
                if (choice.equalsIgnoreCase("EXIT")) {
                    System.out.println("Goodbye!");
        }
    }
}
    //Create team menu for team management
    private static void createTeam(League league, Scanner scanner) {
        System.out.println();
        System.out.println("==== CREATE TEAM =====");

        System.out.print("Enter team ID (EX: T01)1: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter team name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter team city: ");
        String city = scanner.nextLine().trim();

        try {
            Team newTeam = new Team(id, name, city);
            league.promoteTeam(newTeam);
            System.out.println("Team created successfully.");
            System.out.println(newTeam);
        } catch (Exception e) {
            System.out.println("Could not create team: " + e.getMessage());
        }
    }

    //Helper method to select team from league
    private static Team selectTeam(League league, Scanner scanner) {
        System.out.println();
        System.out.println("\n==== SELECT TEAM =====");

        for (int i = 0; i < league.getTeams().size(); i++) {
            Team team = league.getTeams().get(i);
            System.out.println((i + 1) + ". " + team.getName());
        }
        System.out.print("Enter team number (0 to exit): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            if (choice == 0) {
                return null;
            }
            if (choice < 1 || choice > league.getTeams().size()) {
                System.out.println("Invalid team number.");
                return null;
            }
            return league.getTeams().get(choice - 1);
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
            return null;
        }
    }

    //Create player menu for player management
    private static void createPlayer(League league, Scanner scanner) {
        System.out.println();
        System.out.println("==== CREATE PLAYER =====");

        Team team = selectTeam(league, scanner);

        if (team == null) {
            return;
        }

        System.out.print("Enter player ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter player name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter player position (GK, DEF, MID, FWD): ");
        String position = scanner.nextLine().trim();

        try {
            System.out.print("Enter jersey number (1-99): ");
            int jerseyNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter player age (16-50): ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter overall rating (0-99): ");
            int overall = Integer.parseInt(scanner.nextLine().trim());

            Player newPlayer = new Player(
                id, 
                name, 
                position, 
                jerseyNumber, 
                age, 
                overall, //Overall
                overall, //Pace
                overall, //Shooting
                overall, //Passing
                overall, //Dribbling
                overall, //Defending
                overall, //Physicality
                overall, //Simulated attack
                50       //Simulated discipline
            );
            team.addPlayer(newPlayer);
            System.out.println("Player created successfully.");
            System.out.println(newPlayer);
            System.out.println("Added to: " + team.getName());
            
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    
    //Assign coach to team or view team coach
    private static void manageCoach(League league, Scanner scanner) {
        System.out.println();
        System.out.println("==== TEAM COACH =====");
        
        Team team = selectTeam(league, scanner);

        if (team == null) {
            return;
        }
        if (team.getCoach() != null) {
            System.out.println("Current coach: ");
            System.out.println(team.getCoach());
            return;
        }
        System.out.println("This team does not have a coach.");

        System.out.print("Enter coach ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter coach name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter formation (EX:4-3-3): ");
        String formation = scanner.nextLine().trim();

        try {
            Coach coach = new Coach(id, name, formation);
            team.addCoach(coach);
            System.out.println("Coach added successfully.");
            System.out.println(coach);

        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Sets 11 players from a team to starting lineup
    private static void manageLineup(League league, Scanner scanner) {
        System.out.println("\n===== STARTING LINEUP =====");

        Team team = selectTeam(league, scanner);
        if (team == null) {
            return;
        }

        Coach coach = team.getCoach();
        if (coach == null) {
            System.out.println("Assign a coach to this team first.");
            return;
        }

        if (!coach.getStartingLineup().isEmpty()) {
            System.out.println("Starting lineup for " + team.getName());

            for (Player player : coach.getStartingLineup()) {
                System.out.println(player.getName() + " | " + player.getPosition());
            }
            return;
        }
        ArrayList<Player> lineup = new ArrayList<>();
        Player goalkeeper = null;
        for (Player player : team.getPlayers()) {
            if (player.getPosition().equalsIgnoreCase("GK")) {
                if (goalkeeper == null) {
                    goalkeeper = player;
                }
            } else if (lineup.size() < 10) {
                lineup.add(player);
            }
        }
        if (goalkeeper == null || lineup.size() < 10) {
            System.out.println("Team needs 1 goalkeeper and 10 other players.");
            return;
        }
        lineup.add(0, goalkeeper);

        try {
            coach.selectStartingLineup(lineup);
            System.out.println("Starting lineup selected successfully!");

            for (Player player : coach.getStartingLineup()) {
                System.out.println(player.getName() + " | " + player.getPosition());
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    //4. League standings
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
}

