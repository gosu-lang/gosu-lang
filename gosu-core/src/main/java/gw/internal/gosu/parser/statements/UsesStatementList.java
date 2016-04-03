/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.IUsesStatementList;

import java.util.List;

public class UsesStatementList extends Statement implements IUsesStatementList
{
  private List<IUsesStatement> _stmts;

  public UsesStatementList()
  {
  }

  public List<IUsesStatement> getUsesStatements()
  {
    return _stmts;
  }

  public void setUsesStatements( List<IUsesStatement> stmts )
  {
    _stmts = stmts;
  }

  public Object execute()
  {
    // no-op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  public IUsesStatement conflicts( IUsesStatement stmt ) {
    String fqn = stmt.getTypeName();
    if( fqn == null ) {
      return null;
    }
    String relativeName = getRelativeName( fqn );
    boolean bPackage = relativeName.equals( "*" );
    for( IUsesStatement csrStmt : _stmts ) {
      if( csrStmt.isFeatureSpace() || csrStmt.getFeatureInfo() != null ) {
        continue;
      }
      String csrFqn = csrStmt.getTypeName();
      if( csrFqn == null ) {
        continue;
      }
      if( csrFqn.equals( fqn ) ) {
        // Handles duplicate package imports e.g., two or more com.abc.*
        return csrStmt;
      }
      String csrRelativeName = getRelativeName( csrFqn );
      if( !bPackage && csrRelativeName.equals( relativeName ) ) {
        // Handles imports with same relative name e.g., abc.Foo and yoyo.Foo
        return csrStmt;
      }
    }
    return null;
  }

  private String getRelativeName( String fqn ) {
    if( fqn == null ) {
      return "";
    }
    int iDot = fqn.lastIndexOf( '.' );
    if( iDot >= 0 ) {
      if( iDot < fqn.length()-1 ) {
        fqn = fqn.substring( iDot + 1 );
      }
      else {
        fqn = "";
      }
    }
    return fqn;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if( _stmts != null ) {
      for( IUsesStatement stmt : _stmts ) {
        sb.append( stmt.toString() + "\n" );
      }
    }
    return sb.toString();
  }

}
