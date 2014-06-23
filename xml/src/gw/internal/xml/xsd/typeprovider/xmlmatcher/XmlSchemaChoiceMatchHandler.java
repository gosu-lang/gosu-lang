/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.NotFoundException;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public class XmlSchemaChoiceMatchHandler extends XmlMatchHandler {

  private XmlSchemaChoice _choice;
  private XmlMatchHandler _match;
  private XmlMatchHandler _zeroWidthMatchHandler;

  public XmlSchemaChoiceMatchHandler( XmlSchemaChoice choice ) {
    _choice = choice;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _match != null ) {
      _match.match( elementName, collection );
      return;
    }
    List<XmlMatchHandler> matchHandlers = new ArrayList<XmlMatchHandler>( _choice.getItems().size() );
    for ( XmlSchemaParticle particle : _choice.getItems() ) {
      matchHandlers.add( XmlMatchHandler.getMatchHandler( particle ) );
    }
    for ( XmlMatchHandler matchHandler : matchHandlers ) {
      try {
        try {
          matchHandler.match( elementName, collection );
        }
        catch ( MatchFoundException ex ) {
          _match = matchHandler; // glue down to this choice
          throw ex;
        }

        // if control gets here, it's a zero-width match - still glue down to it unless another choice matches. See PL-20076
        if ( _zeroWidthMatchHandler == null ) {
          _zeroWidthMatchHandler = matchHandler;
        }
      }
      catch ( NotFoundException ex ) {
        // continue
      }
    }
    if ( _zeroWidthMatchHandler != null ) {
      _match = _zeroWidthMatchHandler;
    }
    else {
      throw new NotFoundException( "Choice not matched" );
    }
  }

}