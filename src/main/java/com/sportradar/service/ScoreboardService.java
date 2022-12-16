package com.sportradar.service;

import com.sportradar.domain.GameData;
import com.sportradar.exception.GameNotCreatedException;
import com.sportradar.exception.GameNotFoundException;
import com.sportradar.exception.UpdateScoreException;
import java.util.LinkedList;

/**
 * Scoreboard component that shows all the ongoing matches and their scores.
 */
public interface ScoreboardService {

  /**
   * Creates game with initial scores 0 - 0
   *
   * @param homeTeam home team name
   * @param awayTeam away team name
   * @return game id
   * @throws GameNotCreatedException in case game can't be created by some reason
   */
  String startNewGame(String homeTeam, String awayTeam) throws GameNotCreatedException;

  /**
   * Returns game data by game id
   *
   * @param gameId game id
   * @return {@link GameData}
   * @throws GameNotFoundException in case passed game id not belong to any games that in progress
   */
  GameData getGameData(String gameId) throws GameNotFoundException;

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
  void updateScore(String gameId, int homeTeamScore, int awayTeamScore)
      throws UpdateScoreException, GameNotFoundException;

  /**
   * Stops game by passed id
   *
   * @param gameId game id
   * @throws GameNotFoundException in case passed id game not exists
   */
  void stopGame(String gameId) throws GameNotFoundException;

  /**
   * Returns summary od gameas that in progress ordered by their total score. The games with the
   * same total score will be returned ordered by the most recently started match in the
   * scoreboard.
   *
   * @return ordered list of {@link GameData}
   */
  LinkedList<GameData> gamesSummary();
}
