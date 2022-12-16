package com.sportradar.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * Game entity - representation of one row in database
 */
@Builder
@Getter
public class GameEntity {

  private String homeTeam;
  private int homeTeamScore;
  private String awayTeam;
  private int awayTeamScore;
  private Long startedTime;

}
