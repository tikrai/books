package com.gmail.tikrai.books.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.response.TotalPriceResponse;
import com.gmail.tikrai.books.service.BooksService;
import java.util.AbstractMap;
import java.util.Map;
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
    verify(booksService).findByBarcode(book.barcode());
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldGetTotalPrice() {
    when(booksService.getTotalPrice(book.barcode())).thenReturn(book.totalPrice());

    ResponseEntity<TotalPriceResponse> actual = booksController.getTotalPrice(book.barcode());

    ResponseEntity<TotalPriceResponse> expected =
        new ResponseEntity<>(new TotalPriceResponse(book.totalPrice()), HttpStatus.OK);
    assertThat(actual, is(expected));
    verify(booksService).getTotalPrice(book.barcode());
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldCreateBook() {
    when(booksService.create(book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.create(book);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.CREATED);
    assertThat(actual, is(expected));
    verify(booksService).create(book);
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldUpdateBook() {
    when(booksService.update(book.barcode(), book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.update(book.barcode(), book);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.OK);
    assertThat(actual, is(expected));
    verify(booksService).update(book.barcode(), book);
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldUpdateBookField() {
    String fieldName = "name";
    String value = "New Name";
    Map.Entry<String, Object> update = new AbstractMap.SimpleEntry<>(fieldName, value);
    when(booksService.updateField(book.barcode(), fieldName, value)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.updateField(book.barcode(), update);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.OK);
    assertThat(actual, is(expected));
    verify(booksService).updateField(book.barcode(), fieldName, value);
    verifyNoMoreInteractions(booksService);
  }
}
