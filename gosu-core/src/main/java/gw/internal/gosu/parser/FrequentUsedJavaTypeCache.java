/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.IDimension;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.util.Pair;

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
import java.util.WeakHashMap;

public class FrequentUsedJavaTypeCache {
  private static FrequentUsedJavaTypeCache INSTANCE;

  private Map<Class<?>, IJavaType> _typesByClass = new WeakHashMap<>();
  private Map<String, IJavaType> _typesByName = new HashMap<>();
  private boolean _bInited;

  private int _moduleId;

  public static FrequentUsedJavaTypeCache instance() {
    if( INSTANCE == null )
    {
      INSTANCE = new FrequentUsedJavaTypeCache();
    }
    else
    {
      int moduleId = System.identityHashCode( TypeSystem.getModule() );
      if( moduleId != INSTANCE._moduleId )
      {
        INSTANCE = new FrequentUsedJavaTypeCache();
      }
    }
    return INSTANCE;
  }

  private FrequentUsedJavaTypeCache() {
    _moduleId = System.identityHashCode( TypeSystem.getModule() );
  }

  public void init() {
    _bInited = true;
    Set<Class<?>> classes = new HashSet<>();
    classes.add(void.class);
    classes.add(boolean.class);
    classes.add(byte.class);
    classes.add(char.class);
    classes.add(double.class);
    classes.add(float.class);
    classes.add(int.class);
    classes.add(long.class);
    classes.add(short.class);
    classes.add(String.class);
    classes.add(Number.class);
    classes.add(Double.class);
    classes.add(Boolean.class);
    classes.add(Object.class);
    classes.add(Date.class);
    classes.add(Byte.class);
    classes.add(Float.class);
    classes.add(Character.class);
    classes.add(CharSequence.class);
    classes.add(StringBuilder.class);
    classes.add(Integer.class);
    classes.add(Long.class);
    classes.add(Short.class);
    classes.add(BigDecimal.class);
    classes.add(BigInteger.class);
    classes.add(IDimension.class);
    classes.add(Collection.class);
    classes.add(Iterator.class);
    classes.add(Comparable.class);
    classes.add(Iterable.class);
    classes.add(List.class);
    classes.add(LinkedList.class);
    classes.add(Set.class);
    classes.add(Pair.class);
    classes.add(Map.class);
    classes.add(HashSet.class);
    classes.add(ArrayList.class);
    classes.add(HashMap.class);
    classes.add(Class.class);
    classes.add(IType.class);
    classes.add(Throwable.class);
    classes.add(Error.class);
    classes.add(Exception.class);
    classes.add(RuntimeException.class);
    classes.add(Enum.class);    

    for (Class<?> c : classes) {
      IJavaType type = (IJavaType) TypeSystem.get(c);
      _typesByClass.put(c, type);
      _typesByName.put(c.getName(), type);
    }
  }
  
  public final IJavaType getHighUsageType(Class<?> clazz) {
    if( !_bInited ) {
      init();
    }
    return _typesByClass.get(clazz);
  }

  public final IJavaType getHighUsageType(String clazz) {
    if( !_bInited ) {
      init();
    }
    return _typesByName.get(clazz);
  }  
}
