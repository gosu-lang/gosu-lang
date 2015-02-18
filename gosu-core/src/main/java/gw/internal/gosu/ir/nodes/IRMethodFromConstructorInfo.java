/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.parser.ReducedParameterizedDynamicFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.IJavaConstructorInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.IGosuClassInternal;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.lang.reflect.Constructor;

public class IRMethodFromConstructorInfo extends IRFeatureBase implements IRMethod {

  private IConstructorInfo _constructor;

  public IRMethodFromConstructorInfo(IConstructorInfo constructor) {
    _constructor = constructor;
  }

  @Override
  public IRType getReturnType() {
    return IRTypeConstants.pVOID();
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    return getBoundedParameterTypeDescriptors(_constructor);
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    return getMethodDescriptor(_constructor);
  }

  @Override
  public String getName() {
    return "<init>";
  }

  @Override
  public IRType getOwningIRType() {
    return getTrueOwningType(_constructor);
  }

  @Override
  public IType getOwningIType() {
    IType owningType;
    if( _constructor instanceof IJavaConstructorInfo)
    {
      // We have to get the owner type from the method because it may be 
      // different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaClassConstructor m = ((IJavaConstructorInfo)_constructor).getJavaConstructor();
      if( m != null )
      {
        owningType = TypeSystem.get( m.getEnclosingClass() );
      } else {
        owningType = _constructor.getOwnersType();
      }
    } else {
      owningType = _constructor.getOwnersType();
    }

    return owningType;
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forFeatureInfo(_constructor);
  }

  @Override
  public boolean isStatic() {
    return false;
  }

