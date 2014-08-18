/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaParser;
import gw.internal.gosu.parser.java.LeafASTNode;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class JavaSourceModifierList implements IModifierList {
  private static final JavaSourceAnnotationInfo[] NONE = new JavaSourceAnnotationInfo[0];
  private IJavaASTNode _modifiersOrAnnotationsNode;
  private int _modifiers;
  private IJavaAnnotatedElement _owner;
  private JavaSourceAnnotationInfo[] _annotations;

  /**
   * For enum constants.
   */
  public JavaSourceModifierList(JavaSourceEnumConstant owner, IJavaASTNode annotationsNode, int modifiers) {
    _owner = owner;
    _modifiers = modifiers;
    _modifiersOrAnnotationsNode = annotationsNode;
  }

  public JavaSourceModifierList(IJavaAnnotatedElement owner, IJavaASTNode modifiersNode) {
    _owner = owner;
    _modifiersOrAnnotationsNode = modifiersNode;
    for (IJavaASTNode child : modifiersNode.getChildren()) {
      if (child.isLeaf()) {
        int tokenType = ((LeafASTNode) child).getTokenType();
        switch (tokenType) {
          case JavaParser.PUBLIC:
            _modifiers |= Modifier.PUBLIC;
            break;
          case JavaParser.PROTECTED:
            _modifiers |= Modifier.PROTECTED;
            break;
          case JavaParser.PRIVATE:
            _modifiers |= Modifier.PRIVATE;
            break;
          case JavaParser.STATIC:
            _modifiers |= Modifier.STATIC;
            break;
          case JavaParser.ABSTRACT:
            _modifiers |= Modifier.ABSTRACT;
            break;
          case JavaParser.FINAL:
            _modifiers |= Modifier.FINAL;
            break;
          case JavaParser.NATIVE:
            _modifiers |= Modifier.NATIVE;
            break;
          case JavaParser.SYNCHRONIZED:
            _modifiers |= Modifier.SYNCHRONIZED;
            break;
          case JavaParser.STRICTFP:
            _modifiers |= Modifier.STRICT;
            break;
          case JavaParser.TRANSIENT:
            _modifiers |= Modifier.TRANSIENT;
            break;
          case JavaParser.VOLATILE:
            _modifiers |= Modifier.VOLATILE;
            break;
          default:
            throw new RuntimeException("Unknown modifier " + child.getText());
        }
      }
    }
    IJavaClassInfo declaringOwner = owner instanceof JavaSourceType ? (IJavaClassInfo) owner : owner.getEnclosingClass();
    if (declaringOwner.isInterface() || declaringOwner.isAnnotation()) {
      _modifiers |= Modifier.PUBLIC;
      _modifiers |= Modifier.ABSTRACT;
      if (owner instanceof JavaSourceField) {
        _modifiers |= Modifier.STATIC;
      }
    }
    // Types, nested in interfaces are public and static
    if (owner instanceof JavaSourceType && declaringOwner.getEnclosingClass() instanceof JavaSourceInterface) {
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
      if (_modifiersOrAnnotationsNode == null) {
        _annotations = NONE;
      } else {
        List<IJavaASTNode> annotationNodes = _modifiersOrAnnotationsNode.getChildrenOfTypes(JavaASTConstants.annotation);
        List<JavaSourceAnnotationInfo> annotations = new ArrayList<JavaSourceAnnotationInfo>();
        for (IJavaASTNode annotationNode : annotationNodes) {
          annotations.add(new JavaSourceAnnotationInfo(annotationNode, _owner));
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
