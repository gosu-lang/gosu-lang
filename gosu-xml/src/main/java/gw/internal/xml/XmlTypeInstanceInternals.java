/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlTypeInstance;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

public abstract class XmlTypeInstanceInternals {

  private static final XmlTypeInstanceInternals _instance;

  static {
    try {
      Constructor<?> constructor = Class.forName( "gw.xml.XmlTypeInstanceInternalsImpl" ).getDeclaredConstructor();
      constructor.setAccessible( true );
      _instance = (XmlTypeInstanceInternals) constructor.newInstance();
    }
    catch ( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

  public static XmlTypeInstanceInternals instance() {
    return _instance;
  }

  public abstract XmlMixedContentList createContentList( XmlTypeInstance xmlTypeInstance );

  public abstract IType getType( XmlTypeInstance typeInstance );

  public abstract Set<QName> _getAttributeNames( XmlTypeInstance typeInstance );

  public abstract XmlSimpleValue _setAttributeSimpleValue( XmlTypeInstance typeInstance, QName attributeName, XmlSimpleValue value );

  public abstract XmlSimpleValue _setAttributeSimpleValue( XmlTypeInstance typeInstance, String attributeName, XmlSimpleValue value );

  public abstract XmlSimpleValue _getAttributeSimpleValue( XmlTypeInstance typeInstance, QName attributeName );

  public abstract XmlSimpleValue _getAttributeSimpleValue( XmlTypeInstance typeInstance, String attributeName );

  public abstract String _getAttributeValue( XmlTypeInstance typeInstance, String attributeName );

  public abstract String _getAttributeValue( XmlTypeInstance typeInstance, QName attributeName );

  public abstract String _setAttributeValue( XmlTypeInstance typeInstance, String attributeName, String value );

  public abstract String _setAttributeValue( XmlTypeInstance typeInstance, QName attributeName, String value );

  public abstract List<XmlElement> _getChildren( XmlTypeInstance typeInstance, QName qname );

  public abstract List<XmlElement> _removeChildren( XmlTypeInstance typeInstance, QName qname );

  public abstract List<XmlElement> _getChildrenBySubstitutionGroup( XmlTypeInstance typeInstance, IType type );

  public abstract List<XmlElement> _removeChildrenBySubstitutionGroup( XmlTypeInstance typeInstance, IType type );

  public abstract XmlElement _getChildBySubstitutionGroup( XmlTypeInstance typeInstance, IType type );

  public abstract XmlElement _removeChildBySubstitutionGroup( XmlTypeInstance typeInstance, IType type );

  public abstract XmlElement _getChild( XmlTypeInstance typeInstance, QName qname );

  public abstract XmlElement _removeChild( XmlTypeInstance typeInstance, QName qname );

  public abstract List<XmlElement> _getChildren( XmlTypeInstance typeInstance );

  public abstract void _addChild( XmlTypeInstance typeInstance, XmlElement element );

  public abstract XmlSimpleValue _getSimpleValue( XmlTypeInstance typeInstance );

  public abstract void _setSimpleValue( XmlTypeInstance typeInstance, XmlSimpleValue simpleValue );

  public abstract String _getText( XmlTypeInstance typeInstance );

  public abstract void _setText( XmlTypeInstance typeInstance, String text );

  public abstract Object getSchemaInfoByType( IType type );

  public abstract void clearSimpleValue( XmlTypeInstance typeInstance );

  public abstract XmlSchemaTypeSchemaInfo getSchemaInfo( XmlTypeInstance typeInstance );

  public abstract XmlTypeInstance create( IType xmlTypeInstanceType, XmlSchemaTypeSchemaInfo schemaInfo, Object[] args );  
  
}
