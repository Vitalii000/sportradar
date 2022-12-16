package com.sportradar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception for cases when game can't be created by some reason
 */

@Getter
public class GameNotCreatedException extends RuntimeException {

  private final Reason reason;

  public GameNotCreatedException(Reason reason,String homeTeam, String awayTeam) {
    super(String.format(reason.messageTemplate,homeTeam,awayTeam));
    this.reason = reason;
  }

  @Getter
  @AllArgsConstructor
  public enum Reason {
    INVALID_TEAM_NAME("Can't create game with same team names. HomeTeam: %s, AwayTeam: %s"),
    GAME_IN_PROGRESS("Can't create game. Game with same teams already started. HomeTeam: %s, AwayTeam: %s"),
    WRONG_HOME_TEAM("Can't create game. Home team already involved into other game. HomeTeam: %s, AwayTeam: %s"),
    WRONG_AWAY_TEAM("Can't create game. Away team already involved into other game. HomeTeam: %s, AwayTeam: %s");
    private final String messageTemplate;

  }
}
