package com.sportradar.converter;

import com.sportradar.domain.GameData;
import com.sportradar.domain.Team;
import com.sportradar.entity.GameEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameConverter implements Converter<GameEntity, GameData> {

  @Override
  public GameData convert(GameEntity source) {
    return GameData.builder()
        .homeTeam(
            Team.builder().name(source.getHomeTeam()).score(source.getHomeTeamScore()).build())
        .awayTeam(
            Team.builder().name(source.getAwayTeam()).score(source.getAwayTeamScore()).build())
        .startedTime(source.getStartedTime())
        .build();
  }
}
