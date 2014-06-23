/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;

import javax.xml.namespace.QName;

public final class WsdlBinding extends XmlSchemaObject<WsdlBinding> {

  private final QName _qname;
  private final WsdlSoapBinding _soapBinding;
  private final List<WsdlBindingOperation> _bindingOperations;
  private final QName _portTypeName;

  public WsdlBinding( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName qname, WsdlSoapBinding soapBinding, List<WsdlBindingOperation> bindingOperations, QName portTypeName ) {
    super( schemaIndex, locationInfo );
    _qname = qname;
    _soapBinding = soapBinding;
    _bindingOperations = bindingOperations;
    _portTypeName = portTypeName;
  }

  public QName getQName() {
    return _qname;
  }

  public WsdlSoapBinding getSoapBinding() {
    return _soapBinding;
  }

  public List<WsdlBindingOperation> getBindingOperations() {
    return _bindingOperations;
  }

  public QName getPortTypeName() {
    return _portTypeName;
  }

  public WsdlPortType getPortType() {
    return getSchemaIndex().getWsdlPortTypeByQName( getPortTypeName() );
  }
}
