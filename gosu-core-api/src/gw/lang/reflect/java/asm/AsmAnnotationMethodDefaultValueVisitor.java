/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class AsmAnnotationMethodDefaultValueVisitor extends AnnotationVisitor {
  private AsmMethod _asmMethod;

  public AsmAnnotationMethodDefaultValueVisitor( AsmMethod asmMethod ) {
    super( Opcodes.ASM5 );
    _asmMethod = asmMethod;
  }

  @Override
  public void visit( String name, Object value ) {
    _asmMethod.setAnnotationDefaultValue( value );
  }

  @Override
  public void visitEnum( String name, String desc, String value ) {
    _asmMethod.setAnnotationDefaultValue( value );
  }

  @Override
  public AnnotationVisitor visitAnnotation( String name, String desc ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, true );
    _asmMethod.setAnnotationDefaultValue( asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public AnnotationVisitor visitArray( String name ) {
    List<Object> values = new ArrayList<Object>();
    _asmMethod.setAnnotationDefaultValue( values );
    return new AnnotationArrayValuesVisitor( values );
  }
}
