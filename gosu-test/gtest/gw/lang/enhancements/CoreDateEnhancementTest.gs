package gw.lang.enhancements
uses gw.test.TestClass
uses java.util.Date
uses java.util.TimeZone
uses java.util.Locale
uses java.util.Calendar
uses gw.date.Month
uses gw.date.DayOfWeek

class CoreDateEnhancementTest extends TestClass
{
  function testCreateDefaulting() {
    var date = Date.create(2011)
    assertEquals(2011, date.toCalendar().CalendarYear)
    assertEquals(Month.JANUARY, date.MonthOfYearEnum)
    assertEquals(1, date.DayOfMonth)
    assertEquals(0, date.Hour)
    assertEquals(0, date.Minute)
    assertEquals(0, date.Second)
  }
  
  function testCreateFullyPopulated() {
    var date = Date.create(
      :month = MAY,
      :day = 22,
      :year = 2011,
      :hour = 15,
      :minute = 30,
      :second = 45,
      :millisecond = 777
    )
    assertEquals(2011, date.toCalendar().CalendarYear)
    assertEquals(Month.MAY, date.MonthOfYearEnum)
    assertEquals(22, date.DayOfMonth)
    assertEquals(15, date.HourOfDay)
    assertEquals(30, date.Minute)
    assertEquals(45, date.Second)
    assertEquals(777, date.MillisecondInSecond)
  }
  
  function testCreateInTimeZone() {
    // 8 hours difference between GMT and PST in the middle of winter (7 hours in summer)
    var date = Date.create(2011, :hour = 8, :timeZone = TimeZone.GMT)
    assertEquals(2011, date.toCalendar().CalendarYear)
    assertEquals(Month.JANUARY, date.MonthOfYearEnum)
    assertEquals(1, date.DayOfMonth)
    assertEquals(0, date.toCalendar(PST).CalendarHourOfDay)
    assertEquals(0, date.Minute)
    assertEquals(0, date.Second)
  }
  
  function testYesterday() {
    var yesterday = Date.Yesterday.toCalendar()
    assertEquals( 0, yesterday.CalendarMillisecond )
    assertEquals( 0, yesterday.CalendarSecond )
    assertEquals( 0, yesterday.CalendarMinute )
    assertEquals( 0, yesterday.CalendarHourOfDay )
  }

  function testToday() {
    var today = Date.Today.toCalendar()
    assertEquals( 0, today.CalendarMillisecond )
    assertEquals( 0, today.CalendarSecond )
    assertEquals( 0, today.CalendarMinute )
    assertEquals( 0, today.CalendarHourOfDay )
  }

  function testTomorrow() {
    var tomorrow = Date.Tomorrow.toCalendar()
    assertEquals( 0, tomorrow.CalendarMillisecond )
    assertEquals( 0, tomorrow.CalendarSecond )
    assertEquals( 0, tomorrow.CalendarMinute )
    assertEquals( 0, tomorrow.CalendarHourOfDay )
  }

  function testToCalendar() {
    var d = new Date()
    var cal = d.toCalendar()
    assertEquals( TimeZone.getDefault(), cal.TimeZone )
    assertEquals( Calendar.getInstance().FirstDayOfWeek, cal.FirstDayOfWeek )
  }
  
  function testToCalendarWithUSLocale() {
    var d = new Date()
    var cal = d.toCalendar( Locale.US )
    assertEquals( TimeZone.getDefault(), cal.TimeZone )
    assertEquals( 1, cal.FirstDayOfWeek )
  }

  function testToCalendarWithUKLocale() {
    var d = new Date()
    var cal = d.toCalendar( Locale.UK )
    assertEquals( TimeZone.getDefault(), cal.TimeZone )
    assertEquals( 2, cal.FirstDayOfWeek )
  }
  
  function testToCalendarWithTimeZone() {
    var otherTZ = getNonDefaultTimeZone()
    var d = new Date()
    var cal = d.toCalendar( otherTZ )
    assertEquals( otherTZ, cal.TimeZone )
    assertEquals( Calendar.getInstance().FirstDayOfWeek, cal.FirstDayOfWeek )
  }
  
  function testToCalendarWithTimeZoneAndUSLocale() {
    var otherTZ = getNonDefaultTimeZone()
    var d = new Date()
    var cal = d.toCalendar( otherTZ, Locale.US )
    assertEquals( otherTZ, cal.TimeZone )
    assertEquals( 1, cal.FirstDayOfWeek )
  }

  function testToCalendarWithTimeZoneAndUKLocale() {
    var otherTZ = getNonDefaultTimeZone()
    var d = new Date()
    var cal = d.toCalendar( otherTZ, Locale.UK )
    assertEquals( otherTZ, cal.TimeZone )
    assertEquals( 2, cal.FirstDayOfWeek )
  }
  
  function testAM() {
    var date = Date.create(2000, :hour = 13)
    assertFalse(date.AM)
    assertTrue(date.addHours(-2).AM)
    assertFalse(date.addHours(-1).AM)
  }

  function testPM() {
    var date = Date.create(2000, :hour = 13)
    assertTrue(date.PM)
    assertFalse(date.addHours(-2).PM)
    assertTrue(date.addHours(-1).PM)
  }
  
  function testMonthOfYearEnum() {
    var date = Date.create(
      :month = NOVEMBER,
      :day = 2,
      :year = 2011
    )
    assertEquals(Calendar.NOVEMBER, date.toCalendar().CalendarMonth)
    assertEquals(Month.NOVEMBER, date.MonthOfYearEnum)
  }
  
  function testDayOfWeekEnum() {
    var date = Date.create(
      :month = NOVEMBER,
      :day = 2,
      :year = 2011
    )
    assertEquals(Calendar.WEDNESDAY, date.DayOfWeek)
    assertEquals(DayOfWeek.WEDNESDAY, date.DayOfWeekEnum)
  }
 
  function testToStringWithFormat() {
    var date = Date.create(2011, Month.MAY, 22, 15, 30, 45)
    var string = date.toStringWithFormat("MM/dd/yyyy HH:mm:ss")
    assertEquals("05/22/2011 15:30:45", string)
  }
  
  function testToStringWithFormatAndTimeZone() {
    // 8 hours difference between GMT and PST in the middle of winter (7 hours in summer)
    var date = Date.create(2011, Month.FEBRUARY, 22, 15, 30, 45, :timeZone = TimeZone.GMT)
    var string = date.toStringWithFormat("MM/dd/yyyy HH:mm:ss", PST)
    assertEquals("02/22/2011 07:30:45", string)
  }
  
  function testToStringWithFormatAndLocale() {
    var date = Date.create(2011, Month.JANUARY, 22)
    var string = date.toStringWithFormat("dd MMMM yyyy", :locale = Locale.FRENCH)
    assertEquals("22 janvier 2011", string)
  }
  
  function testToSQLDate() {
    var original = Date.Now
    var sql = original.toSQLDate()
    assertTrue(sql typeis java.sql.Date)
    assertEquals(original.Time, sql.Time)
  }

  private function getNonDefaultTimeZone() : TimeZone {
    if (TimeZone.GMT == TimeZone.getDefault()) {
      return TimeZone.getTimeZone( "US/Pacific" )
    }
    else {
      return TimeZone.GMT
    }
  }

  private property get PST() : TimeZone {
    return TimeZone.getTimeZone("America/Los_Angeles")
  }
}
