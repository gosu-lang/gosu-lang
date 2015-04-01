/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 * Represents a member-assignment statement as specified in the Gosu grammar:
 * <pre>
 * <i>member-assignment-statement</i>
 *   &lt;member-access&gt; <b>=</b> &lt;expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class MemberAssignmentStatement extends Statement implements IMemberAssignmentStatement
{
  private Expression _rootExpression;
  private String _strMemberName;
  private Expression _memberExpression;
  private Expression _expression;
  private MemberAccess _ma;
  private boolean _compoundStatement;

  /**
   * Constructs a MemberAssignmentStatement given an ISymbolTable instance.
   */
  public MemberAssignmentStatement()
  {
  }

  public Expression getRootExpression()
  {
    return _rootExpression;
  }

  public void setRootExpression( Expression rootExpression )
  {
    _rootExpression = rootExpression;
  }

  /**
   * @return The name of the property assigned to (the lhs property)
   */
  public String getMemberName()
  {
    return _strMemberName;
  }
  public void setMemberName( String strMemberName )
  {
    _strMemberName = strMemberName;
  }

  /**
   * @return The expression to evaluate and assign to the member
   */
  public Expression getExpression()
  {
    return _expression;
  }
  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  public Expression getMemberExpression()
  {
    return _memberExpression;
  }

  public void setMemberExpression( Expression memberExpression )
  {
    _memberExpression = memberExpression;
  }

  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new IllegalStateException( "Can't execute this parsed element directly" );
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public String toString()
  {
    String strOut = getRootExpression().toString();
    if( _memberExpression != null )
    {
      strOut += "[" + _memberExpression.toString() + "]";
    }
    else
    {
      strOut += "." + _strMemberName;
    }
    Expression expression = getExpression();
    strOut += " = ";
    if( expression == null )
    {
      strOut += "null!";
    }
    else
    {
      strOut += expression.toString();
    }
    return strOut;
  }

  public MemberAccess getMemberAccess()
  {
    return _ma;
  }
  public void setMemberAccess( MemberAccess ma )
  {
    _ma = ma;
  }

  public void setCompoundStatement( boolean compoundStatement )
  {
    _compoundStatement = compoundStatement;
  }

  public boolean isCompoundStatement()
  {
    return _compoundStatement;
  }
}
