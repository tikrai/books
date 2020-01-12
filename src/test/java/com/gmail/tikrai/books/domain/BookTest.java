package com.gmail.tikrai.books.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.fixture.Fixture;
import java.util.Calendar;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.Test;

class BookTest {

  private final ObjectMapper mapper = Fixture.mapper();
  private final int releaseYear = 1600;
  private final int scienceIndex = 5;
  private final String bookJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":2,\"price\":11.1}";
  private final String antiqueJson = "{\"barcode\":\"1234\",\"name\":\"Book Name\","
      + "\"author\":\"Book Author\",\"quantity\":2,\"price\":11.1,\"antiqueReleaseYear\":1600}";
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

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

  @Test
  void shouldValidateRegularBookSuccessfully() {
    Book book = Fixture.book().build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertThat(violations, empty());
  }

  @Test
  void shouldFailValidatingIfBarcodeIsTooShort() {
    Book book = Fixture.book().barcode("a").build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "barcode", "length must be between 2 and 255");
  }

  @Test
  void shouldFailValidatingIfNameIsTooShort() {
    Book book = Fixture.book().name("a").build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "name", "length must be between 2 and 255");
  }

  @Test
  void shouldFailValidatingIfAuthorIsTooShort() {
    Book book = Fixture.book().author("a").build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "author", "length must be between 2 and 255");
  }

  @Test
  void shouldFailValidatingIfQuantityIsTooSmall() {
    Book book = Fixture.book().quantity(-1).build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "quantity", "must be greater than or equal to 1");
  }

  @Test
  void shouldFailValidatingIfPriceIsTooSmall() {
    Book book = Fixture.book().price(-1).build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "price", "must be greater than or equal to 0");
  }

  @Test
  void shouldFailValidatingAntiqueBookIfNotOldEnough() {
    Book book = Fixture.book().antiqueReleaseYear(1999).build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "antiqueReleaseYear", "must be less than or equal to 1900");
  }

  @Test
  void shouldFailValidatingScienceJournalIfIndexIsTooBig() {
    Book book = Fixture.book().scienceIndex(15).build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "scienceIndex", "must be between 1 and 10");
  }

  @Test
  void shouldFailValidatingIfBookIsAntiqueAndScienceJournal() {
    Book book = Fixture.book().antiqueReleaseYear(releaseYear).scienceIndex(scienceIndex).build();
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    validateViolations(violations, "", "Book cannot be both antique and science journal");
  }

  private void validateViolations(
      Set<ConstraintViolation<Book>> violations,
      String property,
      String message
  ) {
    assertThat(violations.size(), is(1));
    assertThat(violations.iterator().next().getPropertyPath().toString(), is(property));
    assertThat(violations.iterator().next().getMessage(), is(message));
  }
}
