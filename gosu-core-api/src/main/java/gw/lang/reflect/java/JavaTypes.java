/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.Autoinsert;
import gw.lang.IAnnotation;
import gw.lang.IDimension;
import gw.lang.InternalAPI;
import gw.lang.Param;
import gw.lang.Params;
import gw.lang.Throws;
import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.AnnotationUsages;
import gw.lang.annotation.IInherited;
import gw.lang.annotation.Repeatable;
import gw.lang.function.IBlock;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IQueryResultSet;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.gs.FragmentInstance;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuClassObject;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.interval.BigDecimalInterval;
import gw.lang.reflect.interval.BigIntegerInterval;
import gw.lang.reflect.interval.ComparableInterval;
import gw.lang.reflect.interval.DateInterval;
import gw.lang.reflect.interval.IInterval;
import gw.lang.reflect.interval.IntegerInterval;
import gw.lang.reflect.interval.LongInterval;
import gw.lang.reflect.interval.NumberInterval;
import gw.lang.reflect.interval.SequenceableInterval;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IProject;

import javax.xml.namespace.QName;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
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
import java.util.TimeZone;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class JavaTypes {
  private static final Map<IProject, Map<Class, IJavaType>> CACHE = new WeakHashMap<IProject, Map<Class, IJavaType>>();

  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        flushCache();
      }
    });
  }

  // primitives

  public static IJavaType pVOID() {
    return getJreType(Void.TYPE);
  }

  public static IJavaType pBOOLEAN() {
    return getJreType(Boolean.TYPE);
  }

  public static IJavaType pBYTE() {
    return getJreType(Byte.TYPE);
  }

  public static IJavaType pCHAR() {
    return getJreType(Character.TYPE);
  }

  public static IJavaType pDOUBLE() {
    return getJreType(Double.TYPE);
  }

  public static IJavaType pFLOAT() {
    return getJreType(Float.TYPE);
  }

  public static IJavaType pINT() {
    return getJreType(Integer.TYPE);
  }

  public static IJavaType pLONG() {
    return getJreType(Long.TYPE);
  }

  public static IJavaType pSHORT() {
    return getJreType(Short.TYPE);
  }

  // jre types

  public static IType RUNNABLE() {
    return getJreType(Runnable.class);
  }

  public static IType THROWS() {
    return getSystemType(Throws.class);
  }

  public static IType VOID() {
    return getJreType(Void.class);
  }

  public static IJavaType STRING() {
    return getJreType(String.class);
  }

  public static IJavaType NUMBER() {
    return getJreType(Number.class);
  }

  public static IJavaType DOUBLE() {
    return getJreType(Double.class);
  }

  public static IJavaType BOOLEAN() {
    return getJreType(Boolean.class);
  }

  public static IJavaType OBJECT() {
    return getJreType(Object.class);
  }

  public static IJavaType DATE() {
    return getJreType(Date.class);
  }

  public static IJavaType BYTE() {
    return getJreType(Byte.class);
  }

  public static IJavaType FLOAT() {
    return getJreType(Float.class);
  }

  public static IJavaType CHARACTER() {
    return getJreType(Character.class);
  }

  public static IJavaType CHAR_SEQUENCE() {
    return getJreType(CharSequence.class);
  }

  public static IJavaType STRING_BUILDER() {
    return getJreType(StringBuilder.class);
  }

  public static IJavaType STRING_BUFFER() {
    return getJreType(StringBuffer.class);
  }

  public static IJavaType INTEGER() {
    return getJreType(Integer.class);
  }

  public static IJavaType LONG() {
    return getJreType(Long.class);
  }

  public static IJavaType SHORT() {
    return getJreType(Short.class);
  }

  public static IJavaType BIG_DECIMAL() {
    return getJreType(BigDecimal.class);
  }

  public static IJavaType BIG_INTEGER() {
    return getJreType(BigInteger.class);
  }

  public static IJavaType COLLECTION() {
    return getJreType(Collection.class);
  }

  public static IJavaType ITERATOR() {
    return getJreType(Iterator.class);
  }

  public static IJavaType COMPARABLE() {
    return getJreType(Comparable.class);
  }

  public static IJavaType ITERABLE() {
    return getJreType(Iterable.class);
  }

  public static IJavaType LIST() {
    return getJreType(List.class);
  }

  public static IJavaType LINKED_LIST() {
    return getJreType(LinkedList.class);
  }

  public static IJavaType SET() {
    return getJreType(Set.class);
  }

  public static IJavaType MAP() {
    return getJreType(Map.class);
  }

  public static IJavaType HASH_SET() {
    return getJreType(HashSet.class);
  }

  public static IJavaType ARRAY_LIST() {
    return getJreType(ArrayList.class);
  }

  public static IJavaType HASH_MAP() {
    return getJreType(HashMap.class);
  }

  public static IJavaType CLASS() {
    return getJreType(Class.class);
  }

  public static IJavaType THROWABLE() {
    return getJreType(Throwable.class);
  }

  public static IJavaType ERROR() {
    return getJreType(Error.class);
  }

  public static IJavaType EXCEPTION() {
    return getJreType(Exception.class);
  }

  public static IJavaType RUNTIME_EXCEPTION() {
    return getJreType(RuntimeException.class);
  }

  public static IJavaType ENUM() {
    return getJreType(Enum.class);
  }

  public static IJavaType QNAME() {
    return getJreType(QName.class);
  }

  public static IJavaType TIME_ZONE() {
    return getJreType(TimeZone.class);
  }

  public static IJavaType ANNOTATION() {
    return getJreType(Annotation.class);
  }

  public static IJavaType REPEATABLE() {
    return getGosuType(Repeatable.class);
  }

  public static IJavaType INHERITED() {
    return getJreType(Inherited.class);
  }

  public static IJavaType LOCK() {
    return getJreType(Lock.class);
  }

  // gosu types

  public static IJavaType ANNOTATION_USAGE() {
    return getGosuType(AnnotationUsage.class);
  }

  public static IJavaType ANNOTATION_USAGES() {
    return getGosuType(AnnotationUsages.class);
  }

  public static IJavaType AUTOINSERT() {
    return getGosuType(Autoinsert.class);
  }

  public static IJavaType IGOSU_OBJECT() {
    return getGosuType(IGosuObject.class);
  }

  public static IJavaType IANNOTATION() {
    return getGosuType(IAnnotation.class);
  }

  public static IJavaType INTERNAL_API() {
    return getGosuType(InternalAPI.class);
  }

  public static IJavaType ITYPE() {
    return getGosuType(IType.class);
  }

  public static IJavaType IDIMENSION() {
    return getGosuType(IDimension.class);
  }

  public static IType FUNCTION_TYPE() {
    return getGosuType(FunctionType.class);
  }

  public static IType IBLOCK() {
    return getGosuType(IBlock.class);
  }

  public static IType GW_LANG_DEPRECATED() {
    return getGosuType(gw.lang.Deprecated.class);
  }

  public static IType FRAGMENT_INSTANCE() {
    return getGosuType(FragmentInstance.class);
  }

  public static IType IEXTERNAL_SYMBOL_MAP() {
    return getGosuType(IExternalSymbolMap.class);
  }

  public static IType PARAM() {
    return getGosuType(Param.class);
  }
  public static IType PARAMS() {
    return getGosuType(Params.class);
  }

  public static IType IQUERY_RESULT_SET() {
    return getGosuType(IQueryResultSet.class);
  }

  public static IJavaType IEXECUTION_ENVIRONMENT() {
    return getGosuType(IExecutionEnvironment.class);
  }

  public static IJavaType IINHERITED() {
    return getGosuType(IInherited.class);
  }

  public static IJavaType IGOSU_CLASS_OBJECT() {
    return getGosuType(IGosuClassObject.class);
  }

  public static IJavaType IINTERVAL() {
    return getGosuType(IInterval.class);
  }

  public static IJavaType INTEGER_INTERVAL() {
    return getGosuType(IntegerInterval.class);
  }

  public static IJavaType LONG_INTERVAL() {
    return getGosuType(LongInterval.class);
  }

  public static IJavaType NUMBER_INTERVAL() {
    return getGosuType(NumberInterval.class);
  }

  public static IJavaType BIG_INTEGER_INTERVAL() {
    return getGosuType(BigIntegerInterval.class);
  }

  public static IJavaType BIG_DECIMAL_INTERVAL() {
    return getGosuType(BigDecimalInterval.class);
  }

  public static IJavaType DATE_INTERVAL() {
    return getGosuType(DateInterval.class);
  }

  public static IJavaType SEQUENCEABLE_INTERVAL() {
    return getGosuType(SequenceableInterval.class);
  }

  public static IJavaType COMPARABLE_INTERVAL() {
    return getGosuType(ComparableInterval.class);
  }

  public static IJavaType IBLOCK_EXPRESSION() {
    return getGosuType(IBlockExpression.class);
  }

  public static IJavaType IPROGRAM_INSTANCE() {
    return getGosuType(IProgramInstance.class);
  }

  // utilities

  private static IJavaType findTypeFromJre(Class c) {
    IJavaType type = (IJavaType) TypeSystem.get(c, TypeSystem.getExecutionEnvironment().getJreModule());
    IExecutionEnvironment execEnv = type.getTypeLoader().getModule().getExecutionEnvironment();
    if (execEnv.getProject().isDisposed()) {
      throw new IllegalStateException("Whoops.... the project associated with type, " + type.getName() + ", is stale. ExecEnv: " + execEnv.getProject());
    }
    return type;
  }

  private static IJavaType findTypeFromProject(Class c) {
    return (IJavaType) TypeSystem.get(c, TypeSystem.getGlobalModule());
  }

  private static IJavaType getCachedType( Class c, boolean bFromJre ) {
    Map<Class, IJavaType> cache = getCACHE();
    IJavaType type = cache.get( c );
    if( type == null ) {
      TypeSystem.lock();
      try {
        type = cache.get( c );
        if( type == null ) {
          if( bFromJre ) {
            type = findTypeFromJre( c );
          }
          else {
            type = findTypeFromProject( c );
          }
          cache.put( c, type );
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
    return type;
  }

  private static Map<Class, IJavaType> getCACHE() {
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment();
    IProject project = execEnv.getProject();
    Map<Class, IJavaType> cache = CACHE.get( project );
    if( cache == null ) {
      synchronized( CACHE ) {
        cache = CACHE.get( project );
        if( cache == null ) {
          cache = new ConcurrentHashMap<Class, IJavaType>();
          CACHE.put( project, cache );
        }
      }
    }
    return cache;
  }

  public static IJavaType getJreType(final Class<?> c) {
    return getCachedType( c, true );
  }

  public static IJavaType getGosuType(final Class<?> c) {
    return getCachedType( c, false );
  }

  public static IJavaType getSystemType(Class<?> c) {
    return getGosuType(c);
  }

  public static void flushCache() {
    getCACHE().clear();
  }
}
