package com.gmail.tikrai.books.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.util.Generated;
import com.gmail.tikrai.books.validators.NotAntiqueScienceBook;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Validation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@NotAntiqueScienceBook
public class BookRequest {
  @Length(min = 2, max = 255)
  private final String barcode;

  @Length(min = 2, max = 255)
  private final String name;

  @Length(min = 2, max = 255)
  private final String author;

  @Min(1)
  private final int quantity;

  @Min(0)
  private final BigDecimal price;

  @Max(1900)
  private final Integer antiqueReleaseYear;

  @Range(min = 1, max = 10)
  private final Integer scienceIndex;

  @JsonCreator
  public BookRequest(
      String barcode,
      String name,
      String author,
      int quantity,
      BigDecimal price,
      Integer antiqueReleaseYear,
      Integer scienceIndex
  ) {
    this.barcode = barcode;
    this.name = name;
    this.author = author;
    this.quantity = quantity;
    this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
    this.antiqueReleaseYear = antiqueReleaseYear;
    this.scienceIndex = scienceIndex;
  }

  public static BookRequest of(Book book) {
    return new BookRequest(
        book.barcode(),
        book.name(),
        book.author(),
        book.quantity(),
        book.price(),
        book.antiqueReleaseYear().orElse(null),
        book.scienceIndex().orElse(null)
    );
  }

  public BookRequest withUpdatedFields(Map<String, Object> updates) {
    BookRequest bookRequest = this;
    for (Map.Entry<String, Object> update: updates.entrySet()) {
      bookRequest = bookRequest.withUpdatedField(update.getKey(), update.getValue());
    }
    return bookRequest;
  }

  private BookRequest withUpdatedField(String fieldName, Object value) {
    BookRequest bookRequest;

    try {
      switch (fieldName) {
        case "barcode":
          throw new ValidationException("Barcode can not be updated");
        case "name":
          bookRequest = withName((String) value);
          break;
        case "author":
          bookRequest = withAuthor((String) value);
          break;
        case "quantity":
          bookRequest = withQuantity((int) value);
          break;
        case "price":
          bookRequest = withPrice(BigDecimal.valueOf((double) value));
          break;
        case "antiqueReleaseYear":
          bookRequest = withAntiqueReleaseYear((int) value);
          break;
        case "scienceIndex":
          bookRequest = withScienceIndex((int) value);
          break;
        default:
          throw new ValidationException(String.format("Book has no field '%s'", fieldName));
      }
    } catch (ClassCastException e) {
      throw new ValidationException(String.format("Incorrect format of '%s' field", fieldName));
    }

    Validation.buildDefaultValidatorFactory().getValidator().validate(bookRequest).stream()
        .findFirst()
        .ifPresent(violation -> {
          throw new ValidationException(
              String.format("%s %s", violation.getPropertyPath().toString(), violation.getMessage())
          );
        });

    return bookRequest;
  }

  private BookRequest withName(String name) {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }

  private BookRequest withAuthor(String author) {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }

  private BookRequest withQuantity(int quantity) {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }

  private BookRequest withPrice(BigDecimal price) {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }

  private BookRequest withAntiqueReleaseYear(int antiqueReleaseYear) {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }

  private BookRequest withScienceIndex(int scienceIndex) {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }

  public Optional<Integer> antiqueReleaseYear() {
    return Optional.ofNullable(antiqueReleaseYear);
  }

  public Optional<Integer> scienceIndex() {
    return Optional.ofNullable(scienceIndex);
  }

  public Book toDomain() {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookRequest request = (BookRequest) o;
    return quantity == request.quantity
        && Objects.equals(barcode, request.barcode)
        && Objects.equals(name, request.name)
        && Objects.equals(author, request.author)
        && Objects.equals(price, request.price)
        && Objects.equals(antiqueReleaseYear, request.antiqueReleaseYear)
        && Objects.equals(scienceIndex, request.scienceIndex);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }
}
