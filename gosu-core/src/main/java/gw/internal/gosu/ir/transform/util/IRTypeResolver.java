/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.util;

import gw.util.FunctionClassUtil;
import gw.internal.gosu.ir.nodes.GosuClassIRType;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.ir.IRType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import java.util.ArrayList;
import java.util.List;

public class IRTypeResolver {

  public static IRType getDescriptor( IType type)
  {
    return getDescriptor( type, false );
  }

  public static IRType getDescriptor( IType type, boolean getConcreteTypeForMetaType )
  {
    type = type instanceof IMetaType ? type : TypeLord.getPureGenericType( type );

    if( type instanceof IJavaType ) // handles primitives too
    {
      try
      {
        return JavaClassIRType.get( ((IJavaType)type).getBackingClassInfo() );
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow( e, type.getName() );
      }
    }
    else if( type instanceof IGosuClassInternal )
    {
      if( IGosuClass.ProxyUtil.isProxy( type ) )
      {
        return getDescriptor( IGosuClass.ProxyUtil.getProxiedType( type ), getConcreteTypeForMetaType );
      }
      else
      {
        return GosuClassIRType.get( type );
      }
    }
    else if( getJavaBackedClass( type ) != null )
    {
      return getDescriptor( getJavaBackedClass( type ) );
    }
    else if( type.isArray() )
    {
      // Array types NEVER use concrete types for meta types
      return getDescriptor(type.getComponentType(), false).getArrayType();
    }
    else if( type instanceof TypeVariableType)
    {
      return getDescriptor( ((TypeVariableType)type).getBoundingType(), getConcreteTypeForMetaType );
    }
    else if( type instanceof IFunctionType)
    {
      IFunctionType funcType = (IFunctionType)type;
      return getDescriptor( FunctionClassUtil.getFunctionInterfaceForArity( funcType.getReturnType() != JavaTypes.pVOID(), funcType.getParameterTypes().length ) );
    }
    else if( type instanceof MetaType)
    {
      if (getConcreteTypeForMetaType) {
        return getConcreteIRTypeForMetaType((MetaType) type);
      } else {
        return getDescriptor( IType.class );
      }
    }
    else
    {
      return getDescriptor( Object.class );
    }
  }

  public static IRType getDescriptor( Class cls )
  {
    return JavaClassIRType.get( cls );
  }

  public static IRType getDescriptor( IJavaClassInfo cls )
  {
    return JavaClassIRType.get( cls );
  }

  public static IRType getConcreteIRTypeForMetaType( MetaType metaType ) {
    if( MetaType.DEFAULT_TYPE_TYPE.get().getName().equals( metaType.getName() ) ||
        MetaType.ROOT_TYPE_TYPE.get().getName().equals( metaType.getName() ) )
    {
      return getDescriptor( IType.class );
    }
    IType underlyingType = metaType.getType();
    if( AbstractElementTransformer.isBytecodeType( underlyingType ) && !isEntityType(underlyingType))
    {
      return getDescriptor( underlyingType.getClass() );
    }
    else
    {
      return getDescriptor( IType.class );
    }
  }

  private static boolean isEntityType(IType type) {
    String namespace = type.getNamespace();
    return "entity".equals(namespace) || "typekey".equals(namespace);
  }

  public static IJavaClassInfo getJavaBackedClass( IType arg )
  {
    if( arg instanceof IJavaBackedType)
    {
      IJavaClassInfo cls = ((IJavaBackedType)arg).getBackingClassInfo();
      if( cls == null )
      {
        throw new IllegalStateException( "Type: " + arg.getName() + " returned null Class from IJavaBackedType.getBackingClassInfo()" );
      }
      return cls;
    }
    return null;
  }

  public static List<IRType> getDescriptors( IJavaClassInfo[] classes )
  {
    List<IRType> irTypes = new ArrayList<IRType>( classes.length );
    for (IJavaClassInfo parameterClass : classes) {
      irTypes.add( getDescriptor( parameterClass ) );
    }
    return irTypes;
  }

  public static List<IRType> getDescriptors( Class[] classes )
  {
    List<IRType> irTypes = new ArrayList<IRType>( classes.length );
    for (Class parameterClass : classes) {
      irTypes.add( getDescriptor( parameterClass ) );
    }
    return irTypes;
  }

  public static List<IRType> getDescriptors( List<IType> types )
  {
    List<IRType> irTypes = new ArrayList<IRType>( types.size() );
    for (IType type : types) {
      irTypes.add( getDescriptor( type ) );
    }
    return irTypes;
  }
}
