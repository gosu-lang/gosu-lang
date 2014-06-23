/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaEnumerationTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaEnumerationFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaSimpleTypeRestrictionIndexer extends XmlSchemaIndexer<XmlSchemaSimpleTypeRestriction> {
  @Override
  public <T> void index( XmlSchemaSimpleTypeRestriction restriction, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    Iterator<XmlSchemaFacet> iter = restriction.getFacets().iterator();
    handleEnumerations( prefix, currentType, createTypes, iter, context, restriction.getBaseType(), restriction.getBaseTypeName() );
    if ( restriction.getBaseType() != null ) {
      invokeIndexer( restriction.getBaseType(), prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }

  static <T> void handleEnumerations( String prefix, XmlSchemaType currentType, boolean createTypes, Iterator<XmlSchemaFacet> iter, T context, XmlSchemaType baseType, QName baseTypeName ) {
    if ( createTypes ) {
      List<XmlSchemaEnumerationFacet> enumerations = new ArrayList<XmlSchemaEnumerationFacet>( 0 );
      while ( iter.hasNext() ) {
        XmlSchemaFacet facet = iter.next();
        if ( facet instanceof XmlSchemaEnumerationFacet ) {
//          String stringValue = facet.getValue();
          enumerations.add( (XmlSchemaEnumerationFacet) facet );
        }
      }
      if ( ! enumerations.isEmpty() ) {
        String packageName = currentType.getSchemaIndex().getPackageName() + currentType.getSchemaIndex().getTypeLoader().getEnumerationsNamespacePrefix();
        String typeName = currentType.getSchemaIndex().makeUniqueTypeName( packageName, prefix );
        XmlSchemaEnumerationTypeData typeData = new XmlSchemaEnumerationTypeData<T>( typeName, enumerations, context, baseType, baseTypeName, currentType );
        currentType.getSchemaIndex().putTypeDataByName( typeName, typeData );
        currentType.getSchemaIndex().registerEnumeration( currentType, typeData );
      }
    }
  }

}
