/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaParser;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;

import java.lang.annotation.Annotation;

public class JavaSourceField implements IJavaClassField {
  protected IJavaASTNode _fieldNode;
  protected JavaSourceType _containingClass;
  protected IModifierList _modifierList;
  protected IJavaClassInfo _type;
  protected IJavaClassType _genericType;

  public static JavaSourceField create(IJavaASTNode fieldNode, JavaSourceType containingType) {
    if (fieldNode.isOfType( JavaASTConstants.fieldDeclaration) || fieldNode.isOfType( JavaASTConstants.interfaceFieldDeclaration)) {
      return new JavaSourceField(fieldNode, containingType);
    } else if (fieldNode.isOfType( JavaASTConstants.enumConstant)) {
      return new JavaSourceEnumConstant(fieldNode, containingType);
    }
    throw new RuntimeException("Unsupported field node type");
  }

  public JavaSourceField(IJavaASTNode fieldNode, JavaSourceType containingClass) {
    _fieldNode = fieldNode;
    _containingClass = containingClass;
  }

  public String getName() {
    return _fieldNode.getChildOfType(JavaParser.IDENTIFIER).getText();
  }

  public String getRhs() {
    IJavaASTNode initializerNode = _fieldNode.getChildOfType( JavaASTConstants.variableInitializer );
    return initializerNode == null ? null : initializerNode.getSource();
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
      _genericType = JavaSourceType.createType(getEnclosingClass(), _fieldNode.getChildOfType(JavaASTConstants.type));
    }
    return _genericType;
  }

  @Override
  public boolean isEnumConstant() {
    return false;
  }

  public IModifierList getModifierList() {
    if (_modifierList == null) {
      _modifierList = new JavaSourceModifierList(this, _fieldNode.getChildOfType( JavaASTConstants.modifiers));
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
