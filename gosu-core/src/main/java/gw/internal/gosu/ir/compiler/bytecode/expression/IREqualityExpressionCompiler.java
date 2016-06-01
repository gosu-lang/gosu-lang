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
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.lang.ir.expression.IRNumericLiteral;

public class IREqualityExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IREqualityExpression expression, IRBytecodeContext context ) {
    if (expression.getLhs() instanceof IRNullLiteral || expression.getRhs() instanceof IRNullLiteral) {
      compareToNull( expression.isEquals(), expression.getLhs() instanceof IRNullLiteral ? expression.getRhs() : expression.getLhs(), context, expression );
    } else if (expression.getLhs().getType().isPrimitive()) {
      if (!expression.getLhs().getType().equals(expression.getRhs().getType())) {
        throw new IllegalArgumentException("Equality expression comparing two different primitive types :" +
                expression.getLhs().getType().getName() + " and " + expression.getRhs().getType().getName());
      }

      IRType lhsType = expression.getLhs().getType();
      if (lhsType.isBoolean() || lhsType.isByte() || lhsType.isChar() || lhsType.isShort() || lhsType.isInt()) {
        compareInts( expression.isEquals(), expression.getLhs(), expression.getRhs(), context, expression );
      } else if (lhsType.isLong()) {
        compareLongs( expression.isEquals(), expression.getLhs(), expression.getRhs(), context, expression );
      } else if (lhsType.isDouble()) {
        compareDoubles( expression.isEquals(), expression.getLhs(), expression.getRhs(), context, expression );
      } else if (lhsType.isFloat()) {
        compareFloats( expression.isEquals(), expression.getLhs(), expression.getRhs(), context, expression );
      } else {
        System.out.println("Unexpected primitive type " + lhsType.getName());
      }
    } else {
      compareObjects( expression.isEquals(), expression.getLhs(), expression.getRhs(), context, expression );
    }
  }

  private static void compareToNull( boolean equals, IRExpression expr, IRBytecodeContext context, IRExpression root ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( expr, context );

    compare( mv, equals ? Opcodes.IFNULL : Opcodes.IFNONNULL, root );
  }

  private static void compareInts( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, IRExpression root ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( lhs, context );
    IRType rhsType = rhs.getType();
    int opcode;
    if( !isInteger0( rhs, rhsType ) )
    {
      IRBytecodeCompiler.compileIRExpression( rhs, context );
      opcode = equals ?  Opcodes.IF_ICMPEQ : Opcodes.IF_ICMPNE;
    }
    else
    {
      opcode = equals ?  Opcodes.IFEQ : Opcodes.IFNE;
    }
    compare( mv, opcode, root );
  }

  public static boolean isInteger0( IRExpression expr, IRType lhsType )
  {
    return expr instanceof IRNumericLiteral && lhsType.isPrimitive() &&
           !(lhsType.isDouble() || lhsType.isFloat() || lhsType.isLong()) &&
           ((IRNumericLiteral) expr).getValue().intValue() == 0;
  }

  private static void compareLongs( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, IRExpression root ) {
    compareNonIntPrimitives( equals, lhs, rhs, context, Opcodes.LCMP, root );
  }

  private static void compareDoubles( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, IRExpression root ) {
    compareNonIntPrimitives( equals, lhs, rhs, context, Opcodes.DCMPL, root );
  }

  private static void compareFloats( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, IRExpression root ) {
    compareNonIntPrimitives( equals, lhs, rhs, context, Opcodes.FCMPL, root );
  }

  private static void compareNonIntPrimitives( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, int compareOp, IRExpression root ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( lhs, context );
    IRBytecodeCompiler.compileIRExpression( rhs, context );
    mv.visitInsn( compareOp );

    compare( mv, equals ? Opcodes.IFEQ : Opcodes.IFNE, root );
  }

  private static void compareObjects( boolean equals, IRExpression lhs, IRExpression rhs, IRBytecodeContext context, IRExpression root ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( lhs, context );
    IRBytecodeCompiler.compileIRExpression( rhs, context );

    compare( mv, equals ?  Opcodes.IF_ACMPEQ : Opcodes.IF_ACMPNE, root );
  }

  private static void compare( MethodVisitor mv, int opcode, IRExpression root ) {
    root.getConditionContext().setOperator( opcode );
    if( isNotPartOfBooleanExpr( root ) )
    {
      compileConditionAssignment( root, mv );
    }
  }
  
}
