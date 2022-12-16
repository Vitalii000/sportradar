package com.sportradar.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sportradar.entity.GameEntity;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InMemoryGameRepositoryTest {

  private static final Instant INSTANT_NOW = Instant.now();

  @Mock
  private Clock clock;
  @InjectMocks
  private InMemoryGameRepository gameRepository;


  @BeforeEach
  void setUp() {
    Mockito.when(clock.instant()).thenReturn(INSTANT_NOW);
  }

  @Test
  public void createGame() {
    // WHEN
    String gameId = gameRepository.createGame("tesm1", "tesm2");
    // THEN
    assertEquals(gameId, "tesm1tesm2");
  }

  @Test
  public void isTeamPlaying() {
    // GIVEN
    gameRepository.createGame("test1", "test2");
    // WHEN
    boolean playing = gameRepository.isTeamPlaying("test1");
    // THEN
    assertTrue(playing);
  }

  @Test
  public void getGame() {
    // GIVEN
    String gameId = gameRepository.createGame("test1", "test2");
    // WHEN
    Optional<GameEntity> maybeGame = gameRepository.getGame(gameId);
    // THEN
    assertTrue(maybeGame.isPresent());
    GameEntity gameEntity = maybeGame.get();
    assertEquals(gameEntity.getHomeTeam(), "test1");
    assertEquals(gameEntity.getHomeTeamScore(), 0);
    assertEquals(gameEntity.getAwayTeam(), "test2");
    assertEquals(gameEntity.getAwayTeamScore(), 0);
    assertEquals(gameEntity.getStartedTime(), INSTANT_NOW.toEpochMilli());

  }

  @Test
  public void removeGame() {
    // GIVEN
    String gameId = gameRepository.createGame("test1", "test2");
    assertFalse(gameRepository.getAllGames().isEmpty());
    // WHEN
    gameRepository.removeGame(gameId);
    // THEN
    assertTrue(gameRepository.getAllGames().isEmpty());
  }

  @Test
  public void updateScore() {
    // GIVEN
    String gameId = gameRepository.createGame("test1", "test2");
    // WHEN
    gameRepository.updateScore(gameId, 1, 2);
    // THEN
    GameEntity gameEntity = gameRepository.getGame(gameId).get();
    assertEquals(gameEntity.getHomeTeam(), "test1");
    assertEquals(gameEntity.getHomeTeamScore(), 1);
    assertEquals(gameEntity.getAwayTeam(), "test2");
    assertEquals(gameEntity.getAwayTeamScore(), 2);
    assertEquals(gameEntity.getStartedTime(), INSTANT_NOW.toEpochMilli());
  }

  @Test
  public void removeAllData() {
    // GIVEN
    gameRepository.createGame("test1", "test2");
    gameRepository.createGame("test3", "test4");
    assertEquals(gameRepository.getAllGames().size(), 2);
    // WHEN
    gameRepository.removeAllData();
    // THEN
    assertTrue(gameRepository.getAllGames().isEmpty());
  }

  @Test
  public void getAllGames() {
    // GIVEN
    gameRepository.createGame("test1", "test2");
    gameRepository.createGame("test3", "test4");
    // WHEN
    Collection<GameEntity> allGames = gameRepository.getAllGames();
    // THEN
    assertEquals(allGames.size(), 2);
    assertTrue(allGames.containsAll(Arrays.asList(
        GameEntity.builder()
            .startedTime(INSTANT_NOW.toEpochMilli())
            .homeTeam("test1")
            .homeTeamScore(0)
            .awayTeam("test2")
            .awayTeamScore(0)
            .build(),
        GameEntity.builder()
            .startedTime(INSTANT_NOW.toEpochMilli())
            .homeTeam("test3")
            .homeTeamScore(0)
            .awayTeam("test4")
            .awayTeamScore(0)
            .build()
    )));
  }
}