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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ClockConfiguration {

  /**
   * Use Clock when working with system time Instant.now(clock).toEpochMilli(). This approach help
   * in future write clear tests without mocking static methods.
   *
   * @return bean Clock
   */
  @Bean
  @ConditionalOnMissingBean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
