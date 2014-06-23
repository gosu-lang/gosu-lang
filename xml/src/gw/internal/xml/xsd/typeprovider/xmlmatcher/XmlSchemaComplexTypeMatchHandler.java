/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;

import javax.xml.namespace.QName;

public class XmlSchemaComplexTypeMatchHandler extends XmlMatchHandler {

  private final XmlSchemaComplexType _complexType;
  private XmlMatchHandler _matchHandler;

  public XmlSchemaComplexTypeMatchHandler( XmlSchemaComplexType complexType ) {
    _complexType = complexType;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _matchHandler == null ) {
      if ( _complexType.getContentModel() != null ) {
        _matchHandler = XmlMatchHandler.getMatchHandler( _complexType.getContentModel() );
      }
      else {
        return;
      }
    }
    _matchHandler.match( elementName, collection );
  }
  
}
