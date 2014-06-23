/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;

import javax.xml.namespace.QName;

public class XmlSchemaComplexContentRestrictionMatchHandler extends XmlMatchHandler {

  private final XmlSchemaComplexContentRestriction _restriction;
  private XmlMatchHandler _particleMatchHandler;

  public XmlSchemaComplexContentRestrictionMatchHandler( XmlSchemaComplexContentRestriction restriction ) {
    _restriction = restriction;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _restriction.getParticle() != null ) {
      if ( _particleMatchHandler == null ) {
        _particleMatchHandler = XmlMatchHandler.getMatchHandler( _restriction.getParticle() );
      }
      _particleMatchHandler.match( elementName, collection );
    }
  }

}