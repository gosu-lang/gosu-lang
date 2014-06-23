/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IProgramInstance;
import gw.util.GosuExceptionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * The root class for all Statements represented in a parse tree. As specified
 * in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public abstract class Statement extends ParsedElement implements IStatement
{
  private static final LeastSigTerminal EMPTY_LST = new LeastSigTerminal( null, false );

  public static final Object VOID_RETURN_VALUE = new Object(){};

  private LeastSigTerminal _cachedLeastSigTerm;

  public Statement()
  {
  }

  @Override
  public Object execute()
  {
    Class<?> cls = getGosuProgram().getBackingClass();
    try
    {
      return ((IProgramInstance)cls.newInstance()).evaluate(null);
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  @Override
  public Object execute(IExternalSymbolMap externalSymbols)
  {
    Class<?> cls = getGosuProgram().getBackingClass();
    try
    {
      return ((IProgramInstance)cls.newInstance()).evaluate(externalSymbols);
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  /**
   * Subclasses should return a String representing the parsed statement.
   */
  public abstract String toString();

  public boolean isNoOp()
  {
    return false;
  }

  public IType getReturnType()
  {
    ArrayList returnStatements = new ArrayList<ReturnStatement>();
    ArrayList<IType> returnTypes = new ArrayList<IType>();
    getContainedParsedElementsByTypesWithIgnoreSet( returnStatements, new HashSet(getExcludedReturnTypeElements()), ReturnStatement.class );
    for( int i = 0; i < returnStatements.size(); i++ )
    {
      ReturnStatement returnStmt = (ReturnStatement)returnStatements.get( i );
      returnTypes.add( returnStmt.getValue().getType() );
    }
    return TypeLord.findLeastUpperBound( returnTypes );
  }

  protected List getExcludedReturnTypeElements()
  {
    return Arrays.asList(IFunctionStatement.class, BlockExpression.class);
  }

  public boolean hasContent()
  {
    if( this instanceof IStatementList )
    {
      IStatement[] statements = ((IStatementList)this).getStatements();
      if( statements != null )
      {
        for( int i = 0; i < statements.length; i++ )
        {
          IStatement statement = statements[i];
          if( !(statement instanceof INoOpStatement) )
          {
            return true;
          }
        }
      }
      return false;
    }
    else
    {
      return !(this instanceof INoOpStatement);
    }
  }

  @Override
  public void setParent( IParsedElement rootElement )
  {
    super.setParent( rootElement );
    _cachedLeastSigTerm = null;
  }

  @Override
  public final ITerminalStatement getLeastSignificantTerminalStatement( boolean[] bAbsolute ) {
    if( _cachedLeastSigTerm == null ) {
      ITerminalStatement stmt = getLeastSignificantTerminalStatement_internal( bAbsolute );
      if( stmt == null ) {
        _cachedLeastSigTerm = EMPTY_LST;
      }
      else {
        _cachedLeastSigTerm = new LeastSigTerminal( stmt, bAbsolute[0] );
      }
    }
    bAbsolute[0] = _cachedLeastSigTerm._bAbsolute;
    return _cachedLeastSigTerm._stmt;
  }

  protected abstract ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute );

  public static ITerminalStatement getLeastSignificant( ITerminalStatement... termStmts ) {
    ITerminalStatement ret = null;

    for( ITerminalStatement stmt : termStmts ) {
      if( stmt != null ) {
        ret = ret == null
              ? stmt
              : ret.getTerminalType().ordinal() < stmt.getTerminalType().ordinal()
                ? ret
                : stmt;
      }
    }
    return ret;
  }

  private static class LeastSigTerminal {
    private ITerminalStatement _stmt;
    private boolean _bAbsolute;

    public LeastSigTerminal( ITerminalStatement stmt, boolean bAbsolute ) {
      _stmt = stmt;
      _bAbsolute = bAbsolute;
    }
  }
}
