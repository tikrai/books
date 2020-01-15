package com.gmail.tikrai.books.request;

import static com.gmail.tikrai.books.utils.Matchers.isOptionalOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.fixture.Fixture;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookRequestTest {

  private final BookRequest bookRequest = Fixture.bookRequest().build();
  private final HashMap<String, Object> updates = new HashMap<>();

  @BeforeEach
  void setup()  {
    updates.clear();
  }

  @Test
  void shouldValidateRegularBookSuccessfully() {
    assertThat(bookRequest.valid(), isOptionalOf(null));
  }

  @Test
  void shouldFailValidatingIfBarcodeIsNull() {
    BookRequest bookRequest = Fixture.bookRequest().barcode(null).build();
    assertThat(bookRequest.valid(), isOptionalOf("'barcode' cannot be null"));
  }

  @Test
  void shouldFailValidatingIfBarcodeIsTooShort() {
    BookRequest bookRequest = Fixture.bookRequest().barcode("a").build();
    assertThat(bookRequest.valid(), isOptionalOf("'barcode' length must be between 2 and 255"));
  }

  @Test
  void shouldFailValidatingIfNameIsNull() {
    BookRequest bookRequest = Fixture.bookRequest().name(null).build();
    assertThat(bookRequest.valid(), isOptionalOf("'name' cannot be null"));
  }

  @Test
  void shouldFailValidatingIfNameIsTooShort() {
    BookRequest bookRequest = Fixture.bookRequest().name("a").build();
    assertThat(bookRequest.valid(), isOptionalOf("'name' length must be between 2 and 255"));
  }

  @Test
  void shouldFailValidatingIfAuthorIsNull() {
    BookRequest bookRequest = Fixture.bookRequest().author(null).build();
    assertThat(bookRequest.valid(), isOptionalOf("'author' cannot be null"));
  }

  @Test
  void shouldFailValidatingIfAuthorIsTooShort() {
    BookRequest bookRequest = Fixture.bookRequest().author("a").build();
    assertThat(bookRequest.valid(), isOptionalOf("'author' length must be between 2 and 255"));
  }

  @Test
  void shouldFailValidatingIfQuantityIsNull() {
    BookRequest bookRequest = Fixture.bookRequest().quantity(null).build();
    assertThat(bookRequest.valid(), isOptionalOf("'quantity' cannot be null"));
  }

  @Test
  void shouldFailValidatingIfQuantityIsTooSmall() {
    BookRequest bookRequest = Fixture.bookRequest().quantity(-1).build();
    assertThat(bookRequest.valid(), isOptionalOf("'quantity' must be greater than or equal to 1"));
  }

  @Test
  void shouldFailValidatingIfPriceIsNull() {
    BookRequest bookRequest = Fixture.bookRequest().price(null).build();
    assertThat(bookRequest.valid(), isOptionalOf("'price' cannot be null"));
  }

  @Test
  void shouldFailValidatingIfPriceIsTooSmall() {
    BookRequest bookRequest = Fixture.bookRequest().price(-1.0).build();
    assertThat(bookRequest.valid(), isOptionalOf("'price' must be greater than or equal to 1"));
  }

  @Test
  void shouldFailValidatingAntiqueBookIfNotOldEnough() {
    BookRequest bookRequest = Fixture.bookRequest().antiqueReleaseYear(1999).build();
    String expected = "'antiqueReleaseYear' must be less than or equal to 1900";
    assertThat(bookRequest.valid(), isOptionalOf(expected));
  }

  @Test
  void shouldFailValidatingScienceJournalIfIndexIsTooBig() {
    BookRequest bookRequest = Fixture.bookRequest().scienceIndex(15).build();
    assertThat(bookRequest.valid(), isOptionalOf("'scienceIndex' must be between 1 and 10"));
  }

  @Test
  void shouldFailValidatingIfBookIsAntiqueAndScienceJournal() {
    BookRequest bookRequest = Fixture.bookRequest()
        .antiqueReleaseYear(1600)
        .scienceIndex(5).build();
    String expected = "Book cannot be both antique and science journal";
    assertThat(bookRequest.valid(), isOptionalOf(expected));
  }

  @Test
  void shouldFailToUpdateBarcodeField() {
    updates.put("barcode", "new barcode");
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, equalTo("Barcode can not be updated"));
  }

  @Test
  void shouldUpdateNameField() {
    String newName = "new name";
    updates.put("name", newName);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, equalTo(Fixture.bookRequest().name(newName).build()));
  }

  @Test
  void shouldUpdateAuthorField() {
    String newAuthor = "new author";
    updates.put("author", newAuthor);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, equalTo(Fixture.bookRequest().author(newAuthor).build()));
  }

  @Test
  void shouldUpdateQuantityField() {
    int newQuantity = 150;
    updates.put("quantity", newQuantity);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, equalTo(Fixture.bookRequest().quantity(newQuantity).build()));
  }

  @Test
  void shouldUpdatePriceFieldWithDoubleValue() {
    double newPrice = 16.0;
    updates.put("price", newPrice);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, equalTo(Fixture.bookRequest().price(newPrice).build()));
  }

  @Test
  void shouldUpdatePriceFieldWithIntegerValue() {
    int newPrice = 16;
    updates.put("price", newPrice);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, equalTo(Fixture.bookRequest().price(16.0).build()));
  }

  @Test
  void shouldUpdateAntiqueReleaseYearField() {
    int newAntiqueReleaseYear = 150;
    updates.put("antiqueReleaseYear", newAntiqueReleaseYear);

    BookRequest actual = bookRequest.withUpdatedFields(updates);

    BookRequest expected = Fixture.bookRequest().antiqueReleaseYear(newAntiqueReleaseYear).build();
    assertThat(actual, equalTo(expected));
  }

  @Test
  void shouldUpdateScienceIndexField() {
    int newScienceIndex = 9;
    updates.put("scienceIndex", newScienceIndex);
    BookRequest actual = bookRequest.withUpdatedFields(updates);
    assertThat(actual, equalTo(Fixture.bookRequest().scienceIndex(newScienceIndex).build()));
  }

  @Test
  void shouldFailToUpdateNonExistingField() {
    updates.put("model", "new model");
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, equalTo("Book has no field 'model'"));
  }

  @Test
  void shouldFailToUpdateProvidingDataOfWrongType() {
    updates.put("price", "1.0");
    String message = assertThrows(
        ValidationException.class,
        () -> bookRequest.withUpdatedFields(updates)
    ).getMessage();
    assertThat(message, equalTo("Incorrect format not 'price' field"));
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
    assertThat(actual, equalTo(expected));
  }
}
