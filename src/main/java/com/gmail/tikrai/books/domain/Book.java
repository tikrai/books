package com.gmail.tikrai.books.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.util.Generated;
import com.gmail.tikrai.books.validators.NotAntiqueScienceBook;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Validation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@NotAntiqueScienceBook
public class Book {
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
  public Book(
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

  public BigDecimal totalPrice() {
    return totalAntiquePrice().map(Optional::of)
        .orElseGet(this::totalScienceJournalPrice)
        .orElseGet(this::totalRegularPrice);
  }

  private Optional<BigDecimal> totalAntiquePrice() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    return antiqueReleaseYear()
        .map(year -> price.multiply(BigDecimal.valueOf(quantity * (currentYear - year) / 10.0)));
  }

  private Optional<BigDecimal> totalScienceJournalPrice() {
    return scienceIndex().map(index -> price.multiply(BigDecimal.valueOf(quantity * index)));
  }

  private BigDecimal totalRegularPrice() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }

  public Book withUpdatedFields(Map<String, Object> updates) {
    Book book = this;
    for (Map.Entry<String, Object> update: updates.entrySet()) {
      book = book.withUpdatedField(update.getKey(), update.getValue());
    }
    return book;
  }

  private Book withUpdatedField(String fieldName, Object value) {
    Book book;

    try {
      switch (fieldName) {
        case "barcode":
          throw new ValidationException("Barcode can not be updated");
        case "name":
          book = withName((String) value);
          break;
        case "author":
          book = withAuthor((String) value);
          break;
        case "quantity":
          book = withQuantity((int) value);
          break;
        case "price":
          book = withPrice(BigDecimal.valueOf((double) value));
          break;
        case "antiqueReleaseYear":
          book = withAntiqueReleaseYear((int) value);
          break;
        case "scienceIndex":
          book = withScienceIndex((int) value);
          break;
        default:
          throw new ValidationException(String.format("Book has no field '%s'", fieldName));
      }
    } catch (ClassCastException e) {
      throw new ValidationException(String.format("Incorrect format of '%s' field", fieldName));
    }

    Validation.buildDefaultValidatorFactory().getValidator().validate(book).stream()
        .findFirst()
        .ifPresent(violation -> {
          throw new ValidationException(
            String.format("%s %s", violation.getPropertyPath().toString(), violation.getMessage())
          );
        });

    return book;
  }

  @JsonProperty("barcode")
  public String barcode() {
    return barcode;
  }

  @JsonProperty("name")
  public String name() {
    return name;
  }

  @JsonProperty("author")
  public String author() {
    return author;
  }

  @JsonProperty("quantity")
  public int quantity() {
    return quantity;
  }

  @JsonProperty("price")
  public BigDecimal price() {
    return price;
  }

  @JsonProperty("antiqueReleaseYear")
  public Optional<Integer> antiqueReleaseYear() {
    return Optional.ofNullable(antiqueReleaseYear);
  }

  @JsonProperty("scienceIndex")
  public Optional<Integer> scienceIndex() {
    return Optional.ofNullable(scienceIndex);
  }

  private Book withName(String name) {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  private Book withAuthor(String author) {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  private Book withQuantity(int quantity) {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  private Book withPrice(BigDecimal price) {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  private Book withAntiqueReleaseYear(int antiqueReleaseYear) {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }

  private Book withScienceIndex(int scienceIndex) {
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
    Book book = (Book) o;
    return quantity == book.quantity
        && Objects.equals(barcode, book.barcode)
        && Objects.equals(name, book.name)
        && Objects.equals(author, book.author)
        && Objects.equals(price, book.price)
        && Objects.equals(antiqueReleaseYear, book.antiqueReleaseYear)
        && Objects.equals(scienceIndex, book.scienceIndex);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }
}
