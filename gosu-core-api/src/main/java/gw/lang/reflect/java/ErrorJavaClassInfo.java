/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.reflect.java;

import gw.lang.javadoc.IClassDocNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

import java.lang.annotation.Annotation;

public class ErrorJavaClassInfo implements IJavaClassInfo {

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
  public boolean isAnnotation() {

    return false;
  }

  @Override
  public boolean isInterface() {

    return false;
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap) {
    return TypeSystem.getErrorType();
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return TypeSystem.getErrorType();
  }

  @Override
  public IJavaClassType getConcreteType() {
    return this;
  }

  @Override
  public String getName() {
    return TypeSystem.getErrorType().getName();
  }

  @Override
  public String getNameSignature() {
    return getName();
  }

  @Override
  public IJavaClassMethod getMethod(String methodName,
                                    IJavaClassInfo... paramTypes) throws NoSuchMethodException {

    return null;
  }

  @Override
  public IJavaClassMethod getDeclaredMethod(String methodName,
                                            IJavaClassInfo... params) throws NoSuchMethodException {

    return null;
  }

  @Override
  public IJavaClassMethod[] getDeclaredMethods() {

    return new IJavaClassMethod[0];
  }

  @Override
  public Object newInstance() throws InstantiationException,
      IllegalAccessException {

    return null;
  }

  @Override
  public Object[] getEnumConstants() {

    return new Object[0];
  }

  @Override
  public IType getJavaType() {

    return TypeSystem.getErrorType();
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {

    return new IJavaClassInfo[0];
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
  public IJavaClassField[] getDeclaredFields() {

    return new IJavaClassField[0];
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
  public IClassDocNode createClassDocNode() {

    return null;
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors() {

    return new IJavaPropertyDescriptor[0];
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors() {

    return new IJavaMethodDescriptor[0];
  }

  @Override
  public boolean hasCustomBeanInfo() {

    return false;
  }

  @Override
  public String getRelativeName() {

    return TypeSystem.getErrorType().getRelativeName();
  }

  @Override
  public String getDisplayName() {

    return TypeSystem.getErrorType().getDisplayName();
  }

  @Override
  public String getSimpleName() {
    return TypeSystem.getErrorType().getRelativeName();
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {

    return true;
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {

    return false;
  }

  @Override
  public IJavaClassField[] getFields() {

    return new IJavaClassField[0];
  }

  @Override
  public boolean isArray() {

    return false;
  }

  @Override
  public IJavaClassInfo getComponentType() {

    return null;
  }

  @Override
  public boolean isEnum() {

    return false;
  }

  @Override
  public int getModifiers() {

    return 0;
  }

  @Override
  public boolean isPrimitive() {

    return false;
  }

  @Override
  public IType getEnclosingType() {

    return null;
  }

  @Override
  public String getNamespace() {

    return TypeSystem.getErrorType().getNamespace();
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {

    return new IJavaClassType[0];
  }

  @Override
  public IJavaClassType getGenericSuperclass() {

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
  public boolean isAssignableFrom(IJavaClassInfo aClass) {

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
  public Class getBackingClass() {

    return null;
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
    return TypeSystem.getGlobalModule();
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return null;
  }

}
