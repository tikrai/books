package com.gmail.tikrai.books.fixture;

import com.gmail.tikrai.books.domain.Book;

public class BookFixture {

  private String barcode = "1234";
  private String name = "Book Name";
  private String author = "Book Author";
  private int quantity = 1;
  private double price = 11.1;
  private Integer antiqueReleaseYear = null;
  private Integer scienceIndex = null;

  public BookFixture barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  public BookFixture name(String name) {
    this.name = name;
    return this;
  }

  public BookFixture author(String author) {
    this.author = author;
    return this;
  }

  public BookFixture quantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public BookFixture price(double price) {
    this.price = price;
    return this;
  }

  public BookFixture antiqueReleaseYear(Integer antiqueReleaseYear) {
    this.antiqueReleaseYear = antiqueReleaseYear;
    return this;
  }

  public BookFixture scienceIndex(Integer scienceIndex) {
    this.scienceIndex = scienceIndex;
    return this;
  }

  public Book build() {
    return new Book(barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex);
  }
}
