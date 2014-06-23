/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRCharacterLiteral;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRCharacterLiteralCompiler extends AbstractBytecodeCompiler
{

  public static void compile( IRCharacterLiteral expression, IRBytecodeContext context )
  {
    MethodVisitor mv = context.getMv();

    int c = expression.getValue();
    switch( c )
    {
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
        if( c >= -128 && c <= 127 )
        {
          mv.visitIntInsn( Opcodes.BIPUSH, c );
        }
        else
        {
          mv.visitIntInsn( Opcodes.SIPUSH, c );
        }
    }
  }
}