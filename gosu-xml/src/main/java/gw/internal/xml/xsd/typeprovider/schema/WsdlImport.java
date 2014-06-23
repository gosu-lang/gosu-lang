/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class WsdlImport extends XmlSchemaObject<WsdlImport> {


  private String _nameSpace;
  private String _location;

  public WsdlImport(XmlSchemaIndex index, LocationInfo locationInfo, String nameSpace, String location){
    super( index, locationInfo );
    setNameSpace(nameSpace);
    setLocation(location);
  }

  public String getNameSpace() {
    return _nameSpace;
  }

  public void setNameSpace(String nameSpace) {
    _nameSpace = nameSpace;
  }

  public String getLocation() {
    return _location;
  }

  public void setLocation(String location) {
    _location = location;
  }
}
