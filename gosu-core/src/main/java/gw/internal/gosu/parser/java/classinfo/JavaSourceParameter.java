/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaLexer;
import gw.internal.gosu.parser.java.LeafASTNode;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;

import java.lang.annotation.Annotation;

public class JavaSourceParameter implements IJavaAnnotatedElement {
  private JavaSourceMethod _method;
  private IJavaASTNode _parameterNode;
  private IJavaClassType _genericType;
  private JavaSourceModifierList _modifierList;
  private IJavaClassInfo _type;

  public JavaSourceParameter(JavaSourceMethod sourceJavaMethod, IJavaASTNode parameterNode) {
    _parameterNode = parameterNode;
    _method = sourceJavaMethod;
  }

  public IJavaClassType getGenericType() {
    if (_genericType == null) {
      IJavaClassType type = JavaSourceType.createType(_method, _parameterNode.getChildOfType(JavaASTConstants.type));
      if (_parameterNode.isOfType(JavaASTConstants.ellipsisParameterDecl)) {
        type = new JavaSourceArrayType(type);
      }

      //handle c-style array declarations correctly
      int cStyleArrayDeclCheck = _parameterNode.getChildOfTypeIndex(JavaLexer.IDENTIFIER) + 1;
      while (cStyleArrayDeclCheck < _parameterNode.getChildren().size()) {
        IJavaASTNode child = _parameterNode.getChild(cStyleArrayDeclCheck);
        if (child instanceof LeafASTNode && ((LeafASTNode) child).getTokenType() == JavaLexer.LBRACKET) {
          type = new JavaSourceArrayType(type);
        }
        cStyleArrayDeclCheck++;
      }

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
    return null;
  }

  public IModifierList getModifierList() {
    if (_modifierList == null) {
      _modifierList = new JavaSourceModifierList(this, _parameterNode.getChildOfType( JavaASTConstants.variableModifiers));
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
