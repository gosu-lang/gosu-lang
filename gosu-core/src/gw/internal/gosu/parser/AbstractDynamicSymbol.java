/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.parser.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.IType;

/**
 */
public abstract class AbstractDynamicSymbol extends Symbol implements IDynamicSymbol, IReducedSymbol
{
  protected ISymbolTable _symTable;
  protected IScriptPartId _scriptPartId;

  /**
   * Constructs AbstractDynamicSymbol for use with an IGosuParser's ISymbolTable.
   *
   * @param symTable The symbol table.
   * @param strName  The symbol name.
   * @param type     The IGosuParser specific type.
   */
  public AbstractDynamicSymbol( ISymbolTable symTable, CharSequence strName, IType type )
  {
    this( symTable, strName, type, null );
  }

  public AbstractDynamicSymbol( ISymbolTable symTable, CharSequence strName, IType type, Object value )
  {
    super( strName.toString(), type, null, value );
    _symTable = symTable;
  }

  protected int assignIndex( IScope scope )
  {
    return -1;
  }

  public void setDynamicSymbolTable( ISymbolTable symTable )
  {
    // Dynamic symbols don't live in the symbol table
  }

  public boolean hasDynamicSymbolTable()
  {
    return false;
  }

  /**
   * @return This is used for storing runtime implementation specific data. Typically,
   *         this field will contain a reference to the class (or executable unit)
   *         containing this function.
   */
  public IScriptPartId getScriptPart()
  {
    return _scriptPartId;
  }

  public void setScriptPart( IScriptPartId partId )
  {
    _scriptPartId = partId;
  }

  public IGosuClassInternal getGosuClass()
  {
    return _scriptPartId != null && _scriptPartId.getContainingType() instanceof IGosuClassInternal
           ? (IGosuClassInternal)_scriptPartId.getContainingType()
           : null;
  }

  public void clearDebugInfo()
  {
    Statement stmt = getCompiledStatementDirectly();
    if( stmt != null )
    {
      stmt.clearParseTreeInformation();
    }
  }

  Statement getCompiledStatementDirectly()
  {
    return (Statement)super.getValue();
  }

  public ISymbolTable getSymbolTable()
  {
    return _symTable;
  }

  @Override
  public boolean isFromJava()
  {
    return getScriptPart() != null &&
           getScriptPart().toString() != null &&
           getScriptPart().toString().startsWith( IGosuClass.PROXY_PREFIX );
  }

  abstract public ISymbol getLightWeightReference();

  abstract public AbstractDynamicSymbol getParameterizedVersion( IGosuClass gsClass );

  @Override
  public boolean isLocal() {
    return false;
  }

  @Override
  public boolean hasTypeVariables() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
