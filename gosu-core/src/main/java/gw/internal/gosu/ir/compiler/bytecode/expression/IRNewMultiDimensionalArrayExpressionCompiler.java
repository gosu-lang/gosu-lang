/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRNewMultiDimensionalArrayExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

import java.util.List;

public class IRNewMultiDimensionalArrayExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRNewMultiDimensionalArrayExpression expression, IRBytecodeContext context ) {
    List<IRExpression> dims = expression.getSizeExpressions();
    for (IRExpression dim : dims) {
      IRBytecodeCompiler.compileIRElement(dim, context);
    }
    MethodVisitor mv = context.getMv();
    mv.visitMultiANewArrayInsn(getTypeForNewArray(expression.getType()), dims.size());
  }

  private static String getTypeForNewArray( IRType atomicType )
  {
    return atomicType.isArray()
           ? atomicType.getDescriptor()
           : atomicType.getSlashName();
  }
}