package gw.internal.gosu.parser;

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class JavaTypeExtensions {
  private JavaTypeExtensions() {
  }

  public static IJavaPropertyInfo maybeExtendProperty(JavaPropertyInfo javaProperty) {
    IJavaPropertyInfo result = javaProperty;
    IJavaClassMethod readMethod = javaProperty.getPropertyDescriptor().getReadMethod();
    if (readMethod != null) {
      IAnnotationInfo extendedPropertyAnnotation = readMethod.getAnnotation(ExtendedProperty.class);
      if (extendedPropertyAnnotation != null) {
        ExtendedTypeDataFactory factory = getExtendedTypeDataFactory((IJavaType) javaProperty.getOwnersType());
        if (factory != null) {
          ExtendedTypeData extendedTypeInfoData = factory.newPropertyData((IJavaType) javaProperty.getOwnersType(), javaProperty.getName());
          result = (IJavaPropertyInfo) Proxy.newProxyInstance(
                  extendedTypeInfoData.getExtensionInterface().getClassLoader(),
                  new Class[] {IJavaPropertyInfo.class, extendedTypeInfoData.getExtensionInterface()},
                  new ExtendedPropertyInvocationHandler(javaProperty, extendedTypeInfoData.getData())
          );
        }
      }
    }
    return result;
  }

  public static IJavaTypeInternal maybeExtendType(JavaType javaType) {
    IJavaTypeInternal result = javaType;
    ExtendedTypeDataFactory factory = getExtendedTypeDataFactory(javaType);
    if (factory != null) {
      ExtendedTypeData extendedTypeInfoData = factory.newTypeData(javaType.getName());
      result = (IJavaTypeInternal) Proxy.newProxyInstance(
              extendedTypeInfoData.getExtensionInterface().getClassLoader(),
              new Class[] {IJavaTypeInternal.class, extendedTypeInfoData.getExtensionInterface()},
              new ExtendedTypeInvocationHandler(javaType, extendedTypeInfoData.getData())
      );
    }
    return result;
  }

  private static ExtendedTypeDataFactory getExtendedTypeDataFactory(IJavaType javaType) {
    boolean extendedType;
    if (ExecutionMode.isRuntime()) {
      Class<?> backingClass = javaType.getBackingClass();
      // Server runtime case. We can't go through the IJavaClassInfo for this case, because it leads to a
      // circularity w.r.t. the JavaType (ClassAnnotationInfo attempts to get the JavaType)
      extendedType = backingClass.isAnnotationPresent(ExtendedType.class);
    } else {
      // Studio case
      extendedType = javaType.getBackingClassInfo().getAnnotation(ExtendedType.class) != null;
    }
    return extendedType ? CommonServices.getEntityAccess().getExtendedTypeDataFactory(javaType.getName()) : null;
  }

  private static abstract class CompositeObjectInvocationHandler<T> implements InvocationHandler {

    private final Class<T> _primaryInterface;
    private final T _primaryObject;
    private final Object _secondaryObject;

    private CompositeObjectInvocationHandler(Class<T> primaryInterface, T primaryObject, Object secondaryObject) {
      _primaryInterface = primaryInterface;
      _primaryObject = primaryObject;
      _secondaryObject = secondaryObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Object result;
      if (isEqualsMethod(method)) {
        result = handleEquals(maybeUnwrapArgument(args[0]));
      } else if (isHashCodeMethod(method)) {
        result = handleHashCode();
      } else {
        Object invocationTarget;
        if (method.getDeclaringClass().isAssignableFrom(_primaryInterface) || Object.class.equals(method.getDeclaringClass())) {
          invocationTarget = _primaryObject;
        } else {
          invocationTarget = _secondaryObject;
        }
        try {
          result = method.invoke(invocationTarget, args);
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      }
      return result;
    }

    private Object maybeUnwrapArgument(Object arg) {
      if (arg != null && Proxy.isProxyClass(arg.getClass())) {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(arg);
        if (invocationHandler instanceof CompositeObjectInvocationHandler) {
          arg = ((CompositeObjectInvocationHandler) invocationHandler)._primaryObject;
        }
      }
      return arg;
    }

    private boolean isEqualsMethod(Method method) {
      return Object.class.equals(method.getDeclaringClass()) && "equals".equals(method.getName());
    }

    private boolean isHashCodeMethod(Method method) {
      return Object.class.equals(method.getDeclaringClass()) && "hashCode".equals(method.getName());
    }

    abstract boolean handleEquals(Object o);
    abstract int handleHashCode();

    protected final T getPrimaryObject() {
      return _primaryObject;
    }
  }

  private static class ExtendedTypeInvocationHandler extends CompositeObjectInvocationHandler<IJavaTypeInternal> {

    private ExtendedTypeInvocationHandler(IJavaTypeInternal primaryObject, Object secondaryObject) {
      super(IJavaTypeInternal.class, primaryObject, secondaryObject);
    }

    @Override
    boolean handleEquals(Object o) {
      return getPrimaryObject().equals(o);
    }

    @Override
    int handleHashCode() {
      return getPrimaryObject().hashCode();
    }
  }

  private static class ExtendedPropertyInvocationHandler extends CompositeObjectInvocationHandler<IJavaPropertyInfo> {
    private ExtendedPropertyInvocationHandler(IJavaPropertyInfo primaryObject, Object secondaryObject) {
      super(IJavaPropertyInfo.class, primaryObject, secondaryObject);
    }

    @Override
    boolean handleEquals(Object o) {
      IJavaPropertyInfo thisObject = getPrimaryObject();
      if (thisObject == o) {
        return true;
      }
      if (!(o instanceof IJavaPropertyInfo)) {
        return false;
      }
      IJavaPropertyInfo thatObject = (IJavaPropertyInfo) o;
      return thisObject.getOwnersType().equals(thatObject.getOwnersType()) &&
              thisObject.getName().equals(thatObject.getName());
    }

    @Override
    int handleHashCode() {
      IJavaPropertyInfo property = getPrimaryObject();
      return 31 * property.getOwnersType().hashCode() + property.getName().hashCode();
    }
  }
}
