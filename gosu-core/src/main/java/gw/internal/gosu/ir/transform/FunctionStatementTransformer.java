/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.internal.gosu.ir.transform.statement.SyntheticFunctionStatementTransformer;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.IBlockClassInternal;
import gw.internal.gosu.parser.ParseTree;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.ThisConstructorFunctionSymbol;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRCatchClause;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.IToken;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IThrowStatement;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class FunctionStatementTransformer extends AbstractElementTransformer<FunctionStatement>
{
  private static final boolean GW_PROFILER_WRAPPING_ENABLED = System.getProperty( "gw.enable.profiler.wrapping" ) != null || System.getProperty( "gw.enable.profiler.wrapping.tags" ) != null;
  private DynamicFunctionSymbol _dfs;

  FunctionStatementTransformer( DynamicFunctionSymbol dfs, TopLevelTransformationContext cc )
  {
    super( cc, dfs == null ? null : dfs.getDeclFunctionStmt() );
    _dfs = dfs;
  }

  IRStatement compile()
  {
    List<IRStatement> statements = new ArrayList<IRStatement>();
    if( _cc().isBlockInvoke() )
    {
      checkcastArgs( statements );
    }
    assignCapturedParamIndexes( statements );
    compileConstructorInitializers( statements );
    Statement statement = (Statement)_dfs.getValueDirectly();
    if( statement instanceof SyntheticFunctionStatement )
    {
      statements.add( SyntheticFunctionStatementTransformer.compile( _dfs, _cc(), (SyntheticFunctionStatement)statement ) );
    }
    else
    {
      statements.add( StatementTransformer.compile( _cc(), statement ) );
    }
    handleImplicitReturns( statement, statements );
    IRStatementList functionBody = new IRStatementList( false, statements );

    if( GW_PROFILER_WRAPPING_ENABLED )
    {
      functionBody = wrapFunctionBodyForProfiler( functionBody );
    }
    return functionBody;
  }

  private static final AtomicInteger _numGeneratedMethods = new AtomicInteger(1);
  private IRStatementList wrapFunctionBodyForProfiler( IRStatementList functionBody )
  {
    if( _dfs.getScriptPart() == null )
    {
      // The function stmt belongs to a block; we currently do not support block-level profiling
      return functionBody;
    }
    String strippedClassName = TypeLord.getPureGenericType( _dfs.getScriptPart().getContainingType() ).getName();
    String generatedClassName = "_profiler." + strippedClassName + "_" + _numGeneratedMethods.getAndIncrement();

    // ProfilerFrame frame = gw.api.profiler.Profiler.push( ProfilerTag.GOSU_METHOD_WRAPPER )
    IRExpression field = getField( IRPropertyFactory.createIRProperty( TypeSystem.getByFullName( "gw.api.profiler.ProfilerTag", TypeSystem.getGlobalModule() ).getTypeInfo().getProperty( "GOSU_METHOD_WRAPPER" ) ), null );
    IRMethod m = IRMethodFactory.createIRMethod( TypeSystem.getByFullName( "gw.api.profiler.Profiler", TypeSystem.getGlobalModule() ),
                                                "push", TypeSystem.getByFullName( "gw.api.profiler.ProfilerFrame", TypeSystem.getGlobalModule() ),
                                                new IType[] {TypeSystem.getByFullName( "gw.api.profiler.ProfilerTag", TypeSystem.getGlobalModule() )},
                                                IRelativeTypeInfo.Accessibility.PUBLIC, true );
    IRExpression push = callMethod( m, null, Arrays.asList( field ) );
    IRSymbol tempSymbol = _cc().makeAndIndexTempSymbol( push.getType() );
    IRAssignmentStatement initAssignment = buildAssignment( tempSymbol, push );

    // frame.setProperty( <generatedClassName>_<methodName> );
    m = IRMethodFactory.createIRMethod( TypeSystem.getByFullName( "gw.api.profiler.ProfilerFrame", TypeSystem.getGlobalModule() ),
                                        "setProperty", JavaTypes.pVOID(),
                                        new IType[] {JavaTypes.STRING()},
                                        IRelativeTypeInfo.Accessibility.PUBLIC, false );
    IRExpression info = new IRStringLiteralExpression( generatedClassName + "_" + generateMethodName() );
    IRExpression setProp = callMethod( m, identifier( tempSymbol ), Arrays.asList( info ) );
    IRMethodCallStatement setPropStmt = new IRMethodCallStatement( setProp );

    // gw.api.profiler.Profiler.pop( frame )
    m = IRMethodFactory.createIRMethod( TypeSystem.getByFullName( "gw.api.profiler.Profiler", TypeSystem.getGlobalModule() ),
                                        "pop", JavaTypes.pVOID(),
                                        new IType[] {TypeSystem.getByFullName( "gw.api.profiler.ProfilerFrame", TypeSystem.getGlobalModule() )},
                                        IRelativeTypeInfo.Accessibility.PUBLIC, true );
    IRExpression pop = callMethod( m, null, Arrays.asList( (IRExpression)identifier( tempSymbol ) ) );
    IRMethodCallStatement popStmt = new IRMethodCallStatement( pop );
    IRTryCatchFinallyStatement tryFinally = new IRTryCatchFinallyStatement( functionBody, Collections.<IRCatchClause>emptyList(), popStmt );

    return new IRStatementList( functionBody.hasScope(), initAssignment, setPropStmt, tryFinally );
  }

  private String generateMethodName()
  {
    String methodName = _dfs.getName();
    String simpleMethodName = methodName.substring( 0, methodName.indexOf( "(" ) );
    if( simpleMethodName.startsWith( "@" ) )
    {
      if( _dfs.getArgs().isEmpty() )
      {
        simpleMethodName = "get" + simpleMethodName.substring( 1 );
      }
      else
      {
        simpleMethodName = "set" + simpleMethodName.substring( 1 );
      }
    }
    else if( simpleMethodName.equals( "super" ) )
    {
      simpleMethodName = "super1";
    }

    //if there is a FunctionStatement associated with this, use it to get the line number
    FunctionStatement statement = _dfs.getDeclFunctionStmt();
    if( statement != null )
    {
      simpleMethodName += "_line_" + statement.getLineNum();
    }

    return simpleMethodName;
  }

  private void handleImplicitReturns(Statement statement, List<IRStatement> statements)
  {
    IType returnType = _dfs.getReturnType();
    boolean[] bAbsolute = {false};
    ITerminalStatement terminalStmt = statement.getLeastSignificantTerminalStatement( bAbsolute );
    if( _cc().isBlockInvoke() )
    {
      if( terminalStmt == null || !bAbsolute[0] )
      {
        //visit a label
        IRReturnStatement returnStatement = new IRReturnStatement( null, nullLiteral() );
        returnStatement.setLineNumber( statement.getLineNum() );
        statements.add( returnStatement );
      }
      else if( _dfs.isLoopImplicitReturn() )
      {
        addImplicitReturn( statements, returnType );
      }
    }
    else if( returnType == JavaTypes.pVOID() &&
             (!bAbsolute[0] ||
              !(terminalStmt instanceof IReturnStatement) &&
              !(terminalStmt instanceof IThrowStatement) &&
              !(terminalStmt instanceof gw.internal.gosu.parser.statements.LoopStatement)) )
    {
      IRReturnStatement returnStmt = new IRReturnStatement();
      returnStmt.setLineNumber( getLastLineOfFunction( statement ) );
      statements.add( returnStmt );
    }
    else if( _dfs.isLoopImplicitReturn() )
    {
      addImplicitReturn( statements, returnType );
    }
  }

  private void addImplicitReturn( List<IRStatement> statements, IType returnType )
  {
    // This return stmt is never executed, it's only here to pacify Java's bytecode verifier
    // which doesn't perform the static analysis thoroughly enough to understand that a return
    // is not needed here.
    gw.lang.ir.statement.IRImplicitReturnStatement returnStatement =
            returnType == JavaTypes.pVOID()
            ? new gw.lang.ir.statement.IRImplicitReturnStatement()
            : new gw.lang.ir.statement.IRImplicitReturnStatement( null, getDefaultConstIns( returnType ) );
    statements.add( returnStatement );
  }

  private void compileConstructorInitializers( List<IRStatement> statements )
  {
    MethodCallStatement mc = _dfs.getInitializer();
    if( mc == null )
    {
      // Not a constructor
      return;
    }

    if( mc.getLineNum() <= 0 && mc.getParent() != null )
    {
      // So the debugger stops freaking out after stepping *into* a ctor
      mc.setLineNum( mc.getParent().getLineNum() );
    }

    _cc().initCapturedSymbolFields( statements );
    _cc().initTypeVarFields( statements );

    statements.add( StatementTransformer.compile( _cc(), mc ) );
    IFunctionSymbol initializerDfs = mc.getMethodCall().getFunctionSymbol();
    if( !(initializerDfs instanceof ThisConstructorFunctionSymbol) )
    {
      _cc().initializeInstanceFields( statements );
    }
  }

  private void assignCapturedParamIndexes( List<IRStatement> statements )
  {
    for( ISymbol paramSym : getActualArgSymbols() ) {
      if (paramSym.isValueBoxed()) {
        IRSymbol symbol = new IRSymbol(paramSym.getName(), getDescriptor(paramSym.getType().getArrayType()), false);
        _cc().putSymbol(symbol);
        IRExpression expression = identifier(_cc().getSymbol(symbol.getName() + "$$unboxedParam"));
        IRExpression value = buildInitializedArray(getDescriptor(paramSym.getType()), exprList(expression));
        statements.add(buildAssignment(symbol, value));
      }
    }
  }

  private List<ISymbol> getActualArgSymbols()
  {
    List<ISymbol> iSymbolList;
    if( _cc().isBlockInvoke() )
    {
      iSymbolList = ((IBlockClassInternal)getGosuClass()).getBlock().getArgs();
    }
    else
    {
      iSymbolList = _dfs.getArgs();
    }
    return iSymbolList;
  }

  private void checkcastArgs( List<IRStatement> statements )
  {
    for( ISymbol paramSym : getActualArgSymbols() )
    {
      IType actualType = paramSym.getType();
      IRSymbol properlyTypedSymbol = new IRSymbol( paramSym.getName() + (paramSym.isValueBoxed() ? "$$unboxedParam" : ""), getDescriptor(actualType), false );
      _cc().putSymbol( properlyTypedSymbol );

      IRExpression value;
      if( actualType.isPrimitive() )
      {
        value = unboxValueToType( actualType, identifier( _cc().getSymbol( paramSym.getName() + "$$blockParam" ) ) );
      }
      else
      {
        value = checkCast( actualType, identifier( _cc().getSymbol( paramSym.getName() + "$$blockParam" ) ) );
      }
      statements.add( buildAssignment( properlyTypedSymbol, value ) );
    }
  }

  public int getLastLineOfFunction( Statement stmt )
  {
    if( stmt == null )
    {
      return -1;
    }
    Statement temp = stmt;
    while( stmt != null && !(stmt instanceof IFunctionStatement) )
    {
      stmt = (Statement)stmt.getParent();
    }
    if( stmt == null )
    {
      stmt = temp;
    }
    ParseTree location = stmt.getLocation();
    List<IParseTree> children = location == null ? null : location.getChildren();
    if( children != null && children.size() > 0 )
    {
      for( IParseTree child : children )
      {
        IParsedElement pe = child.getParsedElement();
        if( pe instanceof IStatementList )
        {
          stmt = (Statement)pe;
          break;
        }
      }
    }
    List<IToken> tokens = stmt.getTokens();
    for( int i = tokens.size()-1; i >= 0; i-- )
    {
      IToken token = tokens.get( i );
      if( token.getText().equals( "}" ) )
      {
        return token.getLine();
      }
    }
    return -1;
  }
}
