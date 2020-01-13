package com.gmail.tikrai.books.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.fixture.Fixture;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
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

  private final Book book = Fixture.book().build();
  private final HashMap<String, Object> updates = new HashMap<>();


  @BeforeEach
  void setup()  {
    updates.clear();
  }

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
  void shouldFailToUpdateBarcodeField() {
    updates.put("barcode", "new barcode");
    String message = assertThrows(
        ValidationException.class,
        () -> book.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("Barcode can not be updated"));
  }

  @Test
  void shouldUpdateNameField() {
    String newName = "new name";
    updates.put("name", newName);
    Book actual = book.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.book().name(newName).build()));
  }

  @Test
  void shouldUpdateAuthorField() {
    String newAuthor = "new author";
    updates.put("author", newAuthor);
    Book actual = book.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.book().author(newAuthor).build()));
  }

  @Test
  void shouldUpdateQuantityField() {
    int newQuantity = 150;
    updates.put("quantity", newQuantity);
    Book actual = book.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.book().quantity(newQuantity).build()));
  }

  @Test
  void shouldUpdatePriceField() {
    double newPrice = 16.0;
    updates.put("price", newPrice);
    Book actual = book.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.book().price(newPrice).build()));
  }

  @Test
  void shouldUpdateAntiqueReleaseYearField() {
    int newAntiqueReleaseYear = 150;
    updates.put("antiqueReleaseYear", newAntiqueReleaseYear);
    Book actual = book.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.book().antiqueReleaseYear(newAntiqueReleaseYear).build()));
  }

  @Test
  void shouldUpdateScienceIndexField() {
    int newScienceIndex = 9;
    updates.put("scienceIndex", newScienceIndex);
    Book actual = book.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.book().scienceIndex(newScienceIndex).build()));
  }

  @Test
  void shouldFailToUpdateNonExistingField() {
    updates.put("model", "new model");
    String message = assertThrows(
        ValidationException.class,
        () -> book.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("Book has no field 'model'"));
  }

  @Test
  void shouldFailToUpdateProvidingDataOfWrongType() {
    updates.put("price", "1.0");
    String message = assertThrows(
        ValidationException.class,
        () -> book.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("Incorrect format of 'price' field"));
  }

  @Test
  void shouldFailToUpdateProvidingInvalidValue() {
    updates.put("price", -1.0);
    String message = assertThrows(
        ValidationException.class,
        () -> book.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("price must be greater than or equal to 0"));
  }

  @Test
  void shouldUpdateNameAuthorQuantityPriceAndNewAntiqueReleaseYearFields() {
    String newName = "new name";
    String newAuthor = "new author";
    int newQuantity = 150;
    double newPrice = 16.0;
    int newAntiqueReleaseYear = 150;

    updates.put("name", newName);
    updates.put("author", newAuthor);
    updates.put("quantity", newQuantity);
    updates.put("price", newPrice);
    updates.put("antiqueReleaseYear", newAntiqueReleaseYear);

    Book actual = book.withUpdatedFields(updates);
    Book expected = Fixture.book()
        .name(newName)
        .author(newAuthor)
        .quantity(newQuantity)
        .price(newPrice)
        .antiqueReleaseYear(newAntiqueReleaseYear)
        .build();
    assertThat(actual, is(expected));
  }

  @Test
  void shouldValidateRegularBookSuccessfully() {
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
