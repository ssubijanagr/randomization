package com.inditex.randomization.instancio;

import java.util.List;

import com.inditex.randomization.common.MyRandomDTO;
import com.inditex.randomization.common.Random;

import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith({RandomizerExtension.class, InstancioExtension.class})
class InstancioTest {

  @Random
  private MyRandomDTO randomDTO;

  @Random
  private Integer integerDTO;

  @Test
  void shouldGenerateRandomItems(
      @Random Boolean booleanDTO,
      @Random MyRandomDTO otherRandomDTO,
      @Random(type = MyRandomDTO.class) final List<MyRandomDTO> randomDTOList) {
    Assertions.assertNotNull(this.integerDTO);
    Assertions.assertNotNull(this.randomDTO);
    Assertions.assertNotNull(booleanDTO);
    Assertions.assertNotNull(otherRandomDTO);
    Assertions.assertFalse(randomDTOList.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("com.inditex.randomization.common.ComposeItemsTestUtil#getRandomItems")
  void shouldGenerateMoreRandomItems(
      final MyRandomDTO object,
      final Boolean isValid,
      @Random final MyRandomDTO randomDTO,
      @Random(type = MyRandomDTO.class) final List<MyRandomDTO> randomDTOList) {
    Assertions.assertEquals(object.isValid(), isValid);
    Assertions.assertNotNull(randomDTO);
    Assertions.assertFalse(randomDTOList.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("com.inditex.randomization.common.ComposeItemsTestUtil#getRandomItems")
  void shouldGenerateMoreRandomItemsWithInstancioExtension(
      final MyRandomDTO object,
      final Boolean isValid,
      @Given final MyRandomDTO randomDTO,
      @Given final List<MyRandomDTO> randomDTOList) {
    Assertions.assertEquals(object.isValid(), isValid);
    Assertions.assertNotNull(randomDTO);
    Assertions.assertFalse(randomDTOList.isEmpty());
    Assertions.assertNotNull(randomDTOList.getFirst());
  }

  @ParameterizedTest
  @MethodSource("com.inditex.randomization.common.ComposeItemsTestUtil#getRandomItems")
  void shouldGenerateMoreRandomItemsWithInstancioExtensionAndCustomProvider(
      final MyRandomDTO object,
      final Boolean isValid,
      @Given final MyRandomDTO randomDTO,
      @RandomIntegerList(size = 10, minValue = 5, maxValue = 40) final List<Integer> randomIntegerList) {
    Assertions.assertEquals(object.isValid(), isValid);
    Assertions.assertNotNull(randomDTO);
    Assertions.assertEquals(randomIntegerList.size(), 10);
  }

}
