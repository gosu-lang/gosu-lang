/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.Tree;

public class JavaSourceAnnotationMethod extends JavaSourceMethod {
  private JavaSourceDefaultValue _defaultValue;

  public JavaSourceAnnotationMethod(MethodTree method, JavaSourceType containingClass) {
    super(method, containingClass);
  }

  @Override
  public Object getDefaultValue() {
    if (_defaultValue == null) {
      final Tree defaultValue = _method.getDefaultValue();
      if (defaultValue != null) {
        if(defaultValue instanceof NewArrayTree) {
          _defaultValue = new JavaSourceDefaultValue(this, "new " + getReturnClassInfo().getName() + defaultValue.toString());
        } else {
          _defaultValue = new JavaSourceDefaultValue(this, defaultValue.toString());
        }
      } else {
        _defaultValue = JavaSourceDefaultValue.NULL;
      }
    }
    return _defaultValue == JavaSourceDefaultValue.NULL ? null : _defaultValue;
  }
}
