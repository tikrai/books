package com.gmail.tikrai.books.repository.rowmappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BooksMapperTest {

  private final ResultSet rs = mock(ResultSet.class);
  private final BooksMapper booksMapper = new BooksMapper();
  private Book book = Fixture.book().build();

  @BeforeEach
  void sutup() throws SQLException {
    when(rs.getString("barcode")).thenReturn(book.barcode());
    when(rs.getString("name")).thenReturn(book.name());
    when(rs.getString("author")).thenReturn(book.author());
    when(rs.getInt("quantity")).thenReturn(book.quantity());
    when(rs.getDouble("price")).thenReturn(book.price());
    when(rs.getInt("antique_release_year")).thenReturn(0);
  }

  @Test
  void shouldMapBookRowSuccessfully() throws SQLException {
    when(rs.getInt("science_index")).thenReturn(0);
    when(rs.wasNull()).thenReturn(true);

    Book actual = booksMapper.mapRow(rs, 0);

    assertEquals(book, actual);
  }

  @Test
  void shouldMapScienceJournalRowSuccessfully() throws SQLException {
    int scienceIndex = 5;
    book = Fixture.book().scienceIndex(scienceIndex).build();
    when(rs.getInt("science_index")).thenReturn(scienceIndex);
    when(rs.wasNull()).thenReturn(true).thenReturn(false);

    Book actual = booksMapper.mapRow(rs, 0);

    assertEquals(book, actual);
  }

  @AfterEach
  void verifyMocks() throws SQLException {
    verify(rs).getString("barcode");
    verify(rs).getString("name");
    verify(rs).getString("author");
    verify(rs).getInt("quantity");
    verify(rs).getDouble("price");
    verify(rs).getInt("antique_release_year");
    verify(rs).getInt("science_index");
    verify(rs, times(2)).wasNull();
    verifyNoMoreInteractions(rs);
  }
}
