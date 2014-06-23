/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.Collection;
import java.util.Map;

import javax.xml.namespace.QName;

public final class WsdlPortType extends XmlSchemaObject<WsdlPortType> {

  private final QName _qname;
  private Map<String, WsdlPortTypeOperation> _portTypeOperations;

  public WsdlPortType( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName qname, Map<String, WsdlPortTypeOperation> portTypeOperations ) {
    super( schemaIndex, locationInfo );
    _qname = qname;
    _portTypeOperations = portTypeOperations;
  }

  public QName getQName() {
    return _qname;
  }

  public WsdlPortTypeOperation getOperationByName( String name ) {
    return _portTypeOperations.get( name );
  }

  public Collection<WsdlPortTypeOperation> getOperations() {
    return _portTypeOperations.values();
  }
}
