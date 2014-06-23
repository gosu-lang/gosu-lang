/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class XmlSchemaObject<T extends XmlSchemaObject> {

  private final XmlSchemaIndex _schemaIndex;
  protected LocationInfo _locationInfo;

  public XmlSchemaObject( XmlSchemaIndex schemaIndex, LocationInfo locationInfo ) {
    _schemaIndex = schemaIndex;
    _locationInfo = locationInfo;
  }

  public XmlSchemaIndex<?> getSchemaIndex() {
    return _schemaIndex;
  }

  protected static <V extends XmlSchemaObject<V>> List<V> copyList( XmlSchemaIndex schemaIndex, List<V> list ) {
    List<V> ret = new ArrayList<V>();
    for ( V v : list ) {
      ret.add( v.copy( schemaIndex ) );
    }
    return ret;
  }

  protected static <K,V extends XmlSchemaObject<V>> Map<K, V> copyMap( XmlSchemaIndex schemaIndex, Map<K, V> map ) {
    HashMap<K, V> ret = new HashMap<K, V>();
    for ( Map.Entry<K, V> entry : map.entrySet() ){
      ret.put( entry.getKey(), entry.getValue().copy(schemaIndex) );
    }
    return ret;
  }

  protected T copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    throw new UnsupportedOperationException( getClass().getName() + " does not implement copy(XmlSchemaIndex)" );
  }

  public final T copy() {
    T copy = copy( getSchemaIndex() );
    copy._locationInfo = _locationInfo;
    return copy;
  }

  public LocationInfo getLocationInfo() {
    return _locationInfo;
  }

}
