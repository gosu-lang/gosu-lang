/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.ReducedParameterizedDynamicFunctionSymbol;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.ir.IRType;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.reflect.IAnnotatedFeatureInfo;
import gw.lang.reflect.IAspectMethodInfoDelegate;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IMethodInfoDelegate;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IRMethodFromMethodInfo extends IRFeatureBase implements IRMethod {

  private IMethodInfo _originalMethod;
  private IMethodInfo _terminalMethod;
  private IFunctionType _functionType;

  public IRMethodFromMethodInfo(IMethodInfo originalMethod, IFunctionType functionType) {
    _originalMethod = originalMethod;

    _terminalMethod = originalMethod;
    while (_terminalMethod instanceof IMethodInfoDelegate && !(_terminalMethod instanceof IAspectMethodInfoDelegate)) {
      _terminalMethod = ((IMethodInfoDelegate) _terminalMethod).getSource();
    }

    _functionType = functionType;
  }

  public IMethodInfo getOriginalMethod() {
    return _originalMethod;
  }

  public IMethodInfo getTerminalMethod() {
    return _terminalMethod;
  }

  @Override
  public IRType getReturnType() {
    return getBoundedReturnType(_terminalMethod);
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    return getBoundedParameterTypeDescriptors(_terminalMethod);
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    return getMethodDescriptor(_terminalMethod);
  }

  @Override
  public String getName() {
    return getActualMethodName(_terminalMethod);
  }

  @Override
  public IRType getOwningIRType() {
    return getTrueOwningType(_terminalMethod);
  }

  @Override
  public IType getOwningIType() {
    IType owningType;
    if( _terminalMethod instanceof IJavaMethodInfo )
    {
      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaClassMethod m = ((IJavaMethodInfo)_terminalMethod).getMethod();
      if( m != null )
      {
        owningType = TypeSystem.get( m.getEnclosingClass() );
      } else {
        owningType = _terminalMethod.getOwnersType();
      }
    } else {
      owningType = _terminalMethod.getOwnersType();
    }

    if( owningType instanceof IMetaType)
    {
      owningType = ((IMetaType)owningType).getType();
    }
    return owningType;
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forFeatureInfo(_terminalMethod);
  }

  @Override
  public boolean isStatic() {
    return _terminalMethod.isStatic();
  }

  public IRType getTargetRootIRType( )
  {
    IRType owner = getOwningIRType();
    if( owner instanceof GosuClassIRType && ((GosuClassIRType)owner).getType() instanceof IGosuEnhancement)
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
    if (_terminalMethod instanceof IGosuMethodInfo && !IGosuClass.ProxyUtil.isProxy(_terminalMethod.getOwnersType())) {
      return ((IGosuMethodInfo) _terminalMethod).getTypeVariables();
    } else {
      return null;
    }
  }

  @Override
  public IFunctionType getFunctionType() {
    return _functionType;
  }

  @Override
  protected boolean isImplicitMethod()
  {
    return isGeneratedEnumMethod();
  }

  @Override
  public boolean isGeneratedEnumMethod()
  {
    return getOwningIType().isEnum() &&
           isStatic() &&
           (hasSignature( "getAllValues" ) ||
            hasSignature( "values" ) ||
            hasSignature( "valueOf", JavaTypes.STRING() ));
  }

  private boolean hasSignature( String name, IType... argTypes )
  {
    if( getName().equals( name ) && _originalMethod.getParameters().length == argTypes.length )
    {
      for( int i = 0; i < argTypes.length; i++ )
      {
        if( !_originalMethod.getParameters()[i].getFeatureType().equals( argTypes[i] ) ) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean isBytecodeMethod() {
    return _terminalMethod instanceof IGosuMethodInfo || _terminalMethod instanceof IJavaMethodInfo;
  }

  private static IRType getTrueOwningType( IMethodInfo mi ) {
    if( mi instanceof IJavaMethodInfo)
    {
      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaClassMethod m = ((IJavaMethodInfo)mi).getMethod();
      if( m != null )
      {
        return IRTypeResolver.getDescriptor( m.getEnclosingClass() );
      }
    }


    return IRTypeResolver.getDescriptor( mi.getOwnersType() );
  }

  @Override
  public boolean couldHaveTypeVariables() {
    return _terminalMethod instanceof IGosuMethodInfo && !IGosuClass.ProxyUtil.isProxy(_terminalMethod.getOwnersType());
  }

  private String getActualMethodName(IMethodInfo methodInfo) {
    if( methodInfo instanceof IJavaMethodInfo)
    {
      // Get the name from the Java method in case a PublishedName attr was used in typeinfo
      return ((IJavaMethodInfo)methodInfo).getMethod().getName();
    }
    else
    {
      return methodInfo.getDisplayName();
    }
  }

  private IRType getBoundedReturnType( IMethodInfo mi )
  {
    if( mi instanceof IJavaMethodInfo )
    {
      return IRTypeResolver.getDescriptor( ((IJavaMethodInfo)mi).getMethod().getReturnClassInfo() );
    }
    else if( mi instanceof IGosuMethodInfo)
    {
      IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo)mi).getDfs();
      while( dfs instanceof ReducedParameterizedDynamicFunctionSymbol)
      {
        ReducedParameterizedDynamicFunctionSymbol pdfs = (ReducedParameterizedDynamicFunctionSymbol)dfs;
        dfs = pdfs.getBackingDfs();
      }

      if( IGosuClass.ProxyUtil.isProxy( dfs.getGosuClass() ) )
      {
        return getBoundedReturnTypeFromProxiedClass( dfs );
      }

      return IRTypeResolver.getDescriptor( TypeLord.getDefaultParameterizedTypeWithTypeVars( dfs.getReturnType() ) );
    }
    else
    {
      return IRTypeResolver.getDescriptor( mi.getReturnType() );
    }
  }

  private IRType getBoundedReturnTypeFromProxiedClass( IReducedDynamicFunctionSymbol dfs )
  {
    IJavaClassMethod m = getJavaMethodFromProxy( dfs );
    return JavaClassIRType.get( m.getReturnClassInfo() );
  }

  public List<IRType> getMethodDescriptor( IMethodInfo mi )
  {
    List<IRType> paramTypes = new ArrayList<IRType>();
    IFunctionType functionType = null;
    if (mi instanceof IGosuMethodInfo && !IGosuClass.ProxyUtil.isProxy( mi.getOwnersType() )) {
      functionType = (IFunctionType) ((IGosuMethodInfo) mi).getDfs().getType();
    }
    addImplicitParameters( getOwningIType(),  functionType, isStatic(), paramTypes );
    paramTypes.addAll( getBoundedParameterTypeDescriptors( mi ) );
    return paramTypes;
  }

  private List<IRType> getBoundedParameterTypeDescriptors( IMethodInfo mi )
  {
    if( mi.getParameters().length == 0 )
    {
      return Collections.emptyList();
    }

    if( mi instanceof IJavaMethodInfo )
    {
      return IRTypeResolver.getDescriptors( ((IJavaMethodInfo)mi).getMethod().getParameterTypes() );
    }
    else if( mi instanceof IGosuMethodInfo )
    {
      IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo)mi).getDfs();
      while( dfs instanceof ReducedParameterizedDynamicFunctionSymbol )
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
    IJavaClassMethod m = getJavaMethodFromProxy( dfs );
    IJavaClassInfo[] paramClasses = m.getParameterTypes();
    List<IRType> paramTypes = new ArrayList<IRType>( paramClasses.length );
    for( int i = 0; i < paramClasses.length; i++ )
    {
      paramTypes.add( JavaClassIRType.get( paramClasses[i] ) );
    }
    return paramTypes;
  }

  private IJavaClassMethod getJavaMethodFromProxy( IReducedDynamicFunctionSymbol dfs )
  {
    IType proxyType = dfs.getGosuClass();
    IJavaType javaType = (IJavaType)IGosuClass.ProxyUtil.getProxiedType( proxyType );

    IType[] boundedDfsParams = new IType[dfs.getArgs().size()];
    for( int i = 0; i < boundedDfsParams.length; i++ )
    {
      IType param = dfs.getArgs().get(i).getType();
      boundedDfsParams[i] = param;
    }

    String name = dfs.getDisplayName();
    IJavaMethodInfo jmi = (IJavaMethodInfo)((IRelativeTypeInfo)javaType.getTypeInfo()).getMethod( javaType, name, boundedDfsParams );
    if( jmi == null && name.startsWith( "@" ) && dfs.getArgs().size() == 1 )
    {
      jmi = (IJavaMethodInfo)((IRelativeTypeInfo)javaType.getTypeInfo()).getMethod( javaType, "set" + name.substring( 1 ), boundedDfsParams );
    }
    return jmi.getMethod();
  }

  @Override
  public IAnnotatedFeatureInfo getFeatureInfo()
  {
    return _terminalMethod;
  }
}

