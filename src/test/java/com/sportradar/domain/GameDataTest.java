package com.sportradar.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GameDataTest {

  @Test
  public void getCurrentResult_shouldReturnCurrentScoreResult() {
    // GIVEN
    GameData gameData = GameData.builder().homeTeam(Team.builder().name("Dynamo").score(2).build())
        .awayTeam(
            Team.builder().name("Inter").score(3).build()).build();
    // WHEN
    String currentScore = gameData.getCurrentResult();
    // THEN
    assertEquals(currentScore, "Dynamo:2 - Inter:3");
  }
}