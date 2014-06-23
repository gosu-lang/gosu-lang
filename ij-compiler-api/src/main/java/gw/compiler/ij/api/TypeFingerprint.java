/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api;

import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.util.fingerprint.FP64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TypeFingerprint {

  private static int nullSafeCompare(String s1, String s2) {
    if (s1 == null) {
      return s2 == null ? 0 : 1;
    }
    if (s2 == null) {
      return -1;
    }
    return s1.compareTo(s2);
  }

  private static final Comparator<IType> TYPE_COMPARATOR = new Comparator<IType>() {
    @Override
    public int compare(IType type1, IType type2) {
      return nullSafeCompare(type1.getName(), type2.getName());
    }
  };

  private static final Comparator<IGosuClass> CLASS_COMPARATOR = new Comparator<IGosuClass>() {
    @Override
    public int compare(IGosuClass class1, IGosuClass class2) {
      return nullSafeCompare(class1.getName(), class2.getName());
    }
  };

  private static final Comparator<IMethodInfo> METHOD_COMPARATOR = new Comparator<IMethodInfo>() {
    public int compare(IMethodInfo method1, IMethodInfo method2) {
      int res = nullSafeCompare(method1.getName(), method2.getName());
      if (res != 0) {
        return res;
      }

      res = compareParameters(method1.getParameters(), method2.getParameters());
      if (res != 0) {
        return res;
      }

      res = nullSafeCompare(method1.getReturnType().getName(), method2.getReturnType().getName());
      return res;
    }
  };

  private static final Comparator<IJavaClassMethod> METHOD_INFO_COMPARATOR = new Comparator<IJavaClassMethod>() {
    public int compare(IJavaClassMethod method1, IJavaClassMethod method2) {
      int res = nullSafeCompare(method1.getName(), method2.getName());
      if (res != 0) {
        return res;
      }

      res = compareParameters(method1.getParameterTypes(), method2.getParameterTypes());
      if (res != 0) {
        return res;
      }

      res = nullSafeCompare(method1.getReturnType().getName(), method2.getReturnType().getName());
      return res;
    }
  };

  private static final Comparator<IConstructorInfo> CONSTRUCTOR_COMPARATOR = new Comparator<IConstructorInfo>() {
    public int compare(IConstructorInfo constructor1, IConstructorInfo constructor2) {
      int res = nullSafeCompare(constructor1.getName(), constructor2.getName());
      if (res != 0) {
        return res;
      }

      res = compareParameters(constructor1.getParameters(), constructor2.getParameters());
      return res;
    }
  };

  private static final Comparator<IJavaClassConstructor> CONSTRUCTOR_INFO_COMPARATOR = new Comparator<IJavaClassConstructor>() {
    public int compare(IJavaClassConstructor constructor1, IJavaClassConstructor constructor2) {
      return compareParameters(constructor1.getParameterTypes(), constructor2.getParameterTypes());
    }
  };

  private static final Comparator<IPropertyInfo> PROPERTY_COMPARATOR = new Comparator<IPropertyInfo>() {
    public int compare(IPropertyInfo property1, IPropertyInfo property2) {
      int res = nullSafeCompare(property1.getName(), property2.getName());
      if (res != 0) {
        return res;
      }

      res = nullSafeCompare(property1.getFeatureType().getName(), property2.getFeatureType().getName());
      return res;
    }
  };

  private static final Comparator<IJavaClassField> PROPERTY_INFO_COMPARATOR = new Comparator<IJavaClassField>() {
    public int compare(IJavaClassField property1, IJavaClassField property2) {
      int res = nullSafeCompare(property1.getName(), property2.getName());
      if (res != 0) {
        return res;
      }

      res = nullSafeCompare(property1.getType().getName(), property2.getType().getName());
      return res;
    }
  };

  private static int compareParameters(IParameterInfo[] params1, IParameterInfo[] params2) {
    int res = Integer.compare(params1.length, params2.length);
    if (res != 0) {
      return res;
    }

    for (int i = 0; i < params1.length; i++) {
      res = nullSafeCompare(params1[i].getName(), params2[i].getName());
      if (res != 0) {
        return res;
      }

      res = nullSafeCompare(params1[i].getFeatureType().getName(), params2[i].getFeatureType().getName());
      if (res != 0) {
        return res;
      }
    }
    return 0;
  }

  private static int compareParameters(IJavaClassInfo[] params1, IJavaClassInfo[] params2) {
    int res = Integer.compare(params1.length, params2.length);
    if (res != 0) {
      return res;
    }

    for (int i = 0; i < params1.length; i++) {
      res = nullSafeCompare(params1[i].getName(), params2[i].getName());
      if (res != 0) {
        return res;
      }
    }
    return 0;
  }

  public static FP64 get(IType type) {
    final FP64 fp = new FP64();
    extend(fp, type);
    return fp;
  }

  public static void extend(FP64 fp, IType type) {
    // the package
    final String namespace = type.getNamespace();
    if (namespace != null) {
      fp.extend(namespace); // we have to support java classes in the "default" package e.g., our own Launcher class
    }

    // the type name
    fp.extend(type.getName());

    // the class modifiers
    final int modifiers = Modifier.setPublic(type.getModifiers(), false);
    fp.extend(modifiers);

    // the superclass declaration, if any
    final IType superType = type.getSupertype();
    if (superType != null) {
      fp.extend(superType.getName());
    }

    // the list of interfaces
    List<IType> ifaces = Arrays.asList(type.getInterfaces());
    for (IType interfaceType : sort(ifaces, TYPE_COMPARATOR)) {
      fp.extend(interfaceType.getName());
    }

    // generic type variables
    final IGenericTypeVariable[] typeVariables = type.getGenericTypeVariables();
    if (typeVariables != null) {
      for (IGenericTypeVariable genericTypeVariable : typeVariables) {
        // the bounding type of the generic type variable
        fp.extend(genericTypeVariable.getBoundingType().getName());
      }
    }

    // the enhanced type for enhancements
    if (type instanceof IGosuEnhancement) {
      final IType enhancedType = ((IGosuEnhancement) type).getEnhancedType();
      if (enhancedType != null) {
        fp.extend(enhancedType.getName());
      }
    }

    // non-private methods
    final ITypeInfo typeInfo = type.getTypeInfo();
    final List<? extends IMethodInfo> methods = typeInfo instanceof IRelativeTypeInfo ?
            ((IRelativeTypeInfo) typeInfo).getMethods(type) :
            typeInfo.getMethods();

    for (IMethodInfo method : sort(methods, METHOD_COMPARATOR)) {
      if (!method.isPrivate()) {
        // the method name
        if (method.getName() != null) {
          fp.extend(method.getName());
        }

        // the return type
        fp.extend(method.getReturnType().getName());

        // parameter types
        for (IParameterInfo parameter : method.getParameters()) {
          // method parameter type
          fp.extend(parameter.getFeatureType().getName());
        }

        if (method instanceof IGenericMethodInfo) {
          for (IGenericTypeVariable typeVariable : ((IGenericMethodInfo) method).getTypeVariables()) {
            // the bounding type of the generic type variable
            fp.extend(typeVariable.getBoundingType().getName());
          }
        }

        // modifiers
        fp.extend(Boolean.toString(method.isPublic()));
        fp.extend(Boolean.toString(method.isPrivate()));
        fp.extend(Boolean.toString(method.isProtected()));
        fp.extend(Boolean.toString(method.isInternal()));
        fp.extend(Boolean.toString(method.isFinal()));
        fp.extend(Boolean.toString(method.isStatic()));
        fp.extend(Boolean.toString(method.isHidden()));
      }
    }

    // non-private constructors
    final List<? extends IConstructorInfo> constructors = typeInfo instanceof IRelativeTypeInfo ?
            ((IRelativeTypeInfo) typeInfo).getConstructors(type) :
            typeInfo.getConstructors();
    for (IConstructorInfo constructor : sort(constructors, CONSTRUCTOR_COMPARATOR)) {
      if (!constructor.isPrivate()) {
        for (IParameterInfo parameter : constructor.getParameters()) {
          // constructor parameter type
          fp.extend(parameter.getFeatureType().getName());
        }

        // modifiers
        fp.extend(Boolean.toString(constructor.isPublic()));
        fp.extend(Boolean.toString(constructor.isPrivate()));
        fp.extend(Boolean.toString(constructor.isProtected()));
        fp.extend(Boolean.toString(constructor.isInternal()));
        fp.extend(Boolean.toString(constructor.isFinal()));
        fp.extend(Boolean.toString(constructor.isStatic()));
//        fp.extend(Boolean.toString(constructor.isHidden()));
      }
    }

    // non-private properties
    final List<? extends IPropertyInfo> properties = typeInfo instanceof IRelativeTypeInfo ?
            ((IRelativeTypeInfo) typeInfo).getProperties(type) :
            typeInfo.getProperties();
    for (IPropertyInfo property : sort(properties, PROPERTY_COMPARATOR)) {
      if (!property.isPrivate()) {
        // the property name
        if (property.getName() != null) {
          fp.extend(property.getName());
        }

        // the property type
        IType featureType = null;
        try {
          featureType = property.getFeatureType();
        }
        catch (Throwable t) {
          //Ignore
        }
        if (featureType != null) {
          fp.extend(featureType.getName());
        }

        // readability
        fp.extend(Boolean.toString(property.isReadable()));
        fp.extend(Boolean.toString(property.isWritable()));

        // modifiers
        fp.extend(Boolean.toString(property.isPublic()));
        fp.extend(Boolean.toString(property.isPrivate()));
        fp.extend(Boolean.toString(property.isProtected()));
        fp.extend(Boolean.toString(property.isInternal()));
        fp.extend(Boolean.toString(property.isFinal()));
        fp.extend(Boolean.toString(property.isStatic()));
        fp.extend(Boolean.toString(property.isHidden()));
      }
    }

    // non-private inner classes
    if (type instanceof IGosuClass) {
      final List<? extends IGosuClass> innerClasses = ((IGosuClass) type).getInnerClasses();
      for (IGosuClass innerClass : sort(innerClasses, CLASS_COMPARATOR)) {
        if (!Modifier.isPrivate(innerClass.getModifiers()) && !innerClass.isAnonymous()) {
          fp.extend(TypeFingerprint.get(innerClass).getRawFingerprint());
        }
      }
    }
  }

  public static void extend(FP64 fp, IJavaClassInfo type) {
    // the package
    final String namespace = type.getNamespace();
    if (namespace != null) {
      fp.extend(namespace); // we have to support java classes in the "default" package e.g., our own Launcher class
    }

    // the type name
    fp.extend(type.getName());

    // the class modifiers
    final int modifiers = Modifier.setPublic(type.getModifiers(), false);
    fp.extend(modifiers);

    // the superclass declaration, if any
    final IJavaClassInfo superType = type.getSuperclass();
    if (superType != null) {
      fp.extend(superType.getName());
    }

    // the list of interfaces
    List<IType> types = new ArrayList<IType>(type.getInterfaces().length);
    for (IType iType : types) {
      types.add(iType);
    }
    for (IType interfaceType : sort(types, TYPE_COMPARATOR)) {
      fp.extend(interfaceType.getName());
    }

    // generic type variables
    final IJavaClassTypeVariable[] typeVariables = type.getTypeParameters();
    if (typeVariables != null) {
      for (IJavaClassTypeVariable genericTypeVariable : typeVariables) {
        // the bounding type of the generic type variable
        IJavaClassType[] bounds = genericTypeVariable.getBounds();
        for (IJavaClassType bound : bounds) {
          fp.extend(bound.getName());
        }
      }
    }

    // the enhanced type for enhancements
    if (type instanceof IGosuEnhancement) {
      final IType enhancedType = ((IGosuEnhancement) type).getEnhancedType();
      if (enhancedType != null) {
        fp.extend(enhancedType.getName());
      }
    }

    // non-private methods
    final List<IJavaClassMethod> methods = Arrays.asList(type.getDeclaredMethods());

    for (IJavaClassMethod method : sort(methods, METHOD_INFO_COMPARATOR)) {
      if (!Modifier.isPrivate(method.getModifiers())) {
        // the method name
        fp.extend(method.getName());

        // the return type
        fp.extend(method.getReturnType().getName());

        // parameter types
        for (IJavaClassInfo parameterType : method.getParameterTypes()) {
          // method parameter type
          fp.extend(parameterType.getName());
        }

        if (method instanceof IGenericMethodInfo) {
          for (IGenericTypeVariable typeVariable : ((IGenericMethodInfo) method).getTypeVariables()) {
            // the bounding type of the generic type variable
            fp.extend(typeVariable.getBoundingType().getName());
          }
        }

        // modifiers
        fp.extend(method.getModifiers());
      }
    }

    // non-private constructors
    final List<IJavaClassConstructor> constructors = Arrays.asList(type.getDeclaredConstructors());
    for (IJavaClassConstructor constructor : sort(constructors, CONSTRUCTOR_INFO_COMPARATOR)) {
      if (!Modifier.isPrivate(constructor.getModifiers())) {
        for (IJavaClassInfo parameterType : constructor.getParameterTypes()) {
          // constructor parameter type
          fp.extend(parameterType.getName());
        }

        // modifiers
        fp.extend(constructor.getModifiers());
      }
    }

    // non-private properties
    final List<IJavaClassField> properties = Arrays.asList(type.getDeclaredFields());
    for (IJavaClassField property : sort(properties, PROPERTY_INFO_COMPARATOR)) {
      if (!Modifier.isPrivate(property.getModifiers())) {
        // the property name
        fp.extend(property.getName());

        // the property type
        fp.extend(property.getType().getName());

        // modifiers
        fp.extend(property.getModifiers());
      }
    }

    // non-private inner classes
    if (type instanceof IGosuClass) {
      final List<? extends IGosuClass> innerClasses = ((IGosuClass) type).getInnerClasses();
      for (IGosuClass innerClass : sort(innerClasses, CLASS_COMPARATOR)) {
        if (!Modifier.isPrivate(innerClass.getModifiers()) && !innerClass.isAnonymous()) {
          fp.extend(TypeFingerprint.get(innerClass).getRawFingerprint());
        }
      }
    }
  }

  private static <T> List<T> sort(List<T> src, Comparator<? super T> comparator) {
    List<T> dst = new ArrayList(src);
    Collections.sort(dst, comparator);
    return dst;
  }
}
