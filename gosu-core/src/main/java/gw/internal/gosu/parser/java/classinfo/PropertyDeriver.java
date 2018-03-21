package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.TypeLord;
import gw.lang.SimplePropertyProcessing;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.ImplicitPropertyUtil;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.ClassInfoUtil;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.JavaTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
public class PropertyDeriver
{
  public static IJavaPropertyDescriptor[] initPropertyDescriptors( IJavaClassInfo jci )
  {
    Map<String, IJavaClassMethod> mapGetters = new HashMap<>();
    Map<String, List<IJavaClassMethod>> mapSetters = new HashMap<>();
    List<IJavaClassMethod> methods = new ArrayList<>();

    // All declared methods, excluding bridge methods
    methods.addAll( Arrays.stream( jci.getDeclaredMethods() )
                      .filter( m -> !m.isBridge() )
                      .collect( Collectors.toList() ) );

    boolean simplePropertyProcessing = jci.getAnnotation( SimplePropertyProcessing.class ) != null;

    populateMaps( mapGetters, mapSetters, methods, simplePropertyProcessing );

    List<IJavaPropertyDescriptor> propertyDescriptors = new ArrayList<>();
    for( Map.Entry<String, IJavaClassMethod> entry : mapGetters.entrySet() )
    {
      String propName = entry.getKey();

      IJavaClassMethod getter = entry.getValue();
      IJavaClassType getterType = getter == null ? null : getter.getGenericReturnType();

      boolean bStatic = getter != null && Modifier.isStatic( getter.getModifiers() );
      IJavaClassMethod setter = findBestMatchingSetter( bStatic, getterType, mapSetters.remove( propName ) );

      if( setter == null )
      {
        setter = maybeFindSetterInSuper( getter, jci.getSuperclass() );
        if( setter == null )
        {
          setter = maybeFindSetterInSuperInterfaces( getter, jci.getInterfaces() );
        }
      }
      addPropertyDescriptor( propertyDescriptors, propName, getter, setter );
    }
    addFromRemainingSetters( jci, mapSetters, propertyDescriptors );
    addInheritedUnrelatedGettersAndSetters( jci, propertyDescriptors );
    return propertyDescriptors.toArray( new IJavaPropertyDescriptor[propertyDescriptors.size()] );
  }

  private static void addPropertyDescriptor( List<IJavaPropertyDescriptor> propertyDescriptors, String propName, IJavaClassMethod getter, IJavaClassMethod setter )
  {
    IType glbActual = setter == null
                      ? getTypeFromMethod( getter )
                      : TypeLord.findGreatestLowerBound( getTypeFromMethod( getter ), getTypeFromMethod( setter ) );
    propertyDescriptors.add( new JavaSourcePropertyDescriptor( propName, glbActual, getter, setter ) );
  }

  //
  //  A     B
  //  |     |
  //  +-----+  A defines getFoo(), B defines setFoo(), C must define a pseudo-declared, read/write property for Foo
  //     |
  //     C
  //
  private static void addInheritedUnrelatedGettersAndSetters( IJavaClassInfo jci, List<IJavaPropertyDescriptor> propertyDescriptors )
  {
    Map<String, IJavaClassMethod> mapGetters = new HashMap<>();
    Map<String, IJavaClassMethod> mapSetters = new HashMap<>();
    findUnpairedMethods( jci, mapGetters, mapSetters );
    if( mapSetters.isEmpty() )
    {
      return;
    }
    for( String name: mapSetters.keySet() )
    {
      IJavaClassMethod getter = mapGetters.get( name );
      if( getter != null )
      {
        IJavaClassMethod setter = mapSetters.get( name );
        if( setter != null )
        {
          IJavaPropertyDescriptor existing = findProperty( name, propertyDescriptors );
          if( existing == null )
          {
            if( doesSetterDescMatchGetterMethod( getter, setter ) )
            {
              addPropertyDescriptor( propertyDescriptors, name, getter, setter );
            }
          }
        }
      }
    }
  }

  private static IJavaPropertyDescriptor findProperty( String name, List<IJavaPropertyDescriptor> propertyDescriptors )
  {
    for( IJavaPropertyDescriptor pd: propertyDescriptors )
    {
      if( pd.getName().equals( name ) )
      {
        return pd;
      }
    }
    return null;
  }

  private static void findUnpairedMethods( IJavaClassInfo jci, Map<String, IJavaClassMethod> mapGetters, Map<String, IJavaClassMethod> mapSetters )
  {
    IJavaClassInfo superclass = jci.getSuperclass();
    if( superclass != null )
    {
      addUnpairedMethods( superclass, mapGetters, mapSetters );
    }
    for( IJavaClassInfo iface: jci.getInterfaces() )
    {
      addUnpairedMethods( iface, mapGetters, mapSetters );
    }
  }

