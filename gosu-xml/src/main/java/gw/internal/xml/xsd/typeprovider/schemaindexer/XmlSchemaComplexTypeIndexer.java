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
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaContentModel;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.GosuClassUtil;
import gw.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaComplexTypeIndexer extends XmlSchemaIndexer<XmlSchemaComplexType> {

  @Override
  public <T> void index( XmlSchemaComplexType xsdType, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    flattenedChildren = new ArrayList<XmlSchemaFlattenedChild>(); // properties are rooted at the complex type level
    pluralityMap = new HashMap<Pair<XmlSchemaPropertyType, QName>, Boolean>();
    usedElementNames = new HashMap<QName, XmlSchemaElementTypeData>();
    isPlural = false;
    xsdType.getSchemaIndex().registerFlattenedChildrenBySchemaType( xsdType, flattenedChildren );
    xsdType.getSchemaIndex().registerChildrenPluralityBySchemaType( xsdType, pluralityMap );
    String xsdTypeName = xsdType.getName();
    String packageName = xsdType.getSchemaIndex().getPackageName();
    boolean createGosuType = true;
    boolean anonymous = false;
    XmlSchemaResourceTypeLoaderBase typeLoader = xsdType.getSchemaIndex().getTypeLoader();
    if ( xsdTypeName == null ) {
      if ( currentType != null ) {
        createGosuType = false;
      }
      packageName += typeLoader.getAnonymousNamespacePrefix() + ".types.complex";
      anonymous = true;
    }
    else {
      packageName += typeLoader.getTypesNamespacePrefix() + ".complex";
    }
    prefix = appendToPrefix( prefix, XmlSchemaIndex.makeCamelCase( xsdTypeName, null ) );
    if ( createGosuType && createTypes ) {
      String typeName = xsdType.getSchemaIndex().makeUniqueTypeName( packageName, prefix );
      prefix = GosuClassUtil.getNameNoPackage( typeName ); // in case a suffix was picked up
      @SuppressWarnings( { "unchecked" } )
      XmlSchemaTypeInstanceTypeData type = new XmlSchemaTypeInstanceTypeData<T>( ( XmlSchemaIndex<T> ) xsdType.getSchemaIndex(), typeLoader, typeName, xsdType, anonymous, context );
      xsdType.getSchemaIndex().putTypeDataByName( typeName, type );
      xsdType.getSchemaIndex().putTypeDataBySchemaObject( xsdType, type );
    }

    XmlSchemaContentModel contentModel = xsdType.getContentModel();
    if ( contentModel != null ) {
      XmlSchemaIndexer.invokeIndexer( contentModel, prefix, flattenedChildren, false, isPlural, xsdType, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }

}
