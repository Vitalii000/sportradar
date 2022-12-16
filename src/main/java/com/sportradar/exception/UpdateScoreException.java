package com.sportradar.exception;

import com.sportradar.domain.GameData;

/**
 * Exception for cases when score can't be updated
 */
public class UpdateScoreException extends RuntimeException {

  private static final String ERROR_MESSAGE_TEMPLATE =
      "Can't update score. Current result-> %s. Requested score %s:%s - %s:%s.";

  public UpdateScoreException(GameData gameData, int homeTeamScoreToUpdate,
      int awayTeamScoreToUpdate) {
    super(String.format(ERROR_MESSAGE_TEMPLATE, gameData.getCurrentResult(),
        gameData.getHomeTeam().getName(),
        homeTeamScoreToUpdate, gameData.getAwayTeam().getName(),
        awayTeamScoreToUpdate));
  }
}
