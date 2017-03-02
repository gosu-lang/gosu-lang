package gw.lang.enhancements
uses java.util.*
uses gw.config.CommonServices
uses gw.date.GosuDateUtil
uses gw.date.DayOfWeek
uses gw.date.Month
uses java.text.SimpleDateFormat

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreDateEnhancement : Date {
  
  /**
   * Create a date, using the specified values. All the values except for the year can be defaulted so
   * <code>Date.create(2011)</code> will create a date on the 1st January 2011 with all the time fields
   * set to zero.
   * <p>
   * The parameters should be self explanatory except for:
   * <ul>
   * <li>month - to make defaulting of the value work, null is accepted as an alternative to JANUARY.
   *     Calling code that does set the parameter should always use an explicit value, not null, for clarity
   * <li>hour - should be in the range 0-23 (always 24 hour clock)
   * <li>day - day of month is 1 based, so 1 is the first day of the month
   * <li>timeZone - if no time zone is specified the default time zone is used
   * </ul>
   * If you specify a large number of parameters it is recommended that you use named parameters for
   * clarity e.g:
   * <pre>
   *   Date.create(
   *     :month = MAY,
   *     :day = 3,
   *     :year = 2011,
   *     :hour = 13,
   *     :minute = 15
   *   )
   * </pre>
   */
  static function create(
      year : int,
      month : Month = null,
      day : int = 1,
      hour : int = 0,
      minute : int = 0,
      second : int = 0,
      millisecond : int = 0,
      timeZone : TimeZone = null) : Date {
    var calendar = timeZone != null ? Calendar.getInstance(timeZone) : Calendar.getInstance()
    calendar.CalendarYear = year
    calendar.CalendarMonthEnum = month != null ? month : JANUARY
    calendar.CalendarDay = day
    calendar.CalendarHourOfDay = hour
    calendar.CalendarMinute = minute
    calendar.CalendarSecond = second
    calendar.CalendarMillisecond = millisecond
    return calendar.Time
  }
  
  /**
   * The date and time right now
   */
  static property get Now() : Date {
    return CommonServices.getEntityAccess().CurrentTime
  }
  
  /**
   * The date at the beginning of today, with hour, minute, second and millisecond all
   * set to zero
   */
  static property get Today() : Date { 
    var cal = Now.toCalendar()
    cal.CalendarMillisecond = 0
    cal.CalendarSecond = 0
    cal.CalendarMinute = 0
    cal.CalendarHourOfDay = 0
    return cal.Time
  }

  /**
   * The date at the beginning of yesterday, with hour, minute, second and millisecond all
   * set to zero
   */
  static property get Yesterday() : Date {
    return Today.addDays(-1)
  }

  /**
   * The date at the beginning of tomorrow, with hour, minute, second and millisecond all
   * set to zero
   */
  static property get Tomorrow() : Date {
    return Today.addDays(1)
  }

  /**
   * Get the millisecond within the current second
   * 
   * @return the millisecond, in the range 0-999
   */
  property get MillisecondInSecond() : int {
    return toCalendar().CalendarMillisecond
  }
  
  /**
   * Get the second of this date and time
   *
   * @return the second, in the range 0-59.
   */
  property get Second() : int {
    return GosuDateUtil.getSecond(this)
  }

  /**
   * Get the minute of this date and time
   *
   * @return the minute, in the range 0-59.
   */
  property get Minute() : int {
    return GosuDateUtil.getMinute(this)
  }

  /**
   * Get the hour of this date and time, base on a 12-hour clock.
   *
   * @return The hour of the time. Based on a 12-hour clock, in the range 0-11
   */
  property get Hour() : int {
    return GosuDateUtil.getHour(this)
  }
  
  /**
   * Is the hour of this date and time AM?
   *
   * @return true if the time is AM, false otherwise
   */
  property get AM() : boolean {
    return GosuDateUtil.isAM(this)
  }

  /**
   * Is the hour of this date and time PM?
   *
   * @return true if the time is PM, false otherwise
   */
  property get PM() : boolean {
    return GosuDateUtil.isPM(this)
  }

  /**
   * Get the hour of this date and time, based on a 24-hour clock.
   *
   * @return The hour of the time. Based on a 24-hour clock, in the range 0-23
   */
  property get HourOfDay() : int {
    return GosuDateUtil.getHourOfDay(this)
  }

  /**
   * Get the day of the week.
   *
   * @return The day of the week. Sunday = 1, Monday = 2, ..., Saturday = 7.
   */
  property get DayOfWeek() : int {
    return GosuDateUtil.getDayOfWeek(this)
  }

  /**
   * Get the day of the week as an enumeration value
   *
   * @return The day of the week. Sunday, Monday, Tuesday, etc.
   */
  property get DayOfWeekEnum() : DayOfWeek {
    return toCalendar().CalendarDayOfWeekEnum
  }

  /**
   * Get the day of the month.
   *
   * @return The day of the month. The first day = 1.
   */
  property get DayOfMonth() : int {
    return GosuDateUtil.getDayOfMonth(this)
  }

  /**
   * Get the day of the year.
   *
   * @return The day number of the year. The first day = 1.
   */
  property get DayOfYear() : int {
    return GosuDateUtil.getDayOfYear(this)
  }

  /**
   * Get the week of month.
   *
   * @return The week of the month. The first week = 1.
   */
  property get WeekOfMonth() : int {
    return GosuDateUtil.getWeekOfMonth(this)
  }

  /**
   * Get the week of the year.
   *
   * @return The week of the year. The first week = 1.
   */
  property get WeekOfYear() : int {
    return GosuDateUtil.getWeekOfYear(this)
  }

  /**
   * The month of the year. Note that this method returns values in the range 1-12,
   * <em>not</em> 0-11; this differs from normal java Calendar behavior. New code is
   * recommended to use MonthOfYearEnum.
   *
   * @return The month of the year, in the range 1-12.
   */
  property get MonthOfYear() : int {
    return GosuDateUtil.getMonth(this)
  }

  /**
   * The month of the year as an enumeration
   *
   * @return The month of the year, January, February etc.
   */
  property get MonthOfYearEnum() : Month {
    return toCalendar().CalendarMonthEnum
  }

  /**
   * Returns a new Calendar representing this Date in the system TimeZone and Locale.
   */
  function toCalendar() : Calendar {
    var cal = Calendar.getInstance(CommonServices.getEntityAccess().TimeZone)
    cal.setTime(this)
    return cal
  }

  /**
   * Returns a new Calendar representing this Date in the specified TimeZone and the system Locale.
   */
  function toCalendar(tz : TimeZone) : Calendar {
    var cal = Calendar.getInstance(tz)
    cal.setTime(this)
    return cal
  }

  /**
   * Returns a new Calendar representing this Date in the specified Locale and the system TimeZone.
   */
  function toCalendar(locale : Locale) : Calendar {
    var cal = Calendar.getInstance(CommonServices.getEntityAccess().TimeZone, locale)
    cal.setTime(this)
    return cal
  }

  /**
   * Returns a new Calendar representing this Date in the specified TimeZone and Locale.
   */
  function toCalendar(tz : TimeZone, locale : Locale) : Calendar {
    var cal = Calendar.getInstance(tz, locale)
    cal.setTime(this)
    return cal
  }

  /**
   * Adds the specified (signed) amount of seconds to the given date. For
   * example, to subtract 5 seconds from the current time of the date, you can
   * achieve it by calling: <code>date.addSeconds(-5)</code>.
   *
   * @param iSeconds The amount of seconds to add.
   * @return A new date with the seconds added.
   */
  function addSeconds(iSeconds : int) : Date {
    return GosuDateUtil.addSeconds(this, iSeconds)
  }

  /**
   * Adds the specified (signed) amount of minutes to the given date. For
   * example, to subtract 5 minutes from the current time of the date, you can
   * achieve it by calling: <code>date.addMinutes(-5)</code>.
   *
   * @param iMinutes The amount of minutes to add.
   * @return A new date with the minutes added.
   */
  function addMinutes(iMinutes : int) : Date {
    return GosuDateUtil.addMinutes(this, iMinutes)
  }

  /**
   * Adds the specified (signed) amount of hours to the given date. For
   * example, to subtract 5 hours from the current date, you can
   * achieve it by calling: <code>date.addHours(-5)</code>.
   *
   * @param iHours The amount of hours to add.
   * @return A new date with the hours added.
   */
  function addHours(iHours : int) : Date {
    return GosuDateUtil.addHours(this, iHours)
  }

  /**
   * Adds the specified (signed) amount of days to the given date. For
   * example, to subtract 5 days from the current date, you can
   * achieve it by calling: <code>date.addDays(-5)</code>.
   *
   * @param iDays The amount of days to add.
   * @return A new date with the days added.
   */
  function addDays(iDays : int) : Date {
    return GosuDateUtil.addDays(this, iDays)
  }

  /**
   * Adds the specified (signed) amount of weeks to the given date. For
   * example, to subtract 5 weeks from the current date, you can
   * achieve it by calling: <code>date.addWeeks(-5)</code>.
   *
   * @param iWeeks The amount of weeks to add.
   * @return A new date with the weeks added.
   */
  function addWeeks(iWeeks : int) : Date {
    return GosuDateUtil.addWeeks(this, iWeeks)
  }

  /**
   * Adds the specified (signed) amount of months to the given date. For
   * example, to subtract 5 months from the current date, you can
   * achieve it by calling: <code>date.addMonths(-5)</code>.
   *
   * @param iMonths The amount of months to add.
   * @return A new date with the months added.
   */
  function addMonths(iMonths : int) : Date {
    return GosuDateUtil.addMonths(this, iMonths)
  }

  /**
   * Adds the specified (signed) amount of years to the given date. For
   * example, to subtract 5 years from the current date, you can
   * achieve it by calling: <code>date.addYears(-5)</code>.
   *
   * @param iYears The amount of years to add.
   * @return A new date with the years added.
   */
  function addYears(iYears : int) : Date {
    return GosuDateUtil.addYears(this, iYears)
  }
  
  /**
   * Convert this date to a string using the given format. The format string should be as described in
   * {@link java.text.SimpleDateFormat}.
   * 
   * @param format a SimpleDataFormat format string, must not be null
   * @param timeZone optional time zone; if omitted or null the default time zone is used
   * @param locale optional locale; if omitted or null the default locale is used
   * 
   * @exception IllegalArgumentException if the given format string is invalid
   * @return formatted date string
   */
  function toStringWithFormat(format : String, timeZone : TimeZone = null, locale : Locale = null) : String {
    var formatter = locale != null ? new SimpleDateFormat(format, locale) : new SimpleDateFormat(format)
    if (timeZone != null) {
      formatter.setTimeZone(timeZone)
    }
    return formatter.format(this)
  }

  /**
   * Create a new java.sql.Date() initialized to the same exact number of
   * milliseconds as this Date
   */
  function toSQLDate() : java.sql.Date {
    return new java.sql.Date(this.Time)
  }
  
}