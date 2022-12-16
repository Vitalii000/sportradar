package com.sportradar.service.impl;

import com.sportradar.entity.GameEntity;
import com.sportradar.service.GameRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Repository that use internal memory for saving information about games
 */
@Repository
@AllArgsConstructor
@Slf4j
public class InMemoryGameRepository implements GameRepository {

  private final Clock clock;

  private static final Map<String, GameEntity> GAME_STORAGE = new HashMap<>();

  @Override
  public String createGame(String homeTeam, String awayTeam) {
    log.info("Creating game for {} : {}", homeTeam, awayTeam);
    GameEntity gameEntity = GameEntity.builder()
        .startedTime(Instant.now(clock).toEpochMilli())
        .homeTeam(homeTeam)
        .homeTeamScore(0)
        .awayTeam(awayTeam)
        .awayTeamScore(0)
        .build();

    String gameId = homeTeam + awayTeam;
    GAME_STORAGE.put(gameId, gameEntity);
    log.info("Game created with id {}", gameId);
    return gameId;
  }

  @Override
  public Optional<GameEntity> getGame(String gameId) {
    return Optional.ofNullable(GAME_STORAGE.get(gameId));
  }

  @Override
  public void removeGame(String gameId) {

  }
}
