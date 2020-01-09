package com.gmail.tikrai.books.repository.rowmappers;

import com.gmail.tikrai.books.domain.Book;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class BooksMapper implements RowMapper<Book> {

  @Override
  public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Book(
        rs.getString("barcode"),
        rs.getString("name"),
        rs.getString("author"),
        rs.getInt("quantity"),
        rs.getDouble("price"),
        rs.getInt("antique_release_year"),
        rs.getInt("science_index")
    );
  }
}
