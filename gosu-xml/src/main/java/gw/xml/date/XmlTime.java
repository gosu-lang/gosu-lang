/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.math.BigDecimal;
import java.text.ParseException;

public class XmlTime extends AbstractXmlDateType
{

  private int _hour;
  private int _minute;
  private BigDecimal _second;

  private XmlTime()
  {
    super( false, false, false, true );
  }

  public XmlTime( String s ) throws ParseException {
    this();
    parseString( s );
  }

  public XmlTime( Calendar cal, boolean useTimeZone )
  {
    this();
    getCalendarFields( cal, useTimeZone );
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
