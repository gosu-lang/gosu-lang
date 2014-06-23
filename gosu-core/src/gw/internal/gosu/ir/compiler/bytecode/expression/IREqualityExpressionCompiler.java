/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IREqualityExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IREqualityExpression expression, IRBytecodeContext context ) {
    if (expression.getLhs() instanceof IRNullLiteral || expression.getRhs() instanceof IRNullLiteral) {
      compareToNull( expression.isEquals(), expression.getLhs() instanceof IRNullLiteral ? expression.getRhs() : expression.getLhs(), context );
    } else if (expression.getLhs().getType().isPrimitive()) {
      if (!expression.getLhs().getType().equals(expression.getRhs().getType())) {
        throw new IllegalArgumentException("Equality expression comparing two different primitive types :" +
                expression.getLhs().getType().getName() + " and " + expression.getRhs().getType().getName());
      }

      IRType lhsType = expression.getLhs().getType();
      if (lhsType.isBoolean() || lhsType.isByte() || lhsType.isChar() || lhsType.isShort() || lhsType.isInt()) {
        compareInts( expression.isEquals(), expression.getLhs(), expression.getRhs(), context );
      } else if (lhsType.isLong()) {
        compareLongs( expression.isEquals(), expression.getLhs(), expression.getRhs(), context );
      } else if (lhsType.isDouble()) {
        compareDoubles( expression.isEquals(), expression.getLhs(), expression.getRhs(), context );
      } else if (lhsType.isFloat()) {
        compareFloats( expression.isEquals(), expression.getLhs(), expression.getRhs(), context );
      } else {
        System.out.println("Unexpected primitive type " + lhsType.getName());
      }
    } else {
      compareObjects( expression.isEquals(), expression.getLhs(), expression.getRhs(), context );
    }
  }

  private static void compareToNull( boolean equals, IRExpression expr, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( expr, context );

    compare( mv, equals ?  Opcodes.IFNULL : Opcodes.IFNONNULL );
  }

  private static void compareInts( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( lhs, context );
    IRBytecodeCompiler.compileIRExpression( rhs, context );

    compare( mv, equals ?  Opcodes.IF_ICMPEQ : Opcodes.IF_ICMPNE );
  }

  private static void compareLongs( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context ) {
    compareNonIntPrimitives( equals, lhs, rhs, context, Opcodes.LCMP );
  }

  private static void compareDoubles( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context ) {
    compareNonIntPrimitives( equals, lhs, rhs, context, Opcodes.DCMPL );
  }

  private static void compareFloats( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context ) {
    compareNonIntPrimitives( equals, lhs, rhs, context, Opcodes.FCMPL );
  }

  private static void compareNonIntPrimitives( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, int compareOp ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( lhs, context );
    IRBytecodeCompiler.compileIRExpression( rhs, context );
    mv.visitInsn( compareOp );

    compare( mv, equals ?  Opcodes.IFEQ : Opcodes.IFNE );
  }

  private static void compareObjects( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( lhs, context );
    IRBytecodeCompiler.compileIRExpression( rhs, context );

    compare( mv, equals ?  Opcodes.IF_ACMPEQ : Opcodes.IF_ACMPNE );
  }

  private static void compare( MethodVisitor mv, int opcode ) {
    Label labelTrue = new Label();
    mv.visitJumpInsn( opcode, labelTrue );
    mv.visitInsn( Opcodes.ICONST_0 );
    Label labelFalse = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, labelFalse );
    mv.visitLabel( labelTrue );
    mv.visitInsn( Opcodes.ICONST_1 );
    mv.visitLabel( labelFalse );
  }
  
}
