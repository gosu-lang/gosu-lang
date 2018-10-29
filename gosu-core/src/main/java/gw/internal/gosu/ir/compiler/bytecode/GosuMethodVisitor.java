/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;


import gw.internal.ext.org.objectweb.asm.*;


public class GosuMethodVisitor extends MethodVisitor
{
  private boolean _bJumpOrReturnOrThrow;
  private MethodVisitor _mv;

  public GosuMethodVisitor( MethodVisitor methodVisitor )
  {
    super( Opcodes.ASM5 );
    _mv = methodVisitor;
  }


  public boolean isLastInstructionJumpOrReturnOrThrow()
  {
    return _bJumpOrReturnOrThrow;
  }

  @Override
  public AnnotationVisitor visitAnnotationDefault()
  {
    return _mv.visitAnnotationDefault();
  }

  @Override
  public AnnotationVisitor visitAnnotation( String s, boolean b )
  {
    return _mv.visitAnnotation( s, b );
  }

  @Override
  public AnnotationVisitor visitParameterAnnotation( int i, String s, boolean b )
  {
    return _mv.visitParameterAnnotation( i, s, b );
  }

  @Override
  public void visitAttribute( Attribute attribute )
  {
    _mv.visitAttribute( attribute );
  }

  @Override
  public void visitCode()
  {
    _mv.visitCode();
  }

  @Override
  public void visitFrame( int i, int i2, Object[] objects, int i3, Object[] objects2 )
  {
    _mv.visitFrame( i, i2, objects, i3, objects2 );
  }

  @Override
  public void visitInsn( int i )
  {
    switch (i)
    {
      case Opcodes.IRETURN:
      case Opcodes.LRETURN:
      case Opcodes.FRETURN:
      case Opcodes.DRETURN:
      case Opcodes.ARETURN:
      case Opcodes.RETURN:
      case Opcodes.ATHROW:
      case Opcodes.GOTO:
        _bJumpOrReturnOrThrow = true;
        break;
      default:
        _bJumpOrReturnOrThrow = false;
    }
    _mv.visitInsn( i );
  }

  @Override
  public void visitIntInsn( int i, int i2 )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitIntInsn( i, i2 );
  }

  @Override
  public void visitVarInsn( int i, int i2 )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitVarInsn( i, i2 );
  }

  @Override
  public void visitTypeInsn( int i, String s )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitTypeInsn( i, s );
  }

  @Override
  public void visitFieldInsn( int i, String s, String s2, String s3 )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitFieldInsn( i, s, s2, s3 );
  }

  @Override
  public void visitMethodInsn( int i, String s, String s2, String s3 )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitMethodInsn( i, s, s2, s3 );
  }

  @Override
  public void visitMethodInsn( int i, String s, String s2, String s3, boolean iface )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitMethodInsn( i, s, s2, s3, iface );
  }

  @Override
  public void visitInvokeDynamicInsn( String s, String s1, Handle handle, Object... objects )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitInvokeDynamicInsn( s, s1, handle, objects );
  }

  @Override
  public void visitJumpInsn( int i, Label label )
  {
    _bJumpOrReturnOrThrow = i == Opcodes.GOTO;
    _mv.visitJumpInsn( i, label );
  }

  @Override
  public void visitLabel( Label label )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitLabel( label );
  }

  @Override
  public void visitLdcInsn( Object o )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitLdcInsn( o );
  }

  @Override
  public void visitIincInsn( int i, int i2 )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitIincInsn( i, i2 );
  }

  @Override
  public void visitTableSwitchInsn( int i, int i2, Label label, Label[] labels )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitTableSwitchInsn( i, i2, label, labels );
  }

  @Override
  public void visitLookupSwitchInsn( Label label, int[] ints, Label[] labels )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitLookupSwitchInsn( label, ints, labels );
  }

  @Override
  public void visitMultiANewArrayInsn( String s, int i )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitMultiANewArrayInsn( s, i );
  }

  @Override
  public void visitTryCatchBlock( Label label, Label label2, Label label3, String s )
  {
    _bJumpOrReturnOrThrow = false;
    _mv.visitTryCatchBlock( label, label2, label3, s );
  }

  @Override
  public void visitLocalVariable( String s, String s2, String s3, Label label, Label label2, int i )
  {
    _mv.visitLocalVariable( s, s2, s3, label, label2, i );
  }

  @Override
  public void visitLineNumber( int i, Label label )
  {
    _mv.visitLineNumber( i, label );
  }

  @Override
  public void visitMaxs( int i, int i2 )
  {
    _mv.visitMaxs( i, i2 );
  }

  @Override
  public void visitEnd()
  {
    _mv.visitEnd();
  }
}
