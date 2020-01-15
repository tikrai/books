package com.gmail.tikrai.books.fixture.request;

import com.gmail.tikrai.books.request.BookRequest;
import java.math.BigDecimal;

public class BookRequestFixture {
  private String barcode = "1234";
  private String name = "Book Name";
  private String author = "Book Author";
  private int quantity = 2;
  private BigDecimal price = BigDecimal.valueOf(11.10);
  private Integer antiqueReleaseYear = null;
  private Integer scienceIndex = null;

  public BookRequestFixture barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  public BookRequestFixture name(String name) {
    this.name = name;
    return this;
  }

  public BookRequestFixture author(String author) {
    this.author = author;
    return this;
  }

  public BookRequestFixture quantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public BookRequestFixture price(double price) {
    this.price = BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP);
    return this;
  }

  public BookRequestFixture antiqueReleaseYear(Integer antiqueReleaseYear) {
    this.antiqueReleaseYear = antiqueReleaseYear;
    return this;
  }

  public BookRequestFixture scienceIndex(Integer scienceIndex) {
    this.scienceIndex = scienceIndex;
    return this;
  }

  public BookRequest build() {
    return new BookRequest(
        barcode, name, author, quantity, price, antiqueReleaseYear, scienceIndex
    );
  }
}
