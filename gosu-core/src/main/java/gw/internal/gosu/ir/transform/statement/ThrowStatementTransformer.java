/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.ThrowStatement;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.parser.EvaluationException;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class ThrowStatementTransformer extends AbstractStatementTransformer<ThrowStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, ThrowStatement stmt )
  {
    ThrowStatementTransformer compiler = new ThrowStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private ThrowStatementTransformer( TopLevelTransformationContext cc, ThrowStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // The throw statement can throw any object.  If we can statically determine that the RHS expression
    // is definitely a throwable, then we can just do a throw.  Otherwise, we need to do an if statement
    // to check if it's a throwable; if so we cast it to Throwable and throw it, and if not we wrap it
    // in an EvaluationException

    IRExpression exceptionValue = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
    if (TypeLord.isSubtype( _stmt().getExpression().getType(), JavaTypes.THROWABLE() ) ) {
      // It's definitely a Throwable:  if it's a synthetic type like a SOAP exception type, the verifier
      // might not actually know it's a throwable, though
      if ( !getDescriptor(Throwable.class).isAssignableFrom(exceptionValue.getType())) {
        // If the IR type of the value isn't assignable to Throwable, then wrap it in a cast, since we know it will work
        // out at runtime (unless someone constructed a totally invalid type loader, in which case . . . what can you do?)
        exceptionValue = buildCast( getDescriptor(Throwable.class), exceptionValue );
      }
      return buildThrow( exceptionValue );
    } else {
      IRSymbol temp = _cc().makeAndIndexTempSymbol( getDescriptor( _stmt().getExpression().getType() ) );
      IRStatement tempAssignment = buildAssignment( temp, exceptionValue );

      IRExpression test = new IRInstanceOfExpression( identifier( temp ), getDescriptor( Throwable.class ) );
      IRStatement trueCase = buildThrow( checkCast( Throwable.class, identifier( temp ) ) );
      IRStatement falseCase = buildThrow( buildNewExpression( EvaluationException.class, new Class[]{String.class},
                                                            exprList( checkCast( String.class, identifier( temp ) ) ) ) );
      IRStatement ifStatement = buildIfElse( test, trueCase, falseCase);

      return new IRStatementList( false, tempAssignment, ifStatement);
    }
  }
}