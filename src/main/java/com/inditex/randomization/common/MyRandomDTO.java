package com.inditex.randomization.common;

public class MyRandomDTO {
  private final Integer first;

  private final long second;

  private final boolean third;

  public MyRandomDTO() {
    this.first = 0;
    this.second = 0;
    this.third = false;
  }

  public MyRandomDTO(final Integer first, final long second, final boolean third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public boolean isValid() {
    return this.first != null && this.first > 0;
  }
}
