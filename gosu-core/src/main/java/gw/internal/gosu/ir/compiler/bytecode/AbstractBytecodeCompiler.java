/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.lang.ir.ConditionContext;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.ir.statement.IRIfStatement;
import gw.lang.ir.statement.IRWhileStatement;

public class AbstractBytecodeCompiler {

  public static final Type OBJECT_TYPE = Type.getType( Object.class );

  public static int getIns( int opcode, IRType type )
  {
    if( opcode == Opcodes.DUP )
    {
      return isWide( type ) ? Opcodes.DUP2 : opcode;
    }

    if( opcode == Opcodes.POP )
    {
      return isWide( type ) ? Opcodes.POP2 : opcode;
    }

    switch( opcode )
    {
      case Opcodes.ILOAD:
      case Opcodes.ISTORE:
      case Opcodes.IALOAD:
      case Opcodes.IASTORE:
      case Opcodes.IADD:
      case Opcodes.ISUB:
      case Opcodes.IMUL:
      case Opcodes.IDIV:
      case Opcodes.IREM:
      case Opcodes.INEG:
      case Opcodes.ISHL:
      case Opcodes.ISHR:
      case Opcodes.IUSHR:
      case Opcodes.IAND:
      case Opcodes.IOR:
      case Opcodes.IXOR:
      case Opcodes.IRETURN:
        break;
      default:
        throw new IllegalArgumentException( "Opcode: " + Integer.toHexString( opcode ) + " is not handled" );
    }

    if( type.isByte() )
    {
      return Type.BYTE_TYPE.getOpcode( opcode );
    }
    else if( type.isChar() )
    {
      return Type.CHAR_TYPE.getOpcode( opcode );
    }
    else if( type.isShort() )
    {
      return Type.SHORT_TYPE.getOpcode( opcode );
    }
    else if( type.isBoolean() )
    {
      return Type.BOOLEAN_TYPE.getOpcode( opcode );
    }
    else if( type.isInt() )
    {
      return Type.INT_TYPE.getOpcode( opcode );
    }
    else if( type.isLong() )
    {
      return Type.LONG_TYPE.getOpcode( opcode );
    }
    else if( type.isFloat() )
    {
      return Type.FLOAT_TYPE.getOpcode( opcode );
    }
    else if( type.isDouble() )
    {
      return Type.DOUBLE_TYPE.getOpcode( opcode );
    }
    else // handles array/ref types
    {
      return OBJECT_TYPE.getOpcode( opcode );
    }
  }

  public static boolean isWide( IRType type ) {
    return (type.getName().equals("long") || type.getName().equals("double") );
  }

  public static void compileConditionAssignment( IRExpression expression, MethodVisitor mv)
  {
    Label end = new Label();
    ConditionContext conditionContext = expression.getConditionContext();
    mv.visitJumpInsn( negateOpcode(conditionContext.getOperator() ), conditionContext.generateFalseLabel() );
    conditionContext.fixLabels( true, mv );
    mv.visitInsn( Opcodes.ICONST_1 );
    mv.visitJumpInsn( Opcodes.GOTO, end );
    conditionContext.fixLabels( false, mv );
    mv.visitInsn(Opcodes.ICONST_0);
    mv.visitLabel( end );
    conditionContext.clear();
  }

  public static boolean isNotPartOfBooleanExpr( IRExpression expression )
  {
    IRElement parent = expression.getParent();
    return !(parent instanceof IRConditionalAndExpression ||
             parent instanceof IRConditionalOrExpression  ||
             parent instanceof IRIfStatement ||
             parent instanceof IRWhileStatement ||
             parent instanceof IRNotExpression);

  }
  protected static int negateOpcode( int op )
  {
    int ret = 0;
    switch(op) {
      case Opcodes.IF_ACMPEQ:
        ret = Opcodes.IF_ACMPNE;
        break;
      case Opcodes.IF_ACMPNE:
        ret = Opcodes.IF_ACMPEQ;
        break;
      case Opcodes.IFEQ:
        ret = Opcodes.IFNE;
        break;
      case Opcodes.IFNE:
        ret = Opcodes.IFEQ;
        break;
      case Opcodes.IF_ICMPEQ:
        ret = Opcodes.IF_ICMPNE;
        break;
      case Opcodes.IF_ICMPNE:
        ret = Opcodes.IF_ICMPEQ;
        break;
      case Opcodes.IFNULL:
        ret = Opcodes.IFNONNULL;
        break;
      case Opcodes.IFNONNULL:
        ret = Opcodes.IFNULL;
        break;
      case Opcodes.IFLT:
        ret = Opcodes.IFGE;
        break;
      case Opcodes.IFGE:
        ret = Opcodes.IFLT;
        break;
      case Opcodes.IFLE:
        ret = Opcodes.IFGT;
        break;
      case Opcodes.IFGT:
        ret = Opcodes.IFLE;
        break;
      case Opcodes.IF_ICMPLE:
        ret = Opcodes.IF_ICMPGT;
        break;
      case Opcodes.IF_ICMPGT:
        ret = Opcodes.IF_ICMPLE;
        break;
      case Opcodes.IF_ICMPLT:
        ret = Opcodes.IF_ICMPGE;
        break;
      case Opcodes.IF_ICMPGE:
        ret = Opcodes.IF_ICMPLT;
        break;
    }
    return ret;
  }

}
