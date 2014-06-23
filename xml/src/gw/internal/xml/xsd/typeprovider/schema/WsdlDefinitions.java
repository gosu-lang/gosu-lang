/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * @author jimwang
 */
public final class WsdlDefinitions extends XmlSchemaObject<WsdlDefinitions> {

  private final List<WsdlTypes> _types;
  private final List<WsdlImport> _imports;
  private final Map<QName, WsdlService> _services;
  private final String _targetNamespace;
  private final Map<QName, WsdlBinding> _bindings;
  private final Map<QName, WsdlPortType> _portTypes;
  private final Map<QName, WsdlMessage> _messages;

  public WsdlDefinitions( XmlSchemaIndex index, LocationInfo locationInfo, List<WsdlTypes> types, List<WsdlImport> imports, Map<QName, WsdlService> services, String targetNamespace, Map<QName, WsdlBinding> bindings, Map<QName, WsdlPortType> portTypes, Map<QName, WsdlMessage> messages ){
    super( index, locationInfo );
    _types = types;
    _imports = imports;
    _services = services;
    _targetNamespace = targetNamespace;
    _bindings = bindings;
    _portTypes = portTypes;
    _messages = messages;
  }

  public List<WsdlTypes> getTypes() {
    return _types;
  }

  public List<WsdlImport> getWsdlImports() {
    return _imports;
  }

  public Map<QName, WsdlService> getServices() {
    return _services;
  }

  public String getTargetNamespace() {
    return _targetNamespace;
  }

  public WsdlBinding getBindingByQName( QName bindingName ) {
    return _bindings.get( bindingName );
  }

  public WsdlPortType getPortTypeByQName( QName portTypeName ) {
    return _portTypes.get( portTypeName );
  }

  public WsdlMessage getMessageByQName( QName messageQName ) {
    return _messages.get( messageQName );
  }

  public Collection<WsdlPortType> getPortTypes() {
    return _portTypes.values();
  }
}
