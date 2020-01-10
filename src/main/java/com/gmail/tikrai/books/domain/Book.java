package com.gmail.tikrai.books.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public class Book {
  private final String barcode;
  private final String name;
  private final String author;
  private final int quantity;
  private final double price;
  private final Integer antiqueReleaseYear;
  private final Integer scienceIndex;

  public Book(String barcode, String name, String author, int quantity, double price) {
    this.barcode = barcode;
    this.name = name;
    this.author = author;
    this.quantity = quantity;
    this.price = price;
    this.antiqueReleaseYear = null;
    this.scienceIndex = null;
  }

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

  public Book withAntiqueReleaseYear(int antiqueReleaseYear) {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, null);
  }

  public Book withScienceIndex(int scienceIndex) {
    return new Book(barcode, name, author, quantity, price, null, scienceIndex);
  }
}
