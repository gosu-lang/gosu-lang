/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRPrimitiveTypeConversion;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRPrimitiveTypeConversionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRPrimitiveTypeConversion conversion, IRBytecodeContext context ) {
    IRBytecodeCompiler.compileIRExpression( conversion.getRoot(), context );

    MethodVisitor mv = context.getMv();
    IRType from = conversion.getFromType();
    IRType to = conversion.getToType();
    if( isIntType( from ) )
    {
      if( to.isLong() )
      {
        mv.visitInsn( Opcodes.I2L );
      }
      else if( to.isByte() )
      {
        mv.visitInsn( Opcodes.I2B );
      }
      else if( to.isChar() )
      {
        mv.visitInsn( Opcodes.I2C );
      }
      else if( to.isShort() )
      {
        mv.visitInsn( Opcodes.I2S );
      }
      else if( to.isFloat() )
      {
        mv.visitInsn( Opcodes.I2F );
      }
      else if( to.isDouble() )
      {
        mv.visitInsn( Opcodes.I2D );
      }
    }
    else if( from.isLong() )
    {
      if( isIntType( to ) )
      {
        mv.visitInsn( Opcodes.L2I );
        maybeConvertIntToByteCharShort(mv, to);
      }
      else if( to.isFloat() )
      {
        mv.visitInsn( Opcodes.L2F );
      }
      else if( to.isDouble() )
      {
        mv.visitInsn( Opcodes.L2D );
      }
    }
    else if( from.isFloat() )
    {
      if( isIntType( to ) )
      {
        mv.visitInsn( Opcodes.F2I );
        maybeConvertIntToByteCharShort(mv, to);
      }
      else if( to.isLong() )
      {
        mv.visitInsn( Opcodes.F2L );
      }
      else if( to.isDouble() )
      {
        mv.visitInsn( Opcodes.F2D );
      }
    }
    else if( from.isDouble() )
    {
      if( isIntType( to ) )
      {
        mv.visitInsn( Opcodes.D2I );
        maybeConvertIntToByteCharShort(mv, to);
      }
      else if( to.isLong() )
      {
        mv.visitInsn( Opcodes.D2L );
      }
      else if( to.isFloat() )
      {
        mv.visitInsn( Opcodes.D2F );
      }
    }
  }

  private static void maybeConvertIntToByteCharShort(MethodVisitor mv, IRType to) {
    if( to.isByte() )
    {
      mv.visitInsn( Opcodes.I2B );
    }
    else if( to.isChar() )
    {
      mv.visitInsn( Opcodes.I2C );
    }
    else if( to.isShort() )
    {
      mv.visitInsn( Opcodes.I2S );
    }
  }

  private static boolean isIntType( IRType type ) {
    return type.isByte() || type.isShort() || type.isChar() || type.isInt();
  }

}
