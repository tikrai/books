package com.gmail.tikrai.books.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.tikrai.books.util.Generated;
import java.math.BigDecimal;
import java.util.Objects;

public class TotalPriceResponse {

  private final BigDecimal totalPrice;

  @JsonCreator
  public TotalPriceResponse(BigDecimal totalPrice) {
    this.totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @JsonProperty("totalPrice")
  public BigDecimal totalPrice() {
    return totalPrice;
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TotalPriceResponse that = (TotalPriceResponse) o;
    return Objects.equals(totalPrice, that.totalPrice);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(totalPrice);
  }
}
