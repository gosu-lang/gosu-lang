/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IParenthesizedExpression;
import gw.lang.reflect.gs.FragmentInstance;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.ImplicitTypeAsExpression;
import gw.internal.gosu.parser.fragments.GosuFragment;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.ThrowStatement;
import gw.lang.ir.IRClass;
import gw.lang.ir.IRType;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.SyntheticIRType;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRMethod;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.lang.parser.statements.ITerminalStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GosuFragmentTransformer extends AbstractElementTransformer<IExpression> {

  public static final String SYMBOLS_PARAM_NAME = "$symbols$";
  public static final String SYMBOLS_PARAM_ARG_NAME = "$symbols$arg";

  private GosuFragment _fragment;
  private GosuFragmentTransformationContext _context;
  private SyntheticIRType _irType;

  public GosuFragmentTransformer(GosuFragment fragment) {
    super(null, null);
    _fragment = fragment;
    _irType = new SyntheticIRType( FragmentInstance.class, fragment.getName(), fragment.getRelativeName());
    _context = new GosuFragmentTransformationContext(_fragment, _irType, fragment.getName(), false );
    setCc( _context );
  }

  public static IRClass transform( GosuFragment fragment ) {
    return new GosuFragmentTransformer( fragment ).transform();
  }

  private IRClass transform() {
    IRClass irClass = new IRClass();
    addHeader( irClass );
    addDefaultConstructor( irClass );
    addEvaluateMethod( irClass );
    addEvaluateRootMethod( irClass );
    return irClass;
  }

  private void addHeader( IRClass irClass ) {
    irClass.setName( _fragment.getName() );
    irClass.setThisType( _irType );
    irClass.setModifiers( Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_SUPER );
    irClass.setSuperType( getDescriptor( FragmentInstance.class ) );
  }

  private void addDefaultConstructor( IRClass irClass ) {
    _context.initBodyContext( false );
    _context.pushScope( true );

    List<IRStatement> statements = new ArrayList<IRStatement>();
    IRMethod irMethod = IRMethodFactory.createConstructorIRMethod( _cc().getSuperType(), new IRType[0] );
    statements.add( new IRMethodCallStatement( callSpecialMethod( getDescriptor( _cc().getSuperType() ), irMethod, pushThis(), Collections.<IRExpression>emptyList() ) ) );
    statements.add( new IRReturnStatement() );

    IRMethodStatement methodStatement = new IRMethodStatement(
            new IRStatementList( true, statements),
            "<init>",
            Opcodes.ACC_PUBLIC,
            false,
            IRTypeConstants.pVOID(),
            Collections.<IRSymbol>emptyList());

    irClass.addMethod(methodStatement);
  }

  private void addEvaluateMethod( IRClass irClass ) {
    // public abstract Object evaluate(IExternalSymbolMap symbols);
    IRSymbol symbolsParam = new IRSymbol(SYMBOLS_PARAM_NAME, getDescriptor(IExternalSymbolMap.class), false);

    _context.initBodyContext( false );
    _context.pushScope( true );
    _context.putSymbol(symbolsParam);

    List<IRStatement> statements = new ArrayList<IRStatement>();

    IExpression expression = _fragment.getExpression();
    if (expression instanceof IProgram) {
      Statement mainStatement = (Statement) ((IProgram) expression).getMainStatement();
      statements.add( StatementTransformer.compile( _context, mainStatement));

      // If the program doesn't terminate, then add in an explicit return null at the end.
      // This is likely in the case of a program that doesn't actually return a value
      boolean[] bAbsolute = {false};
      ITerminalStatement terminalStmt = mainStatement.getLeastSignificantTerminalStatement( bAbsolute );
      if( !bAbsolute[0] ||
          !(terminalStmt instanceof ReturnStatement) &&
          !(terminalStmt instanceof ThrowStatement) )
      {
        statements.add( new IRReturnStatement( null, nullLiteral() ) );
      }
    } else if (expression.getType().equals(JavaTypes.pVOID())) {
      // If the expression has a void type, such as if it's a method call with no return value, then compile
      // it as a synthetic statement and explicitly insert a return null
      statements.add( new IRSyntheticStatement( ExpressionTransformer.compile( expression, _context ) ) );
      statements.add( new IRReturnStatement( null, nullLiteral() ) );
    } else {
      // If the expression has a value, just return that (after boxing it, if necessary)
      IRExpression returnValue = ExpressionTransformer.compile( expression, _context );
      if (returnValue.getType().isPrimitive()) {
        returnValue = boxValue( returnValue.getType(), returnValue );
      }
      statements.add( new IRReturnStatement( null, returnValue ) );
    }

    IRMethodStatement methodStatement = new IRMethodStatement(
            new IRStatementList( true, statements),
            "evaluate",
            Opcodes.ACC_PUBLIC,
            false,
            IRTypeConstants.OBJECT(),
            Collections.singletonList(symbolsParam));

    irClass.addMethod(methodStatement);

  }

  private void addEvaluateRootMethod( IRClass irClass ) {
    // Only bother adding in the method if the fragment's root is an IMemberAccessExpression; otherwise the method
    // is already implemented to return null on FragmentInstance
    IExpression expr = maybeUnwrap( _fragment.getExpression() );
    if ( expr instanceof IMemberAccessExpression) {
      // public abstract Object evaluateRootExpression(IExternalSymbolMap symbols);
      IRSymbol symbolsParam = new IRSymbol(SYMBOLS_PARAM_NAME, getDescriptor(IExternalSymbolMap.class), false);

      _context.initBodyContext( false );
      _context.pushScope( true );
      _context.putSymbol(symbolsParam);

      List<IRStatement> statements = new ArrayList<IRStatement>();
      IRExpression returnValue = ExpressionTransformer.compile( ((IMemberAccessExpression) expr).getRootExpression(), _context );
      if (returnValue.getType().isPrimitive()) {
        returnValue = boxValue( returnValue.getType(), returnValue );
      }
      statements.add( new IRReturnStatement( null, returnValue ) );

      IRMethodStatement methodStatement = new IRMethodStatement(
              new IRStatementList( true, statements),
              "evaluateRootExpression",
              Opcodes.ACC_PUBLIC,
              false,
              IRTypeConstants.OBJECT(),
              Collections.singletonList(symbolsParam));

      irClass.addMethod(methodStatement);
    }
  }

  private IExpression maybeUnwrap( IExpression expression )
  {
    if( expression instanceof ImplicitTypeAsExpression )
    {
      return maybeUnwrap( ((ImplicitTypeAsExpression)expression).getLHS() );
    }
    else if( expression instanceof IParenthesizedExpression )
    {
      return maybeUnwrap( ((IParenthesizedExpression)expression).getExpression() );
    }
    return expression;
  }
}