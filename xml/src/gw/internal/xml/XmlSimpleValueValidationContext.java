/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.HexBinarySimpleValueFactory;
import gw.util.Base64Util;

public class XmlSimpleValueValidationContext {

  // because parsing of certain datatypes can be expensive, this lets us only have to parse them once per validation
  private byte[] _base64Value;
  private byte[] _hexBinaryValue;
  private String _replacedValue;
  private String _collapsedValue;

  public byte[] getByteArray( XmlSchemaPrimitiveType primitiveType, String value ) {
    if ( primitiveType == XmlSchemaPrimitiveType.BASE64BINARY ) {
      if ( _base64Value == null ) {
        _base64Value = Base64Util.decode( value );
      }
      return _base64Value;
    }
    else if ( primitiveType == XmlSchemaPrimitiveType.HEXBINARY ) {
      if ( _hexBinaryValue == null ) {
        _hexBinaryValue = HexBinarySimpleValueFactory.decode( value );
      }
      return _hexBinaryValue;
    }
    else {
      throw new RuntimeException( "getByteArray() called on primitive type " + primitiveType.getQName() );
    }
  }

  public String doReplace( String value ) {
    if ( _replacedValue == null ) {
      StringBuilder sb = new StringBuilder();
      for ( int i = 0; i < value.length(); i++ ) {
        char ch = value.charAt( i );
        switch ( ch ) {
          case '\t':
          case '\n':
          case '\r': {
            ch = ' ';
          }
        }
        sb.append( ch );
      }
      _replacedValue = sb.toString();
    }
    return _replacedValue;
  }

  public String doCollapse( String value ) {
    if ( _collapsedValue == null ) {
      StringBuilder sb = new StringBuilder();
      boolean lastWasWhitespace = true;
      for ( int i = 0; i < value.length(); i++ ) {
        char ch = value.charAt( i );
        switch ( ch ) {
          case '\t':
          case '\n':
          case '\r':
          case ' ': {
            lastWasWhitespace = true;
            continue;
          }
        }
        if ( lastWasWhitespace ) {
          if ( sb.length() > 0 ) {
            sb.append( ' ' );
          }
          lastWasWhitespace = false;
        }
        sb.append( ch );
      }
      _collapsedValue = sb.toString();
    }
    return _collapsedValue;
  }

}
