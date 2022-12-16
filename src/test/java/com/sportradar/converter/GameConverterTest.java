package com.sportradar.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sportradar.domain.GameData;
import com.sportradar.domain.Team;
import com.sportradar.entity.GameEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameConverterTest {

  @InjectMocks private GameConverter gameConverter;

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
    assertEquals(result.getHomeTeam().getName(),"home");
    assertEquals(result.getHomeTeam().getScore(),5);
    assertEquals(result.getAwayTeam().getName(),"away");
    assertEquals(result.getAwayTeam().getScore(),3);
    assertEquals(result.getStartedTime(),123L);
  }
}