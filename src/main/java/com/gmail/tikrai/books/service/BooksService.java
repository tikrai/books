package com.gmail.tikrai.books.service;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ResourceNotFoundException;
import com.gmail.tikrai.books.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BooksService {
  private final BooksRepository booksRepository;

  @Autowired
  public BooksService(BooksRepository booksRepository) {
    this.booksRepository = booksRepository;
  }

  public Book findByBarcode(String barcode) {
    return booksRepository.findByBarcode(barcode)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Book with barcode: %s was not found", barcode)
        ));
  }

  public Book create(Book book) {
    return booksRepository.create(book);
  }
}