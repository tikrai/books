package com.gmail.tikrai.books.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.fixture.Fixture;
import java.util.Calendar;
import org.junit.jupiter.api.Test;

class BookTest {

  private final ObjectMapper mapper = Fixture.mapper();
  private final int releaseYear = 1600;
  private final int scienceIndex = 5;
  private final String bookJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":2,\"price\":11.1}";
  private final String antiqueJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":2,\"price\":11.1,\"antiqueReleaseYear\":1600}";

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
    Book book = Fixture.book().antiqueReleaseYear(releaseYear).build();
    String serialized = mapper.writeValueAsString(book);
    assertThat(serialized, is(antiqueJson));
  }

  @Test
  void shouldDeserializeAntiqueBook() throws JsonProcessingException {
    Book deserialized = mapper.readValue(antiqueJson, Book.class);
    assertThat(deserialized, is(Fixture.book().antiqueReleaseYear(releaseYear).build()));
  }

  @Test
  void shouldFailToContructIfBookIsAntiqueAndScienceJournal() {
    assertThrows(
        ValidationException.class,
        () -> Fixture.book().antiqueReleaseYear(releaseYear).scienceIndex(scienceIndex).build()
    );
  }

  @Test
  void shouldCalculateTotalPriceOfRegularBook() {
    Book book = Fixture.book().build();
    assertThat(book.totalPrice(), is(book.quantity() * book.price()));
  }

  @Test
  void shouldCalculateTotalPriceOfAntiqueBook() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    Book book = Fixture.book().antiqueReleaseYear(releaseYear).build();

    double expected = book.quantity() * book.price() * (currentYear - releaseYear) / 10;
    assertThat(book.totalPrice(), is(expected));
  }

  @Test
  void shouldCalculateTotalPriceOfScienceJournal() {
    Book book = Fixture.book().scienceIndex(scienceIndex).build();

    double expected = book.quantity() * book.price() * scienceIndex;
    assertThat(book.totalPrice(), is(expected));
  }
}
