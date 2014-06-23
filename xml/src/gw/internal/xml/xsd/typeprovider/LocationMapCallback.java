/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.XmlParserCallback;
import gw.lang.reflect.LocationInfo;
import gw.xml.XmlElement;
import org.xml.sax.Locator;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LocationMapCallback implements XmlParserCallback {

  final LocationMap _locationMap;

  public LocationMapCallback(LocationMap locationMap) {
    _locationMap = locationMap;
  }

  @Override
  public void onStartElement( Locator locator, XmlElement element, URL schemaEF ) {
    _locationMap.put( element, new LocationInfo( locator.getLineNumber(), locator.getColumnNumber(), schemaEF ) );
  }
}
