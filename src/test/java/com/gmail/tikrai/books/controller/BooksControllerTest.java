package com.gmail.tikrai.books.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.response.TotalPriceResponse;
import com.gmail.tikrai.books.service.BooksService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BooksControllerTest {

  private final BooksService booksService = mock(BooksService.class);
  private final BooksController booksController = new BooksController(booksService);
  private final Book book = Fixture.book().build();

  @Test
  void shouldFindByBarcode() {
    when(booksService.findByBarcode(book.barcode())).thenReturn(book);

    ResponseEntity<Book> actual = booksController.findByBarcode(book.barcode());

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.OK);
    assertThat(actual, is(expected));

  }

  @Test
  void shouldGetTotalPrice() {
    when(booksService.getTotalPrice(book.barcode())).thenReturn(book.totalPrice());

    ResponseEntity<TotalPriceResponse> actual = booksController.getTotalPrice(book.barcode());

    ResponseEntity<TotalPriceResponse> expected =
        new ResponseEntity<>(new TotalPriceResponse(book.totalPrice()), HttpStatus.OK);
    assertThat(actual, is(expected));
  }

  @Test
  void shouldCreateBook() {
    when(booksService.create(book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.create(book);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.CREATED);
    assertThat(actual, is(expected));
  }
}
