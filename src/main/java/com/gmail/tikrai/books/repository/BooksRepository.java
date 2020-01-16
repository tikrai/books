package com.gmail.tikrai.books.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.domain.Book;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BooksRepository {

  private final ObjectMapper mapper;
  private final File file;

  private final Map<String, Book> allBooks = new HashMap<>();

  @Autowired
  public BooksRepository(
      ObjectMapper mapper,
      @Value("${spring.datasource.url}") String fileName
  ) {
    this.mapper = mapper;
    this.file = new File(fileName);
  }

  public List<Book> findAll() {
    if (allBooks.isEmpty()) {
      readFromFile();
    }
    return new ArrayList<>(allBooks.values());
  }

  public Optional<Book> findByBarcode(String barcode) {
    if (allBooks.isEmpty()) {
      readFromFile();
    }
    return Optional.ofNullable(allBooks.get(barcode));
  }

  public Book create(Book book) {
    allBooks.put(book.barcode(), book);
    try {
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, allBooks.values());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return book;
  }

  public Book update(Book book) {
    return create(book);
  }

  public void flush() {
    allBooks.clear();
  }

  private void readFromFile() {
    try {
      Book[] books = mapper.readValue(file, Book[].class);
      Arrays.stream(books).forEach(book -> allBooks.put(book.barcode(), book));
    } catch (IOException e) {
      //no file - no problem
      System.out.print("");
    }
  }
}
