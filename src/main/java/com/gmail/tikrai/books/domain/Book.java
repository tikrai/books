package com.gmail.tikrai.books.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.tikrai.books.util.Generated;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

public class Book {
  private final String barcode;
  private final String name;
  private final String author;
  private final int quantity;
  private final BigDecimal price;
  private final Integer antiqueReleaseYear;
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
