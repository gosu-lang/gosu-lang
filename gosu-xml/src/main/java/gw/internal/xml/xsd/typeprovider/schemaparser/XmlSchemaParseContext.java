/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaparser;

import gw.internal.xml.xsd.typeprovider.LocationMap;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;
import gw.xml.XmlElement;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class XmlSchemaParseContext {

  private final Map<QName, QName> _redefinedTypes = new HashMap<QName, QName>();
  private final Map<QName, QName> _redefinedGroups = new HashMap<QName, QName>();
  private final Map<QName, QName> _redefinedAttributeGroups = new HashMap<QName, QName>();
  private final Set<URL> _alreadyIncludedSchemas = new HashSet<URL>();
  private int _nextRedefineNumber;
  private LocationMap _locationMap;

  public Map<QName, QName> getRedefinedTypes() {
    return _redefinedTypes;
  }

  public Map<QName, QName> getRedefinedGroups() {
    return _redefinedGroups;
  }

  public Map<QName, QName> getRedefinedAttributeGroups() {
    return _redefinedAttributeGroups;
  }

  public String getNextRedefineName() {
    return XmlSchemaIndex.REDEFINE_PREFIX + _nextRedefineNumber++;
  }

  public boolean isSchemaAlreadyIncluded( URL url ) {
    return _alreadyIncludedSchemas.contains( url );
  }

  public boolean pushIncludedSchema( URL url ) {
    return ! _alreadyIncludedSchemas.add( url );
  }

  public void popIncludedSchema( URL url ) {
    _alreadyIncludedSchemas.remove( url );
  }

  public void setLocationMap( LocationMap locationMap ) {
    _locationMap = locationMap;
  }

  public LocationMap getLocationMap() {
    return _locationMap;
  }
}
