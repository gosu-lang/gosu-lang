/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class AsmField {
  private String _name;
  private int _modifiers;
  private AsmType _type;
  private List<AsmAnnotation> _annotations;
  private AsmClass _owner;
  private Object _staticValue;

  public AsmField( AsmClass owner, int access, String name, String desc, Object value ) {
    _owner = owner;
    _modifiers = access;
    _name = name;
    _annotations = Collections.emptyList();
    _type = AsmUtil.makeType( desc );
    _staticValue = value;
  }

  public String getName() {
    return _name;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public boolean isSynthetic() {
    return (_modifiers & 0x00001000) != 0;
  }

  public boolean isEnumConstant() {
    return (getModifiers() & 0x00004000) != 0;
  }

  public AsmType getType() {
    return _type;
  }
  public void setType( AsmType type ) {
    _type = type;
  }

  public AsmClass getDeclaringClass() {
    return _owner;
  }

  public List<AsmAnnotation> getAnnotations() {
    return _annotations;
  }
  void addAnnotation( AsmAnnotation asmAnnotation ) {
    if( _annotations.isEmpty() ) {
      _annotations = new ArrayList<AsmAnnotation>( 2 );
    }
    _annotations.add( asmAnnotation );
  }
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return getAnnotation( annotationClass ) != null;
  }
  public AsmAnnotation getAnnotation( Class annotationClass ) {
    for( AsmAnnotation anno: getAnnotations() ) {
      if( annotationClass.getName().equals( anno.getType().getName() ) ) {
        return anno;
      }
    }
    return null;
  }

  public Object getStaticValue() {
    return _staticValue;
  }

  public String toString() {
    int mod = getModifiers();
    return ((mod == 0) ? "" : (Modifier.toString( mod ) + " "))
        + getType() + " "
        + getName();
  }
}
