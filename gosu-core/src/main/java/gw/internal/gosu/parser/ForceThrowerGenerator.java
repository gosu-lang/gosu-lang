/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.util.GosuExceptionUtil;
import gw.internal.gosu.ir.compiler.bytecode.IRClassCompiler;

import java.io.IOException;

import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.ClassWriter;
import static gw.internal.ext.org.objectweb.asm.Opcodes.*;

public class ForceThrowerGenerator
{
  public static GosuExceptionUtil.IForceThrower create()
  {
    Class tmpClass = new ClassLoader(GosuExceptionUtil.IForceThrower.class.getClassLoader())
    {
      public Class defineClass()
      {
        byte[] bytes = generateClassBytes();
        return defineClass( "gw.internal.gosu.parser.ForceThrowGeneratorImpl", bytes, 0, bytes.length );
      }
    }.defineClass();
    try
    {
      return (GosuExceptionUtil.IForceThrower)tmpClass.newInstance();
    }
    catch( InstantiationException e )
    {
      throw new RuntimeException( e );
    }
    catch( IllegalAccessException e )
    {
      throw new RuntimeException( e );
    }
  }

  private static byte[] generateClassBytes()
  {
    ClassWriter cw = new ClassWriter( 0 );
    MethodVisitor mv;

    cw.visit( IRClassCompiler.JAVA_VER, ACC_PUBLIC + ACC_SUPER, "gw/internal/gosu/parser/ForceThrowGeneratorImpl", null,
              "java/lang/Object",
              new String[]{"gw/util/GosuExceptionUtil$IForceThrower"} );

    cw.visitSource( "ForceThrowGeneratorImpl.java", null );

    // constructor
    mv = cw.visitMethod( ACC_PUBLIC, "<init>", "()V", null, null );
    mv.visitCode();
    mv.visitVarInsn( ALOAD, 0 );
    mv.visitMethodInsn( INVOKESPECIAL, "java/lang/Object", "<init>", "()V" );
    mv.visitInsn( RETURN );
    mv.visitMaxs( 1, 1 );
    mv.visitEnd();

    // throwException method
    mv = cw.visitMethod( ACC_PUBLIC, "throwException", "(Ljava/lang/Throwable;)V", null, null );
    mv.visitCode();
    mv.visitVarInsn( ALOAD, 1 );
    mv.visitInsn( ATHROW );
    mv.visitMaxs( 1, 2 );
    mv.visitEnd();

    cw.visitEnd();
    return cw.toByteArray();
  }

  public static void main( String[] args )
  {
    try
    {
      throwIt();
    }
    catch( IOException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  private static void throwIt() throws IOException
  {
    throw new IOException();
  }
}
