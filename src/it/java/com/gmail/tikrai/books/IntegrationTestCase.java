package com.gmail.tikrai.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.repository.BooksRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestCase {

  @Value("${local.server.port}")
  private int port;

  @Autowired
  protected BooksRepository booksRepository;

  @Autowired
  private ObjectMapper mapper;

  @Value("${spring.datasource.url}")
  String fileName;

  @BeforeEach
  public void setup() {
    RestAssured.port = port;
    flushData();
  }

  private void flushData() {
    try {
      File file = new File(fileName);
      mapper.writeValue(file, Collections.emptyList());
      booksRepository.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected RequestSpecification given() {
    return RestAssured.given().contentType(ContentType.JSON);
  }
}
