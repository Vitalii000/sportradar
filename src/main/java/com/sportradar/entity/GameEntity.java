package com.sportradar.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Game entity - representation of one row in database
 */
@Builder
@Getter
@EqualsAndHashCode
public class GameEntity {

  private String homeTeam;
  @Setter
  private int homeTeamScore;
  private String awayTeam;
  @Setter
  private int awayTeamScore;
  private Long startedTime;

}
