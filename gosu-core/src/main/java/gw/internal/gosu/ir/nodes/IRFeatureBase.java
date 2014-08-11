/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.config.CommonServices;
import gw.internal.gosu.parser.IGosuTemplateInternal;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.LazyTypeResolver;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.IType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.IJavaFieldPropertyInfo;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.statements.VarStatement;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class IRFeatureBase {

  protected IRType maybeReifyFieldType( IType owner, String name, IType originalType )
  {
    IRType symType;
    if( owner.isParameterizedType() )
    {
      if( owner instanceof IGosuClassInternal)
      {
        IType reifiedOwner = TypeLord.getDefaultParameterizedType( owner.getGenericType() );
        VarStatement field = ((IGosuClassInternal) reifiedOwner).getMemberField( name );
        symType = IRTypeResolver.getDescriptor( field.getType() );
      }
      else if( owner instanceof IJavaType)
      {
        IJavaType javaType = (IJavaType)owner;
        try
        {
          Field field = javaType.getIntrinsicClass().getField( name );
          symType = IRTypeResolver.getDescriptor( field.getType() );
        }
        catch( NoSuchFieldException e )
        {
          throw new RuntimeException( e );
        }
      }
      else
      {
        throw new IllegalArgumentException( "Cannot reify field for type " + owner.getName() + " whose metatype is " + owner.getClass() );
      }
    }
    else
    {
      symType = IRTypeResolver.getDescriptor( originalType );
    }
    return symType;
  }

  protected String resolveFieldName( IType owner, String name )
  {
    // If we have a field on a Java class, its name could have been munged, i.e.
    // $100 becomes _100 in the type info.  So we need to unwrap proxies to see
    // if we have a java class, and if so get the underlying java property.  If
    // that property happens to be a field, then we get the underlying field name.
    if ( IGosuClass.ProxyUtil.isProxy( owner ) ) {
      owner = IGosuClass.ProxyUtil.getProxiedType( owner );
    }
    if( owner instanceof IJavaType) {
      IPropertyInfo property = owner.getTypeInfo().getProperty(name);
      if ( property instanceof IJavaFieldPropertyInfo ) {
        return ((IJavaFieldPropertyInfo) property).getField().getName();
      }
    }

    return name;
  }

  protected void addImplicitParameters( IType owner, IFunctionType functionType, boolean bStatic, List<IRType> params ) {
    addImplicitEnhancementParams( owner, bStatic, params );
    addFunctionTypeParams( functionType, params );
    addImplicitExternalSymbolMapParam( owner, bStatic, params );
  }

  private void addImplicitEnhancementParams( IType owner, boolean bStatic, List<IRType> params )
  {
    if ( owner instanceof IGosuEnhancement && !bStatic ) {
      params.add( IRTypeResolver.getDescriptor( ( (IGosuEnhancement) owner).getEnhancedType() ) );
      if( owner.isParameterizedType() )
      {
        addTypeVariableParameters( params, owner.getTypeParameters().length );
      }
      else if( owner.isGenericType() )
      {
        addTypeVariableParameters( params, owner.getGenericTypeVariables().length );
      }
    }
  }

  private void addImplicitExternalSymbolMapParam( IType owner, boolean bStatic, List<IRType> params )
  {
    if( !isImplicitMethod() )
    {
      if( owner instanceof IGosuProgram && !(owner instanceof IGosuTemplateInternal) )
      {
        params.add( IRTypeResolver.getDescriptor( IExternalSymbolMap.class ) );
      }
      else if( owner != null && bStatic )
      {
        addImplicitExternalSymbolMapParam( owner.getEnclosingType(), bStatic, params );
      }
    }
  }

  protected boolean isImplicitMethod() {
    return false;
  }

  private void addFunctionTypeParams( IFunctionType functionType, List<IRType> params )
  {
    if (functionType == null) {
      return;
    }

    if( functionType.isParameterizedType() )
    {
      addTypeVariableParameters( params, functionType.getTypeParameters().length );
    }
    else if( functionType.isGenericType() )
    {
      addTypeVariableParameters( params, functionType.getGenericTypeVariables().length );
    }
  }  

  protected void addTypeVariableParameters( List<IRType> params, int number ) {
    for( int i = 0; i < number; i++ )
    {
      params.add( IRTypeResolver.getDescriptor( LazyTypeResolver.class ) );
    }
  }

  protected List<IRType> getTypeDescriptors( IParameterInfo[] parameters )
  {
    List<IRType> paramTypes = new ArrayList<IRType>(parameters.length);
    for( int i = 0; i < parameters.length; i++ )
    {
      paramTypes.add( IRTypeResolver.getDescriptor( parameters[i].getFeatureType() ) );
    }
    return paramTypes;
  }

  public static boolean isExternalEntityJavaType( IFeatureInfo fi )
  {
    return fi.getOwnersType() instanceof IJavaType &&
           ((IJavaType)fi.getOwnersType()).getBackingClass() != null &&
           CommonServices.getEntityAccess().isExternal( ((IJavaType)fi.getOwnersType()).getBackingClass() );
  }
}
