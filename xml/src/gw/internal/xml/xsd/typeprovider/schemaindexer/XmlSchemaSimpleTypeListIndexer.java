/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeList;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaSimpleTypeListIndexer extends XmlSchemaIndexer<XmlSchemaSimpleTypeList> {
  @Override
  public <T> void index( XmlSchemaSimpleTypeList list, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    final XmlSchemaSimpleType itemType = list.getItemType();
    if ( itemType != null ) {
      invokeIndexer( itemType, prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }

}