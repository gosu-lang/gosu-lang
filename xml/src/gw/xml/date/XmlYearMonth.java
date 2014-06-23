/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.text.ParseException;

public class XmlYearMonth extends AbstractXmlDateType
{

  private int _year;
  private int _month;

  private XmlYearMonth()
  {
    super( true, true, false, false );
  }

  public XmlYearMonth( String s ) throws ParseException {
    this();
    parseString( s );
  }

  public XmlYearMonth( Calendar cal, boolean useTimeZone )
  {
    this();
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
}