  private static void addUnpairedMethods( IJavaClassInfo type, Map<String, IJavaClassMethod> mapGetters, Map<String, IJavaClassMethod> mapSetters )
  {
    for( IJavaPropertyDescriptor pd: type.getPropertyDescriptors() )
    {
      String name = pd.getName();
      if( pd.getWriteMethod() == null && !mapGetters.containsKey( name ) )
      {
        mapGetters.put( name, pd.getReadMethod() );
      }
      else if( pd.getReadMethod() == null && !mapSetters.containsKey( name ) )
      {
        mapSetters.put( name, pd.getWriteMethod() );
      }
      else
      {
        // a paired property already exists, prevent further pairing
        mapGetters.put( name, null );
        mapSetters.put( name, null );
      }
    }
    findUnpairedMethods( type, mapGetters, mapSetters );
  }

  private static void populateMaps( Map<String, IJavaClassMethod> mapGetters, Map<String, List<IJavaClassMethod>> mapSetters, List<IJavaClassMethod> methods, boolean simplePropertyProcessing )
  {
    for( IJavaClassMethod method : methods )
    {
      ImplicitPropertyUtil.ImplicitPropertyInfo info = JavaSourceUtil.getImplicitProperty( method, simplePropertyProcessing );
      if( info != null )
      {
        if( info.isGetter() && !mapGetters.containsKey( info.getName() ) )
        {
          mapGetters.put( info.getName(), method );
        }
        else if( info.isSetter() )
        {
          List<IJavaClassMethod> list = mapSetters.computeIfAbsent( info.getName(), k -> new ArrayList<>() );
          list.add( method );

          // sort methods so that a property will be chosen deterministically;
          // only applicable to case where we have two or more setters and no getter,
          // so we make a property from the first setter in the list, which must be
          // deterministically chosen (considering a compiled class may not order methods in source order)
          list.sort( Comparator.comparingInt( m -> m.getParameterTypes()[0].getJavaType().getName().hashCode() ) );
        }
      }
    }
  }

  private static void addFromRemainingSetters( IJavaClassInfo jci, Map<String, List<IJavaClassMethod>> mapSetters, List<IJavaPropertyDescriptor> propertyDescriptors )
  {
    for( Map.Entry<String, List<IJavaClassMethod>> entry : mapSetters.entrySet() )
    {
      String propName = entry.getKey();
      List<IJavaClassMethod> setters = entry.getValue();
      IJavaClassMethod getter = null;
      IType glbActual = null;
      JavaSourcePropertyDescriptor prop = null;
      for( IJavaClassMethod setter: setters )
      {
        boolean[] getterNameFound = {false};

        IJavaClassMethod getterCsr = maybeFindGetterInSuper( setter, jci.getSuperclass(), getterNameFound );
        if( getterCsr == null )
        {
          getterCsr = maybeFindGetterInSuperInterfaces( setter, jci.getInterfaces(), getterNameFound );
        }
        if( getterCsr == null && getterNameFound[0] )
        {
          continue;
        }
        IType glbCsr = getterCsr == null
                 ? getTypeFromMethod( setter )
                 : TypeLord.findGreatestLowerBound( getTypeFromMethod( getterCsr ), getTypeFromMethod( setter ) );
        if( (glbActual == null || glbActual.isAssignableFrom( glbCsr )) && (getter == null || getterCsr != null) )
        {
          glbActual = glbCsr;
          getter = getterCsr;
          prop = new JavaSourcePropertyDescriptor( propName, glbCsr, getterCsr, setter );
        }
      }
      if( prop != null )
      {
        propertyDescriptors.add( prop );
      }
    }
  }

  private static IJavaClassMethod findBestMatchingSetter( boolean bStatic, IJavaClassType getterType, List<IJavaClassMethod> setters )
  {
    if( setters == null )
    {
      return null;
    }

    for( Iterator<IJavaClassMethod> iter = setters.iterator(); iter.hasNext(); )
    {
      IJavaClassMethod setter = iter.next();

      iter.remove();

      if( Modifier.isStatic( setter.getModifiers() ) == bStatic  )
      {
        IJavaClassType csrType = setter.getGenericParameterTypes()[0];
        if( (getterType == null || csrType.equals( getterType )) )
        {
          return setter;
        }
      }
    }
    return null;
  }

  private static IType getTypeFromMethod( IJavaClassMethod m )
  {
    if( m.getReturnType() == JavaTypes.pVOID() )
    {
      return m.getGenericParameterTypes()[0].getActualType( TypeVarToTypeMap.EMPTY_MAP, true );
    }
    return ClassInfoUtil.getActualReturnType( m.getGenericReturnType(), TypeVarToTypeMap.EMPTY_MAP, true );
  }

