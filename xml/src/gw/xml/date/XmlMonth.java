/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.Calendar;
import java.text.ParseException;

public class XmlMonth extends AbstractXmlDateType
{

  private int _month;

  private XmlMonth()
  {
    super( false, true, false, false );
  }

  public XmlMonth( String s ) throws ParseException {
    this();
    // The 2001 version of the specification allowed gMonth in the format --MM--
    // The 2004 version corrected it to work like the other date types, and changed
    // the format to --MM
    // Unfortunately, some tools (JAXB) still generate the original format
    s = s.replaceFirst( "^(--\\d\\d)--(.*)", "$1$2" );
    parseString( s );
  }

  public XmlMonth( Calendar cal, boolean useTimeZone )
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

}
