/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.javadoc.IClassDocNode;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.TreeSet;

public interface IJavaClassInfo extends IJavaAnnotatedElement, IJavaClassType, ITypeInfoResolver {
  Object LOCK = IJavaClassInfo.class;

  String getNameSignature();

  String getRelativeName();

  String getDisplayName();

  boolean isArray();

  boolean isEnum();

  boolean isPrimitive();

  boolean isAnnotation();

  boolean isInterface();

  boolean isAssignableFrom(IJavaClassInfo aClass);

  boolean isPublic();
  boolean isProtected();
  boolean isInternal();
  boolean isPrivate();

  Object newInstance() throws InstantiationException, IllegalAccessException;


  IJavaClassMethod getMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException;

  IJavaClassMethod getDeclaredMethod(String methodName, IJavaClassInfo... params) throws NoSuchMethodException;

  IJavaClassMethod[] getDeclaredMethods();

  IJavaMethodDescriptor[] getMethodDescriptors();


  IJavaClassInfo[] getInterfaces();

  IJavaClassType[] getGenericInterfaces();


  IJavaClassInfo getSuperclass();

  IJavaClassType getGenericSuperclass();


  IJavaClassField[] getDeclaredFields();

  IJavaClassField[] getFields();

  Object[] getEnumConstants();

  IJavaPropertyDescriptor[] getPropertyDescriptors();


  IJavaClassConstructor[] getDeclaredConstructors();
  IJavaClassConstructor getConstructor( IJavaClassInfo... params ) throws NoSuchMethodException;


  IType getJavaType();
  void setJavaType(IJavaType javaType);

  IJavaClassTypeVariable[] getTypeParameters();

  IClassDocNode createClassDocNode();

  boolean hasCustomBeanInfo();

  boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);

  boolean isHiddenViaFeatureDescriptor();

  IJavaClassInfo getComponentType();

  int getModifiers();

  IType getEnclosingType();

  IJavaClassInfo getArrayType();

  IJavaClassInfo[] getDeclaredClasses();

  Class getBackingClass();

  ISourceFileHandle getSourceFileHandle();

  /**
   * Two IJavaClassInfo instances are equal if they are both non-arrays and have the same
   * name and module. If the two instances are arrays, then they are equal if their
   * component types are equal.
   *
   * @param o the other object
   * @return whether the two objects represent the same class.
   */
  boolean equals(Object o);

  /**
   * In order to maintain consistency with {@link #equals(Object)}, the hash code of a non-array
   * IJavaClassInfo must be computed as follows:
   * <p/>
   * <code>
   *   getName().hashCode() * 31 + getModule().hashCode()
   * </code>
   * <p/>
   * For arrays, the hashCode is equal to the hashCode of the component type.
   *
   * @return the hash code
   */
  int hashCode();

  static class Util {
    public static IJavaClassMethod[] getMethods( IJavaClassInfo classInfo ) {
      TreeSet<IJavaClassMethod> methods = new TreeSet<IJavaClassMethod>();
      TreeSet<IJavaClassMethod> publicMethods = getPublicMethods( classInfo, methods );
      return publicMethods.toArray( new IJavaClassMethod[publicMethods.size()] );
    }

    private static TreeSet<IJavaClassMethod> getPublicMethods( IJavaClassInfo classInfo, TreeSet<IJavaClassMethod> methods ) {
      for( IJavaClassMethod m : classInfo.getDeclaredMethods() ) {
        if( Modifier.isPublic( m.getModifiers() ) ) {
          methods.add( m );
        }
      }
      IJavaClassInfo superclass = classInfo.getSuperclass();
      if( superclass != null ) {
        getPublicMethods( superclass, methods );
      }
      for( IJavaClassInfo iface : classInfo.getInterfaces() ) {
        getPublicMethods( iface, methods );
      }
      return methods;
    }

    public static IJavaClassMethod get( Method method ) {
      IJavaClassInfo jci = TypeSystem.getJavaClassInfo( method.getDeclaringClass() );
      try {
        return jci.getDeclaredMethod( method.getName(), get( method.getParameterTypes() ) );
      }
      catch( NoSuchMethodException e ) {
        throw new RuntimeException( e );
      }
    }

    public static IJavaClassConstructor get( Constructor ctor ) {
      IJavaClassInfo jci = TypeSystem.getJavaClassInfo( ctor.getDeclaringClass() );
      try {
        return jci.getConstructor( get( ctor.getParameterTypes() ) );
      }
      catch( NoSuchMethodException e ) {
        throw new RuntimeException( e );
      }
    }

    private static IJavaClassInfo[] get( Class[] classes ) {
      IJavaClassInfo[] jcis = new IJavaClassInfo[classes.length];
      for( int i = 0; i < classes.length; i++ ) {
        jcis[i] = TypeSystem.getJavaClassInfo( classes[i] );
      }
      return jcis;
    }
  }
}