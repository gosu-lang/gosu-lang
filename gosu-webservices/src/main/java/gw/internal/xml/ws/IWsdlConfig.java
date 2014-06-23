/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.internal.schema.gw.xsd.guidewire.soapheaders_ref.Guidewire;
import gw.lang.PublishedType;
import gw.lang.PublishedTypes;
import gw.xml.XmlElement;
import gw.xml.XmlParseOptions;
import gw.xml.XmlSerializationOptions;

import java.net.URI;
import java.util.List;

@PublishedTypes({
  @PublishedType( fromType = "gw.internal.schema.gw.xsd.guidewire.soapheaders_ref.Guidewire", toType = "gw.xsd.guidewire.soapheaders_ref.Guidewire" )
})
public interface IWsdlConfig {

  Long getCallTimeout();

  void setCallTimeout( Long timeout );

  List<XmlElement> getRequestSoapHeaders();

  Guidewire getGuidewire();

  void setServerOverrideUrl( URI serverOverrideUrl );

  URI getServerOverrideUrl();

  XmlSerializationOptions getXmlSerializationOptions();

  void setXmlSerializationOptions( XmlSerializationOptions options );

  XmlParseOptions getXmlParseOptions();

  void setXmlParseOptions( XmlParseOptions options );

}
