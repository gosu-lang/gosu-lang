/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.text.ParseException;

public class XmlMonthDay extends AbstractXmlDateType
{

  private int _month;
  private int _day;

  public XmlMonthDay()
  {
    super( false, true, true, false );
  }

  public XmlMonthDay( String s ) throws ParseException {
    this();
    parseString( s );
  }

  public XmlMonthDay( Calendar cal, boolean useTimeZone )
  {
    this();
    getCalendarFields( cal, useTimeZone );
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
