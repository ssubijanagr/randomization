package com.inditex.randomization.instancio;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.inditex.randomization.common.CollectionNotTypedException;
import com.inditex.randomization.common.Random;

import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.lang.NonNull;

public class RandomizerExtension implements ParameterResolver, TestInstancePostProcessor {

  @Override
  public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
    return parameterContext.isAnnotated(Random.class);
  }

  @Override
  public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {

    final Parameter parameter = parameterContext.getParameter();
    final Class<?> parameterType = parameter.getType();
    final Random randomAnnotation = parameter.getAnnotation(Random.class);

    if (List.class.isAssignableFrom(parameterType)) {
      this.failOnMissingType(randomAnnotation, parameter);
      return RandomUtil.nextObjects(randomAnnotation.type(), randomAnnotation.size());
    } else if (Set.class.isAssignableFrom(parameterType)) {
      this.failOnMissingType(randomAnnotation, parameter);
      return iterableToSet(RandomUtil.nextObjects(randomAnnotation.type(), randomAnnotation.size()));
    } else if (Map.class.isAssignableFrom(parameterType)) {
      // The old RandomizerExtension generates empty maps, so we reproduce here the same behavior
      try {
        return parameterType.getDeclaredConstructor().newInstance();
      } catch (final Exception e) {
        return new HashMap();
      }
    } else {
      return RandomUtil.nextObject(parameterType);
    }
  }

  private Object resolve(final Class<?> targetType, final Random annotation) {
    if (targetType.isAssignableFrom(List.class)) {
      if (Void.TYPE.equals(annotation.type())) {
        throw new RuntimeException("No type defined for list");
      }
      return Instancio.ofList(annotation.type()).size(annotation.size()).create();
    } else if (targetType.isAssignableFrom(Set.class)) {
      if (Void.TYPE.equals(annotation.type())) {
        throw new RuntimeException("No type defined for set");
      }
      return Instancio.ofSet(annotation.type()).size(annotation.size()).create();
    } else {
      return Instancio.create(targetType);
    }
  }

  private static <T> Set<T> iterableToSet(@NonNull final Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toSet());
  }

  private void failOnMissingType(final Random randomAnnotation, final Parameter parameter) {
    if (Void.TYPE.equals(randomAnnotation.type())) {
      throw new CollectionNotTypedException();
    }
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
    final Field[] fields = testInstance.getClass().getDeclaredFields();

    for (final Field field : fields) {
      if (AnnotationSupport.isAnnotated(field, Random.class)) {
        final Random annotation = field.getAnnotation(Random.class);

        final Object randomObject = this.resolve(field.getType(), annotation);
        field.setAccessible(true);
        field.set(testInstance, randomObject);
      }
    }
  }
}
