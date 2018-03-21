/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.Tree;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
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
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaSourceElement;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LocklessLazyVar;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import gw.util.Array;

public class JavaArrayClassInfo extends AbstractJavaClassInfo
{
  public static final LocklessLazyVar<IJavaClassInfo[]> INTERFACES =
    new LocklessLazyVar<IJavaClassInfo[]>()
    {
      @Override
      protected IJavaClassInfo[] init()
      {
        // All Java arrays are Cloneable and Serializable (see javadoc for Class#getGenericInterfaces())
        return new IJavaClassInfo[] {
          JavaTypes.getJreType( Cloneable.class ).getBackingClassInfo(),
          JavaTypes.getJreType( Serializable.class ).getBackingClassInfo(),
        };
      }
    };

  private IJavaClassInfo _component;

  public JavaArrayClassInfo( IJavaClassInfo component ) {
    if (component == null) {
      throw new NullPointerException("Cannot have a null array component.");
    }
    _component = component;
  }

  @Override
  public IClassDocNode createClassDocNode() {
    return null;
  }

  @Override
  public IJavaClassInfo getArrayType() {
    return new JavaArrayClassInfo(this);
  }

  @Override
  public IJavaClassInfo getComponentType() {
    return _component;
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
  public IJavaClassField[] getDeclaredFields() {
    return new IJavaClassField[0];
  }

  @Override
  public IJavaClassMethod[] getDeclaredMethods() {
    return new IJavaClassMethod[0];
  }

  @Override
  public String getDisplayName() {
    return _component.getDisplayName() + "[]";
  }

  @Override
  public String getSimpleName() {
    return _component.getSimpleName() + "[]";
  }

  @Override
  public IType getEnclosingType() {
    return null;
  }

  @Override
  public Object[] getEnumConstants() {
    return new Object[0];
  }

  @Override
  public IJavaClassField[] getFields() {
    return new IJavaClassField[0];
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {
    return INTERFACES.get();
  }

  @Override
  public IJavaClassType getGenericSuperclass() {
    return null;
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {
    return INTERFACES.get();
  }

  @Override
  public IType getJavaType() {
    return _component.getJavaType().getArrayType();
  }
  public void setJavaType(IJavaType javaType) {
  }

  @Override
  public IJavaClassMethod getMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    return null;
  }

  @Override
  public IJavaClassMethod getDeclaredMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    return null;
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors() {
    return new IJavaMethodDescriptor[0];
  }

  @Override
  public int getModifiers() {
    return _component.getModifiers();
  }

  @Override
  public String getName() {
    return _component.getName() + "[]";
  }

  public String getNameSignature() {
    return "[" + _component.getNameSignature();
  }

  @Override
  public String getNamespace() {
    return _component.getNamespace();
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors() {
    return new IJavaPropertyDescriptor[0];
  }

  @Override
  public String getRelativeName() {
    return _component.getRelativeName() + "[]";
  }

  @Override
  public IJavaClassInfo getSuperclass() {
    return null;
  }

  @Override
  public IJavaClassTypeVariable[] getTypeParameters() {
    return new IJavaClassTypeVariable[0];
  }

  @Override
  public boolean hasCustomBeanInfo() {
    return false;
  }

  @Override
  public boolean isAnnotation() {
    return false;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return _component.isHiddenViaFeatureDescriptor();
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return _component.isVisibleViaFeatureDescriptor(constraint);
  }

  @Override
  public Object newInstance() throws InstantiationException, IllegalAccessException {
    return null;
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    return null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    return new IAnnotationInfo[0];
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return false;
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap) {
    return _component.getActualType(typeMap).getArrayType();
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return _component.getActualType(typeMap, bKeepTypeVars).getArrayType();
  }

  @Override
  public IJavaClassType getConcreteType() {
    return this;
  }

  @Override
  public IJavaClassInfo[] getDeclaredClasses() {
    return new IJavaClassInfo[0];
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

  public String toString() {
    return _component.getName() + "[]";
  }

  @Override
  public Class getBackingClass() {
    Class backingClass = _component.getBackingClass();
    return backingClass == null ? null : Array.newInstance( backingClass, 0 ).getClass();
  }

  @Override
  public ISourceFileHandle getSourceFileHandle() {
    return null;
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
    return _component.getModule();
  }

  @Override
  public Tree getTree()
  {
    IJavaClassInfo componentType = getComponentType();
    if( componentType instanceof JavaSourceElement )
    {
      return ((JavaSourceElement)componentType).getTree();
    }
    return null;
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return null;
  }

  @Override
  public IJavaClassInfo getDeclaringClass()
  {
    IJavaClassInfo componentType = getComponentType();
    if( componentType instanceof JavaSourceElement )
    {
      return ((JavaSourceElement)componentType).getDeclaringClass();
    }
    return null;
  }
}