  private static IJavaClassMethod maybeFindSetterInSuper(IJavaClassMethod getter, IJavaClassInfo superClass ) {
    if( superClass == null ) {
      return null;
    }
    for( ;superClass != null; superClass = superClass.getSuperclass() ) {
      for( IJavaPropertyDescriptor pd: superClass.getPropertyDescriptors() ) {
        if( doesSetterDescMatchGetterMethod(getter, pd) ) {
          return pd.getWriteMethod();
        }
      }
    }
    return null;
  }

  private static IJavaClassMethod maybeFindSetterInSuperInterfaces(IJavaClassMethod getter, IJavaClassInfo[] superInterfaces ) {
    if( superInterfaces == null || superInterfaces.length == 0 ) {
      return null;
    }
    for( IJavaClassInfo iface: superInterfaces ){
      for( IJavaPropertyDescriptor pd: iface.getPropertyDescriptors() ) {
        if( doesSetterDescMatchGetterMethod( getter, pd ) ) {
          return pd.getWriteMethod();
        }
      }
      IJavaClassMethod setter = maybeFindSetterInSuperInterfaces( getter, iface.getInterfaces() );
      if( setter != null ) {
        return setter;
      }
    }
    return null;
  }

  private static boolean doesSetterDescMatchGetterMethod( IJavaClassMethod getter, IJavaPropertyDescriptor pd )
  {
    final IJavaClassType getterType = getter.getGenericReturnType();
    if( getterType == null )
    {
      return false;
    }
    IJavaClassMethod setter = pd.getWriteMethod();
    if( setter == null )
    {
      return false;
    }

    if( !("get" + pd.getName()).equals( getter.getName() ) && !("is" + pd.getName()).equals( getter.getName() ) )
    {
      return false;
    }

    return setter.getGenericParameterTypes()[0].isAssignableFrom( getterType );
  }

  private static boolean doesSetterDescMatchGetterMethod( IJavaClassMethod getter, IJavaClassMethod setter )
  {
    final IJavaClassType getterType = getter.getGenericReturnType();
    if( getterType == null )
    {
      return false;
    }

    return setter.getGenericParameterTypes()[0].isAssignableFrom( getterType );
  }

  private static IJavaClassMethod maybeFindGetterInSuper( IJavaClassMethod setter, IJavaClassInfo superClass, boolean[] getterNameFound ) {
    if( superClass == null ) {
      return null;
    }
    for( ; superClass != null; superClass = superClass.getSuperclass() ) {
      for( IJavaPropertyDescriptor pd: superClass.getPropertyDescriptors() ) {
        if( doesGetterDescMatchSetterMethod( setter, pd, getterNameFound ) & !isSetterFromPropIncompatible( setter, pd, getterNameFound ) ) {
          return pd.getReadMethod();
        }
      }
    }
    return null;
  }

  private static IJavaClassMethod maybeFindGetterInSuperInterfaces( IJavaClassMethod setter, IJavaClassInfo[] superInterfaces, boolean[] getterNameFound ) {
    if( superInterfaces == null || superInterfaces.length == 0 ) {
      return null;
    }
    for( IJavaClassInfo iface: superInterfaces ){
      for( IJavaPropertyDescriptor pd: iface.getPropertyDescriptors() ) {
        if( doesGetterDescMatchSetterMethod( setter, pd, getterNameFound ) & !isSetterFromPropIncompatible( setter, pd, getterNameFound ) ) {
          return pd.getReadMethod();
        }
      }
      IJavaClassMethod getter = maybeFindGetterInSuperInterfaces( setter, iface.getInterfaces(), getterNameFound );
      if( getter != null ) {
        return getter;
      }
    }
    return null;
  }

  private static boolean doesGetterDescMatchSetterMethod( IJavaClassMethod setter, IJavaPropertyDescriptor pd, boolean[] getterNameFound )
  {
    final IJavaClassType setterType = setter.getParameterTypes().length == 1 ? setter.getParameterTypes()[0] : null;
    if( setterType == null )
    {
      return false;
    }

    IJavaClassMethod getter = pd.getReadMethod();
    if( getter == null )
    {
      return false;
    }

    if( !("set" + pd.getName()).equals( setter.getName() ) )
    {
      return false;
    }

    getterNameFound[0] = true;

    return setterType.isAssignableFrom( getter.getGenericReturnType() );
  }

  private static boolean isSetterFromPropIncompatible( IJavaClassMethod setter, IJavaPropertyDescriptor pd, boolean[] incompatibleSetterFound )
  {
    final IJavaClassType setterType = setter.getParameterTypes().length == 1 ? setter.getParameterTypes()[0] : null;
    if( setterType == null )
    {
      return false;
    }

    IJavaClassMethod setterFromPd = pd.getWriteMethod();
    if( setterFromPd == null )
    {
      return false;
    }

    if( !("set" + pd.getName()).equals( setter.getName() ) )
    {
      return false;
    }

    if( !setterType.equals( setterFromPd.getGenericParameterTypes()[0] ) )
    {
       return incompatibleSetterFound[0] = true;
    }
    return false;
  }
}
