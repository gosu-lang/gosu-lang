/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.config.ExecutionMode;
import gw.lang.Autoinsert;
import gw.lang.TargetModifier;
import gw.lang.StrictGenerics;
import gw.lang.IAnnotation;
import gw.lang.IDimension;
import gw.lang.InternalAPI;
import gw.lang.Param;
import gw.lang.Params;
import gw.lang.Throws;
import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.AnnotationUsages;
import gw.lang.annotation.IInherited;
import gw.lang.reflect.LazyTypeResolver;
import gw.lang.reflect.SourcePosition;
import gw.lang.reflect.features.IFeatureReference;
import gw.lang.reflect.features.IMethodReference;
import java.lang.annotation.Repeatable;
import gw.lang.function.IBlock;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.reflect.ActualName;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IExpando;
import gw.lang.reflect.IQueryResultSet;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
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
import gw.util.Rational;

import javax.script.Bindings;
import javax.xml.namespace.QName;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
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
  private static final Map<IProject, Map<Class, IJavaType>> CACHE = new WeakHashMap<>();
  private static JavaTypes THIS = new JavaTypes();
  
  static {
    TypeSystem.addShutdownListener( JavaTypes::flushCache );
  }

  // primitives

  private IJavaType pVOID = null;
  public static IJavaType pVOID() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pVOID == null ? THIS.pVOID = getJreType( void.class ) : THIS.pVOID;
    }
    return getJreType( void.class );
  }

  private IJavaType pBOOLEAN = null;
  public static IJavaType pBOOLEAN() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pBOOLEAN == null ? THIS.pBOOLEAN = getJreType( boolean.class ) : THIS.pBOOLEAN;
    }    
    return getJreType( boolean.class );
  }

  private IJavaType pBYTE = null;
  public static IJavaType pBYTE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pBYTE == null ? THIS.pBYTE = getJreType( byte.class ) : THIS.pBYTE;
    }        
    return getJreType( byte.class );
  }

  private IJavaType pCHAR = null;
  public static IJavaType pCHAR() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pCHAR == null ? THIS.pCHAR = getJreType( char.class ) : THIS.pCHAR;
    }    
    return getJreType( char.class );
  }

  private IJavaType pDOUBLE = null;
  public static IJavaType pDOUBLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pDOUBLE == null ? THIS.pDOUBLE = getJreType( double.class ) : THIS.pDOUBLE;
    }    
    return getJreType( double.class );
  }

  private IJavaType pFLOAT = null;
  public static IJavaType pFLOAT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pFLOAT == null ? THIS.pFLOAT = getJreType( float.class ) : THIS.pFLOAT;
    }    
    return getJreType( float.class );
  }

  private IJavaType pINT = null;
  public static IJavaType pINT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pINT == null ? THIS.pINT = getJreType( int.class ) : THIS.pINT;
    }    
    return getJreType( int.class );
  }

  private IJavaType pLONG = null;
  public static IJavaType pLONG() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pLONG == null ? THIS.pLONG = getJreType( long.class ) : THIS.pLONG;
    }    
    return getJreType( long.class );
  }

  private IJavaType pSHORT = null;
  public static IJavaType pSHORT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.pSHORT == null ? THIS.pSHORT = getJreType( short.class ) : THIS.pSHORT;
    }    
    return getJreType( short.class );
  }

  // jre types

  private IJavaType RUNNABLE = null;
  public static IType RUNNABLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.RUNNABLE == null ? THIS.RUNNABLE = getJreType( Runnable.class ) : THIS.RUNNABLE;
    }
    return getJreType( Runnable.class );
  }

  private IType THROWS = null;
  public static IType THROWS() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.THROWS == null ? THIS.THROWS = getSystemType( Throws.class ) : THIS.THROWS;
    }  
    return getSystemType( Throws.class );
  }

  private IType VOID = null;
  public static IType VOID() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.VOID == null ? THIS.VOID = getJreType( Void.class ) : THIS.VOID;
    }  
    return getJreType( Void.class );
  }

  private IJavaType STRING = null;
  public static IJavaType STRING() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.STRING == null ? THIS.STRING = getJreType( String.class ) : THIS.STRING;
    }  
    return getJreType(String.class);
  }

  private IJavaType NUMBER = null;
  public static IJavaType NUMBER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.NUMBER == null ? THIS.NUMBER = getJreType( Number.class ) : THIS.NUMBER;
    }  
    return getJreType(Number.class);
  }

  private IJavaType DOUBLE = null;
  public static IJavaType DOUBLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.DOUBLE == null ? THIS.DOUBLE = getJreType( Double.class ) : THIS.DOUBLE;
    }  
    return getJreType(Double.class);
  }

  private IJavaType BOOLEAN = null;
  public static IJavaType BOOLEAN() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.BOOLEAN == null ? THIS.BOOLEAN = getJreType( Boolean.class ) : THIS.BOOLEAN;
    }  
    return getJreType(Boolean.class);
  }

  private IJavaType OBJECT = null;
  public static IJavaType OBJECT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.OBJECT == null ? THIS.OBJECT = getJreType( Object.class ) : THIS.OBJECT;
    }  
    return getJreType(Object.class);
  }

  private IJavaType DATE = null;
  public static IJavaType DATE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.DATE == null ? THIS.DATE = getJreType( Date.class ) : THIS.DATE;
    }  
    return getJreType(Date.class);
  }

  private IJavaType BYTE = null;
  public static IJavaType BYTE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.BYTE == null ? THIS.BYTE = getJreType( Byte.class ) : THIS.BYTE;
    }  
    return getJreType(Byte.class);
  }

  private IJavaType FLOAT = null;
  public static IJavaType FLOAT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.FLOAT == null ? THIS.FLOAT = getJreType( Float.class ) : THIS.FLOAT;
    }  
    return getJreType(Float.class);
  }

  private IJavaType CHARACTER = null;
  public static IJavaType CHARACTER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.CHARACTER == null ? THIS.CHARACTER = getJreType( Character.class ) : THIS.CHARACTER;
    }  
    return getJreType(Character.class);
  }

  private IJavaType CHAR_SEQUENCE = null;
  public static IJavaType CHAR_SEQUENCE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.CHAR_SEQUENCE == null ? THIS.CHAR_SEQUENCE = getJreType( CharSequence.class ) : THIS.CHAR_SEQUENCE;
    }  
    return getJreType(CharSequence.class);
  }

  private IJavaType STRING_BUILDER = null;
  public static IJavaType STRING_BUILDER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.STRING_BUILDER == null ? THIS.STRING_BUILDER = getJreType( StringBuilder.class ) : THIS.STRING_BUILDER;
    }  
    return getJreType(StringBuilder.class);
  }

  private IJavaType STRING_BUFFER = null;
  public static IJavaType STRING_BUFFER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.STRING_BUFFER == null ? THIS.STRING_BUFFER = getJreType( StringBuffer.class ) : THIS.STRING_BUFFER;
    }  
    return getJreType(StringBuffer.class);
  }

  private IJavaType INTEGER = null;
  public static IJavaType INTEGER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.INTEGER == null ? THIS.INTEGER = getJreType( Integer.class ) : THIS.INTEGER;
    }  
    return getJreType(Integer.class);
  }

  private IJavaType LONG = null;
  public static IJavaType LONG() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.LONG == null ? THIS.LONG = getJreType( Long.class ) : THIS.LONG;
    }  
    return getJreType(Long.class);
  }

  private IJavaType SHORT = null;
  public static IJavaType SHORT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.SHORT == null ? THIS.SHORT = getJreType( Short.class ) : THIS.SHORT;
    }  
    return getJreType(Short.class);
  }

  private IJavaType BIG_DECIMAL = null;
  public static IJavaType BIG_DECIMAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() )
    {
      return THIS.BIG_DECIMAL == null ? THIS.BIG_DECIMAL = getJreType( BigDecimal.class ) : THIS.BIG_DECIMAL;
    }
    return getJreType(BigDecimal.class);
  }

  private IJavaType BIG_INTEGER = null;
  public static IJavaType BIG_INTEGER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.BIG_INTEGER == null ? THIS.BIG_INTEGER = getJreType( BigInteger.class ) : THIS.BIG_INTEGER;
    }  
    return getJreType(BigInteger.class);
  }

  private IJavaType COLLECTION = null;
  public static IJavaType COLLECTION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.COLLECTION == null ? THIS.COLLECTION = getJreType( Collection.class ) : THIS.COLLECTION;
    }  
    return getJreType(Collection.class);
  }

  private IJavaType ITERATOR = null;
  public static IJavaType ITERATOR() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ITERATOR == null ? THIS.ITERATOR = getJreType( Iterator.class ) : THIS.ITERATOR;
    }  
    return getJreType(Iterator.class);
  }

  private IJavaType COMPARABLE = null;
  public static IJavaType COMPARABLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.COMPARABLE == null ? THIS.COMPARABLE = getJreType( Comparable.class ) : THIS.COMPARABLE;
    }  
    return getJreType(Comparable.class);
  }

  private IJavaType ITERABLE = null;
  public static IJavaType ITERABLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ITERABLE == null ? THIS.ITERABLE = getJreType( Iterable.class ) : THIS.ITERABLE;
    }  
    return getJreType(Iterable.class);
  }

  private IJavaType LIST = null;
  public static IJavaType LIST() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.LIST == null ? THIS.LIST = getJreType( List.class ) : THIS.LIST;
    }  
    return getJreType(List.class);
  }

  private IJavaType LINKED_LIST = null;
  public static IJavaType LINKED_LIST() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.LINKED_LIST == null ? THIS.LINKED_LIST = getJreType( LinkedList.class ) : THIS.LINKED_LIST;
    }  
    return getJreType(LinkedList.class);
  }

  private IJavaType SET = null;
  public static IJavaType SET() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.SET == null ? THIS.SET = getJreType( Set.class ) : THIS.SET;
    }  
    return getJreType(Set.class);
  }

  private IJavaType MAP = null;
  public static IJavaType MAP() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.MAP == null ? THIS.MAP = getJreType( Map.class ) : THIS.MAP;
    }  
    return getJreType(Map.class);
  }

  private IJavaType HASH_SET = null;
  public static IJavaType HASH_SET() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.HASH_SET == null ? THIS.HASH_SET = getJreType( HashSet.class ) : THIS.HASH_SET;
    }  
    return getJreType(HashSet.class);
  }

  private IJavaType ARRAY_LIST = null;
  public static IJavaType ARRAY_LIST() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ARRAY_LIST == null ? THIS.ARRAY_LIST = getJreType( ArrayList.class ) : THIS.ARRAY_LIST;
    }  
    return getJreType(ArrayList.class);
  }

  private IJavaType HASH_MAP = null;
  public static IJavaType HASH_MAP() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.HASH_MAP == null ? THIS.HASH_MAP = getJreType( HashMap.class ) : THIS.HASH_MAP;
    }  
    return getJreType(HashMap.class);
  }

  private IJavaType CLASS = null;
  public static IJavaType CLASS() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.CLASS == null ? THIS.CLASS = getJreType( Class.class ) : THIS.CLASS;
    }  
    return getJreType(Class.class);
  }

  private IJavaType THROWABLE = null;
  public static IJavaType THROWABLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.THROWABLE == null ? THIS.THROWABLE = getJreType( Throwable.class ) : THIS.THROWABLE;
    }  
    return getJreType(Throwable.class);
  }

  private IJavaType ERROR = null;
  public static IJavaType ERROR() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ERROR == null ? THIS.ERROR = getJreType( Error.class ) : THIS.ERROR;
    }  
    return getJreType(Error.class);
  }

  private IJavaType EXCEPTION = null;
  public static IJavaType EXCEPTION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.EXCEPTION == null ? THIS.EXCEPTION = getJreType( Exception.class ) : THIS.EXCEPTION;
    }  
    return getJreType(Exception.class);
  }

  private IJavaType RUNTIME_EXCEPTION = null;
  public static IJavaType RUNTIME_EXCEPTION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.RUNTIME_EXCEPTION == null ? THIS.RUNTIME_EXCEPTION = getJreType( RuntimeException.class ) : THIS.RUNTIME_EXCEPTION;
    }  
    return getJreType(RuntimeException.class);
  }

  private IJavaType ENUM = null;
  public static IJavaType ENUM() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ENUM == null ? THIS.ENUM = getJreType( Enum.class ) : THIS.ENUM;
    }  
    return getJreType(Enum.class);
  }

  private IJavaType QNAME = null;
  public static IJavaType QNAME() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.QNAME == null ? THIS.QNAME = getJreType( QName.class ) : THIS.QNAME;
    }  
    return getJreType(QName.class);
  }

  private IJavaType TIME_ZONE = null;
  public static IJavaType TIME_ZONE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.TIME_ZONE == null ? THIS.TIME_ZONE = getJreType( TimeZone.class ) : THIS.TIME_ZONE;
    }  
    return getJreType(TimeZone.class);
  }

  private IJavaType ANNOTATION = null;
  public static IJavaType ANNOTATION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ANNOTATION == null ? THIS.ANNOTATION = getJreType( Annotation.class ) : THIS.ANNOTATION;
    }  
    return getJreType(Annotation.class);
  }

  private IJavaType REPEATABLE = null;
  public static IJavaType REPEATABLE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.REPEATABLE == null ? THIS.REPEATABLE = getJreType( Repeatable.class ) : THIS.REPEATABLE;
    }  
    return getJreType(Repeatable.class);
  }

  private IJavaType TARGET = null;
  public static IJavaType TARGET() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.TARGET == null ? THIS.TARGET = getJreType( Target.class ) : THIS.TARGET;
    }
    return getJreType(Target.class);
  }

  private IJavaType TARGET_MODIFIER = null;
  public static IJavaType TARGET_MODIFIER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.TARGET_MODIFIER == null ? THIS.TARGET_MODIFIER = getJreType( TargetModifier.class ) : THIS.TARGET_MODIFIER;
    }
    return getJreType(TargetModifier.class);
  }

  private IJavaType STRICT_GENERICS = null;
  public static IJavaType STRICT_GENERICS() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.STRICT_GENERICS == null ? THIS.STRICT_GENERICS = getJreType( StrictGenerics.class ) : THIS.STRICT_GENERICS;
    }  
    return getJreType(StrictGenerics.class);
  }

  private IJavaType JAVA_LANG_DEPRECATED = null;
  public static IJavaType JAVA_LANG_DEPRECATED() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.JAVA_LANG_DEPRECATED == null ? THIS.JAVA_LANG_DEPRECATED = getJreType( Deprecated.class ) : THIS.JAVA_LANG_DEPRECATED;
    }  
    return getJreType(Deprecated.class);
  }

  private IJavaType INHERITED = null;
  public static IJavaType INHERITED() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.INHERITED == null ? THIS.INHERITED = getJreType( Inherited.class ) : THIS.INHERITED;
    }  
    return getJreType(Inherited.class);
  }

  private IJavaType FUNCTIONAL_INTERFACE = null;
  public static IJavaType FUNCTIONAL_INTERFACE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.FUNCTIONAL_INTERFACE == null ? THIS.FUNCTIONAL_INTERFACE = getJreType( FunctionalInterface.class ) : THIS.FUNCTIONAL_INTERFACE;
    }  
    return getJreType(FunctionalInterface.class);
  }

  private IJavaType LOCK = null;
  public static IJavaType LOCK() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.LOCK == null ? THIS.LOCK = getJreType( Lock.class ) : THIS.LOCK;
    }  
    return getJreType(Lock.class);
  }

  private IJavaType BINDINGS = null;
  public static IJavaType BINDINGS() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.BINDINGS == null ? THIS.BINDINGS = getGosuType( Bindings.class ) : THIS.BINDINGS;
    }
    return getJreType(Bindings.class);
  }


  // gosu types

  private IJavaType ANNOTATION_USAGE = null;
  public static IJavaType ANNOTATION_USAGE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ANNOTATION_USAGE == null ? THIS.ANNOTATION_USAGE = getGosuType( AnnotationUsage.class ) : THIS.ANNOTATION_USAGE;
    }  
    return getGosuType(AnnotationUsage.class);
  }

  private IJavaType ANNOTATION_USAGES = null;
  public static IJavaType ANNOTATION_USAGES() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ANNOTATION_USAGES == null ? THIS.ANNOTATION_USAGES = getGosuType( AnnotationUsages.class ) : THIS.ANNOTATION_USAGES;
    }  
    return getGosuType(AnnotationUsages.class);
  }

  private IJavaType AUTOINSERT = null;
  public static IJavaType AUTOINSERT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.AUTOINSERT == null ? THIS.AUTOINSERT = getGosuType( Autoinsert.class ) : THIS.AUTOINSERT;
    }  
    return getGosuType(Autoinsert.class);
  }

  private IJavaType SOURCE_POSITION = null;
  public static IJavaType SOURCE_POSITION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.SOURCE_POSITION == null ? THIS.SOURCE_POSITION = getGosuType( SourcePosition.class ) : THIS.SOURCE_POSITION;
    }  
    return getGosuType(SourcePosition.class);
  }

  private IJavaType IGOSU_OBJECT = null;
  public static IJavaType IGOSU_OBJECT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IGOSU_OBJECT == null ? THIS.IGOSU_OBJECT = getGosuType( IGosuObject.class ) : THIS.IGOSU_OBJECT;
    }  
    return getGosuType(IGosuObject.class);
  }

  private IJavaType IANNOTATION = null;
  public static IJavaType IANNOTATION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IANNOTATION == null ? THIS.IANNOTATION = getGosuType( IAnnotation.class ) : THIS.IANNOTATION;
    }  
    return getGosuType(IAnnotation.class);
  }

  private IJavaType INTERNAL_API = null;
  public static IJavaType INTERNAL_API() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.INTERNAL_API == null ? THIS.INTERNAL_API = getGosuType( InternalAPI.class ) : THIS.INTERNAL_API;
    }  
    return getGosuType(InternalAPI.class);
  }

  private IJavaType ITYPE = null;
  public static IJavaType ITYPE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ITYPE == null ? THIS.ITYPE = getGosuType( IType.class ) : THIS.ITYPE;
    }  
    return getGosuType(IType.class);
  }

  private IJavaType IDIMENSION = null;
  public static IJavaType IDIMENSION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IDIMENSION == null ? THIS.IDIMENSION = getGosuType( IDimension.class ) : THIS.IDIMENSION;
    }  
    return getGosuType(IDimension.class);
  }

  private IType FUNCTION_TYPE = null;
  public static IType FUNCTION_TYPE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.FUNCTION_TYPE == null ? THIS.FUNCTION_TYPE = getGosuType( FunctionType.class ) : THIS.FUNCTION_TYPE;
    }  
    return getGosuType(FunctionType.class);
  }

  private IType LAZY_TYPE_RESOLVER = null;
  public static IType LAZY_TYPE_RESOLVER() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.LAZY_TYPE_RESOLVER == null ? THIS.LAZY_TYPE_RESOLVER = getGosuType( LazyTypeResolver.class ) : THIS.LAZY_TYPE_RESOLVER;
    }  
    return getGosuType(LazyTypeResolver.class);
  }

  private IType IBLOCK = null;
  public static IType IBLOCK() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IBLOCK == null ? THIS.IBLOCK = getGosuType( IBlock.class ) : THIS.IBLOCK;
    }  
    return getGosuType(IBlock.class);
  }

  private IType IMETHOD_REFERENCE = null;
  public static IType IMETHOD_REFERENCE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IMETHOD_REFERENCE == null ? THIS.IMETHOD_REFERENCE = getGosuType( IMethodReference.class ) : THIS.IMETHOD_REFERENCE;
    }  
    return getGosuType(IMethodReference.class);
  }

  private IType IFEATURE_REFERENCE = null;
  public static IType IFEATURE_REFERENCE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IFEATURE_REFERENCE == null ? THIS.IFEATURE_REFERENCE = getGosuType( IFeatureReference.class ) : THIS.IFEATURE_REFERENCE;
    }  
    return getGosuType(IFeatureReference.class);
  }

  private IType GW_LANG_DEPRECATED = null;
  public static IType GW_LANG_DEPRECATED() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.GW_LANG_DEPRECATED == null ? THIS.GW_LANG_DEPRECATED = getGosuType( gw.lang.Deprecated.class ) : THIS.GW_LANG_DEPRECATED;
    }  
    return getGosuType(gw.lang.Deprecated.class);
  }

  private IType FRAGMENT_INSTANCE = null;
  public static IType FRAGMENT_INSTANCE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.FRAGMENT_INSTANCE == null ? THIS.FRAGMENT_INSTANCE = getGosuType( FragmentInstance.class ) : THIS.FRAGMENT_INSTANCE;
    }  
    return getGosuType(FragmentInstance.class);
  }

  private IType IEXTERNAL_SYMBOL_MAP = null;
  public static IType IEXTERNAL_SYMBOL_MAP() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IEXTERNAL_SYMBOL_MAP == null ? THIS.IEXTERNAL_SYMBOL_MAP = getGosuType( IExternalSymbolMap.class ) : THIS.IEXTERNAL_SYMBOL_MAP;
    }  
    return getGosuType(IExternalSymbolMap.class);
  }

  private IType PARAM = null;
  public static IType PARAM() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.PARAM == null ? THIS.PARAM = getGosuType( Param.class ) : THIS.PARAM;
    }  
    return getGosuType(Param.class);
  }
  private IType PARAMS = null;
  public static IType PARAMS() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.PARAMS == null ? THIS.PARAMS = getGosuType( Params.class ) : THIS.PARAMS;
    }  
    return getGosuType(Params.class);
  }

  private IType IQUERY_RESULT_SET = null;
  public static IType IQUERY_RESULT_SET() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IQUERY_RESULT_SET == null ? THIS.IQUERY_RESULT_SET = getGosuType( IQueryResultSet.class ) : THIS.IQUERY_RESULT_SET;
    }  
    return getGosuType(IQueryResultSet.class);
  }

  private IJavaType IEXECUTION_ENVIRONMENT = null;
  public static IJavaType IEXECUTION_ENVIRONMENT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IEXECUTION_ENVIRONMENT == null ? THIS.IEXECUTION_ENVIRONMENT = getGosuType( IExecutionEnvironment.class ) : THIS.IEXECUTION_ENVIRONMENT;
    }  
    return getGosuType(IExecutionEnvironment.class);
  }

  private IJavaType IINHERITED = null;
  public static IJavaType IINHERITED() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IINHERITED == null ? THIS.IINHERITED = getGosuType( IInherited.class ) : THIS.IINHERITED;
    }  
    return getGosuType(IInherited.class);
  }

  private IJavaType IGOSU_CLASS_OBJECT = null;
  public static IJavaType IGOSU_CLASS_OBJECT() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IGOSU_CLASS_OBJECT == null ? THIS.IGOSU_CLASS_OBJECT = getGosuType( IGosuClassObject.class ) : THIS.IGOSU_CLASS_OBJECT;
    }  
    return getGosuType(IGosuClassObject.class);
  }

  private IJavaType IINTERVAL = null;
  public static IJavaType IINTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IINTERVAL == null ? THIS.IINTERVAL = getGosuType( IInterval.class ) : THIS.IINTERVAL;
    }  
    return getGosuType(IInterval.class);
  }

  private IJavaType INTEGER_INTERVAL = null;
  public static IJavaType INTEGER_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.INTEGER_INTERVAL == null ? THIS.INTEGER_INTERVAL = getGosuType( IntegerInterval.class ) : THIS.INTEGER_INTERVAL;
    }  
    return getGosuType(IntegerInterval.class);
  }

  private IJavaType LONG_INTERVAL = null;
  public static IJavaType LONG_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.LONG_INTERVAL == null ? THIS.LONG_INTERVAL = getGosuType( LongInterval.class ) : THIS.LONG_INTERVAL;
    }  
    return getGosuType(LongInterval.class);
  }

  private IJavaType NUMBER_INTERVAL = null;
  public static IJavaType NUMBER_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.NUMBER_INTERVAL == null ? THIS.NUMBER_INTERVAL = getGosuType( NumberInterval.class ) : THIS.NUMBER_INTERVAL;
    }  
    return getGosuType(NumberInterval.class);
  }

  private IJavaType BIG_INTEGER_INTERVAL = null;
  public static IJavaType BIG_INTEGER_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.BIG_INTEGER_INTERVAL == null ? THIS.BIG_INTEGER_INTERVAL = getGosuType( BigIntegerInterval.class ) : THIS.BIG_INTEGER_INTERVAL;
    }  
    return getGosuType(BigIntegerInterval.class);
  }

  private IJavaType BIG_DECIMAL_INTERVAL = null;
  public static IJavaType BIG_DECIMAL_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.BIG_DECIMAL_INTERVAL == null ? THIS.BIG_DECIMAL_INTERVAL = getGosuType( BigDecimalInterval.class ) : THIS.BIG_DECIMAL_INTERVAL;
    }  
    return getGosuType(BigDecimalInterval.class);
  }

  private IJavaType DATE_INTERVAL = null;
  public static IJavaType DATE_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.DATE_INTERVAL == null ? THIS.DATE_INTERVAL = getGosuType( DateInterval.class ) : THIS.DATE_INTERVAL;
    }  
    return getGosuType(DateInterval.class);
  }

  private IJavaType SEQUENCEABLE_INTERVAL = null;
  public static IJavaType SEQUENCEABLE_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.SEQUENCEABLE_INTERVAL == null ? THIS.SEQUENCEABLE_INTERVAL = getGosuType( SequenceableInterval.class ) : THIS.SEQUENCEABLE_INTERVAL;
    }  
    return getGosuType(SequenceableInterval.class);
  }

  private IJavaType COMPARABLE_INTERVAL = null;
  public static IJavaType COMPARABLE_INTERVAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.COMPARABLE_INTERVAL == null ? THIS.COMPARABLE_INTERVAL = getGosuType( ComparableInterval.class ) : THIS.COMPARABLE_INTERVAL;
    }  
    return getGosuType(ComparableInterval.class);
  }

  private IJavaType IBLOCK_EXPRESSION = null;
  public static IJavaType IBLOCK_EXPRESSION() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IBLOCK_EXPRESSION == null ? THIS.IBLOCK_EXPRESSION = getGosuType( IBlockExpression.class ) : THIS.IBLOCK_EXPRESSION;
    }  
    return getGosuType(IBlockExpression.class);
  }

  private IJavaType IPROGRAM_INSTANCE = null;
  public static IJavaType IPROGRAM_INSTANCE() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IPROGRAM_INSTANCE == null ? THIS.IPROGRAM_INSTANCE = getGosuType( IProgramInstance.class ) : THIS.IPROGRAM_INSTANCE;
    }  
    return getGosuType(IProgramInstance.class);
  }

  private IJavaType IEXPANDO = null;
  public static IJavaType IEXPANDO() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.IEXPANDO == null ? THIS.IEXPANDO = getGosuType( IExpando.class ) : THIS.IEXPANDO;
    }  
    return getGosuType(IExpando.class);
  }

  private IJavaType ACTUAL_NAME = null;
  public static IJavaType ACTUAL_NAME() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.ACTUAL_NAME == null ? THIS.ACTUAL_NAME = getGosuType( ActualName.class ) : THIS.ACTUAL_NAME;
    }  
    return getGosuType(ActualName.class);
  }

  private IJavaType RATIONAL = null;
  public static IJavaType RATIONAL() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return THIS.RATIONAL == null ? THIS.RATIONAL = getGosuType( Rational.class ) : THIS.RATIONAL;
    }
    return getGosuType(Rational.class);
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
    THIS = new JavaTypes();
  }
}
