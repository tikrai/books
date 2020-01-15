package com.gmail.tikrai.books.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gmail.tikrai.books.exception.ValidationException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ValidatorTest {

  @Test
  void shoudValidate() {
    Validator validator = Optional::empty;
    validator.validate();
  }

  @Test
  void shoudFailValidation() {
    Validator validator = () -> Optional.of("message");
    String message = assertThrows(ValidationException.class, validator::validate).getMessage();
    assertThat(message, equalTo("message"));
  }
}
