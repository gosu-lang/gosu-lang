/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuExceptionUtil;
import gw.xml.XmlSimpleValue;

import javax.xml.namespace.QName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

public class SimpleSimpleValueFactory extends XmlSimpleValueFactory {

  private Class<?> _gosuValueType;

  public SimpleSimpleValueFactory( Class gosuValueType ) {
    _gosuValueType = gosuValueType;
  }

  @Override
  public IType getGosuValueType() {
    return TypeSystem.get( _gosuValueType );
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    try {
      Constructor<?> declaredConstructor = _gosuValueType.getDeclaredConstructor( String.class );
      declaredConstructor.setAccessible( true );
      return new Value( declaredConstructor.newInstance( stringValue ) );
    }
    catch( Exception e ) {
      while( e instanceof InvocationTargetException ) {
        e = (Exception)((InvocationTargetException)e).getTargetException();
      }
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  private class Value extends XmlSimpleValueBase {

    private final Object _gosuValue;

    public Value( Object gosuValue ) {
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return SimpleSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Object _getGosuValue() {
      return _gosuValue;
    }

    public String _getStringValue( boolean isEnumCode ) {
      return String.valueOf( _gosuValue );
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return String.valueOf( _gosuValue );
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }

}
