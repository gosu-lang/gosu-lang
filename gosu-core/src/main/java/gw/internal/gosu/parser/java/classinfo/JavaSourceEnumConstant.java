/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;

public class JavaSourceEnumConstant extends JavaSourceField {

  public JavaSourceEnumConstant(IJavaASTNode fieldNode, JavaSourceType containingClass) {
    super(fieldNode, containingClass);
  }

  @Override
  public IJavaClassInfo getType() {
    if (_type == null) {
      _type = (IJavaClassInfo) _containingClass.getConcreteType();
    }
    return _type;
  }

  @Override
  public IJavaClassType getGenericType() {
    if (_genericType == null) {
      _genericType = _containingClass;
    }
    return _genericType;
  }

  @Override
  public boolean isEnumConstant() {
    return true;
  }

  public IModifierList getModifierList() {
    if (_modifierList == null) {
      _modifierList = new JavaSourceModifierList(this, _fieldNode.getChildOfType(JavaASTConstants.annotations), Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
    }
    return _modifierList;
  }
}
