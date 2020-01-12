package com.gmail.tikrai.books.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.tikrai.books.Generated;
import com.gmail.tikrai.books.validators.NotAntiqueScienceBook;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
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
  private final double price;

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
      double price,
      Integer antiqueReleaseYear,
      Integer scienceIndex
  ) {
    this.barcode = barcode;
    this.name = name;
    this.author = author;
    this.quantity = quantity;
    this.price = price;
    this.antiqueReleaseYear = antiqueReleaseYear;
    this.scienceIndex = scienceIndex;
  }

  public double totalPrice() {
    return totalAntiquePrice().map(Optional::of)
        .orElseGet(this::totalScienceJournalPrice)
        .orElseGet(this::totalRegularPrice);
  }

  private Optional<Double> totalAntiquePrice() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    return antiqueReleaseYear().map(year -> quantity * price * (currentYear - year) / 10);
  }

  private Optional<Double> totalScienceJournalPrice() {
    return scienceIndex().map(index -> quantity * price * index);
  }

  private double totalRegularPrice() {
    return quantity * price;
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
  public double price() {
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
    return quantity == book.quantity &&
        Double.compare(book.price, price) == 0 &&
        Objects.equals(barcode, book.barcode) &&
        Objects.equals(name, book.name) &&
        Objects.equals(author, book.author) &&
        Objects.equals(antiqueReleaseYear, book.antiqueReleaseYear) &&
        Objects.equals(scienceIndex, book.scienceIndex);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }
}
