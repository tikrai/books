package com.gmail.tikrai.books.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.fixture.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookTest {

  private final ObjectMapper mapper = Fixture.mapper();
  private final String bookJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":1,\"price\":11.1}";
  private final String antiqueJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":1,\"price\":11.1,\"antiqueReleaseYear\":1600}";

  @Test
  void shouldSerializeRegularBook() throws JsonProcessingException {
    Book book = Fixture.book().build();
    String serialized = mapper.writeValueAsString(book);
    assertThat(serialized, is(bookJson));
  }

  @Test
  void shouldDeserializeRegularBook() throws JsonProcessingException {
    Book deserialized = mapper.readValue(bookJson, Book.class);
    assertThat(deserialized, is(Fixture.book().build()));
  }

  @Test
  void shouldSerializeAntiqueBook() throws JsonProcessingException {
    Book book = Fixture.book().antiqueReleaseYear(1600).build();
    String serialized = mapper.writeValueAsString(book);
    assertThat(serialized, is(antiqueJson));
  }

  @Test
  void shouldDeserializeAntiqueBook() throws JsonProcessingException {
    Book deserialized = mapper.readValue(antiqueJson, Book.class);
    assertThat(deserialized, is(Fixture.book().antiqueReleaseYear(1600).build()));
  }

  @Test
  void shouldFailIfBookIsAntiqueAndCsienceJournal() {
    Assertions.assertThrows(
        ValidationException.class,
        () -> Fixture.book().antiqueReleaseYear(100).scienceIndex(5).build()
    );
  }
}
