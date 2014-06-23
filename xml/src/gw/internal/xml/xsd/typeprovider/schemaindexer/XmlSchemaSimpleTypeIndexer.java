/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeInstanceTypeData;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.GosuClassUtil;
import gw.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaSimpleTypeIndexer extends XmlSchemaIndexer<XmlSchemaSimpleType> {
  @Override
  public <T> void index( XmlSchemaSimpleType xsdType, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    xsdType.getSchemaIndex().registerFlattenedChildrenBySchemaType( xsdType, Collections.<XmlSchemaFlattenedChild>emptyList() ); // simple types have no children
    xsdType.getSchemaIndex().registerChildrenPluralityBySchemaType( xsdType, Collections.<Pair<XmlSchemaPropertyType, QName>, Boolean>emptyMap() ); // simple types have no children
    String xsdTypeName = xsdType.getName();
    String packageName = xsdType.getSchemaIndex().getPackageName();
    boolean createGosuType = true;
    boolean anonymous = false;
    XmlSchemaResourceTypeLoaderBase typeLoader = xsdType.getSchemaIndex().getTypeLoader();
    if ( xsdTypeName == null ) {
      if ( currentType != null ) {
        createGosuType = false;
      }
      packageName += typeLoader.getAnonymousNamespacePrefix() + ".types.simple";
      anonymous = true;
    }
    else {
      packageName += typeLoader.getTypesNamespacePrefix() + ".simple";
    }
    prefix = appendToPrefix( prefix, XmlSchemaIndex.makeCamelCase( xsdTypeName, null ) );
    if ( createGosuType && createTypes ) {
      String typeName = xsdType.getSchemaIndex().makeUniqueTypeName( packageName, prefix );
      prefix = GosuClassUtil.getNameNoPackage( typeName ); // in case a suffix was added
      @SuppressWarnings( { "unchecked" } )
      XmlSchemaTypeInstanceTypeData type = new XmlSchemaTypeInstanceTypeData<T>( ( XmlSchemaIndex<T> ) xsdType.getSchemaIndex(), typeLoader, typeName, xsdType, anonymous, context );
      xsdType.getSchemaIndex().putTypeDataByName( typeName, type );
      xsdType.getSchemaIndex().putTypeDataBySchemaObject( xsdType, type );
    }
    XmlSchemaSimpleTypeContent content = xsdType.getContent();
    if ( content != null ) {
      invokeIndexer( content, prefix, flattenedChildren, topLevel, isPlural, xsdType, createTypes, usedElementNames, pluralityMap, context, todo );
    }

  }

}
