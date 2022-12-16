package com.sportradar.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

public class GameDataTest {

  @Test
  public void getHomeTeam_shouldReturnTeamDataAsNewTeamObject() {
    // GIVEN
    GameData gameData = new GameData("homeTeam", "awayTeam", System.currentTimeMillis());
    // WHEN
    Team homeTeam1 = gameData.getHomeTeam();
    Team homeTeam2 = gameData.getHomeTeam();
    // THEN
    assertNotSame(homeTeam1, homeTeam2);
    assertEquals(homeTeam1, homeTeam2);
  }

  @Test
  public void getAwayTeam() {
    // GIVEN
    GameData gameData = new GameData("homeTeam", "awayTeam", System.currentTimeMillis());
    // WHEN
    Team awayTeam1 = gameData.getAwayTeam();
    Team awayTeam2 = gameData.getAwayTeam();
    // THEN
    assertNotSame(awayTeam1, awayTeam2);
    assertEquals(awayTeam1, awayTeam2);
  }

  @Test
  public void getStartedTime() {
    // GIVEN
    long startedTime = System.currentTimeMillis();
    GameData gameData = new GameData("homeTeam", "awayTeam", startedTime);
    // WHEN
    Long actualStartedTime = gameData.getStartedTime();
    // THEN
    assertEquals(actualStartedTime, startedTime);
  }

  @Test
  public void getCurrentResult_shouldReturnCurrentScoreResult() {
    // GIVEN
    long startedTime = System.currentTimeMillis();
    GameData gameData = new GameData("Dynamo", "Inter", startedTime);
    // WHEN
    String currentScore = gameData.getCurrentResult();
    // THEN
    assertEquals(currentScore,"Dynamo:0 - Inter:0");
  }
}