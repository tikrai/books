package com.gmail.tikrai.books.controller;

import static org.hamcrest.CoreMatchers.equalTo;
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

    assertThat(actual, equalTo(new ResponseEntity<>(book, HttpStatus.OK)));
    verify(booksService).findByBarcode(book.barcode());
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldGetTotalPrice() {
    when(booksService.getTotalPrice(book.barcode())).thenReturn(book.totalPrice());

    ResponseEntity<TotalPriceResponse> actual = booksController.getTotalPrice(book.barcode());

    ResponseEntity<TotalPriceResponse> expected =
        new ResponseEntity<>(new TotalPriceResponse(book.totalPrice()), HttpStatus.OK);
    assertThat(actual, equalTo(expected));
    verify(booksService).getTotalPrice(book.barcode());
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldCreateBook() {
    when(booksService.create(book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.create(bookRequest);

    assertThat(actual, equalTo(new ResponseEntity<>(book, HttpStatus.CREATED)));
    verify(booksService).create(book);
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldUpdateBook() {
    when(booksService.update(book.barcode(), book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.update(book.barcode(), bookRequest);

    assertThat(actual, equalTo(new ResponseEntity<>(book, HttpStatus.OK)));
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
    assertThat(message, equalTo("Modification of 'barcode' field is not allowed"));
    verifyNoMoreInteractions(booksService);
  }

  @Test
  void shouldUpdateBookField() {
    HashMap<String, Object> updates = new HashMap<String, Object>() {};
    when(booksService.updateRequest(book.barcode(), updates)).thenReturn(bookRequest);
    when(booksService.update(book.barcode(), book)).thenReturn(book);

    ResponseEntity<Book> actual = booksController.updateField(book.barcode(), updates);

    assertThat(actual, equalTo(new ResponseEntity<>(book, HttpStatus.OK)));
    verify(booksService).updateRequest(book.barcode(), updates);
    verify(booksService).update(book.barcode(), book);
    verifyNoMoreInteractions(booksService);
  }
}
