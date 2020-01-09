package com.gmail.tikrai.books.service;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.repository.BooksRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BooksService {
  private final BooksRepository booksRepository;

  @Autowired
  public BooksService(BooksRepository booksRepository) {
    this.booksRepository = booksRepository;
  }

  public List<Book> findAll() {
    return booksRepository.findAll();
  }
}
