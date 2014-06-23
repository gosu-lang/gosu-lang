/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSimpleValue;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class EncodedStringSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return JavaTypes.STRING();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value((String)gosuValue);
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value(decode(stringValue));
  }

  private String encode(String gosuValue) {
    StringBuilder sb = new StringBuilder(gosuValue.length());
    for (int i = 0; i != gosuValue.length(); i++) {
      char c = gosuValue.charAt(i);
      if (c < 0x20) {
        switch (c) {
          case 0x9: /* tab */
          case 0xA: /* line feed */
          case 0xD: /* cr */ sb.append(c); continue;
          default: sb.append('^').append((char)('@' + c));
        }
      }
      else if (c == '^') {
        sb.append('^').append('|');
      }
      else if (c == 0xFFFE) {
        sb.append('^').append('{');
      }
      else if (c == 0xFFFF) {
        sb.append('^').append('}');
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private String decode(String stringValue) {
    StringBuilder sb = new StringBuilder(stringValue.length());
    for (int i = 0; i != stringValue.length(); i++) {
      char c = stringValue.charAt(i);
      if (c == '^') {
        char cc = stringValue.charAt(++i);
        switch (cc) {
          case '|' : sb.append('^'); continue;
          case '{' : sb.append((char)0xFFFE); continue;
          case '}' : sb.append((char)0xFFFF); continue;
          default:
            char ch = (char)(cc - '@'); // would throw class cast exception
            sb.append(ch);
        }
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private class Value extends XmlSimpleValueBase {

    private final String _value;

    public Value( String value ) {
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return JavaTypes.STRING();
    }

    @Override
    public Object _getGosuValue() {
      return _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return encode(_value);
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return encode(_value);
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }
}