  public IRType getTargetRootIRType( )
  {
    IRType owner = getOwningIRType();
    if( owner instanceof GosuClassIRType && ((GosuClassIRType)owner).getType() instanceof IGosuEnhancement )
    {
      return IRTypeResolver.getDescriptor( ((IGosuEnhancement)((GosuClassIRType)owner).getType()).getEnhancedType() );
    }
    else
    {
      return owner;
    }
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables() {
    if (_constructor instanceof IGosuConstructorInfo && !IGosuClass.ProxyUtil.isProxy(_constructor.getOwnersType())) {
      return ((IGosuConstructorInfo) _constructor).getTypeVariables();
    } else {
      return null;
    }
  }

  @Override
  public IFunctionType getFunctionType() {
    if (_constructor instanceof IGosuConstructorInfo && !IGosuClass.ProxyUtil.isProxy(_constructor.getOwnersType())) {
      return (IFunctionType) ((IGosuConstructorInfo) _constructor).getDfs().getType();
    } else {
      return null;
    }
  }

  @Override
  public boolean isBytecodeMethod() {
    return (_constructor instanceof IGosuConstructorInfo || _constructor instanceof IJavaConstructorInfo) &&
           !isExternalEntityJavaType( _constructor );
  }

  private static IRType getTrueOwningType( IConstructorInfo mi ) {
    if( mi instanceof IJavaConstructorInfo)
    {
      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaClassConstructor m = ((IJavaConstructorInfo)mi).getJavaConstructor();
      if( m != null )
      {
        return IRTypeResolver.getDescriptor( m.getEnclosingClass() );
      }
    }


    return IRTypeResolver.getDescriptor( mi.getOwnersType() );
  }

  @Override
  public boolean couldHaveTypeVariables() {
    return _constructor instanceof IGosuMethodInfo && !IGosuClass.ProxyUtil.isProxy(_constructor.getOwnersType());
  }

  public List<IRType> getMethodDescriptor( IConstructorInfo mi )
  {
    List<IRType> paramTypes = new ArrayList<IRType>();
    addImplicitConstructorParamTypes( getOwningIType(), paramTypes );
    paramTypes.addAll( getBoundedParameterTypeDescriptors( mi ) );
    return paramTypes;
  }

  private void addImplicitConstructorParamTypes( IType owningType, List<IRType> paramTypes ) {
    addImplicitOuterParamType( owningType, paramTypes );
    addImplicitCapturedSymbolParamTypes( owningType,  paramTypes );
    addImplicitTypeVariableParamTypes( owningType, paramTypes );
    addImplicitEnumParamTypes( owningType, paramTypes );
  }

  private void addImplicitOuterParamType( IType owningType, List<IRType> paramTypes ) {
    // Outer 'this'
    if( AbstractElementTransformer.isNonStaticInnerClass( owningType ) )
    {
      paramTypes.add( IRTypeResolver.getDescriptor( AbstractElementTransformer.getRuntimeEnclosingType( owningType ) ) );
    }
  }

  private void addImplicitCapturedSymbolParamTypes( IType owningType, List<IRType> paramTypes ) {
    // Captured symbols
    // Don't attempt to get captured symbols if the type isn't valid; it'll just throw and result in a cascading break
    if( owningType instanceof IGosuClassInternal && owningType.isValid() ) //&& ((IGosuClassInternal)type).isAnonymous() )
    {
      Map<String, ICapturedSymbol> capturedSymbols = ((IGosuClassInternal)owningType).getCapturedSymbols();
      if( capturedSymbols != null )
      {
        for( ICapturedSymbol sym : capturedSymbols.values() )
        {
          paramTypes.add( IRTypeResolver.getDescriptor( sym.getType().getArrayType() ) );
        }
      }
    }

    // The external symbols are always treated as captured
    if (AbstractElementTransformer.requiresExternalSymbolCapture( owningType ) ) {
      paramTypes.add( IRTypeResolver.getDescriptor( IExternalSymbolMap.class ) );
    }
  }

  private void addImplicitTypeVariableParamTypes( IType owningType, List<IRType> paramTypes ) {
    if ( owningType instanceof IGosuClassInternal && !IGosuClassInternal.ProxyUtil.isProxy( owningType ) ) {
      if (owningType.isParameterizedType()) {
        owningType = owningType.getGenericType();
      }
      addTypeVariableParameters( paramTypes, owningType.getGenericTypeVariables().length);
      addTypeVarsFromEnclosingFunctions( (IGosuClassInternal) owningType, paramTypes );
    }
  }

  private void addTypeVarsFromEnclosingFunctions( IGosuClassInternal gsClass, List<IRType> parameters )
  {
    while( gsClass.isAnonymous() )
    {
      IDynamicFunctionSymbol dfs = AbstractElementTransformer.getEnclosingDFS( gsClass );
      if( dfs == null )
      {
        break;
      }
      addTypeVariableParameters( parameters, AbstractElementTransformer.getTypeVarsForDFS( dfs ).size() );
      gsClass = (IGosuClassInternal)dfs.getGosuClass();
    }
  }

  private void addImplicitEnumParamTypes( IType owningType, List<IRType> paramTypes ) {
    // Enums have name and ordinal arguments implicitly added to their constructors
    if (owningType.isEnum()) {
      paramTypes.add(IRTypeConstants.STRING());
      paramTypes.add(IRTypeConstants.pINT());
    }
  }

   private List<IRType> getBoundedParameterTypeDescriptors( IConstructorInfo mi )
  {
    if( mi.getParameters().length == 0 )
    {
      return Collections.emptyList();
    }

    if( mi instanceof IJavaConstructorInfo )
    {
      return IRTypeResolver.getDescriptors( ((IJavaConstructorInfo)mi).getJavaConstructor().getParameterTypes() );
    }
    else if( mi instanceof IGosuConstructorInfo )
    {
      IReducedDynamicFunctionSymbol dfs = ((IGosuConstructorInfo)mi).getDfs();
      while( dfs instanceof ReducedParameterizedDynamicFunctionSymbol)
      {
        ReducedParameterizedDynamicFunctionSymbol pdfs = (ReducedParameterizedDynamicFunctionSymbol)dfs;
        dfs = pdfs.getBackingDfs();
      }

      List<IRType> boundedTypes = new ArrayList<IRType>( dfs.getArgs().size() );
      if( IGosuClass.ProxyUtil.isProxy( dfs.getGosuClass() ) )
      {
        return getBoundedParamTypesFromProxiedClass( dfs );
      }

      for( int i = 0; i < dfs.getArgs().size(); i++ )
      {
        boundedTypes.add( IRTypeResolver.getDescriptor( TypeLord.getDefaultParameterizedTypeWithTypeVars( dfs.getArgs().get(i).getType() ) ) );
      }
      return boundedTypes;
    }
    else
    {
      return getTypeDescriptors( mi.getParameters() );
    }
  }

  private List<IRType> getBoundedParamTypesFromProxiedClass( IReducedDynamicFunctionSymbol dfs )
  {
    Constructor m = getJavaConstructorFromProxy( dfs );
    return IRTypeResolver.getDescriptors( m.getParameterTypes() );
  }

  private Constructor getJavaConstructorFromProxy( IReducedDynamicFunctionSymbol dfs )
  {
    IType proxyType = dfs.getGosuClass();
    IJavaType javaType = (IJavaType)IGosuClass.ProxyUtil.getProxiedType( proxyType );

    IType[] boundedDfsParams = new IType[dfs.getArgs().size()];
    for( int i = 0; i < boundedDfsParams.length; i++ )
    {
      IType param = dfs.getArgs().get(i).getType();
      if( param instanceof ITypeVariableType && param.getEnclosingType() instanceof IGosuClass )
      {
        param = ((ITypeVariableType)param).getBoundingType();
      }
      boundedDfsParams[i] = param;
    }

    javaType = (IJavaType)TypeLord.getDefaultParameterizedType( javaType );
    IJavaConstructorInfo jmi = (IJavaConstructorInfo)((IRelativeTypeInfo)javaType.getTypeInfo()).getConstructor( javaType, boundedDfsParams );
    return jmi.getRawConstructor();
  }

  @Override
  public boolean isGeneratedEnumMethod()
  {
    return false;
  }
}