/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;

public class JavaSourceAnnotationMethod extends JavaSourceMethod {
  private JavaSourceDefaultValue _defaultValue;

  public JavaSourceAnnotationMethod(IJavaASTNode methodNode, JavaSourceType containingClass) {
    super(methodNode, containingClass);
  }

  @Override
  public Object getDefaultValue() {
    if (_defaultValue == null) {
      IJavaASTNode node = _methodNode.getChildOfType(JavaASTConstants.elementValue);
      if (node != null) {
        IJavaASTNode arrayNode = node.getChildOfType(JavaASTConstants.elementValueArrayInitializer);
        if (arrayNode != null) {
          _defaultValue = new JavaSourceDefaultValue(this, "new " + getReturnClassInfo().getName() +  " {" + arrayNode.getSource() + "}");
        } else {
          String text = node.getSource();
          _defaultValue = new JavaSourceDefaultValue(this, text);
        }
      } else {
        _defaultValue = JavaSourceDefaultValue.NULL;
      }
    }
    return _defaultValue == JavaSourceDefaultValue.NULL ? null : _defaultValue;
  }
}
