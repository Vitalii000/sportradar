package com.sportradar.service.impl;

import com.sportradar.domain.GameData;
import com.sportradar.service.GameRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repository that use internal memory for saving information about games
 */
@Repository
public class InMemoryGameRepository implements GameRepository {

  private static final Map<String, GameData> GAME_STORAGE = new HashMap<>();

  @Override
  public String createGame(GameData gameData) {
    String gameId = gameData.getHomeTeam().getName() + gameData.getAwayTeam().getName();
    GAME_STORAGE.put(gameId, gameData);
    return gameId;
  }

  @Override
  public Optional<GameData> getGame(String gameId) {
    return Optional.ofNullable(GAME_STORAGE.get(gameId));
  }

  @Override
  public void removeGame(String gameId) {
    
  }
}
