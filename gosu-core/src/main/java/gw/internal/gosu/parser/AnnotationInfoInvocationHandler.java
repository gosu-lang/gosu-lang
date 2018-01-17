package gw.internal.gosu.parser;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IType;

import java.lang.annotation.Annotation;
import gw.util.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
*/
class AnnotationInfoInvocationHandler implements InvocationHandler {
  private IAnnotationInfo _ai;

  public AnnotationInfoInvocationHandler( IAnnotationInfo ai ) {
    _ai = ai;
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
    if( method.getName().equals( "equals" ) && method.getParameterTypes().length > 0 ) {
      return equals( (Annotation)proxy, (Annotation)args[0] );
    }
    if( method.getName().equals( "hashCode" ) ) {
      return hashCode( (Annotation)proxy );
    }
    if( method.getName().equals( "toString" ) ) {
      return toString( (Annotation)proxy );
    }
    if( method.getName().equals( "annotationType" ) ) {
      return ((IHasJavaClass)_ai.getType()).getBackingClass();
    }
    Object value = _ai.getFieldValue( method.getName() );
    return maybeCoerceValue( value, method.getReturnType() );
  }

  private Object maybeCoerceValue( Object value, Class type ) {
    if( value != null && !type.isPrimitive() && !type.isInstance( value ) ) {
      if( Enum.class.isAssignableFrom( type ) ) {
        value = Enum.valueOf( type, (String)value );
      }
      else if( Class.class.isAssignableFrom( type ) ) {
        if( value.getClass().getSimpleName().startsWith( "SOAPEntityType" ) ) {
          //## hack:  delete this branch of the enclosing if-stmt after SOAP local types are gone
          try {
            value = Class.forName( "gw.pl.util.webservices.login.fault." + ((IType) value).getRelativeName() );
          }
          catch (ClassNotFoundException e) {
            throw new RuntimeException( e );
          }
        }
        else {
          value = ((IHasJavaClass)value).getBackingClass();
        }
      }
      else if( Annotation.class.isAssignableFrom( type ) ) {
        value = ((IAnnotationInfo)value).getInstance();
      }
      if( type.isArray() ) {
        if( !value.getClass().isArray() ) {
          // Handle single value as one elem array
          Object array = Array.newInstance( type.getComponentType(), 1 );
          Array.set( array, 0, maybeCoerceValue( value, type.getComponentType() ) );
          return array;
        }
        int length = Array.getLength( value );
        Object array = Array.newInstance( type.getComponentType(), length );
        for( int i = 0; i < length; i++ ) {
          Array.set( array, i, maybeCoerceValue( Array.get( value, i ), type.getComponentType() ) );
        }
        value = array;
      }
    }
    return value;
  }
  
  public boolean equals( Annotation a1, Annotation a2 ) {
    if( a1 == a2 ) {
      return true;
    }
    if( a1 == null || a2 == null ) {
      return false;
    }
    Class anno1 = a1.annotationType();
    Class anno2 = a2.annotationType();
    if( !anno1.equals( anno2 ) ) {
      return false;
    }
    try {
      for( Method m : anno1.getDeclaredMethods() ) {
        if( m.getParameterTypes().length == 0 && isValidAnnotationFieldType( m.getReturnType() ) ) {
          Object v1 = m.invoke( a1 );
          Object v2 = m.invoke( a2 );
          if( !fieldEquals( m.getReturnType(), v1, v2 ) ) {
            return false;
          }
        }
      }
    }
    catch( Exception e ) {
      return false;
    }
    return true;
  }

