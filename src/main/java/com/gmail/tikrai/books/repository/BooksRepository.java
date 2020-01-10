package com.gmail.tikrai.books.repository;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.repository.rowmappers.BooksMapper;
import java.util.Objects;
import java.util.Optional;
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

  public Optional<Book> findByBarcode(String barcode) {
    String sql = String.format("SELECT * FROM %s WHERE barcode = '%s'", TABLE, barcode);
    return db.query(sql, new BooksMapper()).stream().filter(Objects::nonNull).findFirst();
  }

  public Book create(Book book) {
    String sql = String.format(
        "INSERT INTO %s ("
            + "barcode, name, author, quantity, price"
            + ", antique_release_year, science_index"
            + ") VALUES ('%s', '%s', '%s', %s, %s, %s, %s)",
        TABLE,
        book.barcode(),
        book.name(),
        book.author(),
        book.quantity(),
        book.price(),
        book.antiqueReleaseYear().orElse(null),
        book.scienceIndex().orElse(null)
    );
    db.update(sql);

    return book;
  }
}
