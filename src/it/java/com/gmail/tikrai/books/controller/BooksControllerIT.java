package com.gmail.tikrai.books.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.gmail.tikrai.books.IntegrationTestCase;
import com.gmail.tikrai.books.domain.Book;
import com.gmail.tikrai.books.fixture.Fixture;
import com.gmail.tikrai.books.repository.BooksRepository;
import com.gmail.tikrai.books.response.TotalPriceResponse;
import com.gmail.tikrai.books.util.RestUtil.Endpoint;
import com.jayway.restassured.response.Response;
import java.util.Collections;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class BooksControllerIT extends IntegrationTestCase {

  @Autowired
  BooksRepository booksRepository;

  private final Book book = Fixture.book().build();
  private final String barcodePath = String.format("%s/%s", Endpoint.BOOKS, book.barcode());

  @Test
  void shouldGetBookByBarcode() {
    booksRepository.create(book);

    Response response = given().get(barcodePath);

    response.then().statusCode(HttpStatus.OK.value());
    assertThat(response.as(Book.class), equalTo(book));
  }

  @Test
  void shouldFailToGetBookByBarcodeIfBarcodeDoNotExist() {
    Response response = given().get(barcodePath);

    String expectedMessage = String.format("Book with barcode '%s' was not found", book.barcode());
    response.then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("status", equalTo(404))
        .body("error", equalTo("Not Found"))
        .body("message", equalTo(expectedMessage))
        .body("path", equalTo(barcodePath));
  }

  @Test
  void shouldGetTotalBookPriceByBarcode() {
    booksRepository.create(book);

    Response response = given().get(barcodePath.concat("/total-price"));

    response.then().statusCode(HttpStatus.OK.value());
    TotalPriceResponse expected = new TotalPriceResponse(book.totalPrice());
    assertThat(response.as(TotalPriceResponse.class), equalTo(expected));
  }

  @Test
  void shouldFailToGetTotalBookPriceByBarcode() {
    Response response = given().get(barcodePath.concat("/total-price"));

    response.then().statusCode(HttpStatus.NOT_FOUND.value());
    String expectedMessage = String.format("Book with barcode '%s' was not found", book.barcode());
    response.then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("status", equalTo(404))
        .body("error", equalTo("Not Found"))
        .body("message", equalTo(expectedMessage))
        .body("path", equalTo(barcodePath.concat("/total-price")));
  }

  @Test
  void shouldCreateBook() {
    Response response = given().body(book).post(Endpoint.BOOKS);

    response.then().statusCode(HttpStatus.CREATED.value());
    assertThat(response.as(Book.class), equalTo(book));
    assertThat(booksRepository.findAll(), equalTo(Collections.singletonList(book)));
  }

  @Test
  void shouldFailToCreateBookIfRequestIsInvalid() {
    Book newBook = Fixture.book().name("a").build();

    Response response = given().body(newBook).post(Endpoint.BOOKS);

    response.then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("status", equalTo(400))
        .body("error", equalTo("Bad Request"))
        .body("message", equalTo("Validation failed for object='book'. Error count: 1"))
        .body("path", equalTo(Endpoint.BOOKS));
    assertThat(booksRepository.findAll(), equalTo(Collections.emptyList()));
  }

  @Test
  void shouldFailToCreateBookIfBarcodeExists() {
    booksRepository.create(book);
    Book newBook = Fixture.book().name("New name").build();

    Response response = given().body(newBook).post(Endpoint.BOOKS);

    String expectedMessage = String.format("Book with barcode '%s' already exists", book.barcode());
    response.then()
        .statusCode(HttpStatus.CONFLICT.value())
        .body("status", equalTo(409))
        .body("error", equalTo("Conflict"))
        .body("message", equalTo(expectedMessage))
        .body("path", equalTo(Endpoint.BOOKS));
    assertThat(booksRepository.findAll(), equalTo(Collections.singletonList(book)));
  }

  @Test
  void shouldUpdateBook() {
    booksRepository.create(book);
    Book updated = Fixture.book().author("new author").build();

    Response response = given().body(updated).put(barcodePath);

    response.then().statusCode(HttpStatus.OK.value());
    assertThat(response.as(Book.class), equalTo(updated));
    assertThat(booksRepository.findAll(), equalTo(Collections.singletonList(updated)));
  }

  @Test
  void shouldFailToUpdateBookIfRequestIsInvalid() {
    booksRepository.create(book);
    Book newBook = Fixture.book().name("a").build();

    Response response = given().body(newBook).put(barcodePath);

    response.then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("status", equalTo(400))
        .body("error", equalTo("Bad Request"))
        .body("message", equalTo("Validation failed for object='book'. Error count: 1"))
        .body("path", equalTo(barcodePath));
    assertThat(booksRepository.findAll(), equalTo(Collections.singletonList(book)));
  }

  @Test
  void shouldFailToUpdateBookIfBarcodeDoNotExist() {
    Book updated = Fixture.book().author("new author").build();

    Response response = given().body(updated).put(barcodePath);

    String expectedMessage = String.format("Book with barcode '%s' was not found", book.barcode());
    response.then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("status", equalTo(404))
        .body("error", equalTo("Not Found"))
        .body("message", equalTo(expectedMessage))
        .body("path", equalTo(barcodePath));
    assertThat(booksRepository.findAll(), equalTo(Collections.emptyList()));
  }

  @Test
  void shouldUpdateBookFields() {
    booksRepository.create(book);
    String newName = "new name";
    String newAuthor = "new author";
    HashMap<String, Object> updates = new HashMap<String, Object>() {
      {
        put("name", newName);
        put("author", newAuthor);
      }
    };

    Response response = given().body(updates).patch(barcodePath);

    Book updated = Fixture.book().name(newName).author(newAuthor).build();
    response.then().statusCode(HttpStatus.OK.value());
    assertThat(response.as(Book.class), equalTo(updated));
    assertThat(booksRepository.findAll(), equalTo(Collections.singletonList(updated)));
  }

  @Test
  void shouldFailToUpdateBookFieldsIfRequestIsInvalid() {
    booksRepository.create(book);
    String badName = "a";
    HashMap<String, Object> updates = new HashMap<String, Object>() {
      {
        put("name", badName);
      }
    };

    Response response = given().body(updates).patch(barcodePath);

    response.then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("status", equalTo(400))
        .body("error", equalTo("Bad Request"))
        .body("message", equalTo("name length must be between 2 and 255"))
        .body("path", equalTo(barcodePath));
    assertThat(booksRepository.findAll(), equalTo(Collections.singletonList(book)));
  }

  @Test
  void shouldFailToUpdateBookFieldsIfBarcodeDoNotExist() {
    String newName = "new name";
    String newAuthor = "new author";
    HashMap<String, Object> updates = new HashMap<String, Object>() {
      {
        put("name", newName);
        put("author", newAuthor);
      }
    };

    Response response = given().body(updates).patch(barcodePath);

    String expectedMessage = String.format("Book with barcode '%s' was not found", book.barcode());
    response.then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("status", equalTo(404))
        .body("error", equalTo("Not Found"))
        .body("message", equalTo(expectedMessage))
        .body("path", equalTo(barcodePath));
    assertThat(booksRepository.findAll(), equalTo(Collections.emptyList()));
  }
}
