package com.gmail.tikrai.books.repository;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.repository.rowmappers.BooksMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BooksRepository {

  public static final String TABLE = "books";
  private static final String NAME = "name";
  private static final String AUTHOR = "author";
  private static final String QUANTITY = "quantity";
  private static final String PRICE = "price";
  private static final String ANTIQUE_RELEASE_YEAR = "antique_release_year";
  private static final String SCIENCE_INDEX = "science_index";
  private static final String DATA_FIELDS = String.join(", ",
      NAME, AUTHOR, QUANTITY, PRICE, ANTIQUE_RELEASE_YEAR, SCIENCE_INDEX);
  private final JdbcTemplate db;

  @Autowired
  public BooksRepository(JdbcTemplate db) {
    this.db = db;
  }

  public List<Book> findAll() {
    String sql = String.format("SELECT * FROM %s", TABLE);
    return db.query(sql, new BooksMapper());
  }

  public Optional<Book> findByBarcode(String barcode) {
    String sql = String.format("SELECT * FROM %s WHERE barcode = '%s'", TABLE, barcode);
    return db.query(sql, new BooksMapper()).stream().filter(Objects::nonNull).findFirst();
  }

  public Book create(Book book) {
    String sql = String.format(
        "INSERT INTO %s VALUES ('%s', '%s', '%s', %s, %s, %s, %s)",
        TABLE,
        book.barcode(),
        book.name(),
        book.author(),
        book.quantity(),
        book.price().unscaledValue(),
        book.antiqueReleaseYear().orElse(null),
        book.scienceIndex().orElse(null)
    );
    db.update(sql);
    return book;
  }

  public Book update(Book book) {
    String sql = String.format(
        "UPDATE %s SET (%s) = ('%s', '%s', %d, %d, %d, %d) WHERE barcode = '%s'",
        TABLE,
        DATA_FIELDS,
        book.name(),
        book.author(),
        book.quantity(),
        book.price().unscaledValue(),
        book.antiqueReleaseYear().orElse(null),
        book.scienceIndex().orElse(null),
        book.barcode()
    );
    db.update(sql);
    return book;
  }
}
