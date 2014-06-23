/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;

import javax.xml.namespace.QName;

public class XmlSchemaComplexContentExtensionMatchHandler extends XmlMatchHandler {

  private final XmlSchemaComplexContentExtension _extension;
  private XmlMatchHandler _baseTypeMatchHandler;
  private XmlMatchHandler _particleMatchHandler;

  public XmlSchemaComplexContentExtensionMatchHandler( XmlSchemaComplexContentExtension extension ) {
    _extension = extension;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _baseTypeMatchHandler == null ) {
      XmlSchemaType baseType = _extension.getSchemaIndex().getXmlSchemaTypeByQName( _extension.getBaseTypeName() );
      _baseTypeMatchHandler = XmlMatchHandler.getMatchHandler( baseType );
    }
    _baseTypeMatchHandler.match( elementName, collection );
    if ( _extension.getParticle() != null ) {
      if ( _particleMatchHandler == null ) {
        _particleMatchHandler = XmlMatchHandler.getMatchHandler( _extension.getParticle() );
      }
      _particleMatchHandler.match( elementName, collection );
    }
  }

}