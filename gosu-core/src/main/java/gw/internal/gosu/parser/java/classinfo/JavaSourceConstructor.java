/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.MethodTree;
import gw.internal.gosu.ir.nodes.IRMethodFromConstructorInfo;
import gw.lang.ir.IRType;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class JavaSourceConstructor extends JavaSourceMethod implements IJavaClassConstructor
{

  private Constructor<?> _constructor;

  public JavaSourceConstructor( MethodTree method, JavaSourceType containingClass )
  {
    super( method, containingClass );
  }

  public boolean isConstructor()
  {
    return true;
  }

  @Override
  public IParameterInfo[] convertGenericParameterTypes( IFeatureInfo container, TypeVarToTypeMap actualParamByVarName )
  {
    return getActualParameterInfos( container, actualParamByVarName, true );
  }

  @Override
  public Object newInstance( Object... args ) throws InvocationTargetException, IllegalAccessException, InstantiationException
  {
    if( _constructor == null )
    {
      assignConstructor();
    }
    return _constructor.newInstance( args );
  }

  private void assignConstructor()
  {
    IJavaClassInfo[] paramTypes = getParameterTypes();
    List<IType> params = new ArrayList<>();
    for( IJavaClassInfo paramType : paramTypes )
    {
      params.add( TypeSystem.get( paramType ) );
    }
    IType javaType = getEnclosingClass().getJavaType();
    IConstructorInfo ci = javaType.getTypeInfo().getConstructor( params.toArray( IType.EMPTY_TYPE_ARRAY ) );

    IRMethodFromConstructorInfo irCtor = new IRMethodFromConstructorInfo( ci );
    List<IRType> allParameterTypes = irCtor.getAllParameterTypes();
    Class[] paramClasses = new Class[allParameterTypes.size()];
    for( int i = 0; i < allParameterTypes.size(); i++ )
    {
      paramClasses[i] = allParameterTypes.get( i ).getJavaClass();
    }

    try
    {
      Class<?> backingClass = getEnclosingClass().getBackingClass();
      _constructor = backingClass.getDeclaredConstructor( paramClasses );
    }
    catch( NoSuchMethodException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  @Override
  public boolean isDefault()
  {
    return false;
  }
}
