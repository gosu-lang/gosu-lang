/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.GosuShop;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.AbstractJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.ITypeInfoResolver;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.lang.annotation.Annotation;

public class JavaSourceUnresolvedClass extends AbstractJavaClassInfo implements IJavaClassType, ITypeInfoResolver {
  private String _simpleName;
  private String _namespace;
  private IModule _gosuModule;
  private ISourceFileHandle _fileHandle;

  public JavaSourceUnresolvedClass( ISourceFileHandle fileHandle, IModule gosuModule ) {
    _fileHandle = fileHandle;
    _simpleName = fileHandle.getRelativeName();
    _namespace = fileHandle.getNamespace();
    _gosuModule = gosuModule;
  }

  @Override
  public String getName() {
    return _namespace + "." + _simpleName;
  }

  @Override
  public String getSimpleName() {
    return _simpleName;
  }

  @Override
  public String getNameSignature() {
    return GosuShop.toSignature(getName());
  }

  @Override
  public String getRelativeName() {
    return _simpleName;
  }

  @Override
  public String getDisplayName() {
    return _simpleName;
  }

  @Override
  public boolean isArray() {
    return false;  
  }

  @Override
  public boolean isEnum() {
    return false;  
  }

  @Override
  public boolean isPrimitive() {
    return false;  
  }

  @Override
  public boolean isAnnotation() {
    return false;  
  }

  @Override
  public boolean isInterface() {
    return false;  
  }

  @Override
  public boolean isPublic() {
    return true;
  }

  @Override
  public boolean isProtected() {
    return false;  
  }

  @Override
  public boolean isInternal() {
    return false;  
  }

  @Override
  public boolean isPrivate() {
    return false;  
  }

  @Override
  public Object newInstance() throws InstantiationException, IllegalAccessException {
    return null;  
  }

  @Override
  public IJavaClassMethod getMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    return null;  
  }

  @Override
  public IJavaClassMethod getDeclaredMethod(String methodName, IJavaClassInfo... params) throws NoSuchMethodException {
    return null;  
  }

  @Override
  public IJavaClassMethod[] getDeclaredMethods() {
    return new IJavaClassMethod[0];  
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors() {
    return new IJavaMethodDescriptor[0];  
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {
    return new IJavaClassInfo[0];  
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {
    return new IJavaClassType[0];  
  }

  @Override
  public IJavaClassInfo getSuperclass() {
    return JavaTypes.OBJECT().getBackingClassInfo();
  }

  @Override
  public IJavaClassType getGenericSuperclass() {
    return JavaTypes.OBJECT().getBackingClassInfo();
  }

  @Override
  public IJavaClassField[] getDeclaredFields() {
    return new IJavaClassField[0];  
  }

  @Override
  public IJavaClassField[] getFields() {
    return new IJavaClassField[0];  
  }

  @Override
  public Object[] getEnumConstants() {
    return new Object[0];  
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors() {
    return new IJavaPropertyDescriptor[0];  
  }

  @Override
  public IJavaClassConstructor[] getDeclaredConstructors() {
    return new IJavaClassConstructor[0];  
  }

  @Override
  public IJavaClassConstructor getConstructor( IJavaClassInfo... params ) throws NoSuchMethodException {
    return null;
  }

  @Override
  public IType getJavaType() {
    return JavaTypes.OBJECT();
  }

  @Override
  public IJavaClassTypeVariable[] getTypeParameters() {
    return new IJavaClassTypeVariable[0];  
  }

  @Override
  public IClassDocNode createClassDocNode() {
    return null;  
  }

  @Override
  public boolean hasCustomBeanInfo() {
    return false;  
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return false;  
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return false;  
  }

  @Override
  public IJavaClassInfo getComponentType() {
    return null;  
  }

  @Override
  public int getModifiers() {
    return Modifier.PUBLIC;
  }

  @Override
  public IType getEnclosingType() {
    return null;  
  }

  @Override
  public IJavaClassInfo getArrayType() {
    return null;  
  }

  @Override
  public IJavaClassInfo[] getDeclaredClasses() {
    return new IJavaClassInfo[0];  
  }

  @Override
  public Class getBackingClass() {
    return null;
  }

  @Override
  public ISourceFileHandle getSourceFileHandle() {
    return _fileHandle;
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
    return new IAnnotationInfo[0];  
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return null;  
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap) {
    return getJavaType();
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return getJavaType();
  }

  @Override
  public IJavaClassType getConcreteType() {
    return this;
  }

  @Override
  public IJavaClassType resolveType(String relativeName, int ignoreFlags) {
    return null;  
  }

  @Override
  public IJavaClassType resolveType(String relativeName, IJavaClassInfo whosAskin, int ignoreFlags) {
    return null;  
  }

  @Override
  public IJavaClassType resolveImport(String relativeName) {
    return null;  
  }

  @Override
  public IModule getModule() {
    return _gosuModule;
  }

  @Override
  public String getNamespace() {
    return _namespace;
  }
}
