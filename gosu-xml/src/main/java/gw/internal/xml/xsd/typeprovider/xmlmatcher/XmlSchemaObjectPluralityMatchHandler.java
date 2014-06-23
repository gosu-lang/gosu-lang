/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.NotFoundException;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.xml.XmlException;

import javax.xml.namespace.QName;

public class XmlSchemaObjectPluralityMatchHandler extends XmlMatchHandler {

  private final XmlSchemaParticle _particle;
  private final Class<? extends XmlMatchHandler> _matchHandlerClass;
  private XmlMatchHandler _currentHandler;
  private int _count = 0;

  public XmlSchemaObjectPluralityMatchHandler( XmlSchemaParticle particle, Class<? extends XmlMatchHandler> matchHandlerClass ) {
    _particle = particle;
    _matchHandlerClass = matchHandlerClass;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    try {
      if ( _currentHandler != null ) {
        try {
          _currentHandler.match( elementName, collection );
        }
        catch ( NotFoundException ignored ) {
        }
        _currentHandler = null;
      }
      if ( _count++ < _particle.getMaxOccurs() ) {
        XmlMatchHandler matchHandler = _matchHandlerClass.getConstructor( _particle.getClass() ).newInstance( _particle );
        try {
          matchHandler.match( elementName, collection );
        }
        catch ( MatchFoundException ex ) {
          _currentHandler = matchHandler;
          throw ex;
        }
      }
    }
    catch ( NotFoundException ex ) {
      if ( _count < _particle.getMinOccurs() ) {
        throw new XmlException( "particle did not satisfy minOccurs", ex );
      }
    }
    catch ( MatchFoundException ex ) {
      throw ex;
    }
    catch ( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

}
