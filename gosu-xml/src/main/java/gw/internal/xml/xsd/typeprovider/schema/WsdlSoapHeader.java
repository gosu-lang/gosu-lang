/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class WsdlSoapHeader extends XmlSchemaObject<WsdlSoapHeader> {

  private final String _partName;
  private final String _use;
  private final QName _messageQName;

  public WsdlSoapHeader( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String partName, String use, QName messageQName ) {
    super( schemaIndex, locationInfo );
    _partName = partName;
    _use = use;
    _messageQName = messageQName;
  }

  public String getPartName() {
    return _partName;
  }

  public String getUse() {
    return _use;
  }

  public WsdlMessage getMessage() {
    return getSchemaIndex().getWsdlMessageByQName( getMessageQName() );
  }

  private QName getMessageQName() {
    return _messageQName;
  }

  public WsdlPart getPart() {
    return getMessage().getPartByName( getPartName() );
  }
}