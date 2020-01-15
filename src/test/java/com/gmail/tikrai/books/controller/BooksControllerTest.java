package com.gmail.tikrai.books.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.request.BookRequest;
import com.gmail.tikrai.books.response.TotalPriceResponse;
import com.gmail.tikrai.books.service.BooksService;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BooksControllerTest {

  private final BooksService booksService = mock(BooksService.class);
  private final BooksController booksController = new BooksController(booksService);
  private final BookRequest bookRequest = Fixture.bookRequest().build();
  private final Book book = bookRequest.toDomain();

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

    ResponseEntity<Book> actual = booksController.create(bookRequest);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.CREATED);
    assertThat(actual, is(expected));
    verify(booksService).create(book);
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldUpdateBook() {
    when(booksService.update(book.barcode(), book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.update(book.barcode(), bookRequest);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.OK);
    assertThat(actual, is(expected));
    verify(booksService).update(book.barcode(), book);
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldFailToUpdateBookIfBarcodeModificationRequested() {
    String oldBarcode = "b000";

    String message = assertThrows(
        ValidationException.class,
        () -> booksController.update(oldBarcode, bookRequest)
    ).getMessage();
    assertThat(message, is("Modification of 'barcode' field is not allowed"));
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldUpdateBookField() {
    String fieldName = "name";
    String fieldValue = "New Name";
    HashMap<String, Object> updates = new HashMap<String, Object>() {
      {
        put(fieldName, fieldValue);
      }
    };
    when(booksService.updateFields(book.barcode(), updates)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.updateField(book.barcode(), updates);

    ResponseEntity<Book> expected = new ResponseEntity<>(book, HttpStatus.OK);
    assertThat(actual, is(expected));
    verify(booksService).updateFields(book.barcode(), updates);
    verifyNoMoreInteractions(booksService);
  }
}
