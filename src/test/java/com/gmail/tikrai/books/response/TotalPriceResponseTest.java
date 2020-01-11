package com.gmail.tikrai.books.response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tikrai.books.fixture.Fixture;
import org.junit.jupiter.api.Test;

class TotalPriceResponseTest {

  private final ObjectMapper mapper = Fixture.mapper();
  private final TotalPriceResponse price = new TotalPriceResponse(1.0);
  private final String priceJson = "{\"totalPrice\":1.0}";

  @Test
  void shouldSerializeRegularBook() throws JsonProcessingException {
    String serialized = mapper.writeValueAsString(price);
    assertThat(serialized, is(priceJson));
  }

  @Test
  void shouldDeserializeRegularBook() throws JsonProcessingException {
    TotalPriceResponse deserialized = mapper.readValue(priceJson, TotalPriceResponse.class);
    assertThat(deserialized, is(price));
  }
}
