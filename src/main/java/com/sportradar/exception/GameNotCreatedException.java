package com.sportradar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception for cases when game can't be created by some reason
 */
@AllArgsConstructor
@Getter
public class GameNotCreatedException extends RuntimeException {

  private final String reason;

}
