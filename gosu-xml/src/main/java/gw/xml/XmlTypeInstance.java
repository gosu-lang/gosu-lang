/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.internal.xml.XmlElementInternals;
import gw.internal.xml.XmlTypeInstanceInternals;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.internal.xml.XmlMixedContentList;
import gw.lang.PublishInGosu;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.LockingLazyVar;

import java.util.LinkedHashMap;

import javax.xml.namespace.QName;

/**
 * Represents an XML backing type instance. Think of this as all of an element's contents ( attributes,
 * child elements, etc ), but without the surrounding element. The reason for the distinction between an element and
 * it's backing type instance is due to the separation of these concepts in the XML Schema specification.
 * The reason this class is called XmlTypeInstance rather than XmlType is because instances of this class
 * represent particular instances of XML Types, they do not represent the types themselves. In other words,
 * a particular XML type might allow a child element named &quot;Foo&quot;, but a particular instance
 * of that type might have a child element named &quot;Foo&quot; that contains the value &quot;Bar&quot;
 */
@PublishInGosu
public class XmlTypeInstance extends XmlBase {

  final XmlSchemaTypeSchemaInfo _schemaInfo;
  LinkedHashMap<QName, XmlSimpleValue> _attributes;
  final XmlMixedContentList _children = XmlTypeInstanceInternals.instance().createContentList( this );
  XmlSimpleValue _simpleValue;
  final IType _type;

  static final LockingLazyVar<IType> ANY_TYPE = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.complex.AnyType" );
    }
  };

  protected XmlTypeInstance( IType type, Object schemaInfo ) {
    super.setTypeInstance( this );
    if ( XmlElementInternals.instance().isAnyType( type ) ) {
      type = null;
      schemaInfo = null;
      setText( "" );
    }
    _type = type;
    _schemaInfo = (XmlSchemaTypeSchemaInfo) schemaInfo;
  }

  @Override
  public IType getIntrinsicType() {
    return _type == null ? ANY_TYPE.get() : _type;
  }

  /**
   * Serializes this type instance to the console. A root element will be supplied whose name is the Gosu typename
   * of this type instance type.
   */
  public void print() {
    new XmlElement( getIntrinsicType().getName(), this ).print();
  }

  /**
   * Serializes this type instance to the console. A root element will be supplied whose name is the Gosu typename
   * of this type instance type.
   * @param options the options to control serialization
   */
  public void print( XmlSerializationOptions options ) {
    new XmlElement( getIntrinsicType().getName(), this ).print( options );
  }

  // For use by subclasses that exist outside the gw.xml package and cannot access XmlTypeInstanceInternals
  protected static Object getSchemaInfoByType( IType type ) {
    return XmlTypeInstanceInternals.instance().getSchemaInfoByType( type );
  }

}
