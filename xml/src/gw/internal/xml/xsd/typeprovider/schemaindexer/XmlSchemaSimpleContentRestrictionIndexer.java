/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttributeOrAttributeGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaSimpleContentRestrictionIndexer extends XmlSchemaIndexer<XmlSchemaSimpleContentRestriction> {
  @Override
  public <T> void index( XmlSchemaSimpleContentRestriction restriction, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    Iterator<XmlSchemaFacet> iter = restriction.getFacets().iterator();
    XmlSchemaSimpleTypeRestrictionIndexer.handleEnumerations( prefix, currentType, createTypes, iter, context, restriction.getBaseType(), restriction.getBaseTypeName() );
    if ( restriction.getBaseType() != null ) {
      invokeIndexer( restriction.getBaseType(), prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
    for ( XmlSchemaAttributeOrAttributeGroup xmlSchemaAttributeOrAttributeGroup : restriction.getAttributes() ) {
      invokeIndexer( xmlSchemaAttributeOrAttributeGroup, prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }

}