  private int hashCode( Annotation a ) {
    int iHash = 0;
    Class cls = a.annotationType();
    for( Method m : cls.getDeclaredMethods() ) {
      try {
        Object value = m.invoke( a );
        iHash += hashField( m.getName(), value );
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }
    return iHash;
  }

  private String toString( Annotation a ) {
    StringBuilder res = new StringBuilder();
    for( Method m : a.annotationType().getDeclaredMethods() ) {
      if( m.getParameterTypes().length > 0 ) {
        continue;
      }
      try {
        res.append( m.getName() ).append( m.invoke( a ) );
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }
    return res.toString();
  }

  private boolean isValidAnnotationFieldType( Class<?> type ) {
    if( type == null ) {
      return false;
    }
    if( type.isArray() ) {
      type = type.getComponentType();
    }
    return type.isPrimitive() || String.class.equals( type ) || type.isEnum() || Class.class.equals( type ) || type.isAnnotation();
  }

  private int hashField( String name, Object value ) {
    int iHash = name.hashCode() * 127;
    if( value.getClass().isArray() ) {
      return iHash ^ arrayFieldHash( value.getClass().getComponentType(), value );
    }
    if( value instanceof Annotation ) {
      return iHash ^ hashCode( (Annotation)value );
    }
    return iHash ^ value.hashCode();
  }

  private boolean fieldEquals( Class<?> type, Object o1, Object o2 ) {
    if( o1 == o2 ) {
      return true;
    }
    if( o1 == null || o2 == null ) {
      return false;
    }
    if( type.isArray() ) {
      return arrayFieldEquals( type.getComponentType(), o1, o2 );
    }
    if( type.isAnnotation() ) {
      return equals( (Annotation)o1, (Annotation)o2 );
    }
    return o1.equals( o2 );
  }

  private boolean arrayFieldEquals( Class<?> componentType, Object o1, Object o2 ) {
    if( componentType.isAnnotation() ) {
      return annotationArrayFieldEquals( (Annotation[])o1, (Annotation[])o2 );
    }
    if( componentType.equals( Byte.TYPE ) ) {
      return Arrays.equals( (byte[])o1, (byte[])o2 );
    }
    if( componentType.equals( Boolean.TYPE ) ) {
      return Arrays.equals( (boolean[])o1, (boolean[])o2 );
    }
    if( componentType.equals( Character.TYPE ) ) {
      return Arrays.equals( (char[])o1, (char[])o2 );
    }
    if( componentType.equals( Short.TYPE ) ) {
      return Arrays.equals( (short[])o1, (short[])o2 );
    }
    if( componentType.equals( Integer.TYPE ) ) {
      return Arrays.equals( (int[])o1, (int[])o2 );
    }
    if( componentType.equals( Long.TYPE ) ) {
      return Arrays.equals( (long[])o1, (long[])o2 );
    }
    if( componentType.equals( Float.TYPE ) ) {
      return Arrays.equals( (float[])o1, (float[])o2 );
    }
    if( componentType.equals( Double.TYPE ) ) {
      return Arrays.equals( (double[])o1, (double[])o2 );
    }

    return Arrays.equals( (Object[])o1, (Object[])o2 );
  }

  private boolean annotationArrayFieldEquals( Annotation[] a1, Annotation[] a2 ) {
    if( a1.length != a2.length ) {
      return false;
    }
    for( int i = 0; i < a1.length; i++ ) {
      if( !equals( a1[i], a2[i] ) ) {
        return false;
      }
    }
    return true;
  }

  private int arrayFieldHash( Class<?> componentType, Object o ) {
    if( componentType.equals( Byte.TYPE ) ) {
      return Arrays.hashCode( (byte[])o );
    }
    if( componentType.equals( Boolean.TYPE ) ) {
      return Arrays.hashCode( (boolean[])o );
    }
    if( componentType.equals( Character.TYPE ) ) {
      return Arrays.hashCode( (char[])o );
    }
    if( componentType.equals( Short.TYPE ) ) {
      return Arrays.hashCode( (short[])o );
    }
    if( componentType.equals( Integer.TYPE ) ) {
      return Arrays.hashCode( (int[])o );
    }
    if( componentType.equals( Long.TYPE ) ) {
      return Arrays.hashCode( (long[])o );
    }
    if( componentType.equals( Float.TYPE ) ) {
      return Arrays.hashCode( (float[])o );
    }
    if( componentType.equals( Double.TYPE ) ) {
      return Arrays.hashCode( (double[])o );
    }
    return Arrays.hashCode( (Object[])o );
  }  
}
