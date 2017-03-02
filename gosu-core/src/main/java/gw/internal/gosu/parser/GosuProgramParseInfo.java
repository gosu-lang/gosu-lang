/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbolTable;

/**
 */
public class GosuProgramParseInfo extends GosuClassParseInfo {
  private ISymbolTable _symTable;
  private ISymbolTable _additionalDFSDecls;
  private Expression _expr;
  private ExternalSymbolMapForMap _externalSymbols;
  private IParsedElement _evalExprOrAnyExpr;
  private IStatement _stmt;

  public GosuProgramParseInfo(IGosuClassInternal gosuClass) {
    super(gosuClass);
  }

  public ISymbolTable getSymbolTable() {
    return _symTable;
  }

  public ExternalSymbolMapForMap getExternalSymbols() {
    return _externalSymbols;
  }

  public ISymbolTable getAdditionalDFSDecls() {
    return _additionalDFSDecls;
  }

  public void setAdditionalDFSDecls(ISymbolTable symbolTable) {
    _additionalDFSDecls = symbolTable;
  }

  public void setSymbolTable(ISymbolTable symbolTable) {
    _symTable = symbolTable;
  }

  public void setExternalSymbols(ExternalSymbolMapForMap externalSymbolMapForMap) {
    _externalSymbols = externalSymbolMapForMap;
  }

  public void setExpression(Expression expr) {
    _expr = expr;
  }

  public IExpression getExpression() {
    return _expr;
  }

  public void setStatement(Statement stmt) {
    _stmt = stmt;
  }

  public IStatement getStatement() {
    return _stmt;
  }

  public IParsedElement getEvalExpression() {
    return _evalExprOrAnyExpr;
  }

  public void setEvalExpression( IParsedElement evalExprOrAnyExpr ) {
    _evalExprOrAnyExpr = evalExprOrAnyExpr;
  }

  @Override
  public void addMemberField( VarStatement varStmt ) {
    super.addMemberField( varStmt );
    if( !varStmt.isStatic() )
    {
      // Remove initializers, fields are assigned in the programs entry point function
      varStmt.setAsExpression( null );
    }
    varStmt.setIsInitializedTopLevelProgVar();
  }
}
