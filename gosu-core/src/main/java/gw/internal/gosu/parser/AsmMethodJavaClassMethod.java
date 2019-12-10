/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.Tree;
import gw.internal.gosu.parser.java.classinfo.AsmClassAnnotationInfo;
import gw.lang.reflect.java.JavaSourceElement;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassBytecodeMethod;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.Parameter;
import gw.lang.reflect.java.asm.AsmAnnotation;
import gw.lang.reflect.java.asm.AsmMethod;
import gw.lang.reflect.java.asm.AsmType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class AsmMethodJavaClassMethod extends JavaSourceElement implements IJavaClassMethod, IJavaClassBytecodeMethod {
  private AsmMethod _method;

  public AsmMethodJavaClassMethod( AsmMethod method ) {
    _method = method;
  }

  @Override
  public IType getReturnType() {
    return TypeSystem.getByFullNameIfValid( _method.getReturnType().getNameWithArrayBrackets() );
  }

  @Override
  public IJavaClassInfo getReturnClassInfo() {
    return JavaSourceUtil.getClassInfo( _method.getReturnType().getNameWithArrayBrackets() );
  }

  @Override
  public String getName() {
    return _method.getName();
  }

  @Override
  public List<Parameter> getParameterInfos()
  {
    return _method.getParameterInfos();
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return JavaSourceUtil.getClassInfo( _method.getDeclaringClass() );
  }

  @Override
  public IJavaClassInfo[] getParameterTypes() {
    List<AsmType> rawTypes = _method.getParameters();
    IJavaClassInfo[] types = new IJavaClassInfo[rawTypes.size()];
    for( int i = 0; i < rawTypes.size(); i++ ) {
      types[i] = JavaSourceUtil.getClassInfo( rawTypes.get( i ).getNameWithArrayBrackets() );
    }
    return types;
  }

  @Override
  public int getModifiers() {
    return _method.getModifiers();
  }

  @Override
  public boolean isSynthetic() {
    return _method.isSynthetic();
  }

  @Override
  public boolean isBridge() {
    return _method.isBridge();
  }

  @Override
  public IJavaClassInfo[] getExceptionTypes() {
    List<AsmType> rawTypes = _method.getExceptions();
    IJavaClassInfo[] types = new IJavaClassInfo[rawTypes.size()];
    for( int i = 0; i < rawTypes.size(); i++ ) {
      types[i] = JavaSourceUtil.getClassInfo( rawTypes.get( i ).getNameWithArrayBrackets() );
    }
    return types;
  }

  @Override
  public Object getDefaultValue() {
    return normalizeValue( _method.getAnnotationDefaultValue() );
  }

  private Object normalizeValue( Object value ) {
    if( value instanceof List ) {
      value = makeArray( (List)value );
    }
    return value;
  }

  private Object makeArray( List l ) {
    try {
      IJavaClassInfo ci = getReturnClassInfo();
      return AsmClassAnnotationInfo.makeArray( ci, l, this );
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  @Override
  public String getReturnTypeName() {
    return _method.getReturnType().getNameWithArrayBrackets();
  }

  @Override
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return _method.isAnnotationPresent( annotationClass );
  }

  @Override
  public IAnnotationInfo getAnnotation( Class annotationClass ) {
    AsmAnnotation annotation = _method.getAnnotation( annotationClass );
    return annotation != null ? new AsmClassAnnotationInfo( annotation, this ) : null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    List<AsmAnnotation> annotations = _method.getAnnotations();
    IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.size()];
    for( int i = 0; i < declaredAnnotations.length; i++ ) {
      declaredAnnotations[i] = new AsmClassAnnotationInfo( annotations.get( i ), this );
    }
    return declaredAnnotations;
  }

  public void setAccessible( boolean accessible ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object invoke( Object ctx, Object[] args ) throws InvocationTargetException, IllegalAccessException {
    throw new UnsupportedOperationException();
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables( IJavaMethodInfo mi ) {
    List<AsmType> typeVars = _method.getMethodType().getTypeParameters();
    FunctionType functionType = new FunctionType( mi, true );
    IJavaClassTypeVariable[] javaTypeVars = new IJavaClassTypeVariable[typeVars.size()];
    for( int i = 0; i < typeVars.size(); i++ ) {
      javaTypeVars[i] = (IJavaClassTypeVariable)AsmTypeJavaClassType.createType( typeVars.get( i ) );
    }
    return GenericTypeVariable.convertTypeVars( functionType, mi.getOwnersType(), javaTypeVars );
  }

  @Override
  public IJavaClassType[] getGenericParameterTypes() {
    List<AsmType> getParam = _method.getGenericParameters();
    IJavaClassType[] types = new IJavaClassType[getParam.size()];
    for( int i = 0; i < getParam.size(); i++ ) {
      AsmType rawType = getParam.get( i );
      IJavaClassType type = AsmTypeJavaClassType.createType( rawType );
      types[i] = type;
    }
    return types;
  }

  @Override
  public IJavaClassType getGenericReturnType() {
    return AsmTypeJavaClassType.createType( _method.getGenericReturnType() );
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode( getParameterTypes() );
    result = 31 * result + getReturnType().hashCode();
    result = 31 * result + getName().hashCode();
    return result;
  }

  public boolean equals( Object o ) {
    if( !(o instanceof IJavaClassMethod) ) {
      return false;
    }

    IJavaClassMethod jcm = (IJavaClassMethod)o;
    return getName().equals( jcm.getName() ) &&
           getReturnType() == jcm.getReturnType() &&
           Arrays.equals( getParameterTypes(), jcm.getParameterTypes() );
  }

  @Override
  public int compareTo( IJavaClassMethod o ) {
    return getName().compareTo( o.getName() );
  }

  public String toString() {
    return _method.toString();
  }

  @Override
  public Tree getTree()
  {
    ISourceFileHandle sfh = getEnclosingClass().getSourceFileHandle();
    if( sfh != null )
    {
      JavaSourceElement sourceMethod = findSourceMethod( sfh );
      if( sourceMethod != null )
      {
        return sourceMethod.getTree();
      }
    }
    return null;
  }

  private JavaSourceElement findSourceMethod( ISourceFileHandle sfh )
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

    try
    {
      return (JavaSourceElement)sourceType.getDeclaredMethod( getName(), getParameterTypes() );
    }
    catch( NoSuchMethodException e )
    {
      throw new RuntimeException( e );
    }
  }
}
