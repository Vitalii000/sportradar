package com.sportradar;

import com.sportradar.domain.GameData;
import org.springframework.stereotype.Component;

@Component
public class Scoreboard {

  public String startNewGame(String homeTeam, String awayTeam) {
    return null;
  }

  public GameData getGameData(String gameId) {
    return null;
  }

  public boolean updateScore(String gameId, int homeTeamScore, int awayTeamScore) {
    return false;
  }

  public boolean stopGame(String gameId) {
    return false;
  }
}
