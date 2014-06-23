/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaAnnotatedElement;

import java.lang.annotation.Annotation;

public class JavaAsmModifierList implements IModifierList {
  private static final AsmClassAnnotationInfo[] NONE = new AsmClassAnnotationInfo[0];
  private int _modifiers;
  private IJavaAnnotatedElement _owner;
  private AsmClassAnnotationInfo[] _annotations;

  public JavaAsmModifierList( IJavaAnnotatedElement owner, AsmClassAnnotationInfo[] annotations, int modifiers ) {
    _owner = owner;
    _modifiers = modifiers;
    _annotations = annotations;
  }

  public boolean hasModifier( int modifierType ) {
    return (_modifiers & modifierType) != 0;
  }

  @Override
  public int getModifiers() {
    return _modifiers;
  }

  @Override
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return getAnnotation( annotationClass ) != null;
  }

  public IAnnotationInfo[] getAnnotations() {
    return _annotations;
  }

  @Override
  public IAnnotationInfo getAnnotation( Class annotationClass ) {
    for( AsmClassAnnotationInfo annotation : _annotations ) {
      if( annotation.getName().equals( annotationClass.getName().replace( '$', '.' ) ) ) {
        return annotation;
      }
    }
    return null;
  }

  public void setModifiers( int modifiers ) {
    this._modifiers = modifiers;
  }

  public String toString() {
    return _owner.toString();
  }
}
