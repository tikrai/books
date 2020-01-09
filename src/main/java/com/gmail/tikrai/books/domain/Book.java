package com.gmail.tikrai.books.domain;

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

  public String getBarcode() {
    return barcode;
  }

  public String getName() {
    return name;
  }

  public String getAuthor() {
    return author;
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
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, null);
  }

  public Book withScienceIndex(int scienceIndex) {
    return new Book(barcode, name, author, quantity, price, null, scienceIndex);
  }
}
