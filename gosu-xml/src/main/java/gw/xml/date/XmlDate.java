/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.text.ParseException;

public class XmlDate extends AbstractXmlDateType {

  private int _year;
  private int _month;
  private int _day;

  public XmlDate() {
    this( Calendar.getInstance(), false );
  }

  public XmlDate( String s ) throws ParseException {
    super( true, true, true, false );
    parseString( s );
  }

  public XmlDate( Calendar cal, boolean useTimeZone ) {
    super( true, true, true, false );
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

}

