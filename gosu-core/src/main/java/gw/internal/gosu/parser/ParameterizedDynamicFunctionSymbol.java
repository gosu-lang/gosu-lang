/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.ScriptPartId;

/**
 */
public class ParameterizedDynamicFunctionSymbol extends DynamicFunctionSymbol
{
  private DynamicFunctionSymbol _dfsDelegate;

  public ParameterizedDynamicFunctionSymbol( IDynamicFunctionSymbol dfsDelegate, IGosuClass owner )
  {
    super( (DynamicFunctionSymbol)dfsDelegate );
    _dfsDelegate = (DynamicFunctionSymbol)dfsDelegate;
    setType( new FunctionType( (FunctionType)dfsDelegate.getType(), owner ) );
    // Use generic types name so that we can look this DFS up by name later on
    String strName = getType().getName() + getParameterDisplay( false );
    if( _dfsDelegate.getName().charAt( 0 ) == '@' && strName.charAt( 0 ) != '@' )
    {
      strName = '@' + strName;
    }
    setName( strName );
    setDisplayName( dfsDelegate.getDisplayName() );
    setScriptPart( new ScriptPartId( owner, null ) );
    assignSuperDfs( dfsDelegate, owner );
  }

  /**
   * Assign the super dfs in terms of the deriving class's type parameters
   */
  private void assignSuperDfs( IDynamicFunctionSymbol dfsDelegate, IGosuClass owner )
  {
    IDynamicFunctionSymbol rawSuperDfs = dfsDelegate.getSuperDfs();
    if( rawSuperDfs instanceof DynamicFunctionSymbol )
    {
      while( rawSuperDfs.getBackingDfs() instanceof DynamicFunctionSymbol && rawSuperDfs.getBackingDfs() != rawSuperDfs )
      {
        rawSuperDfs = rawSuperDfs.getBackingDfs();
      }
      IType ownersType = rawSuperDfs.getDeclaringTypeInfo().getOwnersType();
      if( !IGosuClass.ProxyUtil.isProxy( ownersType ) )
      {
        IType superOwner = TypeLord.findParameterizedType( owner, ownersType );
        if( superOwner == null )
        {
          superOwner = ownersType;
        }
        setSuperDfs( ((DynamicFunctionSymbol)rawSuperDfs).getParameterizedVersion( (IGosuClass)superOwner ) );
      }
    }
  }

  public MethodCallStatement getInitializer()
  {
    return _dfsDelegate.getInitializer();
  }

  public DynamicFunctionSymbol getBackingDfs()
  {
    return _dfsDelegate;
  }

  @Override
  protected String getCannonicalName()
  {
    return _dfsDelegate.getCannonicalName();
  }

  @Override
  public String getFullDescription() {
    return _dfsDelegate.getFullDescription();
  }
  
  public IReducedDynamicFunctionSymbol createReducedSymbol() {
    return new ReducedParameterizedDynamicFunctionSymbol(
        (ReducedDynamicFunctionSymbol) getBackingDfs().createReducedSymbol(), this);
  }
  
}

