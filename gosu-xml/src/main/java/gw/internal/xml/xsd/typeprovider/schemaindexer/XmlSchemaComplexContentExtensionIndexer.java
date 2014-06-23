/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttributeOrAttributeGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaComplexContentExtensionIndexer extends XmlSchemaIndexer<XmlSchemaComplexContentExtension> {
  @Override
  public <T> void index( XmlSchemaComplexContentExtension extension, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    XmlSchemaParticle particle = extension.getParticle();
    if ( particle != null ) {
      XmlSchemaIndexer.invokeIndexer( particle, prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
    for ( XmlSchemaAttributeOrAttributeGroup xmlSchemaAttribute : extension.getAttributes() ) {
      XmlSchemaIndexer.invokeIndexer( xmlSchemaAttribute, prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
    if ( extension.getAnyAttribute() != null) {
      XmlSchemaIndexer.invokeIndexer( extension.getAnyAttribute(), prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }

}