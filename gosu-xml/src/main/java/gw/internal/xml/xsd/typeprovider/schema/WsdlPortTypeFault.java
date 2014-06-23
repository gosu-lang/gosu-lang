/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class WsdlPortTypeFault extends XmlSchemaObject<WsdlPortTypeFault> {

  private final String _name;
  private final QName _messageQName;

  public WsdlPortTypeFault( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String name, QName messageQName ) {
    super( schemaIndex, locationInfo );
    _name = name;
    _messageQName = messageQName;
  }

  public WsdlMessage getMessage() {
    return getSchemaIndex().getWsdlMessageByQName( getMessageQName() );
  }

  private QName getMessageQName() {
    return _messageQName;
  }

  public String getName() {
    return _name;
  }

}
