/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.lang.reflect.IType;
import gw.xml.XmlSimpleValue;

import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;

public abstract class XmlSimpleValueBase extends XmlSimpleValue {

  @Override
  public abstract IType getGosuValueType();

  public abstract List<QName> _getQNames();

  public abstract String _getStringValue( boolean isEnumCode );

  public abstract Object _getGosuValue();

  public abstract String _serialize( XmlSerializationContext context );

  public final String serialize( XmlSerializationContext context ) {
    String value = _serialize( context );
    if ( value == null ) {
      throw new RuntimeException( "Null string value" );
    }
    return value;
  }

  public final void writeTo( IXMLWriter writer, XmlSerializationContext context ) throws IOException {
    writer.addText( serialize( context ) );
  }

  public String getStringValue() {
    return getStringValue( false );
   }

  public String getStringValue( boolean isEnumCode ) {
    String value = XmlSimpleValueInternals.instance()._getStringValue( this, isEnumCode );
    if ( value == null ) {
      throw new RuntimeException( "Null string value" );
    }
    return value;
  }

}
