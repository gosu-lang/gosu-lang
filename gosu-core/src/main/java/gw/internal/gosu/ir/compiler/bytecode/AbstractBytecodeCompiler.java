/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.lang.ir.IRElement;
import gw.lang.ir.IRType;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

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
}
