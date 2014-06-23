/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.UsingStatement;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.IReentrant;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRMonitorLockAcquireStatement;
import gw.lang.ir.statement.IRMonitorLockReleaseStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;

import java.io.Closeable;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

/**
 */
public class UsingStatementTransformer extends AbstractStatementTransformer<UsingStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, UsingStatement stmt )
  {
    UsingStatementTransformer compiler = new UsingStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private UsingStatementTransformer( TopLevelTransformationContext cc, UsingStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // Push a scope in case using body not a statement list
    _cc().pushScope( false );
    try
    {
      return compileUsingVars( getUsingVars() );
    }
    finally
    {
      _cc().popScope();
    }
  }

  private Iterator<? extends IParsedElement> getUsingVars()
  {
    if( _stmt().hasVarStatements() )
    {
      return _stmt().getVarStatements().iterator();
    }
    else
    {
      return Collections.singleton( _stmt().getExpression() ).iterator();
    }
  }

  private IRStatement compileUsingVars( Iterator<? extends IParsedElement> usingVars )
  {
    if( !usingVars.hasNext() )
    {
      // if there are no vars left, return the body of the using stmt.
      IRStatement body = _cc().compile( _stmt().getStatement() );
      visitStatementLineNumber( body, _stmt().getStatement() );
      return body;
    }
    else
    {
      IParsedElement pe = usingVars.next();
      IType type = getType( pe );

      IRStatementList stmtList = new IRStatementList( false );

      IRSymbol symbol = initVar( pe, stmtList );
      stmtList.addStatement( aquire( symbol, type ) );
      stmtList.addStatement( new IRTryCatchFinallyStatement( compileUsingVars( usingVars ),
                                                             Collections.EMPTY_LIST,
                                                             release( symbol, type ) ) );
      return stmtList;
    }
  }

  private IRSymbol initVar( IParsedElement pe, IRStatementList stmtList )
  {
    if( pe instanceof IExpression )
    {
      // evaluate the expression and store it to a temp symbol
      IRExpression expression = ExpressionTransformer.compile( (IExpression)pe, _cc() );
      IRSymbol tempSymbol = _cc().makeAndIndexTempSymbol( expression.getType() );
      IRAssignmentStatement initAssignment = buildAssignment( tempSymbol, expression );
      stmtList.addStatement( initAssignment );
      return tempSymbol;
    }
    else
    {
      IVarStatement varStmt = (IVarStatement)pe;
      // initialize the var
      IRExpression expression = ExpressionTransformer.compile( varStmt.getAsExpression(), _cc() );
      if( varStmt.getSymbol().isValueBoxed() )
      {
        // Box the value
        expression = buildInitializedArray(  getDescriptor( varStmt.getSymbol().getType() ), Collections.singletonList( expression ) );
      }
      IRSymbol varSymbol = _cc().createSymbol( varStmt.getSymbol().getName(),
                                               varStmt.getSymbol().isValueBoxed()
                                               ? getDescriptor( varStmt.getSymbol().getType() ).getArrayType()
                                               : getDescriptor( varStmt.getSymbol().getType() ) );
      IRAssignmentStatement initAssignment = buildAssignment( varSymbol, expression ); // assign the initial value
      stmtList.addStatement( initAssignment );
      visitStatementLineNumber( initAssignment, varStmt );

      // copy it to a temp symbol (so that reassignment doesn't affect the enter/exit semantics)
      IRSymbol tempSymbol = _cc().makeAndIndexTempSymbol( expression.getType() );
      IRAssignmentStatement tempVar = buildAssignment( tempSymbol, identifier( initAssignment.getSymbol() ) );// reassign to a temp var
      stmtList.addStatement( tempVar );
      return tempSymbol;
    }
  }

  private IType getType( IParsedElement pe )
  {
    if( pe instanceof IExpression )
    {
      return ((IExpression) pe).getType();
    }
    else
    {
      return ((IVarStatement)pe).getType();
    }
  }

  private IRStatement aquire( IRSymbol symbol, IType type )
  {
    IRAssignmentStatement tempVar = null;
    if( symbol.getType().isArray() )
    {
      IRExpression boxedValue = buildArrayLoad( new IRIdentifier( symbol ), 0, getDescriptor( type ) );
      symbol = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
      tempVar = buildAssignment( symbol, boxedValue );
    }

    IRStatement acquireStmt;
    if( type.equals(GosuTypes.IMONITORLOCK()) )
    {
      acquireStmt = new IRMonitorLockAcquireStatement( identifier( symbol ) );
    }
    else if( JavaTypes.LOCK().isAssignableFrom( type ) )
    {
      acquireStmt = buildIf( buildNotEquals( identifier( symbol ), nullLiteral() ),
                      buildMethodCall( callMethod( Lock.class, "lock", new Class[0], identifier( symbol ), exprList() ) ) );
    }
    else if( JavaTypes.getGosuType( IReentrant.class ).isAssignableFrom( type ) )
    {
      acquireStmt = buildIf( buildNotEquals( identifier( symbol ), nullLiteral() ),
                      buildMethodCall( callMethod( IReentrant.class, "enter", new Class[0], identifier( symbol ), exprList() ) ) );
    }
    else
    {
      acquireStmt = buildMethodCall( callStaticMethod( GosuRuntimeMethods.class, "invokeLockMethod", new Class[]{Object.class},
                                                exprList( identifier( symbol ) ) ) );
    }
    
    if( tempVar != null )
    {
      acquireStmt = new IRStatementList( false, tempVar, acquireStmt );
    }
    return acquireStmt;
  }

  private IRStatement release( IRSymbol symbol, IType type )
  {
    IRAssignmentStatement tempVar = null;
    if( symbol.getType().isArray() )
    {
      IRExpression boxedValue = buildArrayLoad( new IRIdentifier( symbol ), 0, getDescriptor( type ) );
      symbol = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
      tempVar = buildAssignment( symbol, boxedValue );
    }

    IRStatement releaseStmt;
    if( type.equals(GosuTypes.IMONITORLOCK()) )
    {
      releaseStmt = new IRMonitorLockReleaseStatement( identifier( symbol ) );
    }
    else if( JavaTypes.LOCK().isAssignableFrom( type ) )
    {
      releaseStmt = buildIf( buildNotEquals( identifier( symbol ), nullLiteral() ),
                      buildMethodCall( callMethod( Lock.class, "unlock", new Class[0], identifier( symbol ), exprList() ) ) );
    }
    else if( JavaTypes.getGosuType( IReentrant.class ).isAssignableFrom( type ) )
    {
      releaseStmt = buildIf( buildNotEquals( identifier( symbol ), nullLiteral() ),
                      buildMethodCall( callMethod( IReentrant.class, "exit", new Class[0], identifier( symbol ), exprList() ) ) );
    }
    else if( JavaTypes.getJreType( Closeable.class ).isAssignableFrom( type ) )
    {
      releaseStmt = buildIf( buildNotEquals( identifier( symbol ), nullLiteral() ),
                      buildMethodCall( callMethod( Closeable.class, "close", new Class[0], identifier( symbol ), exprList() ) ) );
    }
    else {
      if( GosuTypes.IDISPOSABLE().isAssignableFrom(type) )
      {
        releaseStmt = buildIf( buildNotEquals( identifier( symbol ), nullLiteral() ),
                        buildMethodCall( callMethod( IRMethodFactory.createIRMethod(GosuTypes.IDISPOSABLE(),
                            "dispose",
                            JavaTypes.pVOID(),
                            IType.EMPTY_ARRAY,
                            IRelativeTypeInfo.Accessibility.PUBLIC,
                            false),
                                                     identifier( symbol ),
                                                     exprList() ) ) );
      }
      else
      {
        releaseStmt = buildMethodCall( callStaticMethod( GosuRuntimeMethods.class, "invokeUnlockOrDisposeOrCloseMethod", new Class[]{Object.class},
                                                  exprList( identifier( symbol ) ) ) );
      }
    }

    if( tempVar != null )
    {
      releaseStmt = new IRStatementList( false, tempVar, releaseStmt );
    }

    if(_stmt().getFinallyStatement() != null )
    {
      IRStatement finallyBody = _cc().compile( _stmt().getFinallyStatement() );
      releaseStmt = new IRStatementList( false, releaseStmt, finallyBody );
    }
    return releaseStmt;
  }
}