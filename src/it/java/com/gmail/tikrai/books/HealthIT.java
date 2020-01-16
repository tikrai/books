package com.gmail.tikrai.books;

import static org.hamcrest.Matchers.equalTo;

import com.gmail.tikrai.books.util.RestUtil.Endpoint;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class HealthIT extends IntegrationTestCase {

  @Test
  void shouldShowHealth() {
    Response response = given().get(Endpoint.HEALTH);

    response.then()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo("UP"))
        .body("components.db.status", equalTo("UP"))
        .body("components.diskSpace.status", equalTo("UP"))
        .body("components.ping.status", equalTo("UP"));
  }
}
