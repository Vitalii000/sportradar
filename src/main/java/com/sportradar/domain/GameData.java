package com.sportradar.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Representation of game data
 */
@Builder
@Getter
public class GameData {

  private final Team homeTeam;
  private final Team awayTeam;
  @Getter
  private final Long startedTime;

  public String getCurrentResult() {
    return String.format("%s:%s - %s:%s", this.homeTeam.getName(), this.homeTeam.getScore(),
        this.awayTeam.getName(), this.awayTeam.getScore());
  }
}
