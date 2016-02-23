/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.BeanInfoUtil;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.module.IModule;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class PropertyDescriptorJavaPropertyDescriptor implements IJavaPropertyDescriptor {
  private PropertyDescriptor _pd;
  private IModule _module;

  public PropertyDescriptorJavaPropertyDescriptor(PropertyDescriptor propertyDescriptor, IModule module) {
    _pd = propertyDescriptor;
    _module = module;
  }

  @Override
  public String getName() {
    return _pd.getName();
  }

  @Override
  public IJavaClassMethod getReadMethod() {
    Method method = getReadMethodSafe();
    return method == null ? null : new MethodJavaClassMethod(method, _module);
  }

  private Method getReadMethodSafe() {
    try {
      return _pd.getReadMethod();
    } catch (NullPointerException originalException) {
      if (shouldFixJREMethodRefIssue()) {
        try {
          // Due to a bug in PropertyDescriptor in later versions of java 1.7, it is possible to get an NPE when
          // retrieving a private write method for a property.  This addresses that situation
          reestablishMethodRef("read");
          return _pd.getReadMethod();
        } catch (Exception e) {
          e.printStackTrace();  // print stack trace, let original exception get thrown
        }
      }
      throw originalException;
    }
  }

  @Override
  public IJavaClassMethod getWriteMethod() {
    Method method = getWriteMethodSafe();
    return method == null ? null : new MethodJavaClassMethod(method, _module);
  }

  private Method getWriteMethodSafe() {
    try {
      return _pd.getWriteMethod();
    } catch (NullPointerException originalException) {
      if (shouldFixJREMethodRefIssue()) {
        try {
          // Due to a bug in PropertyDescriptor in later versions of java 1.7, it is possible to get an NPE when
          // retrieving a private write method for a property.  This addresses that situation
          reestablishMethodRef("write");
          return _pd.getWriteMethod();
        } catch (Exception e) {
          e.printStackTrace();  // print stack trace, let original exception get thrown
        }
      }
      throw originalException;
    }
  }

  private void reestablishMethodRef(String slot) {
    Class clazz = (Class) ReflectUtil.invokeMethod(_pd, "getClass0");
    String signature = slot.equals("write") ? getSetterMethod(clazz, _pd) : getGetterMethod(clazz);
    Method resolvedMethod = findMethod0(clazz, signature);
    Object methodRef = ReflectUtil.getProperty(_pd, slot + "MethodRef");
    ReflectUtil.invokeMethod(methodRef, "set", resolvedMethod);
  }

  private String getGetterMethod(Class rootClass) {
    return rootClass.getName() + ".get" + _pd.getName() + "()";
  }

  private String getSetterMethod(Class rootClass, PropertyDescriptor propertyDescriptor) {
    return rootClass.getName() + ".set" + _pd.getName() + "(" + propertyDescriptor.getPropertyType().getName() + ")";
  }

  static Method findMethod0(Class<?> type, String signature) {
    if (type != null) {
      for (Method method : type.getDeclaredMethods()) {
        if (method.toGenericString().endsWith( " " + signature )) {
          return method;
        }
      }
      return findMethod0(type.getSuperclass(), signature);
    }
    return null;
  }

  @Override
  public IType getPropertyType() {
    return TypeSystem.get(_pd.getPropertyType());
  }

  @Override
  public IJavaClassInfo getPropertyClassInfo() {
    return JavaSourceUtil.getClassInfo(_pd.getPropertyType(), _module);
  }

  @Override
  public boolean isHidden() {
    return _pd.isHidden();
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return BeanInfoUtil.isVisible(_pd, constraint);
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return _pd.isHidden();
  }

  @Override
  public boolean isDeprecated() {
    return _pd.getReadMethod().isAnnotationPresent( gw.lang.Deprecated.class ) ||
              BeanInfoUtil.isDeprecated( _pd ) ||
              (_pd.getReadMethod() != null && _pd.getReadMethod().isAnnotationPresent( java.lang.Deprecated.class ));
  }

  @Override
  public String getDisplayName() {
    return _pd.getDisplayName();
  }

  @Override
  public String getShortDescription() {
    return _pd.getShortDescription();
  }

  static boolean shouldFixJREMethodRefIssue() {
    String vendor = System.getProperty("java.vendor");
    if(vendor != null) {
      return vendor.toLowerCase().contains("sun") || vendor.toLowerCase().contains("oracle");
    } else {
      return false;
    }
  }
}