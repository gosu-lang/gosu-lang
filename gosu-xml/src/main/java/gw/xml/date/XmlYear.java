/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.text.ParseException;

public class XmlYear extends AbstractXmlDateType
{

  private int _year;

  private XmlYear()
  {
    super( true, false, false, false );
  }

  public XmlYear( String s ) throws ParseException {
    this();
    parseString( s );
  }

  public XmlYear( Calendar cal, boolean useTimeZone )
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

}

