/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRRelationalExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRRelationalExpression expression, IRBytecodeContext context ) {
    IRExpression rhs = expression.getRhs();
    IRType rhsType = rhs.getType();
    if (!expression.getLhs().getType().equals(rhsType)) {
      throw new IllegalStateException("Relational expression had one side as a " + expression.getLhs().getType().getName() + " and the other as a " +
      rhsType.getName());
    }

    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );

    boolean isInteger0 = IREqualityExpressionCompiler.isInteger0(rhs, rhsType);
    if(!isInteger0) {
      IRBytecodeCompiler.compileIRExpression(rhs, context);
    }

    MethodVisitor mv = context.getMv();

    IRType type = expression.getLhs().getType();
    IRRelationalExpression.Operation op = expression.getOp();
    int asmOpcode;
    if( type.isLong() || type.isDouble() || type.isFloat() )
    {
      if(op == IRRelationalExpression.Operation.LTE || op == IRRelationalExpression.Operation.LT)
      {
        mv.visitInsn( type.isDouble()
                                      ? Opcodes.DCMPG
                                      : type.isFloat()
                                      ? Opcodes.FCMPG
                                      : Opcodes.LCMP );
      }
      else
      {
        mv.visitInsn( type.isDouble()
                                      ? Opcodes.DCMPL
                                      : type.isFloat()
                                      ? Opcodes.FCMPL
                                      : Opcodes.LCMP );

      }
      asmOpcode = getOpcodeFor0(op);
    }
    else if(isInteger0) {
      asmOpcode = getOpcodeFor0(op);
    }
    else
    {
      if( op == IRRelationalExpression.Operation.LTE )
      {
        asmOpcode = Opcodes.IF_ICMPLE;
      }
      else if( op == IRRelationalExpression.Operation.LT )
      {
        asmOpcode = Opcodes.IF_ICMPLT;
      }
      else if( op == IRRelationalExpression.Operation.GTE )
      {
        asmOpcode = Opcodes.IF_ICMPGE;
      }
      else
      {
        asmOpcode = Opcodes.IF_ICMPGT;
      }
    }
    expression.getConditionContext().setOperator( asmOpcode );
    if( isNotPartOfBooleanExpr( expression ) )
    {
      compileConditionAssignment( expression, mv );
    }
  }

  private static int getOpcodeFor0(IRRelationalExpression.Operation op) {
    int asmOpcode;
    if( op == IRRelationalExpression.Operation.LTE )
    {
      asmOpcode = Opcodes.IFLE;
    }
    else if( op == IRRelationalExpression.Operation.LT )
    {
      asmOpcode = Opcodes.IFLT;
    }
    else if( op == IRRelationalExpression.Operation.GTE )
    {
      asmOpcode = Opcodes.IFGE;
    }
    else
    {
      asmOpcode = Opcodes.IFGT;
    }
    return asmOpcode;
  }
}
