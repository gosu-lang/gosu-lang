/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;

import java.util.List;


public class EnhancementDynamicFunctionSymbol extends DynamicFunctionSymbol
{
  private final Symbol _receiver;

  public EnhancementDynamicFunctionSymbol( ISymbolTable symTable, CharSequence strName, IFunctionType type, List<ISymbol> args, IType enhancedType )
  {
    super( symTable, strName, type, args, (Object)null );

    _receiver = new Symbol( AbstractElementTransformer.ENHANCEMENT_THIS_REF, enhancedType, null  );
  }

  public Symbol getReceiver()
  {
    return _receiver;
  }
}
