/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.date;

/**
 * Enumeration of months, January, February and so on, with utilities for converting to and from the
 * integer constants used by {@link java.util.Calendar}.
 */
public enum Month {
  JANUARY,
  FEBRUARY,
  MARCH,
  APRIL,
  MAY,
  JUNE,
  JULY,
  AUGUST,
  SEPTEMBER,
  OCTOBER,
  NOVEMBER,
  DECEMBER;

  /**
   * The implementation of the functions to convert to and from calendar values assume that neither the
   * values in java.util.Calendar nor the order of constants in this enumeration will ever change. The
   * _valueLookup array is just a private cache of the enumeration value array to avoid allocating a
   * new one every time we do a lookup.
   */
  private static final Month[] _valueLookup = values();

  /**
   * Convert an integer constant as used by the {@link java.util.Calendar#MONTH} field into the
   * corresponding member of this enumeration.
   * @param calendarMonth an integer constant in the range 0-11
   * @return the corresponding enumeration member
   * @throws IllegalArgumentException if the given integer is not in the correct range
   */
  public static Month fromCalendarMonth(int calendarMonth) {
    if (calendarMonth < 0 || calendarMonth >= _valueLookup.length) {
      throw new IllegalArgumentException(calendarMonth + " is not a valid Calendar month");
    }
    return _valueLookup[calendarMonth];
  }

  /**
   * The corresponding integer constant suitable for use with the {@link java.util.Calendar#MONTH} field
   * @return integer in the range 0-11
   */
  public int toCalendarMonth() {
    return ordinal();
  }
}
