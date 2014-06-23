/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.AnyType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeInstanceTypeData;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.GosuClassUtil;
import gw.util.Pair;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaElementIndexer extends XmlSchemaIndexer<XmlSchemaElement> {
  @Override
  public <T> void index( final XmlSchemaElement xsdElement, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, final Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, final T context, List<Runnable> todo ) {
    final QName qname;
    if ( xsdElement.getQName() != null ) {
      qname = xsdElement.getQName();
    }
    else {
      qname = xsdElement.getRefName();
    }

    isPlural |= xsdElement.getMaxOccurs() > 1;
    flattenedChildren.add( new XmlSchemaFlattenedChild( xsdElement, isPlural ) );
    setPlurality( pluralityMap, XmlSchemaPropertyType.ELEMENT, qname, isPlural );
    prefix = appendToPrefix( prefix, XmlSchemaIndex.makeCamelCase( xsdElement.getName(), null ) );
    @SuppressWarnings( {"unchecked"} ) final
    XmlSchemaIndex<T> schemaIndex = (XmlSchemaIndex<T>) (currentType == null ? xsdElement.getSchemaIndex() : currentType.getSchemaIndex());
    final XmlSchemaElementTypeData[] typeData = { null };
    if ( createTypes ) {
      if ( ! usedElementNames.containsKey( qname ) ) {
        // create typeData for top-level or anonymous element (but not for element refs)
        if ( topLevel || xsdElement.getRefName() == null ) {
          boolean anonymousElement = false;
          String packageName = schemaIndex.getPackageName();
          final XmlSchemaResourceTypeLoaderBase<T> typeLoader = schemaIndex.getTypeLoader();
          if ( ! topLevel ) {
            anonymousElement = true;
            packageName += typeLoader.getAnonymousNamespacePrefix() + ".elements";
          }
          else {
            packageName += typeLoader.getElementsNamespacePrefix();
          }
          final String typeName = schemaIndex.makeUniqueTypeName( packageName, prefix );
          prefix = GosuClassUtil.getNameNoPackage( typeName ); // in case a suffix was added
          usedElementNames.put( qname, null );
          final boolean fanonymousElement = anonymousElement;
          if ( topLevel ) {
            typeData[0] = new XmlSchemaElementTypeData<T>( typeLoader, typeName, null, xsdElement, fanonymousElement, context, schemaIndex, null );
            schemaIndex.putTypeDataByName( typeName, typeData[0] );
            xsdElement.getSchemaIndex().putTypeDataBySchemaObject( xsdElement, typeData[ 0 ] );
            usedElementNames.put( qname, typeData[0] );
          }
          else {
            todo.add( new Runnable() {
              @Override
              public void run() {
                // if this element duplicates a top level element by name, and it's type instance is assignable to the
                // top level element's type instance, make it extend that top level element
                boolean createdNewType = true;
                XmlSchemaElement topLevelElement = schemaIndex.getXmlSchemaElementByQNameIfValid( qname );
                XmlSchemaElementTypeData superTypeData = null;
                if ( topLevelElement != null ) {
                  XmlSchemaElementTypeData topLevelElementTypeData = (XmlSchemaElementTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( topLevelElement );
                  XmlSchemaTypeInstanceTypeData topLevelElementTypeInstanceTypeData = topLevelElementTypeData.getXmlTypeInstanceTypeData();
                  XmlSchemaTypeInstanceTypeData myTypeInstanceTypeData;
                  if ( xsdElement.getSchemaType() != null ) {
                    myTypeInstanceTypeData = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( xsdElement.getSchemaType() );
                  }
                  else if ( xsdElement.getSchemaTypeName() != null ) {
                    myTypeInstanceTypeData = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( xsdElement.getSchemaIndex().getXmlSchemaTypeByQName( xsdElement.getSchemaTypeName() ) );
                  }
                  else {
                    XmlSchemaIndex<?> schemaSchemaIndex = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( xsdElement.getSchemaIndex().getTypeLoader().getModule(), "gw.xsd.w3c.xmlschema" );
                    XmlSchemaType anyTypeType = schemaSchemaIndex.getXmlSchemaTypeByQName( AnyType.$QNAME );
                    myTypeInstanceTypeData = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( anyTypeType );
                  }
                  if ( myTypeInstanceTypeData.equals( topLevelElementTypeInstanceTypeData ) ) {
                    typeData[0] = topLevelElementTypeData;
                    createdNewType = false;
                    schemaIndex.removeTypeName( typeName );
                  }
                  else {
                    // assignability check
                    XmlSchemaTypeInstanceTypeData mySuperTypeData = myTypeInstanceTypeData.getSuperTypeData();
                    while ( mySuperTypeData != null ) {
                      if ( mySuperTypeData.equals( topLevelElementTypeInstanceTypeData ) ) {
                        superTypeData = topLevelElementTypeData; // extend the top level element due to assignability
                        break;
                      }
                      mySuperTypeData = mySuperTypeData.getSuperTypeData();
                    }
                  }
                }
                if ( typeData[0] == null ) {
                  typeData[0] = new XmlSchemaElementTypeData<T>( typeLoader, typeName, null, xsdElement, fanonymousElement, context, schemaIndex, superTypeData );
                }
                if ( createdNewType ) {
                  schemaIndex.putTypeDataByName( typeName, typeData[0] );
                }
                xsdElement.getSchemaIndex().putTypeDataBySchemaObject( xsdElement, typeData[ 0 ] );
                usedElementNames.put( qname, typeData[0] );
              }
            } );
          }
        }
      }
      todo.add( new Runnable() {
        @Override
        public void run() {
          if ( typeData[0] == null ) {
            typeData[ 0 ] = usedElementNames.get( qname );
            if ( typeData[0] != null ) {
              xsdElement.getSchemaIndex().putTypeDataBySchemaObject( xsdElement, typeData[ 0 ] );
            }
          }
        }
      } );
    }
    if ( xsdElement.getSchemaTypeName() == null && xsdElement.getSchemaType() != null ) {
      // process anonymous type
      XmlSchemaIndexer.invokeIndexer( xsdElement.getSchemaType(), prefix, flattenedChildren, topLevel, isPlural, null, createTypes, usedElementNames, pluralityMap, context, todo );
    }
  }

}
