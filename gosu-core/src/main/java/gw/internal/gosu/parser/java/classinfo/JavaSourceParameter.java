/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;

import gw.lang.reflect.java.Parameter;
import java.lang.annotation.Annotation;

public class JavaSourceParameter extends Parameter implements IJavaAnnotatedElement {
  private JavaSourceMethod _method;
  private VariableTree _parameterTree;
  private IJavaClassType _genericType;
  private JavaSourceModifierList _modifierList;
  private IJavaClassInfo _type;

  public JavaSourceParameter(JavaSourceMethod sourceJavaMethod, VariableTree parameterTree) {
    super( parameterTree.getName().toString(), 0 );
    _parameterTree = parameterTree;
    _method = sourceJavaMethod;
  }

  public IJavaClassType getGenericType() {
    if (_genericType == null) {
      final Tree parType = _parameterTree.getType();
      IJavaClassType type = JavaSourceType.createType(_method,parType);

      if (type == null) {
        throw new RuntimeException("Parameter generic type cannot be null");
      }
      _genericType = type;
    }
    return _genericType;
  }

  public IJavaClassInfo getType() {
    if (_type == null) {
      _type = (IJavaClassInfo) getGenericType().getConcreteType();
      if (_type == null) {
        throw new RuntimeException("Parameter type cannot be null");
      }
    }
    return _type;
  }

  public String getName() {
    return _parameterTree.getName().toString();
  }

  public IModifierList getModifierList() {
    if (_modifierList == null) {
      _modifierList = new JavaSourceModifierList(this, _parameterTree.getModifiers());
    }
    return _modifierList;
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return false;
  }

  @Override
  public IAnnotationInfo getAnnotation(Class<? extends Annotation> annotationClass) {
    return null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    return new IAnnotationInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public JavaSourceType getEnclosingClass() {
    return _method.getEnclosingClass();
  }

  public JavaSourceMethod getMethod() {
    return _method;
  }

  public String toString( ){
    return getName();
  }

}
