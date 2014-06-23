/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaAttributeTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.GosuClassUtil;
import gw.util.Pair;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaAttributeIndexer extends XmlSchemaIndexer<XmlSchemaAttribute> {

  @Override
  public <T> void index( XmlSchemaAttribute attribute, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    QName qname = attribute.getQName();
    if ( qname == null ) {
      qname = attribute.getRefName();
    }
    if ( attribute.isProhibited() ) {
      flattenedChildren.add( new XmlSchemaFlattenedChild( attribute, false ) );
      setPlurality( pluralityMap, XmlSchemaPropertyType.ATTRIBUTE, qname, false );
    }
    else {
      prefix = appendToPrefix( prefix, XmlSchemaIndex.makeCamelCase( qname.getLocalPart(), null ) );
      flattenedChildren.add( new XmlSchemaFlattenedChild( attribute, isPlural ) );
      setPlurality( pluralityMap, XmlSchemaPropertyType.ATTRIBUTE, qname, isPlural );

      @SuppressWarnings( {"unchecked"} )
      XmlSchemaIndex<T> schemaIndex = (XmlSchemaIndex<T>) (currentType == null ? attribute.getSchemaIndex() : currentType.getSchemaIndex());
      if ( createTypes ) {
        XmlSchemaAttributeTypeData typeData = null;
        // create typeData for top-level or anonymous attribute (but not for attribute refs)
        if ( topLevel || attribute.getRefName() == null ) {
          String packageName = schemaIndex.getPackageName();
          XmlSchemaResourceTypeLoaderBase<T> typeLoader = schemaIndex.getTypeLoader();
          if ( ! topLevel ) {
            packageName += typeLoader.getAnonymousNamespacePrefix() + ".attributes";
          }
          else {
            packageName += ".attributes";
          }
          String defaultValue = attribute.getFixedValue();
          if ( defaultValue == null ) {
            defaultValue = attribute.getDefaultValue();
          }
          String typeName = schemaIndex.makeUniqueTypeName( packageName, prefix );
          prefix = GosuClassUtil.getNameNoPackage( typeName ); // in case a suffix was added
          typeData = new XmlSchemaAttributeTypeData<T>( typeLoader, typeName, null, attribute, defaultValue, context );
          schemaIndex.putTypeDataByName( typeName, typeData );
          //usedElementNames.put( qname, typeData );
        }
        if ( typeData != null ) {
          attribute.getSchemaIndex().putTypeDataBySchemaObject( attribute, typeData );
        }
      }

      if ( attribute.getSchemaTypeName() == null && attribute.getSchemaType() != null ) {
        // process anonymous type
        XmlSchemaIndexer.invokeIndexer( attribute.getSchemaType(), prefix, flattenedChildren, topLevel, isPlural, null, createTypes, usedElementNames, pluralityMap, context, todo );
      }
    }
  }

}