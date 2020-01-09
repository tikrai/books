package com.gmail.tikrai.books.repository;

import com.gmail.tikrai.books.domain.Book;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BooksRepository {

  public List<Book> findAll() {           // todo remove this dummy method
    return Collections.singletonList(
        new Book("name", "author", "barcode", 1, 1.0)
    );
  }
}
