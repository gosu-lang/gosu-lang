/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotation;

import gw.lang.reflect.TypeSystem;
import gw.util.SpaceEfficientHashMap;

import java.lang.annotation.Annotation;
import gw.util.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;

public class Annotations {

  private Annotations() {
  }

  /**
   * Returns a builder that can be used to construct an annotation instance.
   */
  public static <T extends Annotation> Builder<T> builder(Class<T> annotationType) {
    return new Builder<T>(annotationType);
  }

  /**
   * Convenience method that constructs an annotation instance with a single "value" element.
   */
  public static <T extends Annotation> T create(Class<T> annotationType, Object value) {
    return new Builder<T>(annotationType).withValue(value).create();
  }

  /**
   * Convenience method that constructs an annotation instance with no elements.
   */
  public static <T extends Annotation> T create(Class<T> annotationType) {
    return new Builder<T>(annotationType).create();
  }

  public static class Builder<T extends Annotation> {
    private final Class<T> _annotationType;
    private final Map<Method, Element> _configuredElements;

    private Builder(Class<T> annotationType) {
      _annotationType = annotationType;
      _configuredElements = new SpaceEfficientHashMap<Method, Element>();
    }

    public Builder<T> withElement(String name, Object value) {
      Method elementMethod;
      try {
        elementMethod = _annotationType.getMethod(name);
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException(
                "No such element \"" + name + "\" defined on annotation type " + _annotationType.getName());
      }
      if (elementMethod.getReturnType().isPrimitive()) {
        if (!TypeSystem.isBoxedTypeFor(TypeSystem.get(elementMethod.getReturnType()), TypeSystem.getFromObject(value))) {
          throw new IllegalArgumentException(
                  "Element \"" + name + "\" on annotation type " + _annotationType.getName() + " must be of type " +
                          elementMethod.getReturnType() + ". Got " + value +
                          (value == null ? "" : " (type " + value.getClass().getName() + ")"));
        }
      } else if ( elementMethod.getReturnType().isArray() &&
                  elementMethod.getReturnType().getComponentType().isInstance( value ) ) {
        //TODO-dp review this with Scott
//        IType fromObject = TypeSystem.getFromObject( value );
//        Object arr = fromObject.makeArrayInstance( 1 );
//        fromObject.setArrayComponent( arr, 0, value );

        Object arr = Array.newInstance(value.getClass(), 1);
        Array.set(arr, 0, value);
        value = arr;
      } else if (value == null && elementMethod.getDefaultValue() != null) {
        value = elementMethod.getDefaultValue();
      } else if (!elementMethod.getReturnType().isInstance(value)) {
        throw new IllegalArgumentException(
                "Element \"" + name + "\" on annotation type " + _annotationType.getName() + " must be of type " +
                        elementMethod.getReturnType() + ". Got " + value +
                        (value == null ? "" : " (type " + value.getClass().getName() + ")"));
      } else if (value instanceof Object[] && hasNullElements((Object[]) value)) {
        throw new IllegalArgumentException(
                "Object arrays can't have null elements. Got " + Arrays.toString((Object[]) value) + " for element \"" +
                        name + "\"");
      }
      _configuredElements.put(elementMethod, new Element(value));
      return this;
    }

    public Builder<T> withValue(Object value) {
      return withElement("value", value);
    }

    private static class Element {
      private final Object _value;
      private final int _arrayLength;
      private final Class<?> _componentType;

      Element(Object value) {
        assert value != null;
        Class<?> valueClass = value.getClass();
        _arrayLength = valueClass.isArray() ? Array.getLength(value) : -1;
        _componentType = valueClass.getComponentType();
        _value = copyValueIfNecessary(value, _arrayLength, _componentType);
      }

      /**
       * Protects a value from outside modification. Incoming and outgoing values must be copied if necessary in
       * order to preserve the contract of annotations.
       */
      Object getValue() {
        return copyValueIfNecessary(_value, _arrayLength, _componentType);
      }

      boolean valueEquals(Object otherValue) {
        boolean result;
        if (isArray()) {
          if (otherValue.getClass().isArray()) {
            if (_arrayLength == Array.getLength(otherValue)) {
              boolean allElementsEqual = true;
              for (int i = 0; i < _arrayLength; i++) {
                if (!Array.get(_value, i).equals(Array.get(otherValue, i))) {
                  allElementsEqual = false;
                  break;
                }
              }
              result = allElementsEqual;
            } else {
              result = false;
            }
          } else {
            result = false;
          }
        } else {
          result = _value.equals(otherValue);
        }
        return result;
      }

      int valueHashCode() {
        int hash;
        if (isArray()) {
          int arrayHash = 1;
          for (int i = 0; i < _arrayLength; i++) {
            Object arrayElement = Array.get(_value, i);
            arrayHash = 31 * arrayHash + arrayElement.hashCode();
          }
          hash = arrayHash;
        } else {
          hash = _value.hashCode();
        }
        return hash;
      }

      void valueToString(StringBuilder buf) {
        if (isArray()) {
          buf.append('[');
          for (int i = 0; i < _arrayLength; i++) {
            if (i > 0) {
              buf.append(", ");
            }
            buf.append(Array.get(_value, i));
          }
          buf.append(']');
        } else {
          buf.append(_value);
        }
      }

      private boolean isArray() {
        return _componentType != null;
      }

