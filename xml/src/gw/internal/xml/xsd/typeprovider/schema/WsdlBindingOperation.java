/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class WsdlBindingOperation extends XmlSchemaObject<WsdlBindingOperation> {

  private final String _name;
  private final WsdlBindingOutput _bindingOutput;
  private final WsdlSoapOperation _soapOperation;
  private final WsdlBindingInput _bindingInput;

  public WsdlBindingOperation( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String name, WsdlSoapOperation soapOperation, WsdlBindingOutput bindingOutput, WsdlBindingInput bindingInput ) {
    super( schemaIndex, locationInfo );
    _name = name;
    _soapOperation = soapOperation;
    _bindingOutput = bindingOutput;
    _bindingInput = bindingInput;
  }

  public String getName() {
    return _name;
  }

  public WsdlBindingOutput getBindingOutput() {
    return _bindingOutput;
  }

  public WsdlSoapOperation getSoapOperation() {
    return _soapOperation;
  }

  public WsdlBindingInput getBindingInput() {
    return _bindingInput;
  }
}
