/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.AsmMethodJavaClassMethod;
import gw.internal.gosu.parser.MethodJavaClassMethod;
import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Array;
import java.util.List;

public class JavaSourceAnnotationInfo implements IAnnotationInfo {
  private IJavaASTNode _annotationNode;
  private IJavaAnnotatedElement _owner;
  private IModule _gosuModule;
  private String _name;
  private IJavaClassInfo _type;

  public JavaSourceAnnotationInfo(IJavaASTNode annotationNode, IJavaAnnotatedElement owner) {
    _annotationNode = annotationNode;
    _owner = owner;
    _gosuModule = _owner instanceof IJavaClassInfo ? ((IJavaClassInfo) _owner).getModule() : _owner.getEnclosingClass().getModule();
  }

  @Override
  public IType getType() {
    return TypeSystem.getByFullNameIfValid(getName(), _gosuModule);
  }

  @Override
  public Object getInstance() {
    throw new RuntimeException("Not supported for source types");
  }

  @Override
  public Object getFieldValue(String fieldName) {
    initNameAndType();
    try {
      IJavaClassMethod method = _type.getMethod(fieldName);
      return parseValue(method);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private Object parseValue(IJavaClassMethod method) {
    IJavaASTNode valueNode = null;
    IJavaASTNode pairs = _annotationNode.getChildOfType( JavaASTConstants.elementValuePairs );
    if( pairs != null ) {
      for( IJavaASTNode pair : pairs.getChildrenOfTypes( JavaASTConstants.elementValuePair ) ) {
        if( pair.getChild( 0 ).getText().equals( method.getName() ) ) {
          valueNode = pair.getChildOfType( JavaASTConstants.elementValue );
          break;
        }
      }
    }
    else {
      valueNode = _annotationNode.getChildOfType( JavaASTConstants.elementValue );
    }

    if( valueNode == null ) {
      Object defaultValue = method.getDefaultValue();
      if (method instanceof MethodJavaClassMethod || method instanceof AsmMethodJavaClassMethod) {
        if (defaultValue.getClass().isArray()) {
          String[] value = new String[Array.getLength(defaultValue)];
          for (int i = 0; i < value.length; i++) {
            value[i] = Array.get(defaultValue, i).toString();
          }
          return value;
        } else {
          return defaultValue;
        }
      } else {
        return ((JavaSourceDefaultValue) defaultValue).evaluate();
      }
    }

    return evaluate( method.getReturnClassInfo(), valueNode );
  }

  private Object evaluate( IJavaClassInfo type, IJavaASTNode valueNode ) {
    IJavaASTNode annotationValue = valueNode.getChildOfType( JavaASTConstants.annotation );
    if( annotationValue != null ) {
      return new JavaSourceAnnotationInfo(annotationValue, _owner);
    }

    IJavaASTNode arrayNode = valueNode.getChildOfType( JavaASTConstants.elementValueArrayInitializer );
    if( arrayNode != null ) {
      List<IJavaASTNode> children = arrayNode.getChildrenOfTypes( JavaASTConstants.elementValue );
      Object[] arrayResult = null;
      int i = 0;
      for( IJavaASTNode elemValue : children ) {
        Object value = evaluate( type.getComponentType(), elemValue );
        if( arrayResult == null ) {
          arrayResult = (Object[])Array.newInstance( value.getClass(), children.size() );
        }
        arrayResult[i++] = value;
      }
      return arrayResult;
    }

    String text = valueNode.getSource();

    if (type.isEnum()) {
      return parseEnum(text, type);
    } else {
      JavaSourceType enclosingType = getEnclosingType( _owner );
      IExpression pr = CompileTimeExpressionParser.parse( text, enclosingType, handleSingleElementArrayType( text, type ) );
      try {
        return pr.evaluate();
      } catch (Exception e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        return null;
      }
    }
  }

  private IType handleSingleElementArrayType( String text, IJavaClassInfo type ) {
    IType javaType = type.getJavaType();
    if( !javaType.isArray() ) {
      return javaType;
    }
    if( text.startsWith( "{" ) ) {
      return javaType;
    }
    return javaType.getComponentType();
  }

  private JavaSourceType getEnclosingType( IJavaAnnotatedElement owner ) {
    if( owner instanceof JavaSourceType ) {
      return (JavaSourceType)owner;
    }
    return (JavaSourceType)owner.getEnclosingClass();
  }

  private Object parseEnum(String text, IJavaClassInfo type) {
    String enumConstName = text.substring(text.lastIndexOf('.') + 1);
    IJavaClassField[] fields = type.getDeclaredFields();
    for (IJavaClassField field : fields) {
      if (field.isEnumConstant() && field.getName().equals(enumConstName)) {
        return field.getName();
      }
    }
    return null;
  }

  @Override
  public String getName() {
    initNameAndType();
    return _name;
  }

  private void initNameAndType() {
    if (_name == null) {
      IJavaASTNode qNameNode = _annotationNode.getChildOfType(JavaASTConstants.qualifiedName);
      String name = "";
      for (IJavaASTNode node : qNameNode.getChildren()) {
        name += node.getText();
      }
      IJavaClassInfo _containingClass = _owner instanceof IJavaClassInfo ? ((IJavaClassInfo) _owner) : _owner.getEnclosingClass();
      _type = (IJavaClassInfo) JavaSourceType.createType(_containingClass, name, JavaSourceType.IGNORE_NONE);
      _name = _type.getName().replace('$', '.');
    }
  }

  @Override
  public String getDescription() {
    return getName();
  }

  @Override
  public IType getOwnersType() {
    return _owner.getEnclosingClass().getJavaType();
  }

}
