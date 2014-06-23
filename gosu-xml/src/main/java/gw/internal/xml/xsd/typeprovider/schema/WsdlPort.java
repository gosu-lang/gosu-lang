/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;

import javax.xml.namespace.QName;

public final class WsdlPort extends XmlSchemaObject<WsdlPort> {

  private final QName _qname;
  private final QName _bindingName;
  private final List<WsdlSoapAddress> _soapAddresses;
  private final WsdlGwAddress _wsdlGwAddress;
  private final WsdlService _wsdlService;

  public WsdlPort( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName qname, QName bindingName, List<WsdlSoapAddress> soapAddresses, WsdlGwAddress wsdlGwAddress, WsdlService wsdlService ) {
    super( schemaIndex, locationInfo );
    _qname = qname;
    _bindingName = bindingName;
    _soapAddresses = soapAddresses;
    _wsdlGwAddress = wsdlGwAddress;
    _wsdlService = wsdlService;
  }

  public QName getQName() {
    return _qname;
  }

  public QName getBindingName() {
    return _bindingName;
  }

  public List<WsdlSoapAddress> getSoapAddresses() {
    return _soapAddresses;
  }

  public WsdlGwAddress getGwAddress() {
    return _wsdlGwAddress;
  }

  public WsdlBinding getBinding() {
    return getSchemaIndex().getWsdlBindingByQName( _bindingName );
  }

  public WsdlService getService() {
    return _wsdlService;
  }

  @Override
  public String toString() {
    return getQName().toString();
  }

  public String getLocation() {
    String uri = null;
    for ( WsdlSoapAddress el : getSoapAddresses() ) {
      uri = el.getLocation() == null ? null : el.getLocation();
      if ( uri != null ) {
        break;
      }
    }
    return uri;
  }

}
