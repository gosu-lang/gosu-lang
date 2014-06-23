/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRTernaryExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.IRExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRTernaryExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRTernaryExpression expression, IRBytecodeContext context ) {
    // TODO - Gosu Perf - We could make this more efficient by inspecting the conditional expression for common
    // patterns like equality expressions or other comparisons
    MethodVisitor mv = context.getMv();

    Label trueLabel = new Label();
    if ( isComparisonToNull( expression.getTest() ) ) {
      IRBytecodeCompiler.compileIRExpression( getNonNullEqualityOperand( expression.getTest() ), context );
      mv.visitJumpInsn( getNullOpCode( expression.getTest() ), trueLabel ); // i.e. jump to trueLabel if the expression is equal to null
    } else {
      IRBytecodeCompiler.compileIRExpression( expression.getTest(), context );
      mv.visitJumpInsn( Opcodes.IFNE, trueLabel ); // i.e. jump to trueLabel if the expression returned true and thus left a non-zero value on the stack
    }

    IRBytecodeCompiler.compileIRExpression( expression.getFalseValue(), context );
    Label falseLabel = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, falseLabel );
    mv.visitLabel( trueLabel );
    IRBytecodeCompiler.compileIRExpression( expression.getTrueValue(), context );
    mv.visitLabel( falseLabel );
  }

  private static int getNullOpCode( IRExpression test )
  {
    IREqualityExpression equalityExpression = (IREqualityExpression) test;
    if( equalityExpression.isEquals() )
    {
      return Opcodes.IFNULL;
    }
    else
    {
      return Opcodes.IFNONNULL;
    }
  }

  private static boolean isComparisonToNull(IRExpression testExpression) {
    if (testExpression instanceof IREqualityExpression) {
      IREqualityExpression equalityExpression = (IREqualityExpression) testExpression;
      if (equalityExpression.getLhs() instanceof IRNullLiteral || equalityExpression.getRhs() instanceof IRNullLiteral) {
        return true;
      }
    }

    return false;
  }

  private static IRExpression getNonNullEqualityOperand( IRExpression testExpression ) {
    IREqualityExpression equalityExpression = (IREqualityExpression) testExpression;
    if (equalityExpression.getLhs() instanceof IRNullLiteral) {
      return equalityExpression.getRhs();
    } else {
      return equalityExpression.getLhs();
    }
  }
}
