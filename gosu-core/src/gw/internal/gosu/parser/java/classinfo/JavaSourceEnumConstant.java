/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.VariableTree;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;

public class JavaSourceEnumConstant extends JavaSourceField {

  public JavaSourceEnumConstant(VariableTree fieldTree, JavaSourceType containingClass) {
    super(fieldTree, containingClass);
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
      final ModifiersTree modifiers = _fieldTree.getModifiers();
      _modifierList = new JavaSourceModifierList(this, modifiers, Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
    }
    return _modifierList;
  }
}
