/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRNegationExpression;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRNegationExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRNegationExpression expression, IRBytecodeContext context ) {

    IRBytecodeCompiler.compileIRElement( expression.getRoot(), context );

    MethodVisitor mv = context.getMv();
    IRType rootType = expression.getType();
    if( rootType.isByte() || rootType.isChar() || rootType.isShort() || rootType.isInt() )
    {
      mv.visitInsn( Opcodes.INEG );
    }
    else if( rootType.isLong() )
    {
      mv.visitInsn( Opcodes.LNEG );
    }
    else if( rootType.isFloat() )
    {
      mv.visitInsn( Opcodes.FNEG );
    }
    else if( rootType.isDouble() )
    {
      mv.visitInsn( Opcodes.DNEG );
    }
    else
    {
      throw new IllegalArgumentException( "Unexpected root type for a negation expression: " + rootType.getName() );
    }
  }
}
