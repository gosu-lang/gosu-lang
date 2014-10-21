/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.internal.gosu.template.GosuTemplateType;
import gw.lang.parser.IForwardingFunctionSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ITemplateType;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TemplateRenderFunctionSymbol extends DynamicFunctionSymbol implements IForwardingFunctionSymbol
{
  private IMethodInfo _mi;
  private ITemplateType _type;

  public TemplateRenderFunctionSymbol( IGosuClassInternal gsClass, ISymbolTable symTable, ReducedDynamicFunctionSymbol dfs, IMethodInfo mi, GosuTemplateType type, IType[] paramTypes )
  {
    super( symTable, dfs.getDisplayName(),
           new FunctionType( dfs.getDisplayName(), dfs.getReturnType(), paramTypes ),
           makeSymbolsFromTypes( paramTypes ), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    setClassMember( true );
    setName( dfs.getName() );
    _scriptPartId = new ScriptPartId( gsClass, null );
    _mi = mi;
    _type = type;
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }

  public IMethodInfo getMi()
  {
    return _mi;
  }

  public ITemplateType getTemplateType()
  {
    return _type;
  }

  public static List<ISymbol> makeSymbolsFromTypes( IType[] params )
  {
    List<ISymbol> newArgs = new ArrayList<ISymbol>( params.length );
    int i = 0;
    for( IType param : params )
    {
      Symbol sym = new Symbol( "p" + i++, param, null );
      sym.setIndex( i );
      newArgs.add( sym );
    }
    return newArgs;
  }

}
