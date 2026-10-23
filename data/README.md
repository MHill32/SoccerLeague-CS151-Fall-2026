# Custom player dataset

`players.csv` contains 96 real player names selected from the third-party EA_FC_ANALYSIS dataset, which labels itself an FC 27 snapshot dated September 12, 2026. Club aliases are mapped to this project's eight team names. This is a selected simulation roster, not a complete current squad.

Only the Player column was replaced. Ratings, ages, jersey numbers, IDs, and simulation positions remain the original custom/placeholder values supplied by Erik; they are not verified attributes of the named players. Inter's LW and RW slots are approximations using wide players because the source offers no LW/RW matches for that club. The mapping and source positions are recorded in player-name-sources.json.

Sources:
- https://github.com/LakshmiKanth11/EA_FC_ANALYSIS
- Source file: EA_FC_PLAYERS_DATASET_AND_%20DASHBOARD/data/players.csv
- https://www.ea.com/games/ea-sports-fc/ratings

Names were selected by eligible primary/alternate position, preferring primary positions and higher source overall ratings. The original CSV was SoccerLeague_96_Player_Database_Custom8.csv. The supplied source file outside the repository is unchanged.

Load once at application startup, before creating other Player or Team objects:

```java
import java.nio.file.Path;
import main.java.soccerleague.data.PlayerDataLoader;

for (Team team : PlayerDataLoader.loadTeams(Path.of("data", "players.csv"))) {
    league.promoteTeam(team);
}
```

The caller must catch IOException and display its message. Run from the repository root. No Scanner is used for file input. Coaches and starting lineups are not created by this loader.

All rows are validated before model construction. Validation errors include CSV row numbers. Team IDs are generated as CSV-T1, CSV-T2, etc. in first-appearance order; city mappings are explicit in the loader. Unknown teams are rejected until their cities are configured. Quoted commas and escaped quotes are supported; multiline fields are rejected.

Existing model registries cannot be rolled back. If construction conflicts with previously created IDs or remaining capacity, the loader reports the failure, but objects may already have been created. Restart before retrying. Do not use this startup loader for incremental or repeated imports.

Run PlayerDataLoaderTest in its own Java process, like the limit tests: it creates 96 players and cannot share a JVM with other Player-creating test classes under the global limit.
