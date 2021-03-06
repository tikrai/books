package com.gmail.tikrai.books.controller;

import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.exception.ValidationException;
import com.gmail.tikrai.books.request.BookRequest;
import com.gmail.tikrai.books.response.TotalPriceResponse;
import com.gmail.tikrai.books.service.BooksService;
import com.gmail.tikrai.books.util.RestUtil.Endpoint;
import com.gmail.tikrai.books.validation.validators.SizeValidator;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.BOOKS)
public class BooksController {

  private final BooksService booksService;

  public BooksController(BooksService booksService) {
    this.booksService = booksService;
  }

  @GetMapping(value = "/{barcode}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> findByBarcode(
      @PathVariable String barcode
  ) {
    SizeValidator.range("Path variable barcode", barcode, 2, 255).validate();
    return new ResponseEntity<>(booksService.findByBarcode(barcode), HttpStatus.OK);
  }

  @GetMapping(value = "/{barcode}/total-price", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TotalPriceResponse> getTotalPrice(
      @PathVariable String barcode
  ) {
    SizeValidator.range("Path variable barcode", barcode, 2, 255).validate();
    TotalPriceResponse response = new TotalPriceResponse(booksService.getTotalPrice(barcode));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> create(
      @RequestBody BookRequest request
  ) {
    request.validate();
    return new ResponseEntity<>(booksService.create(request.toDomain()), HttpStatus.CREATED);
  }

  @PutMapping(value = "/{barcode}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> update(
      @PathVariable String barcode,
      @RequestBody BookRequest request
  ) {
    SizeValidator.range("Path variable barcode", barcode, 2, 255).validate();
    request.validate();
    Book book = request.toDomain();
    if (!book.barcode().equals(barcode)) {
      throw new ValidationException("Modification of 'barcode' field is not allowed");
    }
    return new ResponseEntity<>(booksService.update(book), HttpStatus.OK);
  }

  @PatchMapping(value = "/{barcode}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> updateField(
      @PathVariable("barcode") String barcode,
      @RequestBody Map<String, Object> updates
  ) {
    SizeValidator.range("Path variable barcode", barcode, 2, 255).validate();
    BookRequest request = booksService.updateRequest(barcode, updates);
    return update(barcode, request);
  }
}
