/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.GosuClassParseInfo;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.IGosuProgramInternal;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.ThisConstructorFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IInitializerSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.Keyword;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ICaseClause;
import gw.lang.parser.statements.ICatchClause;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IContinueStatement;
import gw.lang.parser.statements.IDoWhileStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IHideFieldNoOpStatement;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.ILoopStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ISwitchStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.ITryCatchFinallyStatement;
import gw.lang.parser.statements.IUsingStatement;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class VarInitializationVerifier {

  public enum AssignedState { Unassigned, Partially, Fully }

  private boolean _bFinalOnly;

  private VarInitializationVerifier( boolean bFinalOnly ) {
    _bFinalOnly = bFinalOnly;
  }

  private boolean isFinalOnly() {
    return _bFinalOnly;
  }

  /**
   * Verifies initialization of final fields in the provided class and, recursively, all nested inner classes:
   * <ol>
   *   <li>Verifies that a final field is fully initialized either in the declaration or in the constructor[s]</li>
   *   <li>Verifies that an assignment to a final field is mutually exclusive wrt other assignments to the field</li>
   *   <li>Verifies that a reference to a final field is in a position in the source where the final field is fully initialized</li>
   * </ol>
   */
  public static void verifyFinalFields( IGosuClass gsClass ) {
    new VarInitializationVerifier( true ).verifyFields( (IGosuClassInternal)gsClass );
  }

  /**
   * Verifies initialization of local vars in the provided class and, recursively, all nested inner classes:
   * <ol>
   *   <li>Verifies that a final local var is fully initialized either in the declaration or in the scope of the var</li>
   *   <li>Verifies that an assignment to a final local var is mutually exclusive wrt other assignments to the var</li>
   *   <li>Verifies that a reference to any local var, not just finals, is in a position in the source where the var is fully initialized</li>
   * </ol>
   */
  public static void verifyLocalVars( IGosuClass gsClass, boolean bFinalOnly ) {
    new VarInitializationVerifier( bFinalOnly ).verifyLocals( (IGosuClassInternal)gsClass);
  }

  private void verifyLocals( IGosuClassInternal gsClass ) {
    GosuClassParseInfo parseInfo = gsClass.getParseInfo();
    for( DynamicFunctionSymbol dfs : parseInfo.getConstructorFunctions().values() ) {
      IStatement stmt = (IStatement)dfs.getValueDirectly();
      verifyLocalsRecursively( stmt );
    }
    for( DynamicFunctionSymbol dfs : parseInfo.getMemberFunctions().values() ) {
      IStatement stmt = (IStatement)dfs.getValueDirectly();
      verifyLocalsRecursively( stmt );
    }
    for( DynamicFunctionSymbol dfs : parseInfo.getStaticFunctions() ) {
      IStatement stmt = (IStatement)dfs.getValueDirectly();
      verifyLocalsRecursively( stmt );
    }
  }

  private void verifyLocalsRecursively( IParsedElement pe ) {
    if( pe == null ||
        pe instanceof IBlockExpression ||
        pe instanceof IClassStatement ) {
      return;
    }
    if( pe instanceof IVarStatement && (!isFinalOnly() || ((IVarStatement)pe).isFinal()) ) {
      verifyLocalVar( (IVarStatement)pe );
    }
    IParseTree location = pe.getLocation();
    if( location != null && location.getChildren() != null ) {
      for( IParseTree child: location.getChildren() ) {
        verifyLocalsRecursively( child.getParsedElement() );
      }
    }
  }

  private void verifyLocalVar( IVarStatement varStmt ) {
    boolean bFinal = varStmt.isFinal();
    boolean bAssigned = varStmt.getAsExpression() != null || (varStmt.getSymbol() instanceof Symbol && ((Symbol)varStmt.getSymbol()).isImplicitlyInitialized());
    IStatement enclosingStatement = findEnclosingStatement( varStmt.getParent() );
    List<IStatement> trailingStmts = findTrailingStmts( enclosingStatement, varStmt );
    ArrayList<AssignmentOrReference> assignments = new ArrayList<AssignmentOrReference>();
    AssignedState state = getAssignedStateForStatements( varStmt.getSymbol(), assignments, trailingStmts.toArray( new IStatement[trailingStmts.size()] ), AssignedState.Unassigned );
    verifyVar( varStmt, false, bFinal, bAssigned, assignments );
// IDE-1508. Don't verify that a local var statement is initialized (we only need to verify references to the var, not the var itself)
//    if( bFinal && !bAssigned && state != AssignedState.Fully ) {
//      ParseException parseException = new ParseException( varStmt.getLineNum(), 1, varStmt.getLocation().getColumn(), varStmt.getLocation().getOffset(), varStmt.getLocation().getExtent(),
//                                                          new StandardSymbolTable(), Res.MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT, varStmt.getSymbol().getName() );
//      varStmt.addParseException( parseException );
//    }
  }

  private List<IStatement> findTrailingStmts( IStatement enclosingStatement, IVarStatement finalVar ) {
    IParseTree location = enclosingStatement.getLocation();
    List<IStatement> trailingStmts = new ArrayList<IStatement>();
    if( location != null ) {
      boolean bFound = false;
      for( IParseTree pt: location.getChildren() ) {
        IParsedElement child = pt.getParsedElement();
        if( child instanceof IStatement ) {
          if( !bFound ) {
            if( child == finalVar ) {
              bFound = true;
            }
          }
          else {
            trailingStmts.add( (IStatement)child );
          }
        }
      }
    }
    return trailingStmts;
  }

  private IStatement findEnclosingStatement( IParsedElement pe ) {
    if( pe instanceof IStatement ) {
      return (IStatement)pe;
    }
    return findEnclosingStatement( pe.getParent() );
  }

  private void verifyFields( IGosuClassInternal gsClass ) {
    // Instance fields
    for( VarStatement varStmt: gsClass.getParseInfo().getMemberFields().values() ) {
      if( varStmt.isFinal() ) {
        verifyInstanceField( gsClass, varStmt );
      }
    }
    // Static fields
    for( VarStatement varStmt: gsClass.getParseInfo().getStaticFields().values() ) {
      if( varStmt.isFinal() ) {
        verifyStaticField( gsClass, varStmt );
      }
    }
  }

  private void verifyInstanceField( IGosuClassInternal gsClass, VarStatement varStmt ) {
    boolean bAssigned = varStmt.getAsExpression() != null;
    AssignedState overall = null;
    if( gsClass instanceof IGosuProgramInternal ) {
      for( DynamicFunctionSymbol dfs : gsClass.getParseInfo().getMemberFunctions().values() ) {
        if( dfs.getDisplayName().equals( "evaluate" ) ) {
          overall = verifyInstanceFieldInConstructor( varStmt, bAssigned, overall, dfs );
        }
      }
    }
    else {
      for( DynamicFunctionSymbol dfs: gsClass.getParseInfo().getConstructorFunctions().values() ) {
        overall = verifyInstanceFieldInConstructor( varStmt, bAssigned, overall, dfs );
      }
      verifyInstanceFieldInOtherInstanceFields( gsClass, varStmt );
    }
    if( !bAssigned && overall != AssignedState.Fully ) {
      ParseException parseException = new ParseException( varStmt.getLineNum(), 1, varStmt.getLocation().getColumn(), varStmt.getLocation().getOffset(), varStmt.getLocation().getExtent(),
                                                          new StandardSymbolTable(), Res.MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT, varStmt.getSymbol().getName() );
      varStmt.addParseException( parseException );
    }
  }

  private void verifyInstanceFieldInOtherInstanceFields( IGosuClassInternal gsClass, VarStatement varStmt ) {
    if( gsClass instanceof IGosuProgram ) {
      return;
    }
    ArrayList<AssignmentOrReference> assignments = new ArrayList<AssignmentOrReference>();
    AssignedState state = AssignedState.Unassigned;
    for( IVarStatement vs: gsClass.getParseInfo().getMemberFields().values() ) {
      if( vs == varStmt ) {
        if( varStmt.getAsExpression() != null ) {
          assignments.add( new AssignmentOrReference( vs, assignments, state ) );
          state = AssignedState.Fully;
        }
      }
      else {
        IExpression expr = vs.getAsExpression();
        if( expr != null ) {
          getAssignedState( varStmt.getSymbol(), expr, assignments, state );
        }
      }
    }
    markErrorsOnBadAssignmentsAndReferences( varStmt, true, true, assignments );
  }

  private AssignedState verifyInstanceFieldInConstructor( VarStatement varStmt, boolean bAssigned, AssignedState overall, DynamicFunctionSymbol dfs ) {
    IStatement stmt = (IStatement)dfs.getValueDirectly();
    ArrayList<AssignmentOrReference> assignments = new ArrayList<AssignmentOrReference>();
    AssignedState initState = getAssignedState( varStmt.getSymbol(), dfs.getInitializer(), assignments, AssignedState.Unassigned );
    AssignedState state = getAssignedState( varStmt.getSymbol(), stmt, assignments, initState );
    state = state.ordinal() > initState.ordinal() ? state : initState;
    overall = overall == null ? state : overall.ordinal() > state.ordinal() ? state : overall;
    verifyVar( varStmt, true, true, bAssigned, assignments );
    return overall;
  }

  private void verifyVar( IVarStatement varStmt, boolean bField, boolean bFinal, boolean bAssigned, ArrayList<AssignmentOrReference> assignments ) {
    if( bAssigned ) {
      if( bFinal ) {
        markErrorsOnAssignmentsToFinal( varStmt, assignments );
      }
    }
    else {
      markErrorsOnBadAssignmentsAndReferences( varStmt, bField, bFinal, assignments );
    }
  }

  private void markErrorsOnAssignmentsToFinal( IVarStatement varStmt, ArrayList<AssignmentOrReference> assignments ) {
    for( AssignmentOrReference assignment : assignments ) {
      if( assignment.getStmt() instanceof IStatement ) {
        IStatement s = (IStatement)assignment.getStmt();
        ParseException parseException = new ParseException( s.getLineNum(), 1, s.getLocation().getColumn(), s.getLocation().getOffset(), s.getLocation().getExtent(),
                                                            new StandardSymbolTable(), Res.MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR, varStmt.getSymbol().getName() );
        s.addParseException( parseException );
      }
    }
  }

  private void markErrorsOnBadAssignmentsAndReferences( IVarStatement varStmt, boolean bField, boolean bFinal, ArrayList<AssignmentOrReference> assignments ) {
    for( AssignmentOrReference assignment : assignments ) {
      ResourceKey resKey;
      if( assignment.isBad() ) {
        if( assignment.getStmt() instanceof IStatement ) {
          if( bFinal ) {
            resKey = assignment.isInLoop()
                     ? Res.MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT_LOOP
                     : Res.MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT;
          }
          else {
            continue;
          }
        }
        else if( bFinal || (!bField && !isFinalOnly()) ) {
          resKey = Res.MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT;
        }
        else {
          continue;
        }
        IParsedElement s = assignment.getStmt();
        ParseException parseException = new ParseException( s.getLineNum(), 1, s.getLocation().getColumn(), s.getLocation().getOffset(), s.getLocation().getExtent(),
                                                            new StandardSymbolTable(), resKey, varStmt.getSymbol().getName() );
        s.addParseException( parseException );
      }
    }
  }

  private void verifyStaticField( IGosuClassInternal gsClass, VarStatement varStmt ) {
    //## todo: introduce static constructors

    boolean bAssigned = varStmt.getHasInitializer();
    if( !bAssigned ) {
      ParseException parseException = new ParseException( varStmt.getLineNum(), 1, varStmt.getLocation().getColumn(), varStmt.getLocation().getOffset(), varStmt.getLocation().getExtent(),
                                                          new StandardSymbolTable(), Res.MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT, varStmt.getSymbol().getName() );
      varStmt.addParseException( parseException );
    }
  }


  private AssignedState getAssignedState( ISymbol sym, IParsedElement s, ArrayList<AssignmentOrReference> assignments, AssignedState localState ) {
    if( s == null ) {
      return AssignedState.Unassigned;
    }

    AssignedState retState = null;

    if( s instanceof IFunctionStatement ) {
      IStatement stmt = (IStatement)((IFunctionStatement)s).getDynamicFunctionSymbol().getValueDirectly();
      retState = getAssignedState( sym, stmt, assignments, localState );
    }
    else if( s instanceof IAssignmentStatement ) {
      IAssignmentStatement stmt = (IAssignmentStatement)s;
      if( stmt.getIdentifier().getSymbol().getName().equals( sym.getName() ) ) {
        assignments.add( new AssignmentOrReference( stmt, assignments, localState ) );
        retState = AssignedState.Fully;
      }
      else {
        retState = AssignedState.Unassigned;
      }
      // Process the rhs for identifiers
      getAssignedState( sym, stmt.getExpression(), assignments, localState );
    }
    else if( s instanceof IHideFieldNoOpStatement ) {
      IHideFieldNoOpStatement stmt = (IHideFieldNoOpStatement)s;
      IVarStatement varStmt = stmt.getVarStmt();
      if( varStmt.getAsExpression() != null && varStmt.getSymbol().getName().equals( sym.getName() ) ) {
        assignments.add( new AssignmentOrReference( stmt, assignments, localState ) );
        retState = AssignedState.Fully;
      }
      else {
        retState = AssignedState.Unassigned;
      }
      // Process the rhs for identifiers
      getAssignedState( sym, varStmt.getAsExpression(), assignments, localState );
    }
    else if( s instanceof IMemberAssignmentStatement ) {
      IMemberAssignmentStatement stmt = (IMemberAssignmentStatement)s;
      IExpression rootExpr = stmt.getRootExpression();
      if( rootExpr instanceof IIdentifierExpression &&
          Keyword.KW_this.getName().equals( ((IIdentifierExpression)rootExpr).getSymbol().getName() ) &&
          stmt.getMemberName().equals( sym.getName() ) ) {
        assignments.add( new AssignmentOrReference( stmt, assignments, localState ) );
        retState = AssignedState.Fully;
      }
      else {
        retState = AssignedState.Unassigned;
        // Process the lhs root for identifiers
        getAssignedState( sym, stmt.getRootExpression(), assignments, localState );
      }
      // Process the rhs for identifiers
      getAssignedState( sym, stmt.getExpression(), assignments, localState );
    }
    else if( s instanceof IStatementList ) {
      IStatement[] statements = ((IStatementList)s).getStatements();
      retState = getAssignedStateForStatements( sym, assignments, statements, localState );
    }
    else if( s instanceof ILoopStatement ) {
      int iSize = assignments.size();
      AssignedState state = getAssignedState( sym, ((ILoopStatement)s).getStatement(), assignments, localState );
      if( iSize < assignments.size() ) {
        if( sym.isFinal() ) {
          // Re-evaluate whether or not the assignments in the loop are bad; they're only good if covered by a terminal statement
          for( int i = iSize; i < assignments.size(); i++ ) {
            AssignmentOrReference csr = assignments.get( i );
            csr.determineBad( assignments, true );
          }
        }
      }
      // Loops can't be fully assigned unless the condition is boolean literal true or it's a do-while loop
      retState = state == AssignedState.Fully && !((ILoopStatement)s).isConditionLiteralTrue() && !(s instanceof IDoWhileStatement)
                 ? AssignedState.Partially
                 : state;
      // Process the loop condition/expression for identifiers
      getAssignedState( sym, ((ILoopStatement)s).getExpression(), assignments, localState );
    }
    else if( s instanceof IMethodCallStatement ) {
      AssignedState state = AssignedState.Unassigned;
      IMethodCallExpression methodCall = ((IMethodCallStatement)s).getMethodCall();
      if( methodCall.getFunctionSymbol() instanceof ThisConstructorFunctionSymbol ) {
        IStatement stmt = (IStatement)((ThisConstructorFunctionSymbol)methodCall.getFunctionSymbol()).getDelegate().getValueDirectly();
        state = getAssignedState( sym, stmt, new ArrayList<AssignmentOrReference>()/*don't want these*/, localState );
        if( state != AssignedState.Unassigned ) {
          assignments.add( new AssignmentOrReference( (IStatement)s, assignments, localState ) );
        }
      }
      retState = state;
      // Process the args for identifiers
      IMethodCallExpression methodCallExpr = ((IMethodCallStatement)s).getMethodCall();
      if( methodCallExpr != null ) {
        IExpression[] args = methodCallExpr.getArgs();
        if( args != null ) {
          for( IExpression expr: args ) {
            getAssignedState( sym, expr, assignments, localState );
          }
        }
      }
    }
    else if( s instanceof IIfStatement ) {
      IIfStatement stmt = (IIfStatement)s;
      AssignedState mainStmtState = getAssignedState( sym, stmt.getStatement(), assignments, localState );
      IStatement elseStmt = stmt.getElseStatement();
      if( elseStmt != null ) {
        AssignedState elseStmtState = getAssignedState( sym, elseStmt, assignments, localState );
        switch( mainStmtState ) {
          case Fully:
            retState = elseStmtState == AssignedState.Fully
                       ? AssignedState.Fully
                       : AssignedState.Partially;
            break;
          case Partially:
            retState = AssignedState.Partially;
            break;
          case Unassigned:
            retState = elseStmtState == AssignedState.Unassigned
                       ? AssignedState.Unassigned
                       : AssignedState.Partially;
            break;
        }
      }
      else {
        retState = mainStmtState == AssignedState.Unassigned
                                    ? AssignedState.Unassigned
                                    : AssignedState.Partially;
      }
      // Process condition expr for identifiers
      getAssignedState( sym, stmt.getExpression(), assignments, localState );
    }
    else if( s instanceof ISwitchStatement ) {
      AssignedState state = null;
      IExpression switchExpression = ((ISwitchStatement)s).getSwitchExpression();
      IType enumType = switchExpression != null && switchExpression.getType().isEnum() ? switchExpression.getType() : null;
      List<String> enumValues = enumType != null ? ((IEnumType)enumType).getEnumConstants() : null;
      for( ICaseClause caseClause : ((ISwitchStatement)s).getCases() ) {
        if( enumValues != null && caseClause.getExpression().isCompileTimeConstant() ) {
          try {
            Object value = caseClause.getExpression().evaluate();
            if( value != null ) {
              enumValues.remove( value.toString() );
            }
          }
          catch( Exception err ) {
            // ignore
          }
        }
        List<? extends IStatement> statements = caseClause.getStatements();
        AssignedState csr = getAssignedStateForStatements( sym, assignments, statements.toArray( new IStatement[statements.size()] ), localState );
        boolean bTerminal = doStatementsTerminate( statements );
        if( bTerminal ) {
          state = state == null
                  ? csr
                  : state.ordinal() > csr.ordinal()
                    ? csr
                    : state;
        }
      }
      List<? extends IStatement> defaultStatements = ((ISwitchStatement)s).getDefaultStatements();
      if( defaultStatements != null ) {
        AssignedState csr = getAssignedStateForStatements( sym, assignments, defaultStatements.toArray( new IStatement[defaultStatements.size()] ), localState );
        state = state == null
                ? csr
                : state.ordinal() > csr.ordinal()
                  ? csr
                  : state;
      }
      else if( enumValues == null || !enumValues.isEmpty() ) {
        state = state == null || state == AssignedState.Unassigned
                ? AssignedState.Unassigned
                : AssignedState.Partially;
      }
      retState = state == null ? AssignedState.Unassigned : state;
      // Process switch expr for identifiers
      getAssignedState( sym, switchExpression, assignments, localState );
    }
    else if( s instanceof ITryCatchFinallyStatement ) {
      ITryCatchFinallyStatement tryCatchFinallyStmt = (ITryCatchFinallyStatement)s;
      AssignedState state = getAssignedState( sym, tryCatchFinallyStmt.getTryStatement(), assignments, localState );
      List<? extends ICatchClause> catchStatements = tryCatchFinallyStmt.getCatchStatements();
      if( catchStatements != null ) {
        for( ICatchClause catchClause: catchStatements ) {
          AssignedState caseState = getAssignedState( sym, catchClause.getCatchStmt(), assignments, localState );
          if( caseState != AssignedState.Unassigned && state == AssignedState.Unassigned ) {
            state = AssignedState.Partially;
          }
        }
      }
      IStatement finallyStmt = tryCatchFinallyStmt.getFinallyStatement();
      if( finallyStmt != null ) {
        AssignedState finallyState = getAssignedState( sym, finallyStmt, assignments, localState );
        state = state.ordinal() > finallyState.ordinal() ? state : finallyState;
      }
      retState = state;
    }
    else if( s instanceof IUsingStatement ) {
      retState = getAssignedState( sym, ((IUsingStatement)s).getStatement(), assignments, localState );
      // Process expr for identifiers
      getAssignedState( sym, ((IUsingStatement)s).getExpression(), assignments, localState );
    }
    else if( s instanceof IIdentifierExpression ) {
      ISymbol symbol = ((IIdentifierExpression)s).getSymbol();
      if( !(symbol instanceof IInitializerSymbol) &&
          symbol.getName().equals( sym.getName() ) ) {
        assignments.add( new AssignmentOrReference( (IIdentifierExpression)s, assignments, localState ) );
      }
      retState = AssignedState.Unassigned;
    }
    else if( !(s instanceof IBlockExpression) &&
             !(s instanceof IClassStatement) ) {
      IParseTree location = s.getLocation();
      if( location != null ) {
        for( IParseTree child: location.getChildren() ) {
          getAssignedState( sym, child.getParsedElement(), assignments, localState );
        }
      }
      retState = AssignedState.Unassigned;
    }
    return retState;
  }

  private boolean doStatementsTerminate( List<? extends IStatement> statements ) {
    for( IStatement stmt : statements ) {
      boolean[] bAbsolute = {false};
      ITerminalStatement terminalStmt = stmt.getLeastSignificantTerminalStatement( bAbsolute );
      if( terminalStmt != null && bAbsolute[0] )
      {
        return true;
      }
    }
    return false;
  }

  private IParsedElement getTerminalParent( IParsedElement pe ) {
    IParsedElement parent = pe.getParent();
    if( parent instanceof IStatementList ) {
      do {
        pe = parent;
        parent = parent.getParent();
      } while( parent instanceof IStatementList );
      parent = pe; // outermost statement-list
    }

    //## todo: !!!! this is wrong, we need to find all the case clauses that apply (fall thru) and those are the collective parent
    if( parent instanceof ICaseClause ) {
      parent = parent.getParent(); // switch-stmt is parent of terminals in case-clauses
    }
    return parent;
  }

  static boolean isStatementContainedIn( IParsedElement stmt, IParsedElement container ) {
    if( container == null ) {
      return false;
    }
    while( container != stmt ) {
      if( stmt == null ) {
        return false;
      }
      stmt = stmt.getParent();
    }
    return true;
  }

  private AssignedState getAssignedStateForStatements( ISymbol sym, ArrayList<AssignmentOrReference> assignments, IStatement[] statements, AssignedState localState ) {
    AssignedState state = AssignedState.Unassigned;
    if( statements != null ) {
      for( IStatement stmt : statements ) {
        AssignedState csr = getAssignedState( sym, stmt, assignments, localState.ordinal() > state.ordinal() ? localState : state );
        assignTerminalStatement( assignments, stmt );
        state = csr.ordinal() > state.ordinal() ? csr : state;
      }
    }
    return state;
  }

  private void assignTerminalStatement( ArrayList<AssignmentOrReference> assignments, IStatement stmt ) {
    boolean[] bAbsolute = {false};
    ITerminalStatement terminalStmt = stmt.getLeastSignificantTerminalStatement( bAbsolute );
    if( terminalStmt != null && bAbsolute[0] ) {
      for( AssignmentOrReference assignment : assignments ) {
        if( !assignment.isReference() && isStatementContainedIn( assignment.getStmt(), getTerminalParent( stmt ) ) ) {
          if( assignment.getTerminal() == null || !isStatementContainedIn( terminalStmt, getTerminalContext( assignment.getTerminal() ) ) ) {
            assignment.setTerminal( terminalStmt );
          }
        }
      }
    }
  }

  private IParsedElement getTerminalContext( ITerminalStatement terminal ) {
    if( terminal instanceof IBreakStatement ) {
      return findBreakStatementContext( terminal );
    }
    if( terminal instanceof IContinueStatement ) {
      return findContinueStatementContext( terminal );
    }
    return findEnclosingFunctionStatement( terminal );
  }

  private IParsedElement findEnclosingFunctionStatement( ITerminalStatement terminal ) {
    return findFirstEnclosing( terminal, new Class[] {IFunctionStatement.class} );
  }

  private IParsedElement findContinueStatementContext( ITerminalStatement terminal ) {
    return findFirstEnclosing( terminal, new Class[] {ILoopStatement.class} );
  }

  private IParsedElement findBreakStatementContext( ITerminalStatement terminal ) {
    return findFirstEnclosing( terminal, new Class[] {ILoopStatement.class, ISwitchStatement.class} );
  }

  private IParsedElement findFirstEnclosing( IParsedElement csr, Class[] classes ) {
    if( csr == null ) {
      return null;
    }
    for( Class cls : classes ) {
      //noinspection unchecked
      if( cls.isAssignableFrom( csr.getClass() ) ) {
        return csr;
      }
    }
    return findFirstEnclosing( csr.getParent(), classes );
  }

}
