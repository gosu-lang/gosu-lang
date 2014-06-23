/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.config.CommonServices;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.statements.CatchClause;
import gw.internal.gosu.parser.statements.TryCatchFinallyStatement;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.statement.IRCatchClause;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRIfStatement;
import gw.lang.ir.statement.IRThrowStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.parser.EvaluationException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 */
public class TryCatchFinallyStatementTransformer extends AbstractStatementTransformer<TryCatchFinallyStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, TryCatchFinallyStatement stmt )
  {
    TryCatchFinallyStatementTransformer compiler = new TryCatchFinallyStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private TryCatchFinallyStatementTransformer( TopLevelTransformationContext cc, TryCatchFinallyStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IRStatement tryBody = _cc().compile( _stmt().getTryStatement() );

    List<IRCatchClause> catchStatements = Collections.emptyList();
    if (_stmt().getCatchStatements() != null ) {
      catchStatements = compileCatchStatements();
    }

    IRStatement finallyBody = null;
    if (_stmt().getFinallyStatement() != null) {
      finallyBody = _cc().compile( _stmt().getFinallyStatement() );
    }
    return new IRTryCatchFinallyStatement( tryBody, catchStatements, finallyBody );
  }

  private List<IRCatchClause> compileCatchStatements( )
  {
    List<IRCatchClause> resultingClauses = new ArrayList<IRCatchClause>();
    List<CatchClause> catchStmts = _stmt().getCatchStatements();
    List<CatchClause> otherCatches = new ArrayList<CatchClause>();
    boolean bThrowableFound = false;
    for( CatchClause c : catchStmts )
    {
      Statement catchStmt = c.getCatchStmt();
      _cc().pushScope( false ); // scope for the exception param
      try
      {
        IType type = c.getCatchType();
        if( (type == null || isBytecodeType( type )) && otherCatches.isEmpty() )
        {
          // We need to set up the symbol used within the catch clause.  However, that symbol could be untyped or it
          // could need to be boxed; if so those will be the first statements in the catch body.
          Symbol catchSymbol = c.getSymbol();
          IRSymbol exceptionParam = createCatchClauseSymbol(catchSymbol, type );
          List<IRStatement> bodyStatements = new ArrayList<IRStatement>();
          // We need to wrap (and box) if the declared type is null, otherwise we just need to box
          if ( wrapInEvalException( type ) ) {
            bodyStatements.add( wrapUndeclaredAsEvaluationException( exceptionParam, catchSymbol.getName(), catchSymbol.isValueBoxed() ) );
          } else if ( catchSymbol.isValueBoxed() ) {
            bodyStatements.add( boxCatchSymbol( exceptionParam.getType(), catchSymbol.getName(), identifier( exceptionParam ) ) );
          }
          bodyStatements.add( _cc().compile(catchStmt) );
          resultingClauses.add(new IRCatchClause( exceptionParam, new IRStatementList( false, bodyStatements ) ) );
        }
        else
        {
          bThrowableFound = bThrowableFound || wrapInEvalException( type ) || type == JavaTypes.THROWABLE();
          otherCatches.add( c );
        }
      }
      finally
      {
        _cc().popScope();
      }
    }

    if( !otherCatches.isEmpty() )
    {
      if( !bThrowableFound )
      {
        CatchClause catchClause = new CatchClause();
        catchClause.init( JavaTypes.THROWABLE(), null, null );
        otherCatches.add( catchClause );
      }
      compileOtherCatchStatements(_cc(), otherCatches, resultingClauses );
    }

    return resultingClauses;
  }

  private boolean wrapInEvalException( IType type )
  {
    return type == null && !CommonServices.getEntityAccess().getLanguageLevel().supportsNakedCatchStatements();
  }

  private IRSymbol createCatchClauseSymbol( Symbol symbol, IType type) {
    boolean temp = symbol.isValueBoxed() || (wrapInEvalException( type ));
    String name = symbol.getName();
    if (temp) {
      name = name + "$$temp";
    }
    if (type == null)
    {
      type = CatchClause.getNakedCatchExceptionType();
    }
    IRSymbol irSymbol = new IRSymbol( name, getDescriptor( type ), temp);
    _cc().putSymbol( irSymbol );
    return irSymbol;
  }

  private IRStatement wrapUndeclaredAsEvaluationException( IRSymbol catchSymbol, String properName, boolean isBoxed)
  {
    IRExpression wrapper = wrapCatchSymbol( identifier( catchSymbol ) );
    if (isBoxed) {
      return boxCatchSymbol( catchSymbol.getType(), properName, wrapper );
    } else {
      return reassignCatchSymbol( catchSymbol.getType(), properName, wrapper );
    }
  }

  /**
   * Handle case where a catch clause declares a non-bytecode exception type e.g., soap exception type.
   * In such a case the catch clause and all subsequent catch clauses must be handled in a single
   * catch-clause for Throwable. If none of the subsequent catch clauses handle Throwable explicitly
   * and none of the clauses field the exception, it is rethrown. Note if a 'default' catch clause
   * is declard (i.e. a JavaScript-style catch clause with no exception type declared), it is treated
   * as handling Throwable explicitly; in other words no code is generated to rethrow the exception.
   * <p>
   * For example, the following try-catch statement declares a soap exception, which is not really
   * a valid Java exception type. It's a simple case with no finally clause and no default catch, but
   * it illustrates the essence of the problem.
   * <pre>
   * try {
   *   doSomething()
   * }
   * catch( re : RuntimeException ) {
   *   print( "RuntimeException" )
   * }
   * catch( fake : DoesntActuallyExtendThrowable ) {
   *   print( "DoesntActuallyExtendThrowable" )
   * }
   * catch( e : RealException ) {
   *   print( "RealException" )
   * }
   * </pre>
   * We must treat this statement as the following (simplified) code:
   * <pre>
   * try {
   *   doSomething()
   * }
   * catch( re : RuntimeException ) {
   *   print( "RuntimeException" )
   * }
   * catch( t : Throwable ) {
   *   var rtt = TypeSystem.getFromObject( t )
   *   if( DoesntActuallyExtendThrowable.Type.isAssignableFrom( rtt ) ) {
   *     print( "DoesntActuallyExtendThrowable" )
   *   }
   *   if( RealException.Type.isAssignableFrom( rtt ) ) {
   *     print( "RealException" )
   *   }
   *   else {
   *     throw t
   *   }
   * }
   * </pre>
   */
  private void compileOtherCatchStatements( final TopLevelTransformationContext cc, List<CatchClause> otherCatches, List<IRCatchClause> resultingClauses )
  {
    cc.pushScope( false );
    try {
      // Create a temp symbol to represent the exception variable for the synthetic clause
      IRSymbol exceptionSymbol = new IRSymbol("exception$$temp$$var", getDescriptor( Throwable.class ), true);
      cc.putSymbol( exceptionSymbol );
      List<IRStatement> catchBody = new ArrayList<IRStatement>();

      // Store the exception type in a temporary variable
      IRSymbol exceptionTypeSymbol = new IRSymbol("exceptiontype$$temp$$var", IRTypeConstants.ITYPE(), true);
      cc.putSymbol( exceptionTypeSymbol );
      catchBody.add( buildAssignment( exceptionTypeSymbol, callStaticMethod( TypeSystem.class, "getFromObject", new Class[]{Object.class}, exprList( identifier( exceptionSymbol ) ) ) ) );

      // Build up a nested if-statement
      IRIfStatement lastStatement = null;
      for( CatchClause clause : otherCatches )
      {
        cc.pushScope( false ); // scope for the exception param
        try
        {
          Symbol symbol = clause.getSymbol();
          IType type = clause.getCatchType();
          if( type == null )
          {
            type = JavaTypes.THROWABLE();
          }

          if (symbol != null) {
            if (type != JavaTypes.THROWABLE()) {
              // Test for assignability
              IRExpression test = callMethod( IType.class, "isAssignableFrom", new Class[]{IType.class},
                        pushType( type ),
                        exprList( identifier( exceptionTypeSymbol ) ) );
              // Assign the generic variable to an appropriately typed and boxed symbol, then compile the catch body
              List<IRStatement> body = new ArrayList<IRStatement>();
              body.add( assignCatchClauseSymbol( exceptionSymbol, symbol.getName(), type, symbol.isValueBoxed() ) );
              body.add( cc.compile( clause.getCatchStmt() ) );
              IRIfStatement nestedCatchStatement = new IRIfStatement( test, new IRStatementList( false, body ), null );

              // Add it to the list we're building up; if it's the first statement then set up the variable and add it to
              // the list, otherwise chain it to the previous if statement
              if (lastStatement == null) {
                catchBody.add( nestedCatchStatement );
              } else {
                // It's possible that some clause follows the last if/else clause; if so, the else statement
                // will already be non-null, so in that case we can just drop these statements on the floor
                // since they'll never be executed
                if (lastStatement.getElseStatement() == null) {
                  lastStatement.setElseStatement( nestedCatchStatement );
                }
              }
              lastStatement = nestedCatchStatement;
            } else {
              // No point in testing for assignability, so we know it's a throwable.
              List<IRStatement> body = new ArrayList<IRStatement>();
              body.add( assignCatchClauseSymbol( exceptionSymbol, symbol.getName(), clause.getCatchType(), symbol.isValueBoxed() ) );
              body.add( cc.compile( clause.getCatchStmt() ) );
              if (lastStatement != null) {
                lastStatement.setElseStatement( new IRStatementList( false, body ) );
              } else {
                catchBody.addAll(body);
              }

              // Once we've hit a catch around Throwable, exit the loop immediately
              break;
            }
          } else {
            // Uncaught, so rethrow
            lastStatement.setElseStatement( new IRThrowStatement( identifier( exceptionSymbol ) ) );
          }
        } finally {
          cc.popScope();
        }
      }

      resultingClauses.add(new IRCatchClause( exceptionSymbol, new IRStatementList( false, catchBody ) ) );
    } finally {
      cc.popScope();
    }
  }

  private IRStatement assignCatchClauseSymbol( IRSymbol genericCatchSymbol, String expectedName, IType expectedType, boolean isBoxed) {
    // Either do a check-cast to the expected type or wrap the catch symbol in an EvaluationExeption
    IRType descriptorType;
    IRExpression root;
    if (expectedType == null) {
      descriptorType = getDescriptor( RuntimeException.class );
      root = wrapCatchSymbol( identifier( genericCatchSymbol ) );
    } else {
      descriptorType = getDescriptor( expectedType );
      root = buildCast( descriptorType, identifier( genericCatchSymbol ) );
    }

    if (isBoxed) {
      return boxCatchSymbol( descriptorType, expectedName, root );
    } else {
      return reassignCatchSymbol( descriptorType, expectedName, root );
    }
  }

  private IRStatement reassignCatchSymbol( IRType symbolType, String properName, IRExpression rootValue) {
    IRSymbol newSymbol = new IRSymbol( properName, symbolType, false);
    _cc().putSymbol( newSymbol );
    return buildAssignment( newSymbol, rootValue );
  }

  private IRExpression wrapCatchSymbol( IRExpression rootValue) {
    return callStaticMethod( EvaluationException.class, "wrap", new Class[]{Throwable.class}, exprList( rootValue ) );
  }

  private IRStatement boxCatchSymbol( IRType componentType, String properName, IRExpression rootValue ) {
    // Create a new symbol with the correct name as an array of size 1 containing the expression
    IRSymbol newSymbol = new IRSymbol( properName, componentType.getArrayType(), false);
    _cc().putSymbol( newSymbol );
    IRExpression arrayBox = buildInitializedArray( componentType, exprList( rootValue ) );
    return buildAssignment( newSymbol, arrayBox );
  }

}