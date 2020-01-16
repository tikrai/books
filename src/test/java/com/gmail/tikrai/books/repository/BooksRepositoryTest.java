package com.gmail.tikrai.books.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BooksRepositoryTest {

  private final ObjectMapper mapper = mock(ObjectMapper.class);
  private final String fileName = "filename";
  private final BooksRepository booksRepository = new BooksRepository(mapper, fileName);
  private final Book book = Fixture.book().build();
  private final Book[] books = {book};

  @Test
  void shouldFindAllBooks() throws IOException {
    when(mapper.readValue(any(File.class), eq(Book[].class))).thenReturn(books);

    List<Book> actual = booksRepository.findAll();

    assertThat(actual, is(Collections.singletonList(book)));
    verify(mapper).readValue(any(File.class), eq(Book[].class));
    verifyNoMoreInteractions(mapper);
  }

  @Test
  void shouldFindBookByBarcode() throws IOException {
    when(mapper.readValue(any(File.class), eq(Book[].class))).thenReturn(books);

    Optional<Book> actual = booksRepository.findByBarcode(book.barcode());

    assertThat(actual, is(Optional.of(book)));
    verify(mapper).readValue(any(File.class), eq(Book[].class));
    verifyNoMoreInteractions(mapper);
  }

  @Test
  void shouldCreateBook() throws IOException {
    booksRepository.create(book);

    verify(mapper).writeValue(any(File.class), any());
    verifyNoMoreInteractions(mapper);
  }

  @Test
  void shouldUpdateBook() throws IOException  {
    booksRepository.update(book);

    verify(mapper).writeValue(any(File.class), any());
    verifyNoMoreInteractions(mapper);
  }
}