      private static Object copyValueIfNecessary(Object value, int arrayLength, Class<?> componentType) {
        Object result;
        if (arrayLength > 0) {
          result = Array.newInstance(componentType, arrayLength);
          //noinspection SuspiciousSystemArraycopy
          System.arraycopy(value, 0, result, 0, arrayLength);
        } else {
          // 0-length arrays are immutable, and
          // all other types allowed in annotation elements are immutable
          result = value;
        }
        return result;
      }
    }

    public T create() {
      final Map<Method, Element> elements = buildElements();
      ClassLoader cl = determineClassLoader();
      return _annotationType.cast(Proxy.newProxyInstance( cl,
                                                         new Class<?>[] { _annotationType },
                                                         new AnnotationInvocationHandler( elements, _annotationType ) ));
    }

    private ClassLoader determineClassLoader()
    {
      // First see if the context class loader can see the annotation and
      // use it if so (needed for some hibernate insanity)
      if( canLoadAnnotation( Thread.currentThread().getContextClassLoader() ) )
      {
        return Thread.currentThread().getContextClassLoader();
      }
      else
      {
        // Otherwise, use the annotation types class loader, which actually makes sense
        return _annotationType.getClassLoader();
      }
    }

    private boolean canLoadAnnotation( ClassLoader cl )
    {
      Class loadedVersion = null;
      if(cl != null)
      {
        try
        {
          loadedVersion = Class.forName(_annotationType.getName(), false, cl);
        }
        catch( ClassNotFoundException e )
        {
          //ignore
        }
      }
      return loadedVersion == _annotationType;
    }

    private boolean hasNullElements(Object[] array) {
      for (Object o : array) {
        if (o == null) {
          return true;
        }
      }
      return false;
    }

    private Map<Method, Element> buildElements() {
      Map<Method, Element> elements = new SpaceEfficientHashMap<Method, Element>();
      if(_annotationType != null) {
        for (Method elementMethod : TypeSystem.getDeclaredMethods( _annotationType )) {
          Element value = _configuredElements.get(elementMethod);
          if (value == null) {
            Object defaultValue = elementMethod.getDefaultValue();
            if (defaultValue == null) {
              throw new IllegalStateException(
                  "Can't create annotation of type " + _annotationType.getName() + ": missing value for element \"" +
                  elementMethod.getName() + "\"");
            }
            value = new Element(defaultValue);
          }
          elements.put(elementMethod, value);
        }
      }
      return elements;
    }

    public static class AnnotationInvocationHandler implements InvocationHandler
    {
      private final Map<Method, Element> _elements;
      private final Class _annotationType;

      public AnnotationInvocationHandler( Map<Method, Element> elements, Class annotationType )
      {
        _elements = elements;
        _annotationType = annotationType;
      }

      public Class getAnnotationType() {
        return _annotationType;
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        if (Object.class.equals(method.getDeclaringClass())) {
          if ("equals".equals(method.getName())) {
            result = equalsImpl(args[0]);
          } else if ("hashCode".equals(method.getName())) {
            result = hashCodeImpl();
          } else {
            boolean b = "toString".equals(method.getName());
            assert b;
            result = toStringImpl();
          }
        } else if ( Annotation.class.equals(method.getDeclaringClass())) {
          boolean b = "annotationType".equals(method.getName());
          assert b;
          result = _annotationType;
        } else {
          boolean b = _annotationType.equals(method.getDeclaringClass());
          assert b;
          result = _elements.get(method).getValue();
        }
        return result;
      }

      /**
         * @see java.lang.annotation.Annotation#toString()
       */
      private String toStringImpl() {
        StringBuilder buf = new StringBuilder();
        buf.append('@').append(_annotationType.getName());
        // print elements in deterministic order
        Method[] elementMethods = TypeSystem.getDeclaredMethods( _annotationType );
        if (elementMethods.length > 0) {
          buf.append('(');
          for (int i = 0; i < elementMethods.length; i++) {
            if (i > 0) {
              buf.append(", ");
            }
            Method elementMethod = elementMethods[i];
            buf.append(elementMethod.getName()).append('=');
            _elements.get(elementMethod).valueToString(buf);
          }
          buf.append(')');
        }
        return buf.toString();
      }

      /**
         * @see java.lang.annotation.Annotation#hashCode()
       */
      private int hashCodeImpl() {
        int hash = 0;
        for ( Map.Entry<Method, Element> entry : _elements.entrySet()) {
          hash += hashElement(entry.getKey(), entry.getValue());
        }
        return hash;
      }

      private int hashElement(Method elementMethod, Element element) {
        return (127 * elementMethod.getName().hashCode()) ^ element.valueHashCode();
      }

      /**
         * @see java.lang.annotation.Annotation#equals(Object)
       */
      private boolean equalsImpl(Object obj) throws Throwable {
        if (!_annotationType.isInstance(obj)) {
          return false;
        }
        for (Map.Entry<Method, Element> entry : _elements.entrySet()) {
          Object otherValue = getElementValue(obj, entry.getKey());
          if (!entry.getValue().valueEquals(otherValue)) {
            return false;
          }
        }
        return true;
      }

      private Object getElementValue(Object annotation, Method elementMethod) throws Throwable {
        try {
          return elementMethod.invoke(annotation);
        } catch ( InvocationTargetException e) {
          throw e.getTargetException();
        }
      }
    }
  }
}
