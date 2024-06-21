package com.inditex.randomization.instancio;

import static org.instancio.Select.all;
import static org.instancio.Select.fields;
import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.instancio.settings.Keys.COLLECTION_MIN_SIZE;
import static org.instancio.settings.Keys.INTEGER_MAX;
import static org.instancio.settings.Keys.INTEGER_MIN;
import static org.instancio.settings.Keys.STRING_MAX_LENGTH;
import static org.instancio.settings.Keys.STRING_MIN_LENGTH;

import java.time.LocalDate;
import java.util.List;

import org.instancio.Instancio;
import org.instancio.settings.Settings;

public class RandomUtil {

  static final String ORGANIZATION_ID = "0001";

  static final String UNIVERSAL_ID = "1126";

  static final Integer PERIOD_ID = 4;

  static final LocalDate LD_2021_02_01 = LocalDate.of(2021, 2, 1);

  static final LocalDate LD_2022_01_31 = LocalDate.of(2022, 1, 31);

  private static final Settings SETTINGS = Settings.create()
      .set(STRING_MIN_LENGTH, 3).set(STRING_MAX_LENGTH, 5)
      .set(COLLECTION_MIN_SIZE, 3).set(COLLECTION_MAX_SIZE, 5)
      .set(INTEGER_MIN, 0).set(INTEGER_MAX, Integer.MAX_VALUE);

  /**
   * Next object.
   *
   * @param <T> the generic type
   * @param type the type
   * @return the t
   */
  public static <T> T nextObject(final Class<T> type) {
    return Instancio.of(type)
        .withSettings(SETTINGS)
        .set(fields(field -> ("organizationId").equals(field.getName())), ORGANIZATION_ID)
        .set(fields(field -> ("universalId").equals(field.getName())), UNIVERSAL_ID)
        .set(fields(field -> ("periodId").equals(field.getName())), PERIOD_ID)
        .supply(all(LocalDate.class),
            random -> LocalDate.ofEpochDay(random.longRange(LD_2021_02_01.toEpochDay(), LD_2022_01_31.toEpochDay())))
        .lenient().create();
  }

  /**
   * Next objects.
   *
   * @param <T> the generic type
   * @param type the type
   * @param streamSize the stream size
   * @return the list
   */
  public static <T> List<T> nextObjects(final Class<T> type, final int streamSize) {
    return Instancio.ofList(type)
        .size(streamSize)
        .withSettings(SETTINGS)
        .set(fields(field -> ("organizationId").equals(field.getName())), ORGANIZATION_ID)
        .set(fields(field -> ("universalId").equals(field.getName())), UNIVERSAL_ID)
        .set(fields(field -> ("periodId").equals(field.getName())), PERIOD_ID)
        .supply(all(LocalDate.class),
            random -> LocalDate.ofEpochDay(random.longRange(LD_2021_02_01.toEpochDay(), LD_2022_01_31.toEpochDay())))
        .lenient().create();
  }

}
