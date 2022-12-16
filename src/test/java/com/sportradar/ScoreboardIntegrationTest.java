package com.sportradar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sportradar.domain.GameData;
import com.sportradar.domain.Team;
import com.sportradar.exception.GameNotCreatedException;
import com.sportradar.exception.UpdateScoreException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ScoreboardIntegrationTest {

  private static final String HOME_TEAM = "homeTeam";
  private static final String AWAY_TEAM = "awayTeam";
  @Autowired
  private Scoreboard scoreboard;


  @Test
  public void startNewGame_shouldCreateGame_andAddItToScoreboard() {
    // WHEN
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    // THEN
    assertNotNull(gameId);
    // AND
    scoreboard.getGameData(gameId);
  }

  @Test
  public void startNewGame_shouldReturnGameDataWithInitialScoreByGameId() {
    // GIVEN
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    // WHEN
    GameData actualGameData = scoreboard.getGameData(gameId);
    // THEN
    assertNotNull(gameId);
    // AND
    Team actualHomeTeam = actualGameData.getHomeTeam();
    assertEquals(actualHomeTeam.getName(), HOME_TEAM);
    assertEquals(actualHomeTeam.getScore(), 0);
    // AND
    Team actualAwayTeam = actualGameData.getAwayTeam();
    assertEquals(actualAwayTeam.getName(), AWAY_TEAM);
    assertEquals(actualAwayTeam.getScore(), 0);
    // AND
    assertEquals(actualGameData.getStartedTime(), 123L);
  }


  @Test
  public void startNewGame_shouldNotCreateGameIfHomeAndAwayTeamAreSame() {
    // GIVEN
    String teamName = "testName";
    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboard.startNewGame(teamName, teamName)
    );
    assertEquals(exception.getReason(), "INVALID_TEAM_NAME");
    assertEquals(exception.getMessage(),
        "Can't create game with same team names. HomeTeam: testName, AwayTeam: testName");
  }

  @Test
  public void startNewGame_shouldNotCreateGameIfGameWithPassesTeamsWasAlreadyCreated() {
    // PRECONDITION
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM)
    );
    assertEquals(exception.getReason(), "GAME_IN_PROGRESS");
    assertEquals(exception.getMessage(),
        "Can't create game. Game with same teams already started. HomeTeam: homeTeam, AwayTeam: awayTeam");
  }

  @Test
  public void startNewGame_shouldNotCreateGameIfHomeTeamInvolvedIntoAnotherGame() {
    // PRECONDITION
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // GIVEN
    String newAwayTeam = "newAwayTeam";

    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboard.startNewGame(HOME_TEAM, newAwayTeam)
    );
    assertEquals(exception.getReason(), "WRONG_HOME_TEAM");
    assertEquals(exception.getMessage(),
        "Can't create game. Home team already involved into other game. HomeTeam: homeTeam, AwayTeam: newAwayTeam");
  }

  @Test
  public void startNewGame_shouldNotCreateGameIfAwayTeamInvolvedIntoAnotherGame() {
    // PRECONDITION
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // GIVEN
    String newHomeTeam = "newHomeTeam";

    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboard.startNewGame(newHomeTeam, AWAY_TEAM)
    );
    assertEquals(exception.getReason(), "WRONG_AWAY_TEAM");
    assertEquals(exception.getMessage(),
        "Can't create game. Away team already involved into other game. HomeTeam: newHomeTeam, AwayTeam: awayTeam");
  }

  @ParameterizedTest
  @CsvSource(value = {
      "0, 0",
      "0, 1",
      "1, 0",
      "1, 1"
  })
  public void updateScore_shouldUpdateIfScoreChanged(int homeTeamScore, int awayTeamScore) {
    // PRECONDITION
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);
    GameData actualGameData = scoreboard.getGameData(gameId);
    assertEquals(actualGameData.getHomeTeam().getScore(), 0);
    assertEquals(actualGameData.getAwayTeam().getScore(), 0);
    // WHEN
    boolean updated = scoreboard.updateScore(gameId, homeTeamScore, awayTeamScore);
    // THEN
    assertTrue(updated);
    // AND
    GameData updatedGameData = scoreboard.getGameData(gameId);
    assertEquals(updatedGameData.getHomeTeam().getScore(), homeTeamScore);
    assertEquals(updatedGameData.getAwayTeam().getScore(), awayTeamScore);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "0, 0",
      "5, 4",
      "4, 5",
      "4, 4",
      "4, 6",
      "6, 4",
      "-6, -6"
  })
  public void updateScore_shouldReturnErrorIfScoreDowngraded(int homeTeamScore, int awayTeamScore) {
    // PRECONDITION
    String gameId = scoreboard.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);
    scoreboard.updateScore(gameId, 5, 5);
    GameData actualGameData = scoreboard.getGameData(gameId);
    assertEquals(actualGameData.getHomeTeam().getScore(), 5);
    assertEquals(actualGameData.getAwayTeam().getScore(), 5);
    // WHEN THEN
    assertThrows(
        UpdateScoreException.class,
        () -> scoreboard.updateScore(gameId, homeTeamScore, awayTeamScore)
    );
  }
}