/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.StandardScope;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import java.util.Map;

/**
 */
public class CommonSymbolsScope<K extends CharSequence, V extends ISymbol> extends StandardScope<K, V>
{
  private static final String PRINT = "print";

  public static CommonSymbolsScope make()
  {
    return new CommonSymbolsScope();
  }

  @SuppressWarnings({"unchecked"})
  private CommonSymbolsScope()
  {
    super( null, 6 );
    try
    {
      super.put( (K) PRINT, (V)new LockedDownSymbol( PRINT, new FunctionType( "print", GosuParserTypes.NULL_TYPE(), new IType[]{JavaTypes.OBJECT()} ),
                                                     StandardSymbolTable.PRINT ) );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  @Override
  public CommonSymbolsScope<K, V> copy()
  {
    return new CommonSymbolsScope<>();
  }

  @Override
  public V put( K key, V value )
  {
    throw new UnsupportedOperationException( "Cannot add symbols to the CommonSymbolsScope" );
  }

  @Override
  public void putAll( Map m )
  {
    throw new UnsupportedOperationException( "Cannot add symbols to the CommonSymbolsScope" );
  }

  public static class LockedDownSymbol extends Symbol
  {
    public LockedDownSymbol( CharSequence strName, IType type, Object value )
    {
      super( strName.toString(), type, value );
    }

    @Override
    public void setDynamicSymbolTable( ISymbolTable symTable )
    {
      // Do nothing
    }

    @Override
    public Object getValue()
    {
      return getValueDirectly();
    }
  }


}
