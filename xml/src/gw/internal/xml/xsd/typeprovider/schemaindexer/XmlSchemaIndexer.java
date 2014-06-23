/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAll;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAnyAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttributeGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSequence;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeList;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeUnion;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Indexes a schema into top level elements, types, attribute groups, etc. Creates ITypes for each schema type,
 * and for each top level element. Performs flattening of deeply nested child elements of each complex type.
 * @param <N> The XmlSchemaObject subclass that a particular indexer operates upon
 */
public abstract class XmlSchemaIndexer<N extends XmlSchemaObject> {

  private static final Map<Class<?>, XmlSchemaIndexer<?>> _indexers = new HashMap<Class<?>, XmlSchemaIndexer<?>>();
  
  static {
    _indexers.put( XmlSchemaAll.class, new XmlSchemaAllIndexer() );
    _indexers.put( XmlSchemaAny.class, new XmlSchemaAnyIndexer() );
    _indexers.put( XmlSchemaAttribute.class, new XmlSchemaAttributeIndexer() );
    _indexers.put( XmlSchemaAnyAttribute.class, new XmlSchemaAnyAttributeIndexer() );
    _indexers.put( XmlSchemaAttributeGroup.class, new XmlSchemaAttributeGroupIndexer() );
    _indexers.put( XmlSchemaChoice.class, new XmlSchemaChoiceIndexer() );
    _indexers.put( XmlSchemaComplexContent.class, new XmlSchemaComplexContentIndexer() );
    _indexers.put( XmlSchemaComplexContentExtension.class, new XmlSchemaComplexContentExtensionIndexer() );
    _indexers.put( XmlSchemaComplexContentRestriction.class, new XmlSchemaComplexContentRestrictionIndexer() );
    _indexers.put( XmlSchemaComplexType.class, new XmlSchemaComplexTypeIndexer() );
    _indexers.put( XmlSchemaElement.class, new XmlSchemaElementIndexer() );
    _indexers.put( XmlSchemaGroup.class, new XmlSchemaGroupIndexer() );
    _indexers.put( XmlSchemaSequence.class, new XmlSchemaSequenceIndexer() );
    _indexers.put( XmlSchemaSimpleContent.class, new XmlSchemaSimpleContentIndexer() );
    _indexers.put( XmlSchemaSimpleContentExtension.class, new XmlSchemaSimpleContentExtensionIndexer() );
    _indexers.put( XmlSchemaSimpleContentRestriction.class, new XmlSchemaSimpleContentRestrictionIndexer() );
    _indexers.put( XmlSchemaSimpleType.class, new XmlSchemaSimpleTypeIndexer() );
    _indexers.put( XmlSchemaSimpleTypeList.class, new XmlSchemaSimpleTypeListIndexer() );
    _indexers.put( XmlSchemaSimpleTypeRestriction.class, new XmlSchemaSimpleTypeRestrictionIndexer() );
    _indexers.put( XmlSchemaSimpleTypeUnion.class, new XmlSchemaSimpleTypeUnionIndexer() );
  }
  
  public abstract <T> void index( N xmlSchemaObject, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo );

  public static <T> void invokeIndexer( XmlSchemaObject member, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    XmlSchemaIndexer indexer = _indexers.get( member.getClass() );
    if ( indexer == null ) {
      throw new RuntimeException( "No indexer found for " + member.getClass() );
    }
    else {
      //noinspection unchecked
      indexer.index( member, prefix, flattenedChildren, topLevel, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }  
  
  public static String appendToPrefix( String prefix, String toAppend ) {
    if ( toAppend== null ) {
      return prefix;
    }
    else if ( prefix.length() == 0 ) {
      return toAppend;
    }
    else {
      return prefix + '_' + toAppend;
    }
  }

  protected static void setPlurality( Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, XmlSchemaPropertyType propertyType, QName qname, boolean isPlural ) {
    Pair<XmlSchemaPropertyType, QName> lookupkey = new Pair<XmlSchemaPropertyType, QName>( propertyType, qname );
    Boolean plural = pluralityMap.get( lookupkey );
    if ( plural == null ) {
      pluralityMap.put( lookupkey, isPlural );
    }
    else {
      pluralityMap.put( lookupkey, true );
    }
  }

}
