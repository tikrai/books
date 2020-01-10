package com.gmail.tikrai.books.repository;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.repository.rowmappers.BooksMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class BooksRepositoryTest {

  private JdbcTemplate db = mock(JdbcTemplate.class);
  private BooksRepository booksRepository = new BooksRepository(db);

  @Test
  void shouldFindBookByBarcode() {
    Book book = Fixture.book().build();
    when(db.query(anyString(), any(BooksMapper.class))).thenReturn(Collections.singletonList(book));

    Optional<Book> actual = booksRepository.findByBarcode("code123");

    assertEquals(Optional.of(book), actual);
    verify(db).query(anyString(), any(BooksMapper.class));
    verifyNoMoreInteractions(db);
  }

  @Test
  void shouldCreateBook() {
    Book book = Fixture.book().build();
    booksRepository.create(book);

    String sql = "INSERT INTO books (barcode, name, author, quantity, price, antique_release_year, "
        + "science_index) VALUES ('1234', 'Book Name', 'Book Author', 1, 11.1, null, null)";
    verify(db).update(sql);
    verifyNoMoreInteractions(db);
  }
}
