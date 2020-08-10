package br.com.magalu.challenger.commons.utils;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class WorkingDate {

  public static Calendar getCalendarFromTimstamp(Timestamp timestamp) {
    return GregorianCalendar.from(
        ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp.getSeconds()), ZoneId.systemDefault()));
  }

  public static Timestamp getTimestampFromCalendar(Calendar calendar) {
    return Timestamp.newBuilder().setSeconds(calendar.toInstant().toEpochMilli()).build();
  }
}
