/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;

import javax.xml.namespace.QName;

public class XmlSchemaComplexContentMatchHandler extends XmlMatchHandler {

  private final XmlSchemaComplexContent _complexContent;
  private XmlMatchHandler _matchHandler;

  public XmlSchemaComplexContentMatchHandler( XmlSchemaComplexContent complexContent ) {
    _complexContent = complexContent;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _matchHandler == null ) {
      _matchHandler = XmlMatchHandler.getMatchHandler( _complexContent.getContent() );
    }
    _matchHandler.match( elementName, collection );
  }

}
