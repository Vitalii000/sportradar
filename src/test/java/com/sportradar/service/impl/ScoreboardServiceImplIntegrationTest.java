package com.sportradar.service.impl;

import static com.sportradar.exception.GameNotCreatedException.Reason.GAME_IN_PROGRESS;
import static com.sportradar.exception.GameNotCreatedException.Reason.INVALID_TEAM_NAME;
import static com.sportradar.exception.GameNotCreatedException.Reason.WRONG_AWAY_TEAM;
import static com.sportradar.exception.GameNotCreatedException.Reason.WRONG_HOME_TEAM;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.sportradar.config.TestClockConfiguration;
import com.sportradar.domain.GameData;
import com.sportradar.domain.Team;
import com.sportradar.exception.GameNotCreatedException;
import com.sportradar.exception.GameNotFoundException;
import com.sportradar.exception.UpdateScoreException;
import com.sportradar.repository.GameRepository;
import java.util.LinkedList;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestClockConfiguration.class)
public class ScoreboardServiceImplIntegrationTest {

  private static final String HOME_TEAM = "homeTeam";
  private static final String AWAY_TEAM = "awayTeam";
  @Autowired
  private ScoreboardServiceImpl scoreboardServiceImpl;
  @Autowired
  private GameRepository gameRepository;

  @BeforeEach
  void setUp() {
    gameRepository.removeAllData();
  }

  @Test
  public void startNewGame_shouldCreateGame_andAddItToScoreboard() {
    // WHEN
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    // THEN
    assertNotNull(gameId);
    // AND
    assertTrue(gameRepository.getGame(gameId).isPresent());
  }

