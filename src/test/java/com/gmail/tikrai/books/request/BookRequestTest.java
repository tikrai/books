package com.gmail.tikrai.books.request;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.fixture.Fixture;
import java.util.HashMap;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookRequestTest {
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final int releaseYear = 1600;
  private final int scienceIndex = 5;
  private final BookRequest bookRequest = Fixture.bookRequest().build();
  private final HashMap<String, Object> updates = new HashMap<>();

  @BeforeEach
  void setup()  {
    updates.clear();
  }

  @Test
  void shouldValidateRegularBookSuccessfully() {
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    assertThat(violations, empty());
  }

  @Test
  void shouldFailValidatingIfBarcodeIsTooShort() {
    BookRequest bookRequest = Fixture.bookRequest().barcode("a").build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "barcode", "length must be between 2 and 255");
  }

  @Test
  void shouldFailValidatingIfNameIsTooShort() {
    BookRequest bookRequest = Fixture.bookRequest().name("a").build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "name", "length must be between 2 and 255");
  }

  @Test
  void shouldFailValidatingIfAuthorIsTooShort() {
    BookRequest bookRequest = Fixture.bookRequest().author("a").build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "author", "length must be between 2 and 255");
  }

  @Test
  void shouldFailValidatingIfQuantityIsTooSmall() {
    BookRequest bookRequest = Fixture.bookRequest().quantity(-1).build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "quantity", "must be greater than or equal to 1");
  }

  @Test
  void shouldFailValidatingIfPriceIsTooSmall() {
    BookRequest bookRequest = Fixture.bookRequest().price(-1).build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "price", "must be greater than or equal to 0");
  }

  @Test
  void shouldFailValidatingAntiqueBookIfNotOldEnough() {
    BookRequest bookRequest = Fixture.bookRequest().antiqueReleaseYear(1999).build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "antiqueReleaseYear", "must be less than or equal to 1900");
  }

  @Test
  void shouldFailValidatingScienceJournalIfIndexIsTooBig() {
    BookRequest bookRequest = Fixture.bookRequest().scienceIndex(15).build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "scienceIndex", "must be between 1 and 10");
  }

  @Test
  void shouldFailValidatingIfBookIsAntiqueAndScienceJournal() {
    BookRequest bookRequest = Fixture.bookRequest()
        .antiqueReleaseYear(releaseYear)
        .scienceIndex(scienceIndex).build();
    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);
    validateViolations(violations, "", "Book cannot be both antique and science journal");
  }

  @Test
  void shouldFailToUpdateBarcodeField() {
    updates.put("barcode", "new barcode");
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("Barcode can not be updated"));
  }

  @Test
  void shouldUpdateNameField() {
    String newName = "new name";
    updates.put("name", newName);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.bookRequest().name(newName).build()));
  }

  @Test
  void shouldUpdateAuthorField() {
    String newAuthor = "new author";
    updates.put("author", newAuthor);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.bookRequest().author(newAuthor).build()));
  }

  @Test
  void shouldUpdateQuantityField() {
    int newQuantity = 150;
    updates.put("quantity", newQuantity);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.bookRequest().quantity(newQuantity).build()));
  }

  @Test
  void shouldUpdatePriceField() {
    double newPrice = 16.0;
    updates.put("price", newPrice);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.bookRequest().price(newPrice).build()));
  }

  @Test
  void shouldUpdateAntiqueReleaseYearField() {
    int newAntiqueReleaseYear = 150;
    updates.put("antiqueReleaseYear", newAntiqueReleaseYear);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.bookRequest().antiqueReleaseYear(newAntiqueReleaseYear).build()));
  }

  @Test
  void shouldUpdateScienceIndexField() {
    int newScienceIndex = 9;
    updates.put("scienceIndex", newScienceIndex);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, is(Fixture.bookRequest().scienceIndex(newScienceIndex).build()));
  }

  @Test
  void shouldFailToUpdateNonExistingField() {
    updates.put("model", "new model");
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("Book has no field 'model'"));
  }

  @Test
  void shouldFailToUpdateProvidingDataOfWrongType() {
    updates.put("price", "1.0");
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, is("Incorrect format of 'price' field"));
  }

  @Test
  void shouldFailToUpdateProvidingInvalidValue() {
    updates.put("price", -1.0);
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
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

    BookRequest actual = bookRequest.withUpdatedFields(updates);
    BookRequest expected = Fixture.bookRequest()
        .name(newName)
        .author(newAuthor)
        .quantity(newQuantity)
        .price(newPrice)
        .antiqueReleaseYear(newAntiqueReleaseYear)
        .build();
    assertThat(actual, is(expected));
  }

  private void validateViolations(
      Set<ConstraintViolation<BookRequest>> violations,
      String property,
      String message
  ) {
    assertThat(violations.size(), is(1));
    assertThat(violations.iterator().next().getPropertyPath().toString(), is(property));
    assertThat(violations.iterator().next().getMessage(), is(message));
  }
}
