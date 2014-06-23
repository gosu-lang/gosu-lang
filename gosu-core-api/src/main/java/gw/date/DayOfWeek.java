/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.date;

/**
 * Enumeration of week days, Sunday, Monday and so on, with utilities for converting to and from the
 * integer constants used by {@link java.util.Calendar}.
 */
public enum DayOfWeek {
  SUNDAY,
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY;

  /**
   * The implementation of the functions to convert to and from calendar values assume that neither the
   * values in java.util.Calendar nor the order of constants in this enumeration will ever change. The
   * _valueLookup array is just a private cache of the enumeration value array to avoid allocating a
   * new one every time we do a lookup.
   */
  private static final DayOfWeek[] _valueLookup = values();

  /**
   * Convert an integer constant as used by the {@link java.util.Calendar#DAY_OF_WEEK} field into the
   * corresponding member of this enumeration.
   * @param calendarDayOfWeek an integer constant in the range 1-7
   * @return the corresponding enumeration member
   * @throws IllegalArgumentException if the given integer is not in the correct range
   */
  public static DayOfWeek fromCalendarDayOfWeek(int calendarDayOfWeek) {
    int index = calendarDayOfWeek - 1;
    if (index < 0 || index >= _valueLookup.length) {
      throw new IllegalArgumentException(calendarDayOfWeek + " is not a valid Calendar day of the week");
    }
    return _valueLookup[index];
  }

  /**
   * The corresponding integer constant suitable for use with the {@link java.util.Calendar#DAY_OF_WEEK} field
   * @return integer in the range 1-7
   */
  public int toCalendarDayOfWeek() {
    return ordinal() + 1;
  }
}
