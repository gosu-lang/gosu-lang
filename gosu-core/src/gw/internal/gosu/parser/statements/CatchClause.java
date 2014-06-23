/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.config.CommonServices;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.Symbol;


import gw.lang.parser.statements.ICatchClause;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuObjectUtil;


/**
 * Represents a try-catch-finally-statement as specified in the Gosu grammar:
 * <pre>
 * <i>try-catch-finally-statement</i>
 *   <b>try</b> &lt;statement&gt; [ <b>catch</b> <b>(</b> &lt;identifier&gt; <b>)</b> &lt;statement&gt; ] [ <b>finally</b> &lt;statement&gt; ]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class CatchClause extends Statement implements ICatchClause
{
  private IType _catchType;
  private Statement _catchStmt;
  private Symbol _symbol;
  private int _iOffset;

  public IType getCatchType()
  {
    return _catchType;
  }

  public Statement getCatchStmt()
  {
    return _catchStmt;
  }

  public Symbol getSymbol()
  {
    return _symbol;
  }


  public CatchClause()
  {
  }

  public void init( IType iIntrinsicType, Statement catchStmt, Symbol symbol )
  {
    _catchType = iIntrinsicType;
    _catchStmt = catchStmt;
    if( catchStmt instanceof StatementList )
    {
      // Use the catch-claus's scope
      ((StatementList)catchStmt).setNoScope();
    }
    _symbol = symbol;
  }


  @Override
  public Object execute()
  {
    return _catchStmt.execute();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    return _catchStmt.getLeastSignificantTerminalStatement( bAbsolute );
  }

  @Override
  public String toString()
  {
    return "catch( " + (_symbol == null ? "" : _symbol.getName()) +
           (_catchType == null ? "" : (" : " + _catchType.getName())) + " )\n" +
           (_catchStmt == null ? "" : _catchStmt);

  }

  @Override
  public int getNameOffset( String identifierName )
  {
    return _iOffset;
  }

  @Override
  public void setNameOffset( int iOffset, String identifierName )
  {
    _iOffset = iOffset;
  }

  public boolean declares( String identifierName )
  {
    return _symbol != null &&
           GosuObjectUtil.equals( identifierName, _symbol.getName() );
  }

  public String[] getDeclarations()
  {
    if( _symbol != null )
    {
      return new String[]{_symbol.getName()};
    }
    return null;
  }

  public static IType getNakedCatchExceptionType()
  {
    IType type;
    if( CommonServices.getEntityAccess().getLanguageLevel().supportsNakedCatchStatements() )
    {
      type = JavaTypes.EXCEPTION();
    }
    else
    {
      type = JavaTypes.THROWABLE();
    }
    return type;
  }
}
