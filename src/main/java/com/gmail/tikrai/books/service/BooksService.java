package com.gmail.tikrai.books.service;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ResourceNotFoundException;
import com.gmail.tikrai.books.repository.BooksRepository;
import java.util.Map;
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

  public Double getTotalPrice(String barcode) {
    return findByBarcode(barcode).totalPrice();
  }

  public Book create(Book book) {
    return booksRepository.create(book);
  }

  public Book update(String barcode, Book book) {
    return booksRepository.update(barcode, book);
  }

  public Book updateFields(String barcode, Map<String, Object> updates) {
    Book updated = findByBarcode(barcode).withUpdatedFields(updates);
    return update(barcode, updated);
  }
}
