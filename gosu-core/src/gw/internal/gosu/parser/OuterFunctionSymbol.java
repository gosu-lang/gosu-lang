/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.Keyword;

/**
 */
public class OuterFunctionSymbol extends DynamicFunctionSymbol
{
  private static final String OUTER_FUNCTION = "@" + Keyword.KW_outer;

  public OuterFunctionSymbol( ISymbolTable symTable, ICompilableTypeInternal gsClass )
  {
    super( symTable, OUTER_FUNCTION, new FunctionType( OUTER_FUNCTION, getEnclosingType( gsClass ), null ), null, (Statement)null );
    setPrivate( true );
  }

  private static IType getEnclosingType( ICompilableTypeInternal gsClass )
  {
    IType enhType = gsClass.getEnclosingNonBlockType();
    while( enhType instanceof IGosuEnhancementInternal )
    {
      enhType = ((IGosuEnhancementInternal)enhType).getEnhancedType();
    }
    return enhType;
  }
}
