/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.internal.xml.XmlMixedContentList;
import gw.internal.xml.XmlTypeInstanceInternals;
import gw.internal.xml.XmlUtil;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaTypeInstanceTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.IType;
import gw.util.GosuExceptionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

class XmlTypeInstanceInternalsImpl extends XmlTypeInstanceInternals {

  private static final QName XSI_TYPE_ATTRIBUTE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type" );
  private static final QName XSI_NIL_ATTRIBUTE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil" );

  @Override
  public XmlMixedContentList createContentList( XmlTypeInstance xmlTypeInstance ) {
    return new XmlMixedContentList( xmlTypeInstance );
  }

  public final Set<QName> _getAttributeNames( XmlTypeInstance typeInstance ) {
    LinkedHashMap<QName, XmlSimpleValue> attributes = typeInstance._attributes;
    return attributes == null ? Collections.<QName>emptySet() : Collections.unmodifiableSet( attributes.keySet() );
  }

  public XmlSimpleValue _setAttributeSimpleValue( XmlTypeInstance typeInstance, QName attributeName, XmlSimpleValue value ) {
    if ( attributeName == null ) {
      throw new IllegalArgumentException( "Attribute name cannot be null" );
    }
    if ( attributeName.getPrefix().equals( XMLConstants.XMLNS_ATTRIBUTE )
            || ( attributeName.getPrefix().equals( XMLConstants.DEFAULT_NS_PREFIX ) && attributeName.getLocalPart().equals( XMLConstants.XMLNS_ATTRIBUTE ) )
            || ( attributeName.getNamespaceURI().equals( XMLConstants.XMLNS_ATTRIBUTE_NS_URI ) ) ) {
      throw new XmlException( "Setting xmlns attributes directly is not supported." );
    }
    if ( typeInstance._type != null && attributeName.equals( XSI_TYPE_ATTRIBUTE_QNAME ) ) {
      throw new XmlException( "Setting xsi:type directly is not supported on types other than xs:anyType." );
    }
    if ( attributeName.equals( XSI_NIL_ATTRIBUTE_QNAME ) ) {
      throw new XmlException( "Setting xsi:nil directly is not supported." );
    }
    XmlUtil.validateQName( attributeName );
    XmlSimpleValue oldValue;
    if ( value == null ) {
      oldValue = typeInstance._attributes == null ? null : typeInstance._attributes.remove( attributeName );
    }
    else {
      if ( typeInstance._attributes == null ) {
        typeInstance._attributes = new LinkedHashMap<QName, XmlSimpleValue>();
      }
      oldValue = typeInstance._attributes.put( attributeName, value );
    }
    return oldValue;
  }

  public XmlSimpleValue _setAttributeSimpleValue( XmlTypeInstance typeInstance, String attributeName, XmlSimpleValue value ) {
    return typeInstance.setAttributeSimpleValue( new QName( attributeName ), value );
  }

  public XmlSimpleValue _getAttributeSimpleValue( XmlTypeInstance typeInstance, QName attributeName ) {
    LinkedHashMap<QName, XmlSimpleValue> attributes = typeInstance._attributes;
    return attributes == null ? null : attributes.get( attributeName );
  }

  public XmlSimpleValue _getAttributeSimpleValue( XmlTypeInstance typeInstance, String attributeName ) {
    return typeInstance.getAttributeSimpleValue( new QName( attributeName ) );
  }

  public String _getAttributeValue( XmlTypeInstance typeInstance, String attributeName ) {
    XmlSimpleValue simpleValue = typeInstance.getAttributeSimpleValue( attributeName );
    return simpleValue == null ? null : simpleValue.getStringValue();
  }

  public String _getAttributeValue( XmlTypeInstance typeInstance, QName attributeName ) {
    XmlSimpleValue simpleValue = typeInstance.getAttributeSimpleValue( attributeName );
    return simpleValue == null ? null : simpleValue.getStringValue();
  }

  public String _setAttributeValue( XmlTypeInstance typeInstance, String attributeName, String value ) {
    XmlSimpleValue simpleValue = typeInstance.setAttributeSimpleValue( attributeName, XmlSimpleValue.makeStringInstance( value ) );
    return simpleValue == null ? null : simpleValue.getStringValue();
  }

  public String _setAttributeValue( XmlTypeInstance typeInstance, QName attributeName, String value ) {
    XmlSimpleValue simpleValue = typeInstance.setAttributeSimpleValue( attributeName, XmlSimpleValue.makeStringInstance( value ) );
    return simpleValue == null ? null : simpleValue.getStringValue();
  }

  public List<XmlElement> _getChildren( XmlTypeInstance typeInstance, QName qname ) {
    return getContentList( typeInstance ).getElementsByQName( qname );
  }

  private XmlMixedContentList getContentList( XmlTypeInstance typeInstance ) {
    return typeInstance._children;
  }

