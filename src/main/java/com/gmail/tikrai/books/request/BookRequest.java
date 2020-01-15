package com.gmail.tikrai.books.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.util.Generated;
import com.gmail.tikrai.books.validation.Validator;
import com.gmail.tikrai.books.validation.ValidatorGroup;
import com.gmail.tikrai.books.validation.validators.NullValidator;
import com.gmail.tikrai.books.validation.validators.SizeValidator;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class BookRequest implements Validator {
  private final String barcode;
  private final String name;
  private final String author;
  private final Integer quantity;
  private final BigDecimal price;
  private final Integer antiqueReleaseYear;
  private final Integer scienceIndex;

  @JsonCreator
  public BookRequest(
      String barcode,
      String name,
      String author,
      Integer quantity,
      BigDecimal price,
      Integer antiqueReleaseYear,
      Integer scienceIndex
  ) {
    this.barcode = barcode;
    this.name = name;
    this.author = author;
    this.quantity = quantity;
    this.price = price == null ? null : price.setScale(2, BigDecimal.ROUND_HALF_UP);
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
          bookRequest = withPrice(bigDecimalOf(value));
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
      throw new ValidationException(String.format("Incorrect format not '%s' field", fieldName));
    }
    return bookRequest;
  }

  private BigDecimal bigDecimalOf(Object o) {
    double doubleValue;
    try {
      doubleValue = (double) o;
    } catch (ClassCastException e) {
      doubleValue = (int) o;
    }
    return BigDecimal.valueOf(doubleValue);
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

  public Book toDomain() {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  @Override
  public Optional<String> valid() {
    return ValidatorGroup.of(
        ValidatorGroup.of(
            NullValidator.not("barcode", barcode),
            SizeValidator.range("barcode", barcode, 2, 255)
        ),
        ValidatorGroup.of(
            NullValidator.not("name", name),
            SizeValidator.range("name", name, 2, 255)
        ),
        ValidatorGroup.of(
            NullValidator.not("author", author),
            SizeValidator.range("author", author, 2, 255)
        ),
        ValidatorGroup.of(
            NullValidator.not("quantity", quantity),
            SizeValidator.min("quantity", quantity, 1)
        ),
        ValidatorGroup.of(
            NullValidator.not("price", price),
            SizeValidator.min("price", price, BigDecimal.valueOf(1))
        ),
        SizeValidator.max("antiqueReleaseYear", antiqueReleaseYear, 1900),
        SizeValidator.range("scienceIndex", scienceIndex, 1, 10),
        NullValidator.min(
            new Object[]{antiqueReleaseYear, scienceIndex},
            1,
            "Book cannot be both antique and science journal"
        )
    ).valid();
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
    return Objects.equals(barcode, request.barcode)
        && Objects.equals(name, request.name)
        && Objects.equals(author, request.author)
        && Objects.equals(quantity, request.quantity)
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
