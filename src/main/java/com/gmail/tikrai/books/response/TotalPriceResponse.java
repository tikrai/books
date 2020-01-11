package com.gmail.tikrai.books.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.tikrai.books.Generated;
import java.util.Objects;

public class TotalPriceResponse {

  private final double totalPrice;

  @JsonCreator
  public TotalPriceResponse(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  @JsonProperty("totalPrice")
  public double totalPrice() {
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
    return Double.compare(that.totalPrice, totalPrice) == 0;
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(totalPrice);
  }
}
