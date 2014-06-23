/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;

public final class WsdlPortTypeOperation extends XmlSchemaObject<WsdlPortTypeOperation> {

  private final String _name;
  private final WsdlPortTypeInput _input;
  private final WsdlPortTypeOutput _output;
  private final List<WsdlPortTypeFault> _faults;

  public WsdlPortTypeOperation( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String name, WsdlPortTypeInput input, WsdlPortTypeOutput output, List<WsdlPortTypeFault> faults ) {
    super( schemaIndex, locationInfo );
    _name = name;
    _input = input;
    _output = output;
    _faults = faults;
  }

  public String getName() {
    return _name;
  }

  public WsdlPortTypeInput getInput() {
    return _input;
  }

  public WsdlPortTypeOutput getOutput() {
    return _output;
  }

  public List<WsdlPortTypeFault> getFaults() {
    return _faults;
  }
}
