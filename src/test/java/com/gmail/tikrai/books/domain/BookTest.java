package com.gmail.tikrai.books.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.fixture.Fixture;
import java.math.BigDecimal;
import java.util.Calendar;
import org.junit.jupiter.api.Test;

class BookTest {

  private final ObjectMapper mapper = Fixture.mapper();
  private final int releaseYear = 1600;
  private final String bookJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":2,\"price\":11.10}";
  private final String antiqueJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":2,\"price\":11.10,\"antiqueReleaseYear\":1600}";

  private final Book book = Fixture.book().build();

  @Test
  void shouldSerializeRegularBook() throws JsonProcessingException {
    String serialized = mapper.writeValueAsString(book);
    assertThat(serialized, is(bookJson));
  }

  @Test
  void shouldDeserializeRegularBook() throws JsonProcessingException {
    Book deserialized = mapper.readValue(bookJson, Book.class);
    assertThat(deserialized, is(book));
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
  void shouldCalculateTotalPriceOfRegularBook() {
    assertThat(book.totalPrice(), is(book.price().multiply(BigDecimal.valueOf(book.quantity()))));
  }

  @Test
  void shouldCalculateTotalPriceOfAntiqueBook() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    Book book = Fixture.book().antiqueReleaseYear(releaseYear).build();

    BigDecimal expected = book.price()
        .multiply(BigDecimal.valueOf(book.quantity() * (currentYear - releaseYear) / 10.0));
    assertThat(book.totalPrice(), is(expected));
  }

  @Test
  void shouldCalculateTotalPriceOfScienceJournal() {
    int scienceIndex = 5;
    Book book = Fixture.book().scienceIndex(scienceIndex).build();

    BigDecimal expected = book.price().multiply(BigDecimal.valueOf(book.quantity() * scienceIndex));
    assertThat(book.totalPrice(), is(expected));
  }
}
