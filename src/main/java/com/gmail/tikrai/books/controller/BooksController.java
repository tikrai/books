package com.gmail.tikrai.books.controller;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.service.BooksService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BooksController {

  private final BooksService booksService;

  public BooksController(BooksService booksService) {
    this.booksService = booksService;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Book>> getAll() {
    return new ResponseEntity<>(booksService.findAll(), HttpStatus.OK);
  }
}
