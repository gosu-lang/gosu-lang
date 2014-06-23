/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.tree.JCTree;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;

import java.lang.annotation.Annotation;

import static com.sun.tools.javac.code.Flags.ENUM;

public class JavaSourceField implements IJavaClassField {
  protected VariableTree _fieldTree;
  protected JavaSourceType _containingClass;
  protected IModifierList _modifierList;
  protected IJavaClassInfo _type;
  protected IJavaClassType _genericType;

  public static JavaSourceField create(VariableTree fieldTree, JavaSourceType containingType) {
    if (isEnumInit(fieldTree)) {
      return new JavaSourceEnumConstant(fieldTree, containingType);
    } else {
      return new JavaSourceField(fieldTree, containingType);
    }
  }

  public static boolean isEnumInit(VariableTree tree) {
    final ModifiersTree modifiers = tree.getModifiers();
    final JCTree.JCModifiers mod = (JCTree.JCModifiers) modifiers;
    return (mod.flags & ENUM) != 0;
  }

  public JavaSourceField(VariableTree fieldTree, JavaSourceType containingClass) {
    _fieldTree = fieldTree;
    _containingClass = containingClass;
  }

  public String getName() {
    return _fieldTree.getName().toString();
  }

  public String getRhs() {
    final ExpressionTree rhs = _fieldTree.getInitializer();
    return rhs == null ? null : rhs.toString();
  }

  @Override
  public IJavaClassInfo getType() {
    if (_type == null) {
      IJavaClassType genericType = getGenericType();
      if (_genericType != null) {
        _type = (IJavaClassInfo) genericType.getConcreteType();
      } else {
        _type = IJavaClassType.NULL_TYPE;
      }
    }
    return _type == IJavaClassType.NULL_TYPE ? null : _type;
  }

  @Override
  public IJavaClassType getGenericType() {
    if (_genericType == null) {
      final Tree type = _fieldTree.getType();
      _genericType = JavaSourceType.createType(getEnclosingClass(), type);
    }
    return _genericType;
  }

  @Override
  public boolean isEnumConstant() {
    return false;
  }

  public IModifierList getModifierList() {
    if (_modifierList == null) {
      final ModifiersTree modifiers = _fieldTree.getModifiers();
      _modifierList = new JavaSourceModifierList(this, modifiers);
    }
    return _modifierList;
  }

  public JavaSourceType getEnclosingClass() {
    return _containingClass;
  }

  public String toString() {
    return getEnclosingClass().getName() + " . " + getName();
  }

  @Override
  public boolean isSynthetic() {
    return false;
  }

  @Override
  public int getModifiers() {
    return getModifierList().getModifiers();
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return getModifierList().isAnnotationPresent(annotationClass);
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    return getModifierList().getAnnotation(annotationClass);
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    return getModifierList().getAnnotations();
  }
}
