package com.gmail.tikrai.books.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ResourceNotFoundException;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.repository.BooksRepository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BooksServiceTest {

  private final BooksRepository booksRepository = mock(BooksRepository.class);
  private final BooksService booksService = new BooksService(booksRepository);

  @Test
  void shouldFindByBarcode() {
    Book book = Fixture.book().build();
    when(booksRepository.findByBarcode(book.barcode())).thenReturn(Optional.of(book));

    Book actual = booksService.findByBarcode(book.barcode());

    assertThat(actual, is(book));
    verify(booksRepository).findByBarcode(book.barcode());
    verifyNoMoreInteractions(booksRepository);
  }

  @Test
  void shouldNotFindByBarcode() {
    Book book = Fixture.book().build();
    when(booksRepository.findByBarcode(book.barcode())).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> booksService.findByBarcode(book.barcode())
    );
    verify(booksRepository).findByBarcode(book.barcode());
    verifyNoMoreInteractions(booksRepository);
  }

  @Test
  void shouldGetTotalPrice() {
    Book book = Fixture.book().build();
    when(booksRepository.findByBarcode(book.barcode())).thenReturn(Optional.of(book));

    BigDecimal actual = booksService.getTotalPrice(book.barcode());

    assertThat(actual, is(book.totalPrice()));
    verify(booksRepository).findByBarcode(book.barcode());
    verifyNoMoreInteractions(booksRepository);
  }

  @Test
  void shouldCreateBook() {
    Book book = Fixture.book().build();
    when(booksRepository.create(book)).thenReturn(book);

    Book actual = booksService.create(book);

    assertThat(actual, is(book));
    verify(booksRepository).create(book);
    verifyNoMoreInteractions(booksRepository);
  }

  @Test
  void shouldUpdateBook() {
    Book book = Fixture.book().build();
    when(booksRepository.update(book.barcode(), book)).thenReturn(book);

    Book actual = booksService.update(book.barcode(), book);

    assertThat(actual, is(book));
    verify(booksRepository).update(book.barcode(), book);
    verifyNoMoreInteractions(booksRepository);
  }

  @Test
  void shouldUpdateBookFields() {
    Book book = Fixture.book().build();
    when(booksRepository.findByBarcode(book.barcode())).thenReturn(Optional.of(book));
    when(booksRepository.update(book.barcode(), book)).thenReturn(book);
    HashMap<String, Object> updates = new HashMap<String, Object>() {{
      put("author", book.author());
    }};

    Book actual = booksService.updateFields(book.barcode(), updates);

    assertThat(actual, is(book));
    verify(booksRepository).findByBarcode(book.barcode());
    verify(booksRepository).update(book.barcode(), book);
    verifyNoMoreInteractions(booksRepository);
  }
}
