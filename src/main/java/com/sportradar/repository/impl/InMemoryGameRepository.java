package com.sportradar.repository.impl;

import com.sportradar.entity.GameEntity;
import com.sportradar.repository.GameRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
  private static final Set<String> PLAYING_TEAMS = new HashSet<>();

  @Override
  public String createGame(String homeTeam, String awayTeam) {
    log.debug("Creating game for {} : {}", homeTeam, awayTeam);
    GameEntity gameEntity = GameEntity.builder()
        .startedTime(Instant.now(clock).toEpochMilli())
        .homeTeam(homeTeam)
        .homeTeamScore(0)
        .awayTeam(awayTeam)
        .awayTeamScore(0)
        .build();

    String gameId = homeTeam + awayTeam;
    GAME_STORAGE.put(gameId, gameEntity);
    PLAYING_TEAMS.addAll(Arrays.asList(homeTeam, awayTeam));
    log.debug("Game created with id {}", gameId);
    return gameId;
  }

  @Override
  public boolean isTeamPlaying(String teamName) {
    return PLAYING_TEAMS.contains(teamName);
  }

  @Override
  public Optional<GameEntity> getGame(String gameId) {
    return Optional.ofNullable(GAME_STORAGE.get(gameId));
  }

  @Override
  public void removeGame(String gameId) {
    GameEntity removedGame = GAME_STORAGE.remove(gameId);
    PLAYING_TEAMS.removeAll(Arrays.asList(removedGame.getHomeTeam(), removedGame.getAwayTeam()));
  }

  @Override
  public void updateScore(String gameId, int homeTeamScore, int awayTeamScore) {
    GameEntity gameEntity = GAME_STORAGE.get(gameId);
    gameEntity.setHomeTeamScore(homeTeamScore);
    gameEntity.setAwayTeamScore(awayTeamScore);
  }

  @Override
  public void removeAllData() {
    GAME_STORAGE.clear();
    PLAYING_TEAMS.clear();
  }

  @Override
  public Collection<GameEntity> getAllGames() {
    return GAME_STORAGE.values();
  }
}
