package com.sportradar.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sportradar.domain.GameData;
import com.sportradar.domain.Team;
import com.sportradar.entity.GameEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameConverterTest {

  private GameConverter gameConverter;

  @Test
  public void convert() {
    // GIVEN
    GameEntity gameEntity = GameEntity.builder()
        .homeTeam("home")
        .homeTeamScore(5)
        .awayTeam("away")
        .awayTeamScore(3)
        .startedTime(123L)
        .build();
    // WHEN
    GameData result = gameConverter.convert(gameEntity);
    // THEN
    assertEquals(result, GameData.builder()
        .homeTeam(
            Team.builder().name("home").score(5).build())
        .awayTeam(
            Team.builder().name("away").score(3).build())
        .startedTime(123L)
        .build());
  }
}