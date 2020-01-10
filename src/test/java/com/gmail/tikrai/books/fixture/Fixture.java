package com.gmail.tikrai.books.fixture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.config.ObjectMapperConfiguration;

public final class Fixture {

  public static ObjectMapper mapper() {
    return new ObjectMapperConfiguration().mapper();
  }

  public static BookFixture book() {
    return new BookFixture();
  }
}
