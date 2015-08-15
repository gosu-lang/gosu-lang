/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.tools.javac.tree.JCTree;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class JavaSourceModifierList implements IModifierList {
  private static final JavaSourceAnnotationInfo[] NONE = new JavaSourceAnnotationInfo[0];
  private ModifiersTree _modifiersTree;
  private int _modifiers;
  private IJavaAnnotatedElement _owner;
  private JavaSourceAnnotationInfo[] _annotations;

  /**
   * For enum constants.
   */
  public JavaSourceModifierList(JavaSourceEnumConstant owner, ModifiersTree modifiersTree, int modifiers) {
    _owner = owner;
    _modifiers = modifiers;
    _modifiersTree = modifiersTree;
  }

  public JavaSourceModifierList(IJavaAnnotatedElement owner, ModifiersTree modifiersTree) {
    _owner = owner;
    _modifiersTree = modifiersTree;
    _modifiers = (int)((JCTree.JCModifiers)_modifiersTree).flags;
    IJavaClassInfo declaringOwner = owner instanceof JavaSourceType ? (IJavaClassInfo) owner : owner.getEnclosingClass();
    if( declaringOwner.isInterface() || declaringOwner.isAnnotation() ) {
      if( !modifiersTree.getFlags().contains( javax.lang.model.element.Modifier.DEFAULT ) ) {
        _modifiers |= Modifier.ABSTRACT;
      }
      if (owner instanceof JavaSourceField) {
        _modifiers |= Modifier.STATIC;
      }
    }
    // Types, nested in interfaces are public and static
    if( owner instanceof JavaSourceType && declaringOwner.getEnclosingClass() instanceof JavaSourceInterface ) {
      _modifiers |= Modifier.PUBLIC;
      _modifiers |= Modifier.STATIC;
    }
    if (declaringOwner.getEnclosingClass() == null && !hasModifier(Modifier.PUBLIC) && !hasModifier(Modifier.PROTECTED) && !hasModifier(Modifier.PRIVATE)) {
      _modifiers |= Modifier.INTERNAL;
    }
  }

  public boolean hasModifier(int modifierType) {
    return (_modifiers & modifierType) != 0;
  }

  @Override
  public int getModifiers() {
    return _modifiers;
  }

  private void maybeInitAnnotations() {
    if (_annotations == null) {
      List<? extends AnnotationTree> annotationsTrees = _modifiersTree.getAnnotations();
      if (annotationsTrees.isEmpty()) {
        _annotations = NONE;
      } else {
        List<JavaSourceAnnotationInfo> annotations = new ArrayList<JavaSourceAnnotationInfo>();
        for (AnnotationTree annotationTree : annotationsTrees) {
          annotations.add(new JavaSourceAnnotationInfo(annotationTree, _owner));
        }
        _annotations = annotations.toArray(new JavaSourceAnnotationInfo[annotations.size()]);
      }
    }
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return getAnnotation(annotationClass) != null;
  }

  public IAnnotationInfo[] getAnnotations() {
    maybeInitAnnotations();
    return _annotations;
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    maybeInitAnnotations();
    for (JavaSourceAnnotationInfo annotation : _annotations) {
      if (annotation.getName().equals(annotationClass.getName().replace('$', '.'))) {
        return annotation;
      }
    }
    return null;
  }

  public void setModifiers(int modifiers) {
    this._modifiers = modifiers;
  }

  public String toString() {
    return _owner.toString();
  }
}
