package com.gmail.tikrai.books.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ResourceNotFoundException;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.repository.BooksRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
  }

  @Test
  void shouldNotFindByBarcode() {
    Book book = Fixture.book().build();
    when(booksRepository.findByBarcode(book.barcode())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        ResourceNotFoundException.class,
        () -> booksService.findByBarcode(book.barcode())
    );
  }

  @Test
  void shouldGetTotalPrice() {
    Book book = Fixture.book().build();
    when(booksRepository.findByBarcode(book.barcode())).thenReturn(Optional.of(book));

    Double actual = booksService.getTotalPrice(book.barcode());
    assertThat(actual, is(book.totalPrice()));

  }

  @Test
  void shouldCreateBook() {
    Book book = Fixture.book().build();
    when(booksRepository.create(book)).thenReturn(book);

    Book actual = booksService.create(book);
    assertThat(actual, is(book));
  }
}
