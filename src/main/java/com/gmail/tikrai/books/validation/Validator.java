package com.gmail.tikrai.books.validation;

import com.gmail.tikrai.books.exception.ValidationException;
import java.util.Optional;

public interface Validator {
  Optional<String> valid();

  default void validate() {
    valid().ifPresent(s -> { throw new ValidationException(s); });
  }
}
