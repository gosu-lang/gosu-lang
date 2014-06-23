/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.reflect.LocationInfo;
import gw.xml.XmlElement;

import java.util.HashMap;
import java.util.Map;

public class SimpleLocationMap implements LocationMap {
  private Map<XmlElement, LocationInfo> _locationMap = new HashMap<XmlElement, LocationInfo>();

  @Override
  public void put(XmlElement element, LocationInfo info) {
    _locationMap.put(element, info);
  }

  @Override
  public LocationInfo get(XmlElement element) {
    return _locationMap.get(element);
  }
}
