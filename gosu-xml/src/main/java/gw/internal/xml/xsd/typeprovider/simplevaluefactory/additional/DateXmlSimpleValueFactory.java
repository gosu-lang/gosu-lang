/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory.additional;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.xml.XmlSimpleValue;
import gw.xml.date.XmlDateTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.namespace.QName;

public class DateXmlSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return TypeSystem.get( Date.class );
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (Date) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    try {
      XmlDateTime dateTime = new XmlDateTime( stringValue );
      return new Value( dateTime.toCalendar().getTime() );
    }
    catch ( ParseException ex ) {
      throw new RuntimeException( ex );
    }
  }

  private class Value extends XmlSimpleValueBase {

    private Date _value;

    public Value( Date value ) {
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return DateXmlSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Object _getGosuValue() {
      return _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      Calendar cal = new GregorianCalendar();
      cal.setTime( _value );
      return new XmlDateTime( cal, true ).toString();
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return getStringValue();
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }

}
