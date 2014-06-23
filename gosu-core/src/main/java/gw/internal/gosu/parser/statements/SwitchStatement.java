/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ISwitchStatement;
import gw.lang.parser.statements.ITerminalStatement;

import java.util.List;


/**
 * Represents a switch-statement as specified in the Gosu grammar:
 * <pre>
 * <i>switch-statement</i>
 *   <b>switch</b> <b>(</b>&lt;expression&gt;<b>) {</b> [switch-cases] [switch-default] <b>}</b>
 * <p/>
 * <i>switch-cases</i>
 *   &lt;switch-case&gt;
 *   &lt;switch-cases&gt; &lt;switch-case&gt;
 * <p/>
 * <i>switch-case</i>
 *   <b>case</b> &lt;expression&gt; <b>:</b> [statement-list]
 * <p/>
 * <i>switch-default</i>
 *   <b>default</b> <b>:</b> [statement-list]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class SwitchStatement extends Statement implements ISwitchStatement
{
  protected Expression _switchExpression;
  protected CaseClause[] _cases;
  protected List<Statement> _defaultStatements;

  public Expression getSwitchExpression()
  {
    return _switchExpression;
  }

  public void setSwitchExpression( Expression switchExpression )
  {
    _switchExpression = switchExpression;
  }

  public CaseClause[] getCases()
  {
    return _cases;
  }

  public void setCases( CaseClause[] cases )
  {
    _cases = cases;
  }

  public List<Statement> getDefaultStatements()
  {
    return _defaultStatements;
  }

  public void setDefaultStatements( List<Statement> defaultStatements )
  {
    _defaultStatements = defaultStatements;
  }

  /**
   * Execute the switch statement
   */
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new CannotExecuteGosuException();
  }

  /**
   * bAbsolute is true iff there are no break terminals anywhere in any cases and
   * the default clause's terminator is non-break and absolute
   */
  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    ITerminalStatement termRet = null;
    bAbsolute[0] = true;
    boolean bBreak = false;
    if( _cases != null )
    {
      for( int i = 0; i < _cases.length; i++ )
      {
        List caseStatements = _cases[i].getStatements();
        if( caseStatements != null && caseStatements.size() > 0 )
        {
          boolean bCaseAbs = true;
          for( int iStmt = 0; iStmt < caseStatements.size(); iStmt++ )
          {
            boolean[] bCsr = {false};
            ITerminalStatement terminalStmt = ((Statement)caseStatements.get( iStmt )).getLeastSignificantTerminalStatement( bCsr );
            if( terminalStmt != null )
            {
              bCaseAbs = bCsr[0];
              if( !(terminalStmt instanceof IBreakStatement) ) {
                termRet = getLeastSignificant( termRet, terminalStmt );
              }
              else {
                bAbsolute[0] = bCaseAbs = false;
                bBreak = true;
              }
              //## todo: this can be true iff the switch-expr type is an enum and all the constants are covered in the cases and they all have the same absolute non-break terminal (not an unusual use-case)
            }
          }
          bAbsolute[0] = bAbsolute[0] && bCaseAbs;
        }
      }
    }
    boolean bDefaultContributed = false;
    if( _defaultStatements != null && _defaultStatements.size() > 0 ) {
      if( !bBreak ) {
        // If none of the cases have a break, the cases all either fall through to the default clause
        // or never flow to the next stmt following this switch.  Therefore, the default clause's terminal,
        // if one is present, establishes the switch's terminal.
        bAbsolute[0] = true;
      }
      boolean bDefaultAbs = false;
      for( int i = 0; i < _defaultStatements.size(); i++ )
      {
        boolean[] bCsr = {false};
        ITerminalStatement terminalStmt = _defaultStatements.get( i ).getLeastSignificantTerminalStatement( bCsr );
        if( terminalStmt != null ) {
          if( !(terminalStmt instanceof IBreakStatement) ) {
            bDefaultAbs = bCsr[0];
            termRet = getLeastSignificant( termRet, terminalStmt );
            bDefaultContributed = true;
          }
        }
      }
      bAbsolute[0] = bAbsolute[0] && bDefaultAbs;
    }
    bAbsolute[0] = bAbsolute[0] && termRet != null && bDefaultContributed;
    return termRet;
  }

  @Override
  public String toString()
  {
    String strRet = "switch( " + getSwitchExpression().toString() + " )\n {\n";
    if( _cases != null )
    {
      for( int i = 0; i < _cases.length; i++ )
      {
        strRet += "case " + _cases[i].getExpression().toString() + ":\n";
        List caseStatements = _cases[i].getStatements();
        if( caseStatements != null )
        {
          for( int iStmt = 0; iStmt < caseStatements.size(); iStmt++ )
          {
            strRet += (caseStatements.get( iStmt )).toString();
          }
        }
      }

      if( _defaultStatements != null )
      {
        strRet += "default:\n";
        for( int iStmt = 0; iStmt < _defaultStatements.size(); iStmt++ )
        {
          strRet += (_defaultStatements.get( iStmt )).toString();
        }
      }
    }
    strRet += "\n}";
    return strRet;
  }


}
