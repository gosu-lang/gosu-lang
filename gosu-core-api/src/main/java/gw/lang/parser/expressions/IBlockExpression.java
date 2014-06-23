/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IScope;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IExpression;
import gw.lang.parser.IBlockClass;

import java.util.List;

public interface IBlockExpression extends IExpression
{
  List<ISymbol> getArgs();

  ICapturedSymbol getCapturedSymbol( String strName );

  boolean isWithinScope( ISymbol sym, ISymbolTable symbolTable );

  IScope getScope();

  IParsedElement getBody();

  public IBlockClass getBlockGosuClass();

}
