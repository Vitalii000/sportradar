package com.sportradar.utils;

import com.sportradar.domain.GameData;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for sort functionality
 */
public class GameSortUtils {

  private GameSortUtils() {
  }

  private final static Comparator<GameData> SCORE_SUM_COMPARATOR = Comparator.comparing(
      gameData -> gameData.getHomeTeam().getScore() + gameData.getAwayTeam().getScore());
  private final static Comparator<GameData> START_TIME_COMPARATOR = Comparator.comparing(
      GameData::getStartedTime);

  /**
   * Sorts games by total sum of scores and started time
   *
   * @param gameDataList games
   * @return sorted result
   */
  public static LinkedList<GameData> sortGameData(List<GameData> gameDataList) {
    return gameDataList.stream()
        .sorted(SCORE_SUM_COMPARATOR.thenComparing(START_TIME_COMPARATOR).reversed())
        .collect(Collectors.toCollection(LinkedList::new));
  }
}
