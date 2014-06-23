/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.XmlUtil;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSimpleValue;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.xml.namespace.QName;

public class QNameSimpleValueFactory extends XmlSimpleValueFactory {

  private final boolean _validating;

  public QNameSimpleValueFactory( boolean validating ) {
    _validating = validating;
  }

  @Override
  public IType getGosuValueType() {
    return JavaTypes.QNAME();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (QName) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
//    if ( context.getNamespaces() == null ) {
//      throw new XmlSimpleValueException( "Attempt to deserialize QName outside of a namespace context" );
//    }
    String[] parts = XmlUtil.splitQName( stringValue );
    String namespaceURI = context.getNamespaces().get( parts[ 0 ] );
    return new Value( new QName( namespaceURI, parts[ 1 ], parts[ 0 ] ) );
  }

  public class Value extends XmlSimpleValueBase {

    private final QName _qname;

    public Value( QName qname ) {
      if ( _validating ) {
        XmlUtil.validateQName(qname);
      }
      _qname = qname;
    }

    @Override
    public IType getGosuValueType() {
      return JavaTypes.QNAME();
    }

    @Override
    public Object _getGosuValue() {
      return _qname;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return isEnumCode ? _qname.getLocalPart() : _qname.toString();
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      TreeSet<String> availablePrefixes = context.getNamespaceUriToPrefixMap().get( _qname.getNamespaceURI() );
      return XmlUtil.qnameToString( availablePrefixes.iterator().next(), _qname.getLocalPart() );
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.singletonList( _qname );
    }

  }

}
