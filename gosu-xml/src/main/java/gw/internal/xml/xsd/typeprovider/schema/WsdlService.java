/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;
import gw.util.concurrent.LockingLazyVar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.namespace.QName;

public final class WsdlService extends XmlSchemaObject<WsdlService> {

  public static final Comparator<QName> QNAME_COMPARATOR = new Comparator<QName>() {
    @Override
    public int compare( QName o1, QName o2 ) {
      return o1.toString().compareTo( o2.toString() );
    }
  };

  private final QName _qname;
  private final List<WsdlPort> _ports;
  private final LockingLazyVar<Map<QName,WsdlPort>> _portsByQName = new LockingLazyVar<Map<QName, WsdlPort>>() {
    @Override
    protected Map<QName, WsdlPort> init() {
      TreeMap<QName, WsdlPort> ret = new TreeMap<QName, WsdlPort>( QNAME_COMPARATOR );
      for ( WsdlPort port : _ports ) {
        ret.put( port.getQName(), port );
      }
      return Collections.unmodifiableMap( ret );
    }
  };

  public WsdlService( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName qname, List<WsdlPort> ports ) {
    super( schemaIndex, locationInfo );
    _qname = qname;
    _ports = ports;
  }

  public QName getQName() {
    return _qname;
  }

  public List<WsdlPort> getPorts() {
    return _ports;
  }

  @Override
  public String toString() {
    return getQName().toString();
  }

  public Map<QName,WsdlPort> getPortsByQName() {
    return _portsByQName.get();
  }

  public String getLocationIfAllPortsHaveSameLocation() {
    String location = null;
    boolean foundOne = false;
    for ( WsdlPort port : _ports ) {
      if ( port.getLocation() == null ) {
        continue;
      }
      if ( foundOne && ! port.getLocation().equals( location ) ) {
        location = null;
        break; // found multiple different locations
      }
      foundOne = true;
      location = port.getLocation();
    }
    return location;
  }

}
