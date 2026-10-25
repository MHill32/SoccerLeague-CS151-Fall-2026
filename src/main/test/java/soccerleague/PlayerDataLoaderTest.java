package main.test.java.soccerleague;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import main.java.soccerleague.data.PlayerDataLoader;
import main.java.soccerleague.model.Player;
import main.java.soccerleague.model.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerDataLoaderTest extends TestSupport {
    @TempDir
    Path temporaryDirectory;

    private String header() {
        return "Team,Player,Position,OVR,PAC,SHO,PAS,DRI,DEF,PHY,SimAttack,SimDiscipline,PlayerID,JerseyNumber,Age\n";
    }

    private Path write(String content) throws IOException {
        Path file = temporaryDirectory.resolve("players.csv");
        Files.writeString(file, content);
        return file;
    }

    @Test
    public void testLoadsActualDatasetAndMapsAttributes() throws IOException {
        ArrayList<Team> teams = PlayerDataLoader.loadTeams(Path.of("data", "players.csv"));
        assertEquals(8, teams.size());
        HashSet<String> ids = new HashSet<>();
        for (Team team : teams) {
            assertEquals(12, team.getRosterSize());
            assertEquals(1, team.getPlayers().stream()
                    .filter(p -> p.getPosition().equals("GK")).count());
            for (Player player : team.getPlayers()) {
                assertTrue(ids.add(player.getPlayerID()));
            }
        }
        assertEquals(96, ids.size());
        Team paris = teams.get(0);
        assertEquals("Paris Saint-Germain", paris.getName());
        assertEquals("Paris", paris.getCity());
        Player player = paris.getPlayers().get(0);
        assertEquals("P0101", player.getPlayerID());
        assertEquals("Matvey Safonov", player.getName());
        assertEquals("GK", player.getPosition());
        assertEquals(1, player.getJerseyNumber());
        assertEquals(19, player.getAge());
        assertEquals(79, player.getOverall());
        assertEquals(46, player.getPace());
        assertEquals(39, player.getShooting());
        assertEquals(67, player.getPassing());
        assertEquals(41, player.getDribbling());
        assertEquals(40, player.getDefending());
        assertEquals(74, player.getPhysicality());
        assertEquals(47, player.getSimAttack());
        assertEquals(53, player.getSimDiscipline());
        assertEquals(0, player.getGoals());
    }

    @Test
    public void testMissingHeader() throws IOException {
        Path file = write("Team,Player\nArsenal,Alex\n");
        assertThrows(IOException.class, () -> PlayerDataLoader.loadTeams(file));
    }

    @Test
    public void testLateInvalidRowReportsLine() throws IOException {
        Path file = write(header()
                + "Arsenal,Alex,ST,80,80,80,80,80,80,80,80,80,BAD-A,9,25\n"
                + "Arsenal,Sam,ST,100,80,80,80,80,80,80,80,80,BAD-B,10,25\n");
        IOException error = assertThrows(IOException.class, () -> PlayerDataLoader.loadTeams(file));
        assertTrue(error.getMessage().contains("row 3"));
        assertTrue(error.getMessage().contains("OVR"));
    }

    @Test
    public void testDuplicateIDInFile() throws IOException {
        String row = "Arsenal,Alex,ST,80,80,80,80,80,80,80,80,80,DUP-CSV,9,25\n";
        Path file = write(header() + row + row);
        IOException error = assertThrows(IOException.class, () -> PlayerDataLoader.loadTeams(file));
        assertTrue(error.getMessage().contains("Duplicate PlayerID"));
    }

    @Test
    public void testMalformedQuotedField() throws IOException {
        Path file = write(header() + "Arsenal,\"Unclosed name,ST,80\n");
        assertThrows(IOException.class, () -> PlayerDataLoader.loadTeams(file));
    }

    @Test
    public void testMissingFile() {
        assertThrows(IOException.class, () -> PlayerDataLoader.loadTeams(
                temporaryDirectory.resolve("missing.csv")));
    }
}
