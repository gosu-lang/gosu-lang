/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.IType;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public interface ObjectWrapper {

  public Object getWrappedObj();
  public void setWrappedObj(Object wrappedObj);

  public static final ObjectWrapperHelper HELPER = new ObjectWrapperHelper();

  static class ObjectWrapperHelper {
    public ObjectWrapper createWrapper( final IType type, final Object obj, List<Class> interfaces) {

      final Object[] value = new Object[1];
      value[0] = obj;
      ArrayList<Class> ifaces = new ArrayList<Class>(interfaces);
      ifaces.add(ObjectWrapper.class);
      ifaces.add(IGosuObject.class);
      return (ObjectWrapper) Proxy.newProxyInstance(ObjectWrapper.class.getClassLoader(), ifaces.toArray(new Class<?>[ifaces.size()]),
              new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                  if (method.equals(IGosuObject.class.getDeclaredMethod("getIntrinsicType"))) {
                    return type;
                  } else if (method.equals(Object.class.getDeclaredMethod("equals", Object.class))) {
                    return proxy == args[0];
                  } else if (method.equals(ObjectWrapper.class.getDeclaredMethod("getWrappedObj"))) {
                    return value[0];
                  } else if (method.equals(ObjectWrapper.class.getDeclaredMethod("setWrappedObj", Object.class))) {
                    return value[0] = args[0];
                  } else {
                    return method.invoke(value[0], args);
                  }
                }
              });
    }

  }
}
