/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Attribute;
import gw.internal.ext.org.objectweb.asm.FieldVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

/**
 */
public class FieldDeclarationVisitor extends FieldVisitor {
  private AsmField _field;

  public FieldDeclarationVisitor( AsmField field ) {
    super( Opcodes.ASM7 );
    _field = field;
  }

  @Override
  public AnnotationVisitor visitAnnotation( String desc, boolean bVisibleAtRuntime ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, bVisibleAtRuntime );
    _field.addAnnotation( asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public void visitAttribute( Attribute attribute ) {
  }

  @Override
  public void visitEnd() {
  }
}
