/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.Tree;
import gw.internal.gosu.parser.java.classinfo.AsmClassAnnotationInfo;
import gw.lang.reflect.java.JavaSourceElement;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.gosuc.simple.CompilerDriverException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.AsmAnnotation;
import gw.lang.reflect.java.asm.AsmField;

import java.lang.annotation.Annotation;
import java.util.List;

public class AsmFieldJavaClassField extends JavaSourceElement implements IJavaClassField
{
  private AsmField _field;

  public AsmFieldJavaClassField( AsmField field ) {
    _field = field;
  }

  @Override
  public boolean isSynthetic() {
    return _field.isSynthetic();
  }

  @Override
  public int getModifiers() {
    return _field.getModifiers();
  }

  @Override
  public String getName() {
    return _field.getName();
  }

  @Override
  public IJavaClassInfo getType() {
    return JavaSourceUtil.getClassInfo( _field.getType().getRawType().getNameWithArrayBrackets() );
  }

  @Override
  public IJavaClassType getGenericType() {
    IJavaClassType type = AsmTypeJavaClassType.createType( _field.getType() );
    if( type == null ) {
      throw new CompilerDriverException( "Unable to create a generic type for the field " + _field.getName() + " on " + _field.getDeclaringClass().getName() + ". Please make sure all the external dependencies are present on the classpath.\n" +
                                         "Type : " + _field.getType() + ", Type.class " + _field.getType().getClass().getName() + " GenericType : " + _field.getType() + ", GenericType.class : " + _field.getType().getClass().getName() );
    }
    return type;
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return JavaSourceUtil.getClassInfo( _field.getDeclaringClass() );
  }

  @Override
  public boolean isEnumConstant() {
    return _field.isEnumConstant();
  }

  @Override
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return _field.isAnnotationPresent( annotationClass );
  }

  @Override
  public IAnnotationInfo getAnnotation( Class annotationClass ) {
    AsmAnnotation annotation = _field.getAnnotation( annotationClass );
    return annotation != null ? new AsmClassAnnotationInfo( annotation, this ) : null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    List<AsmAnnotation> annotations = _field.getAnnotations();
    IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.size()];
    for( int i = 0; i < declaredAnnotations.length; i++ ) {
      declaredAnnotations[i] = new AsmClassAnnotationInfo( annotations.get( i ), this );
    }
    return declaredAnnotations;
  }

  public String toString() {
    return _field.toString();
  }

  public Object getStaticValue( ) {
    return _field.getStaticValue();
  }

  @Override
  public Tree getTree()
  {
    ISourceFileHandle sfh = getEnclosingClass().getSourceFileHandle();
    if( sfh != null )
    {
      JavaSourceElement sourceMethod = findSourceField( sfh );
      if( sourceMethod != null )
      {
        return sourceMethod.getTree();
      }
    }
    return null;
  }

  private JavaSourceElement findSourceField( ISourceFileHandle sfh )
  {
    IJavaClassInfo sourceType = JavaSourceType.createTopLevel( sfh );
    if( sourceType == null )
    {
      return null;
    }

    IType enclosingClass = getEnclosingClass().getJavaType();
    if( enclosingClass.getEnclosingType() != null )
    {
      sourceType = findInnerSourceType( sourceType, enclosingClass.getName() );
    }

    return (JavaSourceElement)sourceType.getDeclaredField( getName() );
  }
}
