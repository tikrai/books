package com.gmail.tikrai.books.repository.rowmappers;

import com.gmail.tikrai.books.domain.Book;
import java.math.BigDecimal;
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
        BigDecimal.valueOf(rs.getInt("price"), 2),
        getInteger(rs, "antique_release_year"),
        getInteger(rs, "science_index")
    );
  }

  private Integer getInteger(ResultSet rs, String strColName) throws SQLException {
    int value = rs.getInt(strColName);
    return rs.wasNull() ? null : value;
  }
}
