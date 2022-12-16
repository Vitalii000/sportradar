package com.sportradar;

import com.sportradar.domain.GameData;
import com.sportradar.exception.GameNotCreatedException;
import com.sportradar.exception.GameNotCreatedException.Reason;
import com.sportradar.exception.GameNotFoundException;
import com.sportradar.exception.UpdateScoreException;
import com.sportradar.service.GameRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.LinkedList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Scoreboard component that shows all the ongoing matches and their scores.
 */
@Slf4j
@Component
@AllArgsConstructor
public class Scoreboard {

  private final Clock clock;
  private final GameRepository gameRepository;


  /**
   * Creates game with initial scores 0 - 0
   *
   * @param homeTeam home team name
   * @param awayTeam away team name
   * @return game id
   * @throws GameNotCreatedException in case game can't be created by some reason
   */
  public String startNewGame(String homeTeam, String awayTeam) throws GameNotCreatedException {
    if (homeTeam.equals(awayTeam)) {
      throw new GameNotCreatedException(Reason.INVALID_TEAM_NAME, homeTeam, awayTeam);
    }

    GameData gameData = new GameData(homeTeam, awayTeam, Instant.now(clock).toEpochMilli());
    return gameRepository.createGame(gameData);
  }

  /**
   * Returns game data by game id
   *
   * @param gameId game id
   * @return {@link GameData}
   * @throws GameNotFoundException in case passed game id not belong to any games that in progress
   */
  public GameData getGameData(String gameId) throws GameNotFoundException {
    return gameRepository.getGame(gameId).orElseThrow(
        () -> new GameNotFoundException(String.format("Game with id %s is not found", gameId)));
  }

  /**
   * Set desired score for game
   *
   * @param gameId        game id
   * @param homeTeamScore score for home team
   * @param awayTeamScore score for away team
   * @return true if score updates, otherwise returns false in case score was not updated (example -
   * requested update from 1-1 to 1-1)
   * @throws UpdateScoreException in case score can't be updated due to violating business
   *                              requirements (example - downgrading score)
   */
  public void updateScore(String gameId, int homeTeamScore, int awayTeamScore)
      throws UpdateScoreException, GameNotFoundException {
    log.debug("Try to update score for game {}", gameId);
    GameData gameData = getGameData(gameId);
    if (gameData.getHomeTeam().getScore() > homeTeamScore
        || gameData.getAwayTeam().getScore() > awayTeamScore) {
      throw new UpdateScoreException(gameData);
    }
    log.debug("Before update game {}, result {}", gameId, gameData.getCurrentResult());

  }

  /**
   * Stops game by passed id
   *
   * @param gameId game id
   * @throws GameNotFoundException in case passed id game not exists
   */
  public void stopGame(String gameId) throws GameNotFoundException {
    log.debug("Try to stop game {}", gameId);
    GameData gameData = getGameData(gameId);
    log.debug("Stopping game {}", gameId);
    gameRepository.removeGame(gameId);
    log.debug("Game stopped {} with result {}", gameId, gameData.getCurrentResult());
  }

  /**
   * Returns summary od gameas that in progress ordered by their total score. The games with the
   * same total score will be returned ordered by the most recently started match in the
   * scoreboard.
   *
   * @return ordered list of {@link GameData}
   */
  public LinkedList<GameData> gamesSummary() {
    return null;
  }
}
