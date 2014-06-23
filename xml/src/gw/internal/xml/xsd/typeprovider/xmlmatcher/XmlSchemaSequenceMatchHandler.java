/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSequence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public class XmlSchemaSequenceMatchHandler extends XmlMatchHandler {

  private XmlSchemaSequence _sequence;
  private List<XmlMatchHandler> _matchHandlers;

  public XmlSchemaSequenceMatchHandler( XmlSchemaSequence sequence ) {
    _sequence = sequence;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _matchHandlers == null ) {
      _matchHandlers = new ArrayList<XmlMatchHandler>( _sequence.getItems().size() );
      for ( XmlSchemaParticle particle : _sequence.getItems() ) {
        _matchHandlers.add( XmlMatchHandler.getMatchHandler( particle ) );
      }
    }
    for ( XmlMatchHandler matchHandler : _matchHandlers ) {
      matchHandler.match( elementName, collection );
    }
  }

}
