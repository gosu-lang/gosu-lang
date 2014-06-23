/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class WsdlPortTypeOutput extends XmlSchemaObject<WsdlPortTypeOutput> {

  private final QName _messageQName;

  public WsdlPortTypeOutput( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName messageQName ) {
    super( schemaIndex, locationInfo );
    _messageQName = messageQName;
  }

  public QName getMessageQName() {
    return _messageQName;
  }

  public WsdlMessage getMessage() {
    return getSchemaIndex().getWsdlMessageByQName( getMessageQName() );
  }

}
