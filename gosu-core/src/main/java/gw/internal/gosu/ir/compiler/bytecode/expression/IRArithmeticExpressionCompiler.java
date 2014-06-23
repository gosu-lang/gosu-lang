/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRArithmeticExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRArithmeticExpression expression, IRBytecodeContext context ) {
    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
    IRBytecodeCompiler.compileIRExpression( expression.getRhs(), context );

    context.getMv().visitInsn( getInstruction( expression.getType(), expression.getOp() ) );
  }

  protected static int getInstruction( IRType type, IRArithmeticExpression.Operation op ) {
    switch(op) {
      case Addition:
        return getIns( Opcodes.IADD, type );
      case Subtraction:
        return getIns( Opcodes.ISUB, type );
      case Multiplication:
        return getIns( Opcodes.IMUL, type );
      case Division:
        return getIns( Opcodes.IDIV, type );
      case Remainder:
        return getIns( Opcodes.IREM, type );
      case ShiftLeft:
        return getIns( Opcodes.ISHL, type );
      case ShiftRight:
        return getIns( Opcodes.ISHR, type );
      case UnsignedShiftRight:
        return getIns( Opcodes.IUSHR, type );
      case BitwiseAnd:
        return getIns( Opcodes.IAND, type );
      case BitwiseOr:
        return getIns( Opcodes.IOR, type );
      case BitwiseXor:
        return getIns( Opcodes.IXOR, type );
      default:
        throw new IllegalArgumentException("Unrecognized operation " + op);    
    }
  }
}
