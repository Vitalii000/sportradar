package com.sportradar.service;

import com.sportradar.domain.GameData;
import com.sportradar.entity.GameEntity;
import java.util.Optional;

public interface GameRepository {

  String createGame(String homeTeam, String awayTeam);

  Optional<GameEntity> getGame(String gameId);

  void removeGame(String gameId);
}
