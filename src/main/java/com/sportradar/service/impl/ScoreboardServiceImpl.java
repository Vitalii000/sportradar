package com.sportradar.service.impl;

import com.sportradar.converter.GameConverter;
import com.sportradar.domain.GameData;
import com.sportradar.entity.GameEntity;
import com.sportradar.exception.GameNotCreatedException;
import com.sportradar.exception.GameNotCreatedException.Reason;
import com.sportradar.exception.GameNotFoundException;
import com.sportradar.exception.UpdateScoreException;
import com.sportradar.repository.GameRepository;
import com.sportradar.service.ScoreboardService;
import com.sportradar.utils.GameSortUtils;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 */
@Slf4j
@Component
@AllArgsConstructor
public class ScoreboardServiceImpl implements ScoreboardService {

  private final GameRepository gameRepository;
  private final GameConverter gameConverter;


  /**
   * {@inheritDoc}
   */
  @Override
  public String startNewGame(String homeTeam, String awayTeam) throws GameNotCreatedException {
    homeTeam = homeTeam.trim();
    awayTeam = awayTeam.trim();
    validateGame(homeTeam, awayTeam);
    return gameRepository.createGame(homeTeam, awayTeam);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public GameData getGameData(String gameId) throws GameNotFoundException {
    GameEntity gameEntity = gameRepository.getGame(gameId).orElseThrow(
        () -> new GameNotFoundException(String.format("Game with id %s is not found", gameId)));
    return gameConverter.convert(gameEntity);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void updateScore(String gameId, int homeTeamScore, int awayTeamScore)
      throws UpdateScoreException, GameNotFoundException {
    log.debug("Try to update score for game {}", gameId);
    GameData gameData = getGameData(gameId);
    if (gameData.getHomeTeam().getScore() > homeTeamScore
        || gameData.getAwayTeam().getScore() > awayTeamScore) {
      throw new UpdateScoreException(gameData, homeTeamScore, awayTeamScore);
    }
    log.debug("Before update game {}, result {}", gameId, gameData.getCurrentResult());
    gameRepository.updateScore(gameId, homeTeamScore, awayTeamScore);
    log.debug("After update game {}, result {}", gameId, getGameData(gameId).getCurrentResult());
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void stopGame(String gameId) throws GameNotFoundException {
    log.debug("Try to stop game {}", gameId);
    GameData gameData = getGameData(gameId);
    log.debug("Stopping game {}", gameId);
    gameRepository.removeGame(gameId);
    log.debug("Game stopped {} with result {}", gameId, gameData.getCurrentResult());
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public LinkedList<GameData> gamesSummary() {
    Collection<GameEntity> gameEntities = gameRepository.getAllGames();
    return GameSortUtils.sortGameData(
        gameEntities.stream().map(gameConverter::convert).collect(Collectors.toList()));
  }

  /**
   * Validate if game with passed teams can be started
   *
   * @param homeTeam home team
   * @param awayTeam away team
   */
  private void validateGame(String homeTeam, String awayTeam) {
    if (homeTeam.equals(awayTeam)) {
      throw new GameNotCreatedException(Reason.INVALID_TEAM_NAME, homeTeam, awayTeam);
    }

    if (gameRepository.getGame(homeTeam + awayTeam).isPresent()) {
      throw new GameNotCreatedException(Reason.GAME_IN_PROGRESS, homeTeam, awayTeam);
    }

    if (gameRepository.isTeamPlaying(homeTeam)) {
      throw new GameNotCreatedException(Reason.WRONG_HOME_TEAM, homeTeam, awayTeam);
    }
    if (gameRepository.isTeamPlaying(awayTeam)) {
      throw new GameNotCreatedException(Reason.WRONG_AWAY_TEAM, homeTeam, awayTeam);
    }
  }
}
