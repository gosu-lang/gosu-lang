/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Utilities for java.util.Date objects. These utilities are not intended to be used directly; use the corresponding
 * enhancements on Date instead.
 */
public class GosuDateUtil {

  /**
   * Adds the specified (signed) amount of seconds to the given date. For
   * example, to subtract 5 seconds from the current time of the date, you can
   * achieve it by calling: <code>addSeconds(Date, -5)</code>.
   *
   * @param date     The time.
   * @param iSeconds The amount of seconds to add.
   *
   * @return A new date with the seconds added.
   */
  public static Date addSeconds(Date date, int iSeconds) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.SECOND, iSeconds);
    return dateTime.getTime();
  }

  /**
   * Adds the specified (signed) amount of minutes to the given date. For
   * example, to subtract 5 minutes from the current time of the date, you can
   * achieve it by calling: <code>addMinutes(Date, -5)</code>.
   *
   * @param date     The time.
   * @param iMinutes The amount of minutes to add.
   *
   * @return A new date with the minutes added.
   */
  public static Date addMinutes(Date date, int iMinutes ) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.MINUTE, iMinutes);
    return dateTime.getTime();
  }

  /**
   * Adds the specified (signed) amount of hours to the given date. For
   * example, to subtract 5 hours from the current date, you can
   * achieve it by calling: <code>addHours(Date, -5)</code>.
   *
   * @param date   The time.
   * @param iHours The amount of hours to add.
   *
   * @return A new date with the hours added.
   */
  public static Date addHours(Date date, int iHours) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.HOUR, iHours);
    return dateTime.getTime();
  }


  /**
   * Adds the specified (signed) amount of days to the given date. For
   * example, to subtract 5 days from the current date, you can
   * achieve it by calling: <code>addDays(Date, -5)</code>.
   *
   * @param date  The time.
   * @param iDays The amount of days to add.
   *
   * @return A new date with the days added.
   */
  public static Date addDays(Date date, int iDays) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.DATE, iDays);
    return dateTime.getTime();
  }

  /**
   * Adds the specified (signed) amount of weeks to the given date. For
   * example, to subtract 5 weeks from the current date, you can
   * achieve it by calling: <code>addWeeks(Date, -5)</code>.
   *
   * @param date   The time.
   * @param iWeeks The amount of weeks to add.
   *
   * @return A new date with the weeks added.
   */
  public static Date addWeeks(Date date, int iWeeks) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.WEEK_OF_YEAR, iWeeks);
    return dateTime.getTime();
  }

  /**
   * Adds the specified (signed) amount of months to the given date. For
   * example, to subtract 5 months from the current date, you can
   * achieve it by calling: <code>addMonths(Date, -5)</code>.
   *
   * @param date    The time.
   * @param iMonths The amount of months to add.
   *
   * @return A new date with the months added.
   */
  public static Date addMonths(Date date, int iMonths) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.MONTH, iMonths);
    return dateTime.getTime();
  }

  /**
   * Adds the specified (signed) amount of years to the given date. For
   * example, to subtract 5 years from the current date, you can
   * achieve it by calling: <code>addYears(Date, -5)</code>.
   *
   * @param date   The time.
   * @param iYears The amount of years to add.
   *
   * @return A new date with the years added.
   */
  public static Date addYears(Date date, int iYears) {
    Calendar dateTime = dateToCalendar(date);
    dateTime.add(Calendar.YEAR, iYears);
    return dateTime.getTime();
  }

  /**
   * Get the second of the time
   *
   * @param date The time.
   *
   * @return The second of the time, always in the range 0-59
   */
  public static int getSecond(Date date) {
    return dateToCalendar(date).get(Calendar.SECOND);
  }

  /**
   * Get the minute of the time
   *
   * @param date The time.
   *
   * @return The minute of the time, always in the range 0-59
   */
  public static int getMinute(Date date) {
    return dateToCalendar(date).get(Calendar.MINUTE);
  }

  /**
   * Get the hour of the time, base on a 12-hour clock.
   *
   * @param date The time.
   *
   * @return The hour of the time. Based on a 12-hour clock.
   */
  public static int getHour(Date date) {
    return dateToCalendar(date).get(Calendar.HOUR);
  }

  /**
   * Is the time AM?
   *
   * @param date The time.
   *
   * @return true if the time is AM, false otherwise
   */
  public static boolean isAM(Date date) {
    return dateToCalendar(date).get(Calendar.AM_PM) == Calendar.AM;
  }

  /**
   * Is the time PM?
   *
   * @param date The time.
   *
   * @return true if the time is PM, false otherwise
   */
  public static boolean isPM(Date date) {
    return dateToCalendar(date).get(Calendar.AM_PM) == Calendar.PM;
  }

  /**
   * Get the hour of the time, based on a 24-hour clock.
   *
   * @param date The time.
   *
   * @return The hour of the time. Based on a 24-hour clock.
   */
  public static int getHourOfDay(Date date) {
    return dateToCalendar(date).get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Get the day of week.
   *
   * @param date The date.
   *
   * @return The day of week. Sunday = 1, Monday = 2, ..., Saturday = 7.
   */
  public static int getDayOfWeek(Date date) {
    return dateToCalendar(date).get(Calendar.DAY_OF_WEEK);
  }

  /**
   * Get the day of month.
   *
   * @param date The date.
   *
   * @return The day of the month. The first day = 1.
   */
  public static int getDayOfMonth(Date date) {
    return dateToCalendar(date).get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Get the day of year.
   *
   * @param date The date.
   *
   * @return The day number of the year. The first day = 1.
   */
  public static int getDayOfYear(Date date) {
    return dateToCalendar(date).get(Calendar.DAY_OF_YEAR);
  }

  /**
   * Get the week of month.
   *
   * @param date The date.
   *
   * @return The week within the month. The first week = 1.
   */
  public static int getWeekOfMonth(Date date) {
    return dateToCalendar(date).get(Calendar.WEEK_OF_MONTH);
  }

  /**
   * Get the week of the year.
   *
   * @param date The date.
   *
   * @return The week of the year. The first week = 1.
   */
  public static int getWeekOfYear(Date date) {
    return dateToCalendar(date).get(Calendar.WEEK_OF_YEAR);
  }

  /**
   * The month of the year.
   *
   * @param date The date
   *
   * @return The month of the year, in the range 1-12
   */
  public static int getMonth(Date date) {
    // This is zero-based (Why?.. I don't know.)
    return dateToCalendar(date).get(Calendar.MONTH) + 1;
  }

  /**
   * Get the year.
   *
   * @param date The date.
   *
   * @return The year of the date.
   */
  public static int getYear(Date date) {
    return dateToCalendar(date).get(Calendar.YEAR);
  }

  private static Calendar dateToCalendar(Date date) {
    if (date == null) {
      throw new NullPointerException( "Null date" );
    }
    Calendar dateTime = Calendar.getInstance();
    dateTime.setTime(date);
    return dateTime;
  }

}
