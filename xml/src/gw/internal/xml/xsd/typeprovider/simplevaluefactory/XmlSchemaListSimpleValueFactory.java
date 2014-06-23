/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.XmlSimpleValueInternals;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class XmlSchemaListSimpleValueFactory extends XmlSimpleValueFactory {

  private final XmlSimpleValueFactory _itemFactory;
  private final XmlSimpleValueValidator _itemValidator;
  private final LockingLazyVar<IType> _gosuValueType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return JavaTypes.LIST().getParameterizedType( _itemFactory.getGosuValueType() );
    }
  };

  public XmlSchemaListSimpleValueFactory( XmlSimpleValueFactory itemFactory, XmlSimpleValueValidator itemValidator ) {
    _itemFactory = itemFactory;
    _itemValidator = itemValidator;
  }

  @Override
  public IType getGosuValueType() {
    return _gosuValueType.get();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    //noinspection unchecked
    return new Value( new ItemList( (List<Object>) gosuValue ), false );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    final ItemList itemList = new ItemList( Collections.emptyList() );
    final Value value = new Value( itemList, isDefault );
    final String[] strings = stringValue.split( " " );
    for ( String string : strings ) {
      if (string.length() != 0) {
        itemList._items.add( (XmlSimpleValueBase) _itemFactory.deserialize( context, string, isDefault ) );
      }
    }
    return value;
  }

  private class Value extends XmlSimpleValueBase {
    private final ItemList _value;
    private final boolean _isDefault;

    public Value( ItemList value, boolean isDefault ) {
      _value = value;
      _isDefault = isDefault;
    }

    @Override
    public IType getGosuValueType() {
      return XmlSchemaListSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Object _getGosuValue() {
      // If this is returned as a default value, the backing list doesn't actually exist anywhere on the parent
      // element... Therefore, values set into it would never affect the parent element, so it might as well
      // be unmodifiable. See Jira PL-9876
      return _isDefault ? Collections.unmodifiableList( _value ) : _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return _value.getStringValue( isEnumCode );
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return _value.serialize( context );
    }

    @Override
    public List<QName> _getQNames() {
      return _value.getQNames();
    }
  }

  private class ItemList extends AbstractList<Object> implements IGosuObject {

    private List<XmlSimpleValueBase> _items = new ArrayList<XmlSimpleValueBase>( 0 );

    public ItemList( List<Object> items ) {
      addAll( items );
    }

    public String getStringValue( boolean isEnumCode ) {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      for ( XmlSimpleValueBase item : _items ) {
        if ( ! first ) {
          sb.append( ' ' );
        }
        first = false;
        sb.append( item.getStringValue( isEnumCode ) );
      }
      return sb.toString();
    }

    public String serialize( XmlSerializationContext context ) {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      for ( XmlSimpleValueBase item : _items ) {
        if ( ! first ) {
          sb.append( ' ' );
        }
        first = false;
        final String str = XmlSimpleValueInternals.instance().serialize( item, context );
        if ( str.length() == 0 ) {
          throw new XmlSimpleValueException( "xsd:list member length is zero" );
        }
        checkForSpaceInItem( str );
        sb.append( str );
      }
      return sb.toString();
    }

    private void checkForSpaceInItem( String str ) {
      if ( str.contains( " " ) ) {
        throw new XmlSimpleValueException( "xsd:list member '" + str + "' contains space" );
      }
    }

    @Override
    public Object get( int index ) {
      return _items.get( index ).getGosuValue();
    }

    @Override
    public int size() {
      return _items.size();
    }

    @Override
    public IType getIntrinsicType() {
      return getGosuValueType();
    }

    @Override
    public void add( int index, Object element ) {
      _items.add( index, (XmlSimpleValueBase) convertItemGosuValueToStorageValue( element ) );
    }

    @Override
    public Object set( int index, Object element ) {
      final XmlSimpleValue storageValue = convertItemGosuValueToStorageValue( element );
      XmlSimpleValueInternals.instance().validate( storageValue, _itemValidator, new XmlSimpleValueValidationContext() );
      final XmlSimpleValue simpleValue = _items.set( index, (XmlSimpleValueBase) storageValue );
      return simpleValue == null ? null : simpleValue.getGosuValue();
    }

    @Override
    public Object remove( int index ) {
      final XmlSimpleValue simpleValue = _items.remove( index );
      return simpleValue == null ? null : simpleValue.getGosuValue();
    }

    @Override
    public boolean add( Object o ) {
      return _items.add( (XmlSimpleValueBase) convertItemGosuValueToStorageValue( o ) );
    }

    @Override
    public void clear() {
      _items.clear();
    }

    public List<QName> getQNames() {
      List<QName> ret = new ArrayList<QName>( 0 );
      for ( XmlSimpleValue item : _items ) {
        ret.addAll( item.getQNames() );
      }
      return ret;
    }

    private XmlSimpleValue convertItemGosuValueToStorageValue( Object gosuValue ) {
      XmlSimpleValue value = _itemFactory.gosuValueToStorageValue( gosuValue );
      String stringValue = value.getStringValue();
      checkForSpaceInItem( stringValue );
      return value;
    }

  }

}
