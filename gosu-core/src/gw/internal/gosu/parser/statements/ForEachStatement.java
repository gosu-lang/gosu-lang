/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;




import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.Symbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.statements.IAssertStatement;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.ILoopStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IThrowStatement;
import gw.util.GosuObjectUtil;

/**
 * Represents a foreach statement as specified in the Gosu grammar:
 * <pre>
 * <i>for...in-statement</i>
 *   <b>for</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;expression&gt; [ <b>index</b> &lt;identifier&gt; ] <b>)</b> &lt;statement&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ForEachStatement extends LoopStatement implements IForEachStatement
{
  protected Symbol _identifier;
  protected Expression _expression;
  protected Symbol _indexIdentifier;
  private Symbol _iterIdentifier;
  protected Statement _statement;
  protected IStackProvider _stackProvider;
  private int _iIdentifierOffset;
  private int _iIndexIdentifierOffset;
  private int _iIterOffset;
  private boolean _bStructuralIterable;

  /**
   * Constructs a ForEachStatement given an ISymbolTable instance.
   */
  public ForEachStatement( ISymbolTable stackProvider )
  {
    _stackProvider = stackProvider;
  }

  /**
   * @return The name of the Indentifier in the statement.
   */
  public Symbol getIdentifier()
  {
    return _identifier;
  }

  /**
   * @param identifier
   */
  public void setIdentifier( Symbol identifier )
  {
    _identifier = identifier;
  }

  /**
   * @return The name of the Index Identifier, or null of not specified.
   */
  public Symbol getIndexIdentifier()
  {
    return _indexIdentifier;
  }

  /**
   * @param indexIdentifier
   */
  public void setIndexIdentifier( Symbol indexIdentifier )
  {
    _indexIdentifier = indexIdentifier;
  }


  /**
   * @return The name of the Index Identifier, or null of not specified.
   */
  public Symbol getIteratorIdentifier()
  {
    return _iterIdentifier;
  }

  public void setIteratorIdentifier( Symbol iterIdentifier )
  {
    _iterIdentifier = iterIdentifier;
  }

  @Override
  public IExpression getExpression()
  {
    return getInExpression();
  }

  /**
   * @return The In Expression.
   */
  public Expression getInExpression()
  {
    return _expression;
  }

  /**
   * @param expression The In Expression.
   */
  public void setInExpression( Expression expression )
  {
    _expression = expression;
  }

  /**
   * @return The statement to execute in the interation.
   */
  public Statement getStatement()
  {
    return _statement;
  }

  /**
   * @param statement The statement to execute in the interation.
   */
  public void setStatement( Statement statement )
  {
    _statement = statement;
    if( _statement instanceof StatementList )
    {
      // Use this for-stmt's scope. This is purely a performance feature and not
      // without a drawback -- in the debugger this for-stmt's scope may have
      // residual symbols from the execution of its statement-list. This is
      // really only an issue in the debugger where one may see symbols that are
      // left over from the previous step in the loop.
      ((StatementList)_statement).setNoScope();
    }
  }

  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    if( _statement != null )
    {
      ITerminalStatement terminalStmt = _statement.getLeastSignificantTerminalStatement( bAbsolute );
      if( terminalStmt instanceof IReturnStatement ||
          terminalStmt instanceof IAssertStatement ||
          terminalStmt instanceof IThrowStatement ||
          terminalStmt instanceof ILoopStatement )
      {
        bAbsolute[0] = false;
        return terminalStmt;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    String strIndex = _indexIdentifier == null ? null : _indexIdentifier.getName();
    if( strIndex != null )
    {
      strIndex = " index " + strIndex;
    }
    else
    {
      strIndex = "";
    }

    return "for( " + (getIdentifier() == null ? "" : getIdentifier().getName()) + " in " + toString(getInExpression()) + strIndex + ")\n" +
           toString(getStatement());
  }
  
  private String toString(Object o) {
    return o == null ? "" : o.toString();
  }

  @Override
  public int getNameOffset( String identifierName )
  {
    if (identifierName.toString().equals(_identifier.getName())) {
      return _iIdentifierOffset;
    } else if (identifierName.toString().equals(_indexIdentifier.getName())) {
      return _iIndexIdentifierOffset;
    } else if (identifierName.toString().equals(_iterIdentifier.getName())) {
      return _iIterOffset;
    } else {
      throw new RuntimeException("Wrong name " + identifierName);
    }
  }
  @Override
  public void setNameOffset( int iOffset, String identifierName )
  {
    _iIdentifierOffset = iOffset;
  }
  public void setIndexNameOffset( int iOffset )
  {
    _iIndexIdentifierOffset = iOffset;
  }
  public void setIterNameOffset( int iOffset )
  {
    _iIterOffset = iOffset;
  }


  public boolean declares( String identifierName )
  {
    return ((getIdentifier() != null) && GosuObjectUtil.equals( getIdentifier().getName(), identifierName )) ||
           ((getIndexIdentifier() != null) && GosuObjectUtil.equals( getIndexIdentifier().getName(), identifierName )) ||
           ((getIteratorIdentifier() != null) && GosuObjectUtil.equals( getIteratorIdentifier().getName(), identifierName ));
  }

  public String[] getDeclarations() {
    if (getIndexIdentifier() != null) {
      return new String[] {getIdentifier().getName(), getIndexIdentifier().getName()};
    } else if (getIdentifier() != null) {
      return new String[] {getIdentifier().getName()};
    } else if (getIteratorIdentifier() != null) {
      return new String[] {getIteratorIdentifier().getName()};
    } else {
      return new String[1];
    }
  }

  public boolean isStructuralIterable()
  {
    return _bStructuralIterable;
  }
  public void setStructuralIterable( boolean bStructuralIterable )
  {
    _bStructuralIterable = bStructuralIterable;
  }
}
