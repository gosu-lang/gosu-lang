/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;

import javax.xml.namespace.QName;

public class XmlSchemaGroupMatchHandler extends XmlMatchHandler {

  private XmlSchemaGroup _group;
  private XmlMatchHandler _matchHandler;

  public XmlSchemaGroupMatchHandler( XmlSchemaGroup group ) {
    _group = group;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _matchHandler == null ) {
      _matchHandler = XmlMatchHandler.getMatchHandler( _group.getParticle() );
    }
    _matchHandler.match( elementName, collection );
  }

}
