package main.java.soccerleague.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;

/** Loads custom player data once, at startup, before other teams or players exist. */
public final class PlayerDataLoader {
    private static final String[] REQUIRED_HEADERS = {
        "Team", "Player", "Position", "OVR", "PAC", "SHO", "PAS", "DRI",
        "DEF", "PHY", "SimAttack", "SimDiscipline", "PlayerID", "JerseyNumber", "Age"
    };

    private PlayerDataLoader() {
    }

    public static ArrayList<Team> loadTeams(Path csvPath) throws IOException {
        if (csvPath == null) {
            throw new IllegalArgumentException("CSV path cannot be null");
        }

        ArrayList<Map<String, String>> rows = new ArrayList<>();
        HashSet<String> playerIDs = new HashSet<>();
        LinkedHashMap<String, String> cities = new LinkedHashMap<>();

        // Validate the entire file before reserving any model IDs.
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                throw new IOException("Player CSV is empty");
            }
            List<String> headers = parseLine(firstLine.replaceFirst("^\\uFEFF", ""));
            if (new HashSet<>(headers).size() != headers.size()) {
                throw new IOException("CSV contains duplicate headers");
            }
            for (String header : REQUIRED_HEADERS) {
                if (!headers.contains(header)) {
                    throw new IOException("Missing CSV header: " + header);
                }
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    List<String> values = parseLine(line);
                    if (values.size() != headers.size()) {
                        throw new IllegalArgumentException("Column count does not match header");
                    }
                    Map<String, String> row = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        row.put(headers.get(i), values.get(i));
                    }
                    for (String header : REQUIRED_HEADERS) {
                        if (row.get(header).isBlank()) {
                            throw new IllegalArgumentException(header + " cannot be blank");
                        }
                    }
                    if (!playerIDs.add(row.get("PlayerID"))) {
                        throw new IllegalArgumentException("Duplicate PlayerID: " + row.get("PlayerID"));
                    }
                    validateNumber(row, "JerseyNumber", 1, 99);
                    validateNumber(row, "Age", 16, 50);
                    for (String rating : new String[] {
                            "OVR", "PAC", "SHO", "PAS", "DRI", "DEF", "PHY",
                            "SimAttack", "SimDiscipline"}) {
                        validateNumber(row, rating, 0, 99);
                    }
                    cities.put(row.get("Team"), cityFor(row.get("Team")));
                    rows.add(row);
                    if (rows.size() > 100 || cities.size() > 100) {
                        throw new IllegalArgumentException("CSV exceeds the 100-instance limit");
                    }
                } catch (IllegalArgumentException | IOException exception) {
                    throw new IOException("CSV row " + lineNumber + ": " + exception.getMessage(), exception);
                }
            }
        }
        if (rows.isEmpty()) {
            throw new IOException("Player CSV contains no players");
        }

        LinkedHashMap<String, Team> teams = new LinkedHashMap<>();
        try {
            int teamNumber = 1;
            for (Map.Entry<String, String> entry : cities.entrySet()) {
                Team team = new Team("CSV-T" + teamNumber++, entry.getKey(), entry.getValue());
                teams.put(entry.getKey(), team);
            }
            for (Map<String, String> row : rows) {
                Player player = new Player(
                        row.get("PlayerID"), row.get("Player"), row.get("Position"),
                        Integer.parseInt(row.get("JerseyNumber")), Integer.parseInt(row.get("Age")),
                        Integer.parseInt(row.get("OVR")), Integer.parseInt(row.get("PAC")),
                        Integer.parseInt(row.get("SHO")), Integer.parseInt(row.get("PAS")),
                        Integer.parseInt(row.get("DRI")), Integer.parseInt(row.get("DEF")),
                        Integer.parseInt(row.get("PHY")), Integer.parseInt(row.get("SimAttack")),
                        Integer.parseInt(row.get("SimDiscipline")));
                teams.get(row.get("Team")).addPlayer(player);
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            throw new IOException("Could not create imported objects: " + exception.getMessage()
                    + ". Load only once at startup, before creating other players or teams."
                    + " Some objects may already have been created; restart before retrying.", exception);
        }
        return new ArrayList<>(teams.values());
    }

    private static void validateNumber(Map<String, String> row, String field, int min, int max) {
        int value;
        try {
            value = Integer.parseInt(row.get(field));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(field + " must be a whole number", exception);
        }
        if (value < min || value > max) {
            throw new IllegalArgumentException(field + " must be between " + min + " and " + max);
        }
    }

    private static String cityFor(String team) {
        switch (team) {
            case "Paris Saint-Germain": return "Paris";
            case "Liverpool": return "Liverpool";
            case "Inter Milan": return "Milan";
            case "Real Madrid": return "Madrid";
            case "Barcelona": return "Barcelona";
            case "Manchester City": return "Manchester";
            case "Bayern Munich": return "Munich";
            case "Arsenal": return "London";
            default: throw new IllegalArgumentException("No city configured for team: " + team);
        }
    }

    // Supports quoted commas and escaped quotes. Records must occupy one line.
    private static List<String> parseLine(String line) throws IOException {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        boolean closedQuote = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (quoted) {
                if (character == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        value.append('"');
                        i++;
                    } else {
                        quoted = false;
                        closedQuote = true;
                    }
                } else {
                    value.append(character);
                }
            } else if (character == ',') {
                values.add(value.toString().trim());
                value.setLength(0);
                closedQuote = false;
            } else if (character == '"') {
                if (!value.toString().isBlank() || closedQuote) {
                    throw new IOException("Unexpected quote in CSV field");
                }
                value.setLength(0);
                quoted = true;
            } else if (closedQuote && !Character.isWhitespace(character)) {
                throw new IOException("Unexpected text after closing quote");
            } else if (!closedQuote) {
                value.append(character);
            }
        }
        if (quoted) {
            throw new IOException("Unclosed quote; multiline CSV fields are not supported");
        }
        values.add(value.toString().trim());
        return values;
    }
}
