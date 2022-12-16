package com.sportradar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sportradar.domain.GameData;
import com.sportradar.domain.Team;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

public class GameSortUtilsTest {

  @Test
  public void sortGameData_shouldSortGamesByTotalSumOfScoresAndStartedTime() {
    // GIVEN
    // a. Mexico 0 - Canada 5
    GameData gameMexicoVsCanada = GameData.builder()
        .homeTeam(Team.builder().name("Mexico").score(0).build())
        .awayTeam(Team.builder().name("Canada").score(5).build())
        .startedTime(111L)
        .build();

    // b. Spain 10 - Brazil 2
    GameData gameSpainVsBrazil = GameData.builder()
        .homeTeam(Team.builder().name("Spain").score(10).build())
        .awayTeam(Team.builder().name("Brazil").score(2).build())
        .startedTime(222L)
        .build();

    // c.Germany 2 - France 2
    GameData gameGermanyVsFrance = GameData.builder()
        .homeTeam(Team.builder().name("Germany").score(2).build())
        .awayTeam(Team.builder().name("France").score(2).build())
        .startedTime(333L)
        .build();

    // c.Uruguay 6 - Italy 6
    GameData gameUruguayVsItaly = GameData.builder()
        .homeTeam(Team.builder().name("Uruguay").score(6).build())
        .awayTeam(Team.builder().name("Italy").score(6).build())
        .startedTime(444L)
        .build();

    // c.Argentina 3 - Italy 1
    GameData gameArgentinaVsAustralia = GameData.builder()
        .homeTeam(Team.builder().name("Argentina").score(3).build())
        .awayTeam(Team.builder().name("Australia").score(1).build())
        .startedTime(555L)
        .build();

    // WHEN
    LinkedList<GameData> result = GameSortUtils.sortGameData(
        Arrays.asList(gameMexicoVsCanada, gameSpainVsBrazil, gameGermanyVsFrance,
            gameUruguayVsItaly, gameArgentinaVsAustralia));
    // THEN
    assertEquals(result.size(), 5);
    // Uruguay 6 - Italy 6
    assertEquals(result.get(0).getHomeTeam().getName(), "Uruguay");
    assertEquals(result.get(0).getHomeTeam().getScore(), 6);
    assertEquals(result.get(0).getAwayTeam().getName(), "Italy");
    assertEquals(result.get(0).getAwayTeam().getScore(), 6);

    // Spain 10 - Brazil 2
    assertEquals(result.get(1).getHomeTeam().getName(), "Spain");
    assertEquals(result.get(1).getHomeTeam().getScore(), 10);
    assertEquals(result.get(1).getAwayTeam().getName(), "Brazil");
    assertEquals(result.get(1).getAwayTeam().getScore(), 2);

    // Mexico 0 - Canada 5
    assertEquals(result.get(2).getHomeTeam().getName(), "Mexico");
    assertEquals(result.get(2).getHomeTeam().getScore(), 0);
    assertEquals(result.get(2).getAwayTeam().getName(), "Canada");
    assertEquals(result.get(2).getAwayTeam().getScore(), 5);

    // Argentina 3 - Australia 1
    assertEquals(result.get(3).getHomeTeam().getName(), "Argentina");
    assertEquals(result.get(3).getHomeTeam().getScore(), 3);
    assertEquals(result.get(3).getAwayTeam().getName(), "Australia");
    assertEquals(result.get(3).getAwayTeam().getScore(), 1);

    // Germany 2 - France 2
    assertEquals(result.get(4).getHomeTeam().getName(), "Germany");
    assertEquals(result.get(4).getHomeTeam().getScore(), 2);
    assertEquals(result.get(4).getAwayTeam().getName(), "France");
    assertEquals(result.get(4).getAwayTeam().getScore(), 2);
  }
}