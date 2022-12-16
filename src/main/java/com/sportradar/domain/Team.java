package com.sportradar.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class Team {

  /**
   * Name of the team. Assumed that name should be unique.
   */
  private final String name;
  /**
   * Amount of scored goals. Assumed that score can't be bigger then {@link Integer#MAX_VALUE}
   */
  private final int score;

}