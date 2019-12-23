package com.ladino.simpletest.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

  /**
   * Private constructor since this is an utility class
   */
  private Utils() {

  }

  public static String generateUUID() {
    String uuid = UUID.randomUUID().toString();
    return uuid;
  }

  public static Double generateRandomAmount() {
    return ThreadLocalRandom.current().nextDouble(0.01d, 999.99d);
  }

  public static LocalDate DateToLocalDate(Date d) {
    Instant instant = d.toInstant();

    LocalDate localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();

    return localDate;
  }
}

