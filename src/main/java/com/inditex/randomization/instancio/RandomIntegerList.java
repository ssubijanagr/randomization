package com.inditex.randomization.instancio;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.IntStream;

import com.inditex.randomization.instancio.RandomIntegerList.ListProvider;

import org.instancio.junit.Given;
import org.instancio.junit.GivenProvider;

@Given(ListProvider.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomIntegerList {

  int size();

  int minValue() default 0;

  int maxValue() default Integer.MAX_VALUE;

  class ListProvider implements GivenProvider {

    @Override
    public Object provide(ElementContext elementContext) {
      final RandomIntegerList list = elementContext.getAnnotation(RandomIntegerList.class);

      return IntStream.range(0, list
          .size())
          .mapToObj(i -> this.createRandomInt(elementContext)) // or x -> new Object(x).. or any other constructor
          .toList();
    }

    private Object createRandomInt(ElementContext elementContext) {
      final RandomIntegerList list = elementContext.getAnnotation(RandomIntegerList.class);
      return elementContext.random().intRange(list.minValue(), list.maxValue());
    }
  }
}
