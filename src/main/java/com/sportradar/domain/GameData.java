package com.sportradar.domain;

import lombok.Getter;

/**
 * Representation of game data
 */
public class GameData {

  private final Team homeTeam;
  private final Team awayTeam;
  @Getter
  private final Long startedTime;

  public GameData(String homeTeam, String awayTeam, Long startedTime) {
    this.homeTeam = new Team(homeTeam);
    this.awayTeam = new Team(awayTeam);
    this.startedTime = startedTime;
  }

  public Team getHomeTeam() {
    return new Team(this.homeTeam);
  }

  public Team getAwayTeam() {
    return new Team(awayTeam);
  }

  public String getCurrentResult() {
    return String.format("%s:%s - %s:%s", this.homeTeam.getName(), this.homeTeam.getScore(),
        this.awayTeam.getName(), this.awayTeam.getScore());
  }
}
