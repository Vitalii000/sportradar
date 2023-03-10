package com.sportradar.exception;

/**
 * Exception for cases when desired game not exists
 */
public class GameNotFoundException extends RuntimeException {

  public GameNotFoundException(String message) {
    super(message);
  }
}
