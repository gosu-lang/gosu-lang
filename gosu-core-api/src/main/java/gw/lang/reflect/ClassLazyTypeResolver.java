package gw.lang.reflect;


import gw.lang.IDimension;
import gw.lang.reflect.java.IJavaType;
import gw.util.Pair;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 */
public class ClassLazyTypeResolver extends LazyTypeResolver {
  public static final ClassLazyTypeResolver Object = new ClassLazyTypeResolver( Object.class );
  public static final ClassLazyTypeResolver String = new ClassLazyTypeResolver( String.class );
  public static final ClassLazyTypeResolver Date = new ClassLazyTypeResolver( Date.class );
  public static final ClassLazyTypeResolver CharSequence = new ClassLazyTypeResolver( CharSequence.class );
  public static final ClassLazyTypeResolver StringBuilder = new ClassLazyTypeResolver( StringBuilder.class );
  public static final ClassLazyTypeResolver StringBuffer = new ClassLazyTypeResolver( StringBuffer.class );
  public static final ClassLazyTypeResolver Boolean = new ClassLazyTypeResolver( Boolean.class );
  public static final ClassLazyTypeResolver Character = new ClassLazyTypeResolver( Character.class );
  public static final ClassLazyTypeResolver Byte = new ClassLazyTypeResolver( Byte.class );
  public static final ClassLazyTypeResolver Short = new ClassLazyTypeResolver( Short.class );
  public static final ClassLazyTypeResolver Integer = new ClassLazyTypeResolver( Integer.class );
  public static final ClassLazyTypeResolver Long = new ClassLazyTypeResolver( Long.class );
  public static final ClassLazyTypeResolver Float = new ClassLazyTypeResolver( Float.class );
  public static final ClassLazyTypeResolver Double = new ClassLazyTypeResolver( Double.class );
  public static final ClassLazyTypeResolver BigDecimal = new ClassLazyTypeResolver( BigDecimal.class );
  public static final ClassLazyTypeResolver BigInteger = new ClassLazyTypeResolver( BigInteger.class );
  public static final ClassLazyTypeResolver Number = new ClassLazyTypeResolver( Number.class );
  public static final ClassLazyTypeResolver IDimension = new ClassLazyTypeResolver( IDimension.class );
  public static final ClassLazyTypeResolver Collection = new ClassLazyTypeResolver( Collection.class );
  public static final ClassLazyTypeResolver Iterator = new ClassLazyTypeResolver( Iterator.class );
  public static final ClassLazyTypeResolver Comparable = new ClassLazyTypeResolver( Comparable.class );
  public static final ClassLazyTypeResolver Iterable = new ClassLazyTypeResolver( Iterable.class );
  public static final ClassLazyTypeResolver List = new ClassLazyTypeResolver( List.class );
  public static final ClassLazyTypeResolver ArrayList = new ClassLazyTypeResolver( ArrayList.class );
  public static final ClassLazyTypeResolver LinkedList = new ClassLazyTypeResolver( LinkedList.class );
  public static final ClassLazyTypeResolver Set = new ClassLazyTypeResolver( Set.class );
  public static final ClassLazyTypeResolver HashSet = new ClassLazyTypeResolver( HashSet.class );
  public static final ClassLazyTypeResolver Map = new ClassLazyTypeResolver( Map.class );
  public static final ClassLazyTypeResolver HashMap = new ClassLazyTypeResolver( HashMap.class );
  public static final ClassLazyTypeResolver Stack = new ClassLazyTypeResolver( Stack.class );
  public static final ClassLazyTypeResolver Pair = new ClassLazyTypeResolver( Pair.class );
  public static final ClassLazyTypeResolver Class = new ClassLazyTypeResolver( Class.class );
  public static final ClassLazyTypeResolver IType = new ClassLazyTypeResolver( IType.class );
  public static final ClassLazyTypeResolver Throwable = new ClassLazyTypeResolver( Throwable.class );
  public static final ClassLazyTypeResolver Error = new ClassLazyTypeResolver( Error.class );
  public static final ClassLazyTypeResolver Exception = new ClassLazyTypeResolver( Exception.class );
  public static final ClassLazyTypeResolver RuntimeException = new ClassLazyTypeResolver( RuntimeException.class );
  public static final ClassLazyTypeResolver Runnable = new ClassLazyTypeResolver( Runnable.class );
  public static final ClassLazyTypeResolver Callable = new ClassLazyTypeResolver( Callable.class );
  public static final ClassLazyTypeResolver Executor = new ClassLazyTypeResolver( Executor.class );
  public static final ClassLazyTypeResolver Future = new ClassLazyTypeResolver( Future.class );
  public static final ClassLazyTypeResolver Enum = new ClassLazyTypeResolver( Enum.class );

  private final Class _class;

  public ClassLazyTypeResolver( Class type ) {
    _class = type;
  }

  @Override
  protected IType init() {
    return TypeSystem.get( _class );
  }

  public static java.lang.String getCachedFieldName( Class cls ) {
    String fieldName = cls.getSimpleName();
    try {
      for( Field f: ClassLazyTypeResolver.class.getDeclaredFields() ) {
        if( f.getName().equals( fieldName ) && ((ClassLazyTypeResolver)f.get( null ))._class == cls ) {
          return fieldName;
        }
      }
      return null;
    }
    catch( IllegalAccessException e ) {
      throw new RuntimeException( e );
    }
  }
}
