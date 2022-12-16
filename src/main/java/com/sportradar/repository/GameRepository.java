package com.sportradar.repository;

import com.sportradar.entity.GameEntity;
import java.util.Collection;
import java.util.Optional;

public interface GameRepository {

  String createGame(String homeTeam, String awayTeam);

  boolean isTeamPlaying(String teamName);

  Optional<GameEntity> getGame(String gameId);

  void removeGame(String gameId);

  void updateScore(String gameId, int homeTeamScore, int awayTeamScore);

  void removeAllData();

  Collection<GameEntity> getAllGames();
}