  public List<XmlElement> _removeChildren( XmlTypeInstance typeInstance, QName qname ) {
    List<XmlElement> ret = new ArrayList<XmlElement>();
    Iterator<IXmlMixedContent> iter = typeInstance._children.iterator();
    while ( iter.hasNext() ) {
      IXmlMixedContent content = iter.next();
      if ( content instanceof XmlElement ) {
        XmlElement element = (XmlElement) content;
        if ( element.getQName().equals( qname ) ) {
          ret.add( element );
          iter.remove();
        }
      }
    }
    return ret;
  }

  public List<XmlElement> _getChildrenBySubstitutionGroup( XmlTypeInstance typeInstance, IType type ) {
    return getContentList( typeInstance ).getElementsBySubstitutionGroup( type );
  }

  public List<XmlElement> _removeChildrenBySubstitutionGroup( XmlTypeInstance typeInstance, IType type ) {
    return getContentList( typeInstance ).removeElementsBySubstitutionGroup( type );
  }

  public XmlElement _getChildBySubstitutionGroup( XmlTypeInstance typeInstance, IType type ) {
    final List<XmlElement> elementList = _getChildrenBySubstitutionGroup( typeInstance, type );
    if ( elementList.isEmpty() ) {
      return null;
    }
    if ( elementList.size() > 1 ) {
      throw new MultipleContentMatchesException( "Multiple matches for " + type.getName() );
    }
    return elementList.get( 0 );
  }

  public XmlElement _removeChildBySubstitutionGroup( XmlTypeInstance typeInstance, IType type ) {
    final XmlElement child = _getChildBySubstitutionGroup( typeInstance, type );
    if ( child != null ) {
      typeInstance.getChildren().remove( child );
    }
    return child;
  }

  public XmlElement _getChild( XmlTypeInstance typeInstance, QName qname ) {
    List<XmlElement> elementList = typeInstance.getChildren( qname );
    if ( elementList.isEmpty() ) {
      return null;
    }
    else if ( elementList.size() == 1 ) {
      return elementList.get( 0 );
    }
    else {
      throw new MultipleContentMatchesException( "Multiple matches for " + qname );
    }
  }

  public XmlElement _removeChild( XmlTypeInstance typeInstance, QName qname ) {
    List<XmlElement> elementList = typeInstance.getChildren( qname );
    if ( elementList.isEmpty() ) {
      return null;
    }
    else if ( elementList.size() == 1 ) {
      XmlElement child = elementList.get( 0 );
      typeInstance._children.remove( child );
      return child;
    }
    else {
      throw new MultipleContentMatchesException( "Multiple matches for " + qname );
    }
  }

  public List<XmlElement> _getChildren( XmlTypeInstance typeInstance ) {
    return typeInstance._children.getAllElements();
  }

  public void _addChild( XmlTypeInstance typeInstance, XmlElement element ) {
    typeInstance._children.add( element );
  }

  public XmlSimpleValue _getSimpleValue( XmlTypeInstance typeInstance ) {
    return typeInstance._simpleValue;
  }

  public void _setSimpleValue( XmlTypeInstance typeInstance, XmlSimpleValue simpleValue ) {
    typeInstance._children.clear();
    typeInstance._simpleValue = simpleValue;
  }

  public String _getText( XmlTypeInstance typeInstance ) {
    return typeInstance._simpleValue == null ? "" : typeInstance._simpleValue.getStringValue();
  }

  public void _setText( XmlTypeInstance typeInstance, String text ) {
    if ( text == null ) {
      throw new IllegalArgumentException( "text cannot be null" );
    }
    typeInstance._children.clear();
    typeInstance._simpleValue = XmlSimpleValue.makeStringInstance( text );
  }

  @Override
  public IType getType( XmlTypeInstance typeInstance ) {
    return typeInstance._type;
  }

  public XmlSchemaTypeSchemaInfo getSchemaInfo( XmlTypeInstance typeInstance ) {
    return typeInstance._schemaInfo;
  }

  public void clearSimpleValue( XmlTypeInstance typeInstance ) {
    typeInstance._simpleValue = null;
  }

  public XmlTypeInstance create( IType xmlTypeInstanceType, XmlSchemaTypeSchemaInfo schemaInfo, Object[] args ) {
    IXmlSchemaTypeInstanceTypeData typeData = (IXmlSchemaTypeInstanceTypeData) xmlTypeInstanceType;
    XmlTypeInstance instance;
    try {
      instance = (XmlTypeInstance) typeData.getConstructorInternal().newInstance( xmlTypeInstanceType, schemaInfo );
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
    if ( args.length > 0 ) {
      xmlTypeInstanceType.getTypeInfo().getProperty( "$Value" ).getAccessor().setValue( instance, args[0] );
    }
    return instance;
  }

  public Object getSchemaInfoByType( IType type ) {
    return XmlSchemaIndex.getSchemaIndexByType( type ).getSchemaInfoByTypeName( type.getName() );
  }
  
}
