/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class IRMethodForConstructorSymbol implements IRMethod {

  private IType _gosuClass;
  private DynamicFunctionSymbol _dfs;
  private int _numberOfTypeParameters;

  public IRMethodForConstructorSymbol(IType gosuClass, DynamicFunctionSymbol dfs, int numberOfTypeParameters) {
    _gosuClass = gosuClass;
    _dfs = dfs;
    _numberOfTypeParameters = numberOfTypeParameters;
  }

  @Override
  public IRType getReturnType() {
    return IRTypeConstants.pVOID();
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    List<IRType> explicitParameterTypes = new ArrayList<IRType>();
    for (IType declaredParam : _dfs.getArgTypes() ) {
      explicitParameterTypes.add( IRTypeResolver.getDescriptor( declaredParam ) );
    }
    return explicitParameterTypes;
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    return getConstructorParamTypes( _dfs.getArgTypes(), _numberOfTypeParameters, _gosuClass );
  }

  @Override
  public String getName() {
    return "<init>";
  }

  @Override
  public IRType getOwningIRType() {
    return IRTypeResolver.getDescriptor(_gosuClass);
  }

  @Override
  public IType getOwningIType() {
    return _gosuClass;
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forSymbol( _dfs );
  }

  @Override
  public boolean isStatic() {
    return false;
  }

  @Override
  public IRType getTargetRootIRType() {
    return getOwningIRType();
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables() {
    return null;
  }

  @Override
  public IFunctionType getFunctionType() {
    return (IFunctionType) _dfs.getType();
  }

  @Override
  public boolean isGeneratedEnumMethod()
  {
    return false;
  }

  @Override
  public boolean isBytecodeMethod() {
    return true;
  }

  /**
   * Parameters are order like so:
   * ctor( [OuterThis,] [This,] [CapturedSymbols,] [TypeParams,] [EnumParams,] params )
   */
  protected List<IRType> getConstructorParamTypes( IType[] declaredParams, int iTypeParams, IType type )
  {
    List<IRType> params = new ArrayList<IRType>();

    // Insert outer 'this'
    if( isNonStaticInnerClass( type ) )
    {
      params.add( IRTypeResolver.getDescriptor( getRuntimeEnclosingType( type ) ) );
    }

    // Insert captured symbols
    // Don't attempt to get captured symbols if the type isn't valid; it'll just throw and result in a cascading break
    if( type instanceof IGosuClassInternal && type.isValid() ) //&& ((IGosuClassInternal)type).isAnonymous() )
    {
      Map<String, ICapturedSymbol> capturedSymbols = ((IGosuClassInternal)type).getCapturedSymbols();
      if( capturedSymbols != null )
      {
        for( ICapturedSymbol sym : capturedSymbols.values() )
        {
          params.add( IRTypeResolver.getDescriptor( sym.getType().getArrayType() ) );
        }
      }
    }

    // The external symbols are always treated as captured
    if (AbstractElementTransformer.requiresExternalSymbolCapture( type ) ) {
      params.add( IRTypeResolver.getDescriptor( IExternalSymbolMap.class ) );
    }

    // Insert type-parameter types
    if( iTypeParams > 0 )
    {
      for( int i = 0; i < iTypeParams; i++ )
      {
        params.add(IRTypeConstants.ITYPE());
      }
    }

    // Enums have name and ordinal arguments implicitly added to their constructors
    if (type.isEnum()) {
      params.add(IRTypeConstants.STRING());
      params.add(IRTypeConstants.pINT());
    }

    // Add declared parameters
    for (IType declaredParam : declaredParams ) {
      params.add( IRTypeResolver.getDescriptor( declaredParam ) );
    }

    return params;
  }

  /**
   * @param type
   * @return the actual runtime enclosing type of this type (handles the case of enhancements, when
   * the "enclosing type" at runtime will be the enhanced object, rather than the acutal enclosing type)
   */
  public IType getRuntimeEnclosingType( IType type )
  {
    IType enclosingType = maybeUnwrapProxy( type.getEnclosingType() );
    if( enclosingType instanceof IGosuEnhancement)
    {
      IGosuEnhancement enhancement = (IGosuEnhancement)enclosingType;
      enclosingType = enhancement.getEnhancedType();
    }
    return enclosingType;
  }

  private IType maybeUnwrapProxy( IType type )
  {
    if( type != null && type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    return type == null ? null : IGosuClass.ProxyUtil.getProxiedType( type );
  }

  public boolean isNonStaticInnerClass( IType type )
  {
    return (type instanceof IGosuClass) && type.getEnclosingType() != null && !((IGosuClass)type).isStatic();
  }

  @Override
  public boolean couldHaveTypeVariables() {
    return true;
  }
}
