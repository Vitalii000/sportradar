/*
 * Copyright (c) 2020 TRUSTONIC LIMITED
 *   All rights reserved
 *
 * The present software is the confidential and proprietary information of
 * TRUSTONIC LIMITED. You shall not disclose the present software and shall
 * use it only in accordance with the terms of the license agreement you
 * entered into with TRUSTONIC LIMITED. This software may be subject to
 * export or import laws in certain countries.
 */
package com.sportradar.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
public class TestClockConfiguration {

  private static final Instant MOCK_INSTANT = ZonedDateTime.parse("2020-08-15T08:00:00.000Z[UTC]")
      .toInstant();

  /**
   * Allow mock system time during integration test. Use Instant.now(clock).toEpochMilli() when need
   * to work with system time.
   *
   * @return mocked Clock
   */
  @Bean
  @Primary
  public Clock clock() {
    return Clock.fixed(MOCK_INSTANT, ZoneOffset.UTC);
  }
}
