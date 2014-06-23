/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import java.util.Map;

public abstract class ThreadSafeSymbolTable implements ISymbolTable
{
  private ISymbolTable _defaultSymTable;


  public ThreadSafeSymbolTable( boolean bDefineCommonSymbols )
  {
    _defaultSymTable = new StandardSymbolTable( bDefineCommonSymbols );
  }

  public ISymbolTable copy()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.copy();
  }

  public ISymbol getSymbol( CharSequence name )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getSymbol( name );
  }

  public void putSymbol( ISymbol symbol )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.putSymbol( symbol );
    symbol.setDynamicSymbolTable( this );
  }

  public ISymbol removeSymbol( CharSequence name )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.removeSymbol( name );
  }

  public Map getSymbols()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getSymbols();
  }

  public Map getSymbols( int iScopeOffset, int iPrivateGlobalIndex )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getSymbols( iScopeOffset, iPrivateGlobalIndex );
  }

  public int getTotalSymbolCount()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getTotalSymbolCount();
  }

  public int getScopeCount()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getScopeCount();
  }

  public int getPrivateGlobalScopeCount()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getPrivateGlobalScopeCount();
  }

  public IScope pushScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.pushScope();
  }

  public IScope pushScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.pushScope( scope );
  }

  public void pushPrivateGlobalScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.pushPrivateGlobalScope( scope );
  }

  public void popGlobalScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.popGlobalScope( scope );
  }

  public IScope popScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.popScope();
  }

  public IScope peekScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.peekScope();
  }

  public IScope peekScope( int iPos )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.peekScope( iPos );
  }

  public IScope popScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.popScope( scope );
  }

  public IScope pushIsolatedScope( IActivationContext activationCtx )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.pushIsolatedScope( activationCtx );
  }

  public void defineCommonSymbols()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.defineCommonSymbols();
  }

  public int getNextStackIndex()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getNextStackIndex();
  }

  public int getNextStackIndexForScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getNextStackIndexForScope( scope );
  }

  public boolean hasIsolatedScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.hasIsolatedScope();
  }

  public ISymbol getThisSymbolFromStackOrMap()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getThisSymbolFromStackOrMap();
  }

  public ISymbolTable getTargetSymbolTable()
  {
    ISymbolTable threadLocalSymTable = getThreadLocalSymbolTable();
    if( threadLocalSymTable != null )
    {
      return threadLocalSymTable;
    }
    else
    {
      throw new RuntimeException( "Thread-local symbol table is null" );
    }
  }

  public boolean isSymbolWithinScope( ISymbol sym, IScope scope )
  {
    ISymbolTable symbolTable = getTargetSymbolTable();
    return symbolTable.isSymbolWithinScope( sym, scope );
  }

  public IScope peekIsolatedScope()
  {
    ISymbolTable symbolTable = getTargetSymbolTable();
    return symbolTable.peekIsolatedScope();    
  }

  protected ISymbolTable getDefaultSymbolTable()
  {
    return _defaultSymTable;
  }

  public void clearDefaultSymbolTable()
  {
    _defaultSymTable = new StandardSymbolTable( false );
  }

  /**
   * Get a thread-local symbol table. This is typically an instance of
   * StandardSymbol table you maintain in a simple ThreadLocal.
   */
  protected abstract ISymbolTable getThreadLocalSymbolTable();
}
