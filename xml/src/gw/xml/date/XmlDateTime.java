/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.math.BigDecimal;
import java.text.ParseException;

public class XmlDateTime extends AbstractXmlDateType {

  private int _year;
  private int _month;
  private int _day;
  private int _hour;
  private int _minute;
  private BigDecimal _second;

  public XmlDateTime() {
    this( Calendar.getInstance(), true );
  }

  public XmlDateTime( String s ) throws ParseException {
    super( true, true, true, true );
    parseString( s );
  }

  public XmlDateTime( Calendar cal, boolean useTimeZone ) {
    super( true, true, true, true );
    getCalendarFields( cal, useTimeZone );
  }

  public int getYear() {
    return _year;
  }

  public void setYear( int year ) {
    _year = year;
  }

  public int getMonth() {
    return _month;
  }

  public void setMonth( int month ) {
    _month = month;
  }

  public int getDay() {
    return _day;
  }

  public void setDay( int day ) {
    _day = day;
  }

  public int getHour() {
    return _hour;
  }

  public void setHour( int hour ) {
    _hour = hour;
  }

  public int getMinute() {
    return _minute;
  }

  public void setMinute( int minute ) {
    _minute = minute;
  }

  public BigDecimal getSecond() {
    return _second;
  }

  public void setSecond( BigDecimal second ) {
    _second = second;
  }
}

