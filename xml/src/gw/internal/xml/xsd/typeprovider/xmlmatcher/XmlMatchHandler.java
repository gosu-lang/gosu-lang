/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAll;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSequence;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

public abstract class XmlMatchHandler {

  private static final Map<Class<? extends XmlSchemaObject>, Class<? extends XmlMatchHandler>> _handlerClasses = new HashMap<Class<? extends XmlSchemaObject>, Class<? extends XmlMatchHandler>>();

  static {
    _handlerClasses.put( XmlSchemaComplexType.class, XmlSchemaComplexTypeMatchHandler.class );
    _handlerClasses.put( XmlSchemaComplexContent.class, XmlSchemaComplexContentMatchHandler.class );
    _handlerClasses.put( XmlSchemaComplexContentExtension.class, XmlSchemaComplexContentExtensionMatchHandler.class );
    _handlerClasses.put( XmlSchemaComplexContentRestriction.class, XmlSchemaComplexContentRestrictionMatchHandler.class );
    _handlerClasses.put( XmlSchemaSequence.class, XmlSchemaSequenceMatchHandler.class );
    _handlerClasses.put( XmlSchemaChoice.class, XmlSchemaChoiceMatchHandler.class );
    _handlerClasses.put( XmlSchemaElement.class, XmlSchemaElementMatchHandler.class );
    _handlerClasses.put( XmlSchemaAny.class, XmlSchemaAnyMatchHandler.class );
    _handlerClasses.put( XmlSchemaGroup.class, XmlSchemaGroupMatchHandler.class );
    _handlerClasses.put( XmlSchemaSimpleType.class, XmlSchemaSimpleTypeMatchHandler.class );
    _handlerClasses.put( XmlSchemaAll.class, XmlSchemaAllMatchHandler.class );
  }

  public abstract void match( QName elementName, XmlSchemaCollection collection );

  public static XmlMatchHandler getMatchHandler( XmlSchemaObject schemaObject ) {
    try {
      Class<? extends XmlMatchHandler> handlerClass = _handlerClasses.get( schemaObject.getClass() );
      if ( handlerClass == null ) {
        throw new RuntimeException( "No match handler found for class " + schemaObject.getClass().getName() );
      }
      if ( schemaObject instanceof XmlSchemaParticle ) {
        XmlSchemaParticle particle = (XmlSchemaParticle) schemaObject;
        if ( particle.getMinOccurs() != 1 || particle.getMaxOccurs() != 1 ) {
          return new XmlSchemaObjectPluralityMatchHandler( particle, handlerClass );
        }
      }
      return handlerClass.getConstructor( schemaObject.getClass() ).newInstance( schemaObject );
    }
    catch ( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

}
