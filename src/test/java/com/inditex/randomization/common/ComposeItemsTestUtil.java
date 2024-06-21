package com.inditex.randomization.common;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ComposeItemsTestUtil {

  public static Stream<Arguments> getRandomItems() {
    return Stream.of(
        Arguments.of(new MyRandomDTO(), false),
        Arguments.of(new MyRandomDTO(1, 12, false), true));
  }
}
