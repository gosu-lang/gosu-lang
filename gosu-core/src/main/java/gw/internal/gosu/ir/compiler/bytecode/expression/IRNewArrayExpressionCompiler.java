/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRNewArrayExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRNewArrayExpression expression, IRBytecodeContext context ) {
    IRBytecodeCompiler.compileIRExpression( expression.getSizeExpression(), context );

    MethodVisitor mv = context.getMv();
    createArrayInstruction(expression.getComponentType(), mv);
  }

  public static void createArrayInstruction(IRType expressionType, MethodVisitor mv) {
    if( expressionType.isPrimitive() )
    {
      mv.visitIntInsn( Opcodes.NEWARRAY, getPrimitiveTypeForNewArray( expressionType ) );
    }
    else
    {
      mv.visitTypeInsn( Opcodes.ANEWARRAY, getTypeForNewArray( expressionType ) );
    }
  }

  private static String getTypeForNewArray( IRType atomicType )
  {
    return atomicType.isArray()
           ? atomicType.getDescriptor()
           : atomicType.getSlashName();
  }

  public static int getPrimitiveTypeForNewArray( IRType atomicType )
  {
    if( atomicType.isByte() )
    {
      return Opcodes.T_BYTE;
    }
    if( atomicType.isChar() )
    {
      return Opcodes.T_CHAR;
    }
    if( atomicType.isBoolean() )
    {
      return Opcodes.T_BOOLEAN;
    }
    if( atomicType.isShort() )
    {
      return Opcodes.T_SHORT;
    }
    if( atomicType.isInt() )
    {
      return Opcodes.T_INT;
    }
    if( atomicType.isLong() )
    {
      return Opcodes.T_LONG;
    }
    if( atomicType.isFloat() )
    {
      return Opcodes.T_FLOAT;
    }
    if( atomicType.isDouble() )
    {
      return Opcodes.T_DOUBLE;
    }

    throw new IllegalStateException( "Unhandled primitive type: " + atomicType.getName() );
  }

}
