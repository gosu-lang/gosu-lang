/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

import java.util.List;

public class ReloadClassesIndicatorCompiler implements Opcodes {
  private static long _compileCount = 0;

  private List<String> _changedTypes;
  private String _strScript;

  public static byte[] updateReloadClassesIndicator(List<String> changedTypes, String strScript ) {
    return new ReloadClassesIndicatorCompiler( changedTypes, strScript ).updateReloadClassesIndicator();
  }

  private ReloadClassesIndicatorCompiler( List<String> changedTypes, String strScript ) {
    _changedTypes = changedTypes;
    _strScript = strScript;
  }

  private byte[] updateReloadClassesIndicator() {
    ClassWriter cw = new ClassWriter( 0 );
    MethodVisitor mv;

    cw.visit( V1_6, ACC_PUBLIC + ACC_SUPER, "gw/internal/gosu/parser/ReloadClassesIndicator", null, "java/lang/Object", null );

    cw.visitSource( "ReloadClassesIndicator.java", null );

    {
      mv = cw.visitMethod( ACC_PUBLIC, "<init>", "()V", null, null );
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel( l0 );
      mv.visitLineNumber( 5, l0 );
      mv.visitVarInsn( ALOAD, 0 );
      mv.visitMethodInsn( INVOKESPECIAL, "java/lang/Object", "<init>", "()V" );
      mv.visitInsn( RETURN );
      Label l1 = new Label();
      mv.visitLabel( l1 );
      mv.visitLocalVariable( "this", "Lgw/internal/gosu/parser/ReloadClassesIndicator;", null, l0, l1, 0 );
      mv.visitMaxs( 1, 1 );
      mv.visitEnd();
    }
    {
      mv = cw.visitMethod( ACC_PUBLIC + ACC_STATIC, "timestamp", "()J", null, null );
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel( l0 );
      mv.visitLineNumber( 7, l0 );
      mv.visitLdcInsn( ++_compileCount );
      mv.visitInsn( LRETURN );
      mv.visitMaxs( 2, 0 );
      mv.visitEnd();
    }
    {
      mv = cw.visitMethod( ACC_PUBLIC + ACC_STATIC, "changedTypes", "()[Ljava/lang/String;", null, null );
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel( l0 );
      mv.visitLineNumber( 11, l0 );
      mv.visitLdcInsn( _changedTypes.size() );
      mv.visitTypeInsn( ANEWARRAY, "java/lang/String" );
      for( int i = 0; i < _changedTypes.size(); i++ ) {
        mv.visitInsn( DUP );
        mv.visitLdcInsn( i );
        mv.visitLdcInsn( _changedTypes.get( i ) );
        mv.visitInsn( AASTORE );
      }
      mv.visitInsn( ARETURN );
      mv.visitMaxs( 4, 0 );
      mv.visitEnd();
    }
    {
      mv = cw.visitMethod( ACC_PUBLIC + ACC_STATIC, "getScript", "()Ljava/lang/String;", null, null );
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel( l0 );
      mv.visitLineNumber( 15, l0 );
      visitStringOrNull( mv, _strScript );
      mv.visitInsn( ARETURN );
      mv.visitMaxs( 1, 0 );
      mv.visitEnd();
    }
    cw.visitEnd();

    return cw.toByteArray();
  }

  private void visitStringOrNull( MethodVisitor mv, String value ) {
    if( value == null ) {
      mv.visitInsn( ACONST_NULL );
    }
    else {
      mv.visitLdcInsn( value );
    }
  }
}