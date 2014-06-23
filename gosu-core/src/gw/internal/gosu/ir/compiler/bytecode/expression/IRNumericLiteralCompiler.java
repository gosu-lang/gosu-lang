/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRNumericLiteralCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRNumericLiteral expression, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();

    Number constant = expression.getValue();
    if( constant instanceof Byte ||
             constant instanceof Short ||
             constant instanceof Integer )
    {
      int iValue = constant.intValue();
      if (iValue <= 5 && iValue >= -1)
      {
        switch( iValue )
        {
          case -1:
            mv.visitInsn( Opcodes.ICONST_M1);
            break;
          case 0:
            mv.visitInsn( Opcodes.ICONST_0 );
            break;
          case 1:
            mv.visitInsn( Opcodes.ICONST_1 );
            break;
          case 2:
            mv.visitInsn( Opcodes.ICONST_2 );
            break;
          case 3:
            mv.visitInsn( Opcodes.ICONST_3 );
            break;
          case 4:
            mv.visitInsn( Opcodes.ICONST_4 );
            break;
          case 5:
            mv.visitInsn( Opcodes.ICONST_5 );
            break;
          default:
            throw new IllegalArgumentException("Whoops");
        }
      }
      else if (iValue >= -128 && iValue <= 127)
      {
        mv.visitIntInsn( Opcodes.BIPUSH, iValue );
      }
      else if (iValue >= -32768 && iValue <= 32767)
      {
        mv.visitIntInsn( Opcodes.SIPUSH, iValue );
      }
      else
      {
        mv.visitLdcInsn( constant );
      }
    }
    else if( constant instanceof Long )
    {
      long lValue = constant.longValue();
      if( lValue == 0 )
      {
        mv.visitInsn( Opcodes.LCONST_0 );
      }
      else if( lValue == 1 )
      {
        mv.visitInsn( Opcodes.LCONST_1 );
      }
      else
      {
        mv.visitLdcInsn( constant );
      }
    }
    else if( constant instanceof Float )
    {
      Float fValue = (Float)constant;
      if( fValue == 0 && !fValue.toString().startsWith( "-" ) )
      {
        mv.visitInsn( Opcodes.FCONST_0 );
      }
      else if( fValue == 1 )
      {
        mv.visitInsn( Opcodes.FCONST_1 );
      }
      else if( fValue == 2 )
      {
        mv.visitInsn( Opcodes.FCONST_2 );
      }
      else
      {
        mv.visitLdcInsn( constant );
      }
    }
    else if( constant instanceof Double )
    {
      Double dValue = (Double) constant;
      if( dValue == 0 && !dValue.toString().startsWith( "-" ) )
      {
        mv.visitInsn( Opcodes.DCONST_0 );
      }
      else if( dValue == 1 )
      {
        mv.visitInsn( Opcodes.DCONST_1 );
      }
      else
      {
        mv.visitLdcInsn( constant );
      }
    } else {
      throw new IllegalStateException("Unexpected numeric literal type " + constant.getClass());
    }
  }
}
