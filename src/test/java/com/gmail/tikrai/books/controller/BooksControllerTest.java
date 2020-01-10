package com.gmail.tikrai.books.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.service.BooksService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BooksControllerTest {

  private final BooksService booksService = mock(BooksService.class);
  private final BooksController booksController = new BooksController(booksService);
  private final Book book = Fixture.book().build();
dfbtbsdt
  @Test
  void shouldFindByBarcode() {
    when(booksService.findByBarcode(book.barcode())).thenReturn(book);

    ResponseEntity<Book> actual = booksController.findByBarcode(book.barcode());

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.OK);
    assertEquals(expected, actual);
  }

  @Test
  void shouldCreateBook() {
    when(booksService.create(book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.create(book);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.CREATED);
    assertEquals(expected, actual);
  }
}
