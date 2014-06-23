/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRRelationalExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRRelationalExpression expression, IRBytecodeContext context ) {
    if (!expression.getLhs().getType().equals(expression.getRhs().getType())) {
      throw new IllegalStateException("Relational expression had one side as a " + expression.getLhs().getType().getName() + " and the other as a " +
      expression.getRhs().getType().getName());
    }

    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
    IRBytecodeCompiler.compileIRExpression( expression.getRhs(), context );

    MethodVisitor mv = context.getMv();

    Label trueLabel = new Label();

    IRType type = expression.getLhs().getType();
    IRRelationalExpression.Operation op = expression.getOp();
    if( type.isLong() || type.isDouble() || type.isFloat() )
    {
      mv.visitInsn( type.isDouble()
                    ? Opcodes.DCMPL
                    : type.isFloat()
                      ? Opcodes.FCMPL
                      : Opcodes.LCMP );

      if( op == IRRelationalExpression.Operation.LTE )
      {
        mv.visitJumpInsn( Opcodes.IFLE, trueLabel );
      }
      else if( op == IRRelationalExpression.Operation.LT )
      {
        mv.visitJumpInsn( Opcodes.IFLT, trueLabel );
      }
      else if( op == IRRelationalExpression.Operation.GTE )
      {
        mv.visitJumpInsn( Opcodes.IFGE, trueLabel );
      }
      else
      {
        mv.visitJumpInsn( Opcodes.IFGT, trueLabel );
      }
    }
    else
    {
      if( op == IRRelationalExpression.Operation.LTE )
      {
        mv.visitJumpInsn( Opcodes.IF_ICMPLE, trueLabel );
      }
      else if( op == IRRelationalExpression.Operation.LT )
      {
        mv.visitJumpInsn( Opcodes.IF_ICMPLT, trueLabel );
      }
      else if( op == IRRelationalExpression.Operation.GTE )
      {
        mv.visitJumpInsn( Opcodes.IF_ICMPGE, trueLabel );
      }
      else
      {
        mv.visitJumpInsn( Opcodes.IF_ICMPGT, trueLabel );
      }
    }
    mv.visitInsn( Opcodes.ICONST_0 );
    Label falseLabel = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, falseLabel );
    mv.visitLabel( trueLabel );
    mv.visitInsn( Opcodes.ICONST_1 );
    mv.visitLabel( falseLabel );
  }
}
