package com.sportradar;

import com.sportradar.domain.GameData;
import com.sportradar.exception.GameNotCreatedException;
import com.sportradar.exception.GameNotFoundException;
import com.sportradar.exception.GameNotStoppedException;
import com.sportradar.exception.UpdateScoreException;
import java.util.LinkedList;
import org.springframework.stereotype.Component;

@Component
public class Scoreboard {

  /**
   * Creates game with initial scores 0 - 0
   *
   * @param homeTeam home team name
   * @param awayTeam away team name
   * @return game id
   * @throws GameNotCreatedException in case game can't be created by some reason
   */
  public String startNewGame(String homeTeam, String awayTeam) throws GameNotCreatedException {
    return null;
  }

  public GameData getGameData(String gameId) throws GameNotFoundException {
    return null;
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
  public boolean updateScore(String gameId, int homeTeamScore, int awayTeamScore)
      throws UpdateScoreException {
    return false;
  }

  /**
   * Stops game by passed id
   *
   * @param gameId game id
   * @throws GameNotStoppedException in case passed id game can't be stopped
   */
  public void stopGame(String gameId) throws GameNotStoppedException {
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
