package com.inditex.randomization.common;

import java.util.function.Predicate;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;

/**
 * Enable registration of custom randomizers within the execution of a test.
 *
 * <p>A test class can implement this interface to provide their own random data targeting certain properties:</p>
 *
 * <pre class="code">
 * <code class="java">
 *
 * <b>&#064;ExtendWith(RandomizerExtension.class)</b>
 * public class ExampleTest implements CustomRandomFieldProvider {
 *
 *   &#064;Override
 *   public void registerCustomRandomizers(CustomRandomizerRegistry registry) {
 *     // only positive integers
 *     registry.registerRandomizer(Integer.class, new IntegerRangeRandomizer(1, 10_000));
 *   }
 * }
 * </code>
 * </pre>
 *
 * <p>Additionally an application can register randomizers globally (i.e. for every test) by means of the Java Service Provider Interface
 * (SPI). For that purpose it will be enough to provide an additional file inside the resource directory META-INF/services. The file name
 * will be the fully-qualified name of the SPI:</p>
 *
 * <pre>
 *  resources
 *    └── META-INF
 *       └── services
 *          └── com.inditex.amigafwk.test.randomizer.CustomRandomFieldProvider
 * </pre>
 *
 * <p>The contents of the file will be one line with the fully-qualified name of every implementation (or service provider) available.</p>
 *
 * <p>Find below some common use cases:</p>
 *
 * <pre class="code">
 * <code class="java">
 *
 * <b>&#064;ExtendWith(RandomizerExtension.class)</b>
 * public class ExampleTest implements CustomRandomFieldProvider {
 *
 *   &#064;Override
 *   public void registerCustomRandomizers(CustomRandomizerRegistry registry) {
 *     // enum constants stringified
 *     registry.registerRandomizer(
 *       StringField.named("state"),
 *       StringRandomizerFactory.ofEnum(State.class)
 *     );
 *
 *     // date fields formatted as strings
 *     registry.registerRandomizer(
 *       StringField.named("created_at"),
 *       StringRandomizerFactory.ofLocalDate()
 *     );
 *
 *     // nested fields can be specified with a dot; case doesn't matter
 *     registry.registerRandomizer(IntField.named("person.age"), new IntegerRangeRandomizer(18, 65));
 *
 *     // constant value (so much for randomness)
 *     registry.registerRandomizer(IntField.named("month"), new ConstantRandomizer(3));
 *
 *     // random value within a set of constants
 *     registry.registerRandomizer(StringField.named("country"), new ChoiceRandomizer("en", "es", "fr", "de"));
 *   }
 * }
 * </code>
 * </pre>
 *
 * @see Randomizer
 * @see StringRandomizerFactory
 */
public interface CustomRandomFieldProvider {

  /**
   * Register one or more randomizers.
   *
   * @param registry registry of custom randomizers provided by the underlying implementation.
   * @see CustomRandomizerRegistry#registerRandomizer(Predicate, Randomizer)
   * @see CustomRandomizerRegistry#registerRandomizer(Class, Randomizer)
   */
  void registerCustomRandomizers(CustomRandomizerRegistry registry);

  default String getDescription() {
    return this.getClass().getSimpleName();
  }

}
