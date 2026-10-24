# SoccerLeague-CS151-Fall-2026

## Overview
Soccer League management system repository for Project 2. The system allows users: 
- Manage, view, and create teams
- View team stats
- Manage and create players
- Assign coaches to teams and set their lineups 
- View, schedule, and simulate matches
- View league standings
From an interactive menu. The system load 4 teams and their players from a CSV file. Users can create and add
their own teams, players, assign coaches, and simulate/schedule matches as listed above. The match simulations generate goals and cards that occur during a match, and leave the result of the match in the league standings.

## Developers
- Mason Hill - League, Standing, Match, UI, Main, README, Corresponding Unit Tests for owned classes
- Erik Covarrubias - Team, Player, Coach, StatTrack, UI, Corresponding Unit Tests for owned classes
- Pratham Patel - MatchEvent, GoalEvent, CardEvent, UML, Corresponding Unit Tests for owned classes

## Design
Main Classes:
- League: Manages teams, scheduled matches, and league standings.
- Team: Stores team information, players, coaches, and team stats.
- Player: Represents players and their attributes and stats.
- Coach: Manages formations and starting lineups.
- Match: Manages scheduled matches, simulations, scores, and match events.
- Standing: Tracks wins, draws, losses, goals scored, goals allowed, and points.

Abstract Classes and Interfaces:
- MatchEvent (Abstract Class): Shared functionality for match events.
    - GoalEvent: Goals scored during a match.
    - CardEvent: Yellow or Red card occurences during a match.
- StatTrack (Interface): Provides a structure for retrieving player and team stats.

Exception Handling:
- Includes custom exceptions to handle duplicate objects, maximum instance limits, and invalid match operations.

## Installation Instructions
Requirements:
- Java Development Kit (JDK)
- Visual Studio Code with Java extension, or other IDEs that are compatible with Java
- 1. Clone the GitHub repository
- 2. Open the project in IDE
- 3. Open Main.java
- 4. Run the program using IDE run option or in terminal
- 5. Interact and follow the console menu prompts

## Usage
Main Menu:
===== SOCCER LEAGUE MANAGER =====
1. View Teams
2. Manage Teams and Players
3. Manage Matches
4. View League standings
0. Exit

- View Teams: Displayes the current teams in the league, roster size, if a coach is assigned, and team stats.

- Manage Teams and Players: Allows user to: Create new teams, Create Players, Set/View team coaches, automatically select and view starting lineups.

- Manage Matches: Allows user to: View scheduled matches, schedule matches between selected teams, simulate scheduled matches, and view completed match scores and match events.

- View League Standings: Displays league standings, matches played, wins, draws, losses, goals for, goals against, goal difference, and points.

- Exit: Stops the running application.

## Contributions
- Mason: Implemented league management, standings, match handling, and Console UI
- Erik: Implemented team, player attributes and stats, and coach managment, and console UI
- Pratham: Implemented the abstract class match event system, its goal and card event subclasses, and UML.
Team collaborated on integration, testing, exception handling, and overall project design.