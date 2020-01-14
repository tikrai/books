package com.gmail.tikrai.books.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.repository.rowmappers.BooksMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class BooksRepositoryTest {

  private final JdbcTemplate db = mock(JdbcTemplate.class);
  private final BooksRepository booksRepository = new BooksRepository(db);
  private final Book book = Fixture.book().build();

  @Test
  void shouldFindAllBooks() {
    when(db.query(anyString(), any(BooksMapper.class))).thenReturn(Collections.singletonList(book));

    List<Book> actual = booksRepository.findAll();

    assertThat(actual, is(Collections.singletonList(book)));
    verify(db).query(anyString(), any(BooksMapper.class));
    verifyNoMoreInteractions(db);
  }

  @Test
  void shouldFindBookByBarcode() {
    when(db.query(anyString(), any(BooksMapper.class))).thenReturn(Collections.singletonList(book));

    Optional<Book> actual = booksRepository.findByBarcode("code123");

    assertThat(actual, is(Optional.of(book)));
    verify(db).query(anyString(), any(BooksMapper.class));
    verifyNoMoreInteractions(db);
  }

  @Test
  void shouldCreateBook() {
    booksRepository.create(book);

    String expectedQuery = "INSERT INTO books "
        + "VALUES ('1234', 'Book Name', 'Book Author', 2, 1110, null, null)";
    verify(db).update(expectedQuery);
    verifyNoMoreInteractions(db);
  }

  @Test
  void shouldUpdateBook() {
    booksRepository.update(book.barcode(), book);

    String expectedQuery = "UPDATE books "
        + "SET (name, author, quantity, price, antique_release_year, science_index) = "
        + "('Book Name', 'Book Author', 2, 1110, null, null) WHERE barcode = '1234'";
    verify(db).update(expectedQuery);
    verifyNoMoreInteractions(db);
  }
}