  @Test
  public void startNewGame_shouldNotCreateGameIfHomeAndAwayTeamAreSame() {
    // GIVEN
    String teamName = "testName";
    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboardServiceImpl.startNewGame(teamName, teamName)
    );
    assertEquals(exception.getReason(), INVALID_TEAM_NAME);
    assertEquals(exception.getMessage(),
        "Can't create game with same team names. HomeTeam: testName, AwayTeam: testName");
  }

  @ParameterizedTest
  @MethodSource("sameTeamWithDifferentWriting")
  public void startNewGame_shouldNotCreateGameIfGameWithPassesTeamsWasAlreadyCreated(
      String homeTeam, String awayTeam) {
    // PRECONDITION
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboardServiceImpl.startNewGame(homeTeam, awayTeam)
    );
    assertEquals(exception.getReason(), GAME_IN_PROGRESS);
    assertEquals(exception.getMessage(),
        "Can't create game. Game with same teams already started. HomeTeam: homeTeam, AwayTeam: awayTeam");
  }

  private static Stream<Arguments> sameTeamWithDifferentWriting() {
    return Stream.of(
        arguments("homeTeam", "awayTeam"),
        arguments("homeTeam ", "awayTeam"),
        arguments("homeTeam ", " awayTeam"),
        arguments("homeTeam ", " awayTeam   "),
        arguments("    homeTeam ", " awayTeam   ")
    );
  }

  @Test
  public void startNewGame_shouldNotCreateGameIfHomeTeamInvolvedIntoAnotherGame() {
    // PRECONDITION
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // GIVEN
    String newAwayTeam = "newAwayTeam";

    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboardServiceImpl.startNewGame(HOME_TEAM, newAwayTeam)
    );
    assertEquals(exception.getReason(), WRONG_HOME_TEAM);
    assertEquals(exception.getMessage(),
        "Can't create game. Home team already involved into other game. HomeTeam: homeTeam, AwayTeam: newAwayTeam");
  }

  @Test
  public void startNewGame_shouldNotCreateGameIfAwayTeamInvolvedIntoAnotherGame() {
    // PRECONDITION
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // GIVEN
    String newHomeTeam = "newHomeTeam";

    // WHEN THEN
    GameNotCreatedException exception = assertThrows(
        GameNotCreatedException.class,
        () -> scoreboardServiceImpl.startNewGame(newHomeTeam, AWAY_TEAM)
    );
    assertEquals(exception.getReason(), WRONG_AWAY_TEAM);
    assertEquals(exception.getMessage(),
        "Can't create game. Away team already involved into other game. HomeTeam: newHomeTeam, AwayTeam: awayTeam");
  }

  @Test
  public void getGameData_shouldReturnGameDataWithInitialScoreByGameId() {
    // GIVEN
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    // WHEN
    GameData actualGameData = scoreboardServiceImpl.getGameData(gameId);
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
    assertEquals(actualGameData.getStartedTime(), 1597478400000L);
  }


  @Test
  public void getGameData_shouldReturnErrorIfPassedGameIdNotExists() {
    // GIVEN
    String randomGameId = "randomGameId";
    // WHEN THEN
    GameNotFoundException exception = assertThrows(
        GameNotFoundException.class,
        () -> scoreboardServiceImpl.getGameData(randomGameId)
    );
    assertEquals(exception.getMessage(),
        "Game with id randomGameId is not found");
  }


  @Test
  public void updateScore_shouldReturnErrorIfGameIdNotExists() {
    // GIVEN
    String randomGameId = "randomGameId";
    // WHEN THEN
    GameNotFoundException exception = assertThrows(
        GameNotFoundException.class,
        () -> scoreboardServiceImpl.updateScore(randomGameId, 1, 1)
    );
    assertEquals(exception.getMessage(),
        "Game with id randomGameId is not found");
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
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);
    GameData actualGameData = scoreboardServiceImpl.getGameData(gameId);
    assertEquals(actualGameData.getHomeTeam().getScore(), 0);
    assertEquals(actualGameData.getAwayTeam().getScore(), 0);
    // WHEN THEN
    assertDoesNotThrow(() -> scoreboardServiceImpl.updateScore(gameId, homeTeamScore, awayTeamScore));
    // AND
    GameData updatedGameData = scoreboardServiceImpl.getGameData(gameId);
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
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);
    scoreboardServiceImpl.updateScore(gameId, 5, 5);
    GameData actualGameData = scoreboardServiceImpl.getGameData(gameId);
    assertEquals(actualGameData.getHomeTeam().getScore(), 5);
    assertEquals(actualGameData.getAwayTeam().getScore(), 5);
    // WHEN THEN
    UpdateScoreException exception = assertThrows(
        UpdateScoreException.class,
        () -> scoreboardServiceImpl.updateScore(gameId, homeTeamScore, awayTeamScore)
    );
    assertEquals(exception.getMessage(),
        String.format(
            "Can't update score. Current result-> homeTeam:5 - awayTeam:5. Requested score homeTeam:%s - awayTeam:%s.",
            homeTeamScore, awayTeamScore));
  }

  @Test
  public void stopGame_shouldStopGameIfPassedGameIdInProgress() {
    // PRECONDITION
    String gameId = scoreboardServiceImpl.startNewGame(HOME_TEAM, AWAY_TEAM);
    assertNotNull(gameId);

    // WHEN THEN
    assertDoesNotThrow(() -> scoreboardServiceImpl.stopGame(gameId));
    assertThrows(
        GameNotFoundException.class,
        () -> scoreboardServiceImpl.getGameData(gameId)
    );
  }

  @Test
  public void stopGame_shouldReturnErrorPassedGameIdNotExists() {
    // GIVEN
    String randomGameId = "randomGameId";
    // WHEN THEN
    GameNotFoundException exception = assertThrows(
        GameNotFoundException.class,
        () -> scoreboardServiceImpl.stopGame(randomGameId)
    );
    assertEquals(exception.getMessage(), "Game with id randomGameId is not found");
  }

  @Test
  public void gamesSummary_shouldReturnSummaryOfGamesOrderedByTheirTotalScore() {
    // PRECONDITION

    // a. Mexico 0 - Canada 5
    String gameIdMexicoVsCanada = scoreboardServiceImpl.startNewGame("Mexico", "Canada");
    scoreboardServiceImpl.updateScore(gameIdMexicoVsCanada, 0, 5);

    // b. Spain 10 - Brazil 2
    String gameIdSpainVsBrazil = scoreboardServiceImpl.startNewGame("Spain", "Brazil");
    scoreboardServiceImpl.updateScore(gameIdSpainVsBrazil, 10, 2);

    // c.Germany 2 - France 2
    String gameIdGermanyVsFrance = scoreboardServiceImpl.startNewGame("Germany", "France");
    scoreboardServiceImpl.updateScore(gameIdGermanyVsFrance, 2, 2);

    // c.Uruguay 6 - Italy 6
    String gameIdUruguayVsItaly = scoreboardServiceImpl.startNewGame("Uruguay", "Italy");
    scoreboardServiceImpl.updateScore(gameIdUruguayVsItaly, 6, 6);

    // c.Argentina 3 - Italy 1
    String gameIdArgentinaVsAustralia = scoreboardServiceImpl.startNewGame("Argentina", "Australia");
    scoreboardServiceImpl.updateScore(gameIdArgentinaVsAustralia, 3, 1);

    // WHEN
    LinkedList<GameData> gamesSummary = scoreboardServiceImpl.gamesSummary();
    // THEN
    assertEquals(gamesSummary.size(), 5);

    // Spain 10 - Brazil 2
    assertEquals(gamesSummary.get(0).getHomeTeam().getName(), "Spain");
    assertEquals(gamesSummary.get(0).getHomeTeam().getScore(), 10);
    assertEquals(gamesSummary.get(0).getAwayTeam().getName(), "Brazil");
    assertEquals(gamesSummary.get(0).getAwayTeam().getScore(), 2);

    // Uruguay 6 - Italy 6
    assertEquals(gamesSummary.get(1).getHomeTeam().getName(), "Uruguay");
    assertEquals(gamesSummary.get(1).getHomeTeam().getScore(), 6);
    assertEquals(gamesSummary.get(1).getAwayTeam().getName(), "Italy");
    assertEquals(gamesSummary.get(1).getAwayTeam().getScore(), 6);

    // Mexico 0 - Canada 5
    assertEquals(gamesSummary.get(2).getHomeTeam().getName(), "Mexico");
    assertEquals(gamesSummary.get(2).getHomeTeam().getScore(), 0);
    assertEquals(gamesSummary.get(2).getAwayTeam().getName(), "Canada");
    assertEquals(gamesSummary.get(2).getAwayTeam().getScore(), 5);

    // Argentina 3 - Australia 1
    assertEquals(gamesSummary.get(3).getHomeTeam().getName(), "Argentina");
    assertEquals(gamesSummary.get(3).getHomeTeam().getScore(), 3);
    assertEquals(gamesSummary.get(3).getAwayTeam().getName(), "Australia");
    assertEquals(gamesSummary.get(3).getAwayTeam().getScore(), 1);

    // Germany 2 - France 2
    assertEquals(gamesSummary.get(4).getHomeTeam().getName(), "Germany");
    assertEquals(gamesSummary.get(4).getHomeTeam().getScore(), 2);
    assertEquals(gamesSummary.get(4).getAwayTeam().getName(), "France");
    assertEquals(gamesSummary.get(4).getAwayTeam().getScore(), 2);
  }
}