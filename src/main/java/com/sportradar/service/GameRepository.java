package com.sportradar.service;

import com.sportradar.domain.GameData;
import java.util.Optional;

public interface GameRepository {

  String createGame(GameData gameData);

  Optional<GameData> getGame(String gameId);

  void removeGame(String gameId);
}
