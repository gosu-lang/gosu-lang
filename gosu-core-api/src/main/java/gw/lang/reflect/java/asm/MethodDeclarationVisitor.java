/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

/**
 */
public class MethodDeclarationVisitor extends MethodVisitor {
  private AsmMethod _asmMethod;

  public MethodDeclarationVisitor( AsmMethod method ) {
    super( Opcodes.ASM5 );
    _asmMethod = method;
  }

  @Override
  public AnnotationVisitor visitAnnotationDefault() {
    return new AsmAnnotationMethodDefaultValueVisitor( _asmMethod );
  }

  @Override
  public AnnotationVisitor visitAnnotation( String desc, boolean bVisibleAtRuntime ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, bVisibleAtRuntime );
    _asmMethod.addAnnotation( asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public AnnotationVisitor visitParameterAnnotation( int parameter, String desc, boolean bVisibleAtRuntime ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, bVisibleAtRuntime );
    _asmMethod.addParameterAnnotation( parameter, asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public void visitLineNumber( int iLine, Label label ) {
    _asmMethod.assignLineNumber( iLine );
  }

  @Override
  public void visitParameter( String name, int access ) {
    _asmMethod.assignParameter( name, access );
  }
}
