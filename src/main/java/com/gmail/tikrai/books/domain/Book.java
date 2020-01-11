package com.gmail.tikrai.books.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.tikrai.books.Generated;
import com.gmail.tikrai.books.exception.ValidationException;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

public class Book {
  private final String barcode;
  private final String name;
  private final String author;
  private final int quantity;
  private final double price;
  private final Integer antiqueReleaseYear;
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

    if (antiqueReleaseYear != null && scienceIndex != null ) {
      throw new ValidationException("Book cannot be both antique and science journal");
    }
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
