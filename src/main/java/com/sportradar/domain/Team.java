package com.sportradar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Team {

  /**
   * Name of the team. Assumed that name should be unique.
   */
  private final String name;
  /**
   * Amount of scored goals. Assumed that score can't be bigger then {@link Integer#MAX_VALUE}
   */
  private int score = 0;

  /**
   * Copy constructor - use when {@link  Team} need to be returned without real links to internal
   * objects. To avoid unexpected data modification.
   *
   * @param team {@link Team}
   */
  public Team(Team team) {
    this.name = team.name;
    this.score = team.score;
  }
}