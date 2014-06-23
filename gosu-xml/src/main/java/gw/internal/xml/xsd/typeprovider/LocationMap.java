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

public interface LocationMap {
  void put(XmlElement element, LocationInfo info);

  LocationInfo get(XmlElement element);
}
