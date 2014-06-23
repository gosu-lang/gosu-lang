/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.statements.ICatchClause;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.ILoopStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.ITryCatchFinallyStatement;

import java.util.List;

/**
*/
class AssignmentOrReference {
  private IParsedElement _stmt;
  private ITerminalStatement _terminal;
  private VarInitializationVerifier.AssignedState _localState;
  private boolean _bBad;
  private boolean _bInLoop;
  private boolean _bReference;

  AssignmentOrReference( IStatement stmt, List<AssignmentOrReference> assignments, VarInitializationVerifier.AssignedState localState ) {
    _stmt = stmt;
    _localState = localState;
    determineBad( assignments, false );
  }

  AssignmentOrReference( IIdentifierExpression idExpr, List<AssignmentOrReference> assignments, VarInitializationVerifier.AssignedState localState ) {
    _stmt = idExpr;
    _bReference = true;
    _localState = localState;
    determineBad( assignments, false );
  }

  IParsedElement getStmt() {
    return _stmt;
  }

  ITerminalStatement getTerminal() {
    return _terminal;
  }
  void setTerminal( ITerminalStatement terminal ) {
    _terminal = terminal;
  }

  VarInitializationVerifier.AssignedState getLocalState() {
    return _localState;
  }

  boolean isBad() {
    return _bBad;
  }
  private void setBad( boolean bBad ) {
    _bBad = bBad;
  }

  boolean isReference() {
    return _bReference;
  }

  boolean isInLoop() {
    return _bInLoop;
  }

  void determineBad( List<AssignmentOrReference> assignments, boolean bInLoop ) {
    if( isBad() ) {
      // Already bad
      return;
    }
    if( bInLoop && isReference() ) {
      // This is intended for re-determining the badness of Assignments inside a loop
      return;
    }
    outer:
    for( AssignmentOrReference csr : assignments ) {
      if( csr.isReference() ) {
        continue;
      }
      if( csr.getTerminal() != null ) {

        // If the terminal's ctx contains this assignment, the csr is irrelevant, move to next
        switch( csr.getTerminal().getTerminalType() ) {
          case ReturnOrThrow:
          case ForeverLoop:
            continue;

          case Break:
          {
            IParsedElement pe = csr.getTerminal();
            while( pe != null ) {
              if( pe instanceof ILoopStatement ||
                  pe instanceof SwitchStatement ) {
                if( VarInitializationVerifier.isStatementContainedIn( getStmt(), pe ) ) {
                  continue outer;
                }
              }
              pe = pe.getParent();
            }
            break;
          }

          case Continue:
          {
            IParsedElement pe = csr.getTerminal();
            while( pe != null ) {
              if( pe instanceof ILoopStatement ) {
                if( VarInitializationVerifier.isStatementContainedIn( getStmt(), pe ) ) {
                  continue outer;
                }
              }
              pe = pe.getParent();
            }
            break;
          }
        }
      }
      else {
        if( separatedByIfElse( csr.getStmt(), getStmt() ) ) {
          continue;
        }
        if( separatedByCatchClauses( csr.getStmt(), getStmt() ) ) {
          continue;
        }
      }
      // The csr assignment may conflict with this one, so it's bad
      setBad( true );
      // This is only to indicate this is bad because it's in a loop, to provide a better error msg
      _bInLoop = bInLoop;
      break;
    }
    if( isReference() ) {
      setBad( !isBad() || getLocalState() != VarInitializationVerifier.AssignedState.Fully );
    }
  }

  private boolean separatedByIfElse( IParsedElement stmt1, IParsedElement stmt2 ) {
    return _separatedByIfElse( stmt1, stmt1, stmt2 );
  }
  private boolean _separatedByIfElse( IParsedElement origStmt, IParsedElement stmt1, IParsedElement stmt2 ) {
    if( stmt1 == null ) {
      return false;
    }
    if( stmt1 instanceof IIfStatement &&
        VarInitializationVerifier.isStatementContainedIn( origStmt, ((IIfStatement)stmt1).getStatement() ) ) {
      IStatement elseStmt = ((IIfStatement)stmt1).getElseStatement();
      if( elseStmt != null && VarInitializationVerifier.isStatementContainedIn( stmt2, elseStmt ) ) {
        return true;
      }
    }
    return _separatedByIfElse( origStmt, stmt1.getParent(), stmt2 );
  }

  private boolean separatedByCatchClauses( IParsedElement stmt1, IParsedElement stmt2 ) {
    if( stmt1 == null ) {
      return false;
    }
    if( stmt1 instanceof ICatchClause ) {
      IParsedElement parent = stmt1.getParent();
      if( parent instanceof ITryCatchFinallyStatement ) {
        for( ICatchClause catchClause: ((ITryCatchFinallyStatement)parent).getCatchStatements() ) {
          if( catchClause != stmt1 && VarInitializationVerifier.isStatementContainedIn( stmt2, catchClause ) ) {
            return true;
          }
        }
      }
    }
    return separatedByCatchClauses( stmt1.getParent(), stmt2 );
  }
}
