package com.gmail.tikrai.books.service;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ResourceNotFoundException;
import com.gmail.tikrai.books.exception.UniqueIdentifierException;
import com.gmail.tikrai.books.repository.BooksRepository;
import com.gmail.tikrai.books.request.BookRequest;
import java.math.BigDecimal;
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
            String.format("Book with barcode '%s' was not found", barcode)
        ));
  }

  public BigDecimal getTotalPrice(String barcode) {
    return findByBarcode(barcode).totalPrice();
  }

  public Book create(Book book) {
    if (booksRepository.findByBarcode(book.barcode()).isPresent()) {
      throw new UniqueIdentifierException(
          String.format("Book with barcode '%s' already exists", book.barcode())
      );
    }
    return booksRepository.create(book);
  }

  public Book update(String barcode, Book book) {
    findByBarcode(barcode);
    return booksRepository.update(book);
  }

  public BookRequest updateRequest(String barcode, Map<String, Object> updates) {
    return BookRequest.of(findByBarcode(barcode)).withUpdatedFields(updates);
  }
}
