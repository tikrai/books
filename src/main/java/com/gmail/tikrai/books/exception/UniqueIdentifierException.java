package com.gmail.tikrai.books.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UniqueIdentifierException extends RuntimeException {

  public UniqueIdentifierException(String message) {
    super(message);
  }
}
