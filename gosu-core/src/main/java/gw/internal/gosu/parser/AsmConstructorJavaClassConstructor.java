/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.Tree;
import gw.internal.gosu.parser.java.classinfo.AsmClassAnnotationInfo;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassBytecodeConstructor;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.JavaSourceElement;
import gw.lang.reflect.java.Parameter;
import gw.lang.reflect.java.asm.AsmAnnotation;
import gw.lang.reflect.java.asm.AsmMethod;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.module.IModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AsmConstructorJavaClassConstructor extends JavaSourceElement implements IJavaClassConstructor, IJavaClassBytecodeConstructor {
  private AsmMethod _ctor;
  private IModule _module;

  public AsmConstructorJavaClassConstructor( AsmMethod ctor, IModule module ) {
    _ctor = ctor;
    _module = module;
  }

  @Override
  public IJavaClassInfo[] getExceptionTypes() {
    List<AsmType> rawTypes = _ctor.getExceptions();
    IJavaClassInfo[] types = new IJavaClassInfo[rawTypes.size()];
    for( int i = 0; i < rawTypes.size(); i++ ) {
      types[i] = JavaSourceUtil.getClassInfo( rawTypes.get( i ).getRawType().getName(), _module );
    }
    return types;
  }

  @Override
  public List<Parameter> getParameterInfos()
  {
    return _ctor.getParameterInfos();
  }

  @Override
  public int getModifiers() {
    return _ctor.getModifiers();
  }

  @Override
  public boolean isSynthetic() {
    return _ctor.isSynthetic();
  }

  @Override
  public IParameterInfo[] convertGenericParameterTypes( IFeatureInfo container, TypeVarToTypeMap actualParamByVarName ) {
    return JavaMethodInfo.convertGenericParameterTypes( container, actualParamByVarName, getGenericParameterTypes(), getEnclosingClass(), getParameterInfos() );
  }

  private IJavaClassType[] getGenericParameterTypes() {
    List<AsmType> rawTypes = _ctor.getGenericParameters();
    IJavaClassType[] types = new IJavaClassType[rawTypes.size()];
    for( int i = 0; i < rawTypes.size(); i++ ) {
      types[i] = AsmTypeJavaClassType.createType( rawTypes.get( i ), _module );
    }
    return types;
  }

  @Override
  public IJavaClassInfo[] getParameterTypes() {
    List<AsmType> rawParamTypes = _ctor.getParameters();
    IJavaClassInfo[] paramTypes = new IJavaClassInfo[rawParamTypes.size()];
    for( int i = 0; i < rawParamTypes.size(); i++ ) {
      paramTypes[i] = JavaSourceUtil.getClassInfo( rawParamTypes.get( i ).getRawType().getNameWithArrayBrackets(), _module );
    }
    return paramTypes;
  }

  public Object newInstance( Object[] objects ) throws InvocationTargetException, IllegalAccessException, InstantiationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isDefault() {
    return false;
  }

  @Override
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return _ctor.isAnnotationPresent( annotationClass );
  }

  @Override
  public IAnnotationInfo getAnnotation( Class annotationClass ) {
    AsmAnnotation annotation = _ctor.getAnnotation( annotationClass );
    return annotation != null ? new AsmClassAnnotationInfo( annotation, this ) : null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    List<AsmAnnotation> annotations = _ctor.getAnnotations();
    IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.size()];
    for( int i = 0; i < declaredAnnotations.length; i++ ) {
      declaredAnnotations[i] = new AsmClassAnnotationInfo( annotations.get( i ), this );
    }
    return declaredAnnotations;
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return JavaSourceUtil.getClassInfo( _ctor.getDeclaringClass(), _module );
  }

  @Override
  public Tree getTree()
  {
    JavaSourceElement sourceCtor = findSourceConstructor();
    if( sourceCtor != null )
    {
      return sourceCtor.getTree();
    }
    return null;
  }

  private JavaSourceElement findSourceConstructor()
  {
    ISourceFileHandle sfh = getEnclosingClass().getSourceFileHandle();
    if( sfh == null )
    {
      return null;
    }

    IJavaClassInfo sourceType = JavaSourceType.createTopLevel( sfh, getEnclosingClass().getModule() );
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
      return (JavaSourceElement)sourceType.getConstructor( getParameterTypes() );
    }
    catch( NoSuchMethodException e )
    {
      throw new RuntimeException( e );
    }
  }
}
