package com.gmail.tikrai.books.domain;

import java.util.Optional;

public class Book {
  private final String name;
  private final String author;
  private final String barcode;
  private final int quantity;
  private final double price;
  private final Integer antiqueReleaseYear;
  private final Integer scienceIndex;

  public Book(String name, String author, String barcode, int quantity, double price) {
    this.name = name;
    this.author = author;
    this.barcode = barcode;
    this.quantity = quantity;
    this.price = price;
    this.antiqueReleaseYear = null;
    this.scienceIndex = null;
  }

  private Book(
      String name,
      String author,
      String barcode,
      int quantity,
      double price,
      Integer antiqueReleaseYear,
      Integer scienceIndex
  ) {
    this.name = name;
    this.author = author;
    this.barcode = barcode;
    this.quantity = quantity;
    this.price = price;
    this.antiqueReleaseYear = antiqueReleaseYear;
    this.scienceIndex = scienceIndex;
  }

  public String getName() {
    return name;
  }

  public String getAuthor() {
    return author;
  }

  public String getBarcode() {
    return barcode;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }

  public Optional<Integer> getAntiqueReleaseYear() {
    return Optional.ofNullable(antiqueReleaseYear);
  }

  public Optional<Integer> getScienceIndex() {
    return Optional.ofNullable(scienceIndex);
  }

  public Book withAntiqueReleaseYear(int antiqueReleaseYear) {
    return new Book(name, author, barcode, quantity, price, antiqueReleaseYear, null);
  }

  public Book withScienceIndex(int scienceIndex) {
    return new Book(name, author, barcode, quantity, price, null, scienceIndex);
  }
}
