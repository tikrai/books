package com.gmail.tikrai.books.controller;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.service.BooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BooksController {

  private final BooksService booksService;

  public BooksController(BooksService booksService) {
    this.booksService = booksService;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> findByBarcode(
      @RequestParam String barcode
  ) {
    return new ResponseEntity<>(booksService.findByBarcode(barcode), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> create(
      @RequestBody Book book
  ) {
    System.out.println("Returning " + book);
    return new ResponseEntity<>(booksService.create(book), HttpStatus.CREATED);
  }
}
