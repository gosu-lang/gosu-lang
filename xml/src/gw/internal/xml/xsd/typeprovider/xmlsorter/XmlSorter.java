/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAll;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSequence;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Sorts children of an element in order to make the ordering match an XML schema.
 */
public final class XmlSorter {

  private static final Map<Class<? extends XmlSchemaObject>,Class<? extends XmlSortHandler>> _handlers = new HashMap<Class<? extends XmlSchemaObject>, Class<? extends XmlSortHandler>>();

  static {
    _handlers.put( XmlSchemaChoice.class, XmlSchemaChoiceXmlSortHandler.class );
    _handlers.put( XmlSchemaComplexContent.class, XmlSchemaComplexContentXmlSortHandler.class );
    _handlers.put( XmlSchemaComplexContentExtension.class, XmlSchemaComplexContentExtensionXmlSortHandler.class );
    _handlers.put( XmlSchemaComplexContentRestriction.class, XmlSchemaComplexContentRestrictionXmlSortHandler.class );
    _handlers.put( XmlSchemaComplexType.class, XmlSchemaComplexTypeXmlSortHandler.class );
    _handlers.put( XmlSchemaElement.class, XmlSchemaElementXmlSortHandler.class );
    _handlers.put( XmlSchemaGroup.class, XmlSchemaGroupXmlSortHandler.class );
    _handlers.put( XmlSchemaSequence.class, XmlSchemaSequenceXmlSortHandler.class );
    _handlers.put( XmlSchemaAll.class, XmlSchemaAllXmlSortHandler.class ); // uses same handler as xsd:sequence
    _handlers.put( XmlSchemaAny.class, XmlSchemaAnyXmlSortHandler.class );
  }

  private XmlSorter() {}

  public static XmlSortHandler createHandler( XmlSchemaObject schemaObject ) {
    Class<? extends XmlSchemaObject> schemaObjectClass = schemaObject.getClass();
    Class<? extends XmlSortHandler> handlerClass = _handlers.get( schemaObjectClass );
    if ( handlerClass == null ) {
      throw new RuntimeException( "No handler found for " + schemaObjectClass );
    }
    try {
      final Pair<Long, Long> minMaxOccurs = getMinMaxOccurs( schemaObject );
      if ( minMaxOccurs.getFirst() != 1 || minMaxOccurs.getSecond() != 1 ) {
        return new XmlSchemaObjectPluralityXmlSortHandler( minMaxOccurs.getFirst(), minMaxOccurs.getSecond(), schemaObject, handlerClass );
      }
      else {
        return handlerClass.getConstructor( schemaObjectClass ).newInstance( schemaObject );
      }
    }
    catch ( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

  public static Pair<Long,Long> getMinMaxOccurs( XmlSchemaObject schemaObject ) {
    final Pair<Long, Long> minMaxOccurs;
    if ( schemaObject instanceof XmlSchemaParticle ) {
      XmlSchemaParticle particle = (XmlSchemaParticle) schemaObject;
      minMaxOccurs = new Pair<Long, Long>( particle.getMinOccurs(), particle.getMaxOccurs() );
    }
    else {
      minMaxOccurs = new Pair<Long, Long>( 1L, 1L );
    }
    return minMaxOccurs;
  }

  public static List<XmlElement> sort( XmlSchemaType xmlSchemaObject, LinkedList<XmlElement> remainingChildren, final List<XmlSchemaIndex> requiredSchemas ) {

    final XmlSortHandler rootHandler = xmlSchemaObject == null ? null : createHandler( xmlSchemaObject );
    XmlSortHandler metaHandler = new XmlSortHandler( xmlSchemaObject ) {

      public XmlSortException _xmlSortException;

      @Override
      protected List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
        List<XmlElement> sortedChildren;
        try {
          sortedChildren = rootHandler == null ? Collections.<XmlElement>emptyList() : rootHandler.sort( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
        }
        catch ( XmlSortException ex ) {
          throw _xmlSortException == null ? ex : _xmlSortException; // prefer the "extra elements" exception over the actual exception
        }
        if ( remainingChildren.size() != sortedChildren.size() ) {
          LinkedList<XmlElement> localRemainingChildren = new LinkedList<XmlElement>( remainingChildren );
          // this is NOT equivalent to removeAll() when multiples of the same child exist in the content list
          for ( XmlElement localSortedChild : sortedChildren ) {
            localRemainingChildren.remove( localSortedChild );
          }
          _xmlSortException = new XmlSortException( "Extra elements found: " + localRemainingChildren );
          throw _xmlSortException;
        }
        return sortedChildren;
      }

      @Override
      protected boolean selectNextChoiceDirect() {
        //noinspection SimplifiableConditionalExpression
        return rootHandler == null ? false : rootHandler.selectNextChoice();
      }

      @Override
      public void _reset() {
        if ( rootHandler != null ) {
          rootHandler.reset();
        }
      }

      @Override
      protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
        if ( rootHandler != null ) {
          rootHandler.checkMissingRequiredElements( remainingChildren, requiredSchemas );
        }
      }

      @Override
      public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
        if ( rootHandler != null ) {
          rootHandler.match( children, requiredSchemas );
        }
      }

    };
    metaHandler.checkMissingRequiredElements( remainingChildren, requiredSchemas );
    try {
      return metaHandler.sort( remainingChildren, true, requiredSchemas, new HashSet<XmlElement>( remainingChildren ) );
    }
    catch ( XmlSortException ex ) {
      // try non-preferred
      metaHandler.reset();
      return metaHandler.sort( remainingChildren, false, requiredSchemas, new HashSet<XmlElement>( remainingChildren ) );
    }
  }
}
