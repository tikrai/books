package com.gmail.tikrai.books.repository;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.repository.rowmappers.BooksMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BooksRepository {

  private static final String TABLE = "books";

  private final JdbcTemplate db;

  @Autowired
  public BooksRepository(JdbcTemplate db) {
    this.db = db;
  }

  public List<Book> findAll() {
    String query = String.format("SELECT * FROM %s", TABLE);
    return db.query(query, new BooksMapper());
  }
}
