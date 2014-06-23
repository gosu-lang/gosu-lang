/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IDFSBackedFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IProgramInstance;
import gw.util.GosuExceptionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class ReducedDynamicFunctionSymbol extends ReducedSymbol implements IReducedDynamicFunctionSymbol {

  private IType[] _argTypes;
  private IType _returnType;
  private List<IReducedSymbol> _args;
  private IReducedDynamicFunctionSymbol _superDfs;
  private final boolean _isConstructor;

  ReducedDynamicFunctionSymbol(DynamicFunctionSymbol dfs) {
    super( dfs );
    _isConstructor = dfs.isConstructor();
    _argTypes = dfs.getArgTypes();
    _returnType = dfs.getReturnType();
    _args = makeArgs(dfs);
    _fullDescription = dfs.getFullDescription();
    DynamicFunctionSymbol superDfs = dfs.getSuperDfs();
    if (superDfs != null) {
      _superDfs = superDfs.createReducedSymbol();
    }
  }

  private List<IReducedSymbol> makeArgs(IDynamicFunctionSymbol dfs) {
    List<ISymbol> args = dfs.getArgs();
    List<IReducedSymbol> newArgs = new ArrayList<IReducedSymbol>(args.size());
    for (ISymbol arg : args) {
      newArgs.add( arg.createReducedSymbol() );
    }
    return newArgs;
  }

  @Override
  public IType[] getArgTypes() {
    return _argTypes;
  }

  @Override
  public IType getReturnType() {
    return _returnType;
  }

  @Override
  public String getFullDescription() {
    return _fullDescription;
  }

  @Override
  public List<IReducedSymbol> getArgs() {
    return _args;
  }

  @Override
  public IReducedDynamicFunctionSymbol getSuperDfs() {
    return _superDfs;
  }

  @Override
  public boolean isSuperOrThisConstructor() {
    return SuperConstructorFunctionSymbol.class.isAssignableFrom(getSymbolClass()) ||
            ThisConstructorFunctionSymbol.class.isAssignableFrom(getSymbolClass());
  }

  @Override
  public IReducedDynamicFunctionSymbol getBackingDfs() {
    return this;
  }

  @Override
  public IAttributedFeatureInfo getMethodOrConstructorInfo()
  {
    IGosuClass declaringType = getGosuClass();
    if( declaringType == null )
    {
      return null;
    }

    ITypeInfo typeInfo = declaringType.getTypeInfo();

    List<? extends IMethodInfo> methods;
    if (typeInfo instanceof IRelativeTypeInfo) {
      methods = ((IRelativeTypeInfo) typeInfo).getMethods( declaringType );
    } else {
      methods = typeInfo.getMethods();
    }
    for( IMethodInfo mi : methods ) {
      if (mi instanceof IDFSBackedFeatureInfo) {
        IReducedDynamicFunctionSymbol dfs = ((IDFSBackedFeatureInfo) mi).getDfs();
        if (this.equals(dfs) || getBackingDfs().equals(dfs)) {
          return mi;
        }
      }
    }

    List<? extends IConstructorInfo> ctors;
    if (typeInfo instanceof IRelativeTypeInfo) {
      ctors = ((IRelativeTypeInfo)typeInfo).getConstructors( declaringType );
    } else {
      ctors = typeInfo.getConstructors();
    }
    for( IConstructorInfo ci : ctors ) {
      if (ci instanceof IDFSBackedFeatureInfo) {
        IReducedDynamicFunctionSymbol dfs = ((IDFSBackedFeatureInfo) ci).getDfs();
        if (this.equals(dfs) || getBackingDfs().equals(dfs)) {
          return ci;
        } else if (((this instanceof ReducedSuperConstructorFunctionSymbol) ||
                (this instanceof ReducedThisConstructorFunctionSymbol)) &&
                (dfs.getArgs().equals(getArgs()))) {
          return ci;
        }
      }
    }

    return null;
  }

  @Override
  public int hashCode()
  {
    return getName().hashCode();
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || !(o instanceof ReducedDynamicFunctionSymbol))
    {
      return false;
    }

    ReducedDynamicFunctionSymbol that = (ReducedDynamicFunctionSymbol)o;

    String strName = getName();
    return !(strName != null ? !strName.equals( that.getName() ) : that.getName() != null);
  }

  /**
   * Invokes the dynamic function.
   */
  public Object invoke( Object[] args )
  {
    return invokeFromBytecode(args);
  }

  private Object invokeFromBytecode( Object[] args )
  {
    IGosuClassInternal gsClass = getGosuClass();
    if( gsClass == null )
    {
      throw new IllegalStateException( "Did not find Gosu Class/Program" );
    }
    Class<?> javaClass = gsClass.getBackingClass();
    IProgramInstance instance = null;
    if( gsClass instanceof IGosuProgram)
    {
      try
      {
        instance = (IProgramInstance)javaClass.newInstance();
        instance.evaluate(null);
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow(e);
      }
    }
    IMethodInfo mi = gsClass.getTypeInfo().getMethod(gsClass, getDisplayName(), getArgTypes());
    return mi.getCallHandler().handleCall( instance, args );
  }
  public List<IGosuAnnotation> getAnnotations() {
    List<IGosuAnnotation> result;
    IAttributedFeatureInfo featureInfo = getMethodOrConstructorInfo();
    if (featureInfo instanceof GosuBaseAttributedFeatureInfo) {
      IModifierInfo modifierInfo = ((GosuClassTypeInfo)getGosuClass().getTypeInfo()).getModifierInfo((GosuBaseAttributedFeatureInfo) featureInfo);
      result = modifierInfo != null ? modifierInfo.getAnnotations() : Collections.<IGosuAnnotation>emptyList();
    } else {
      result = Collections.emptyList();
    }
    return result;
  }

  public boolean isVarPropertyGet() {
    return VarPropertyGetFunctionSymbol.class.isAssignableFrom(getSymbolClass());
  }

  public boolean isVarPropertySet() {
    return VarPropertySetFunctionSymbol.class.isAssignableFrom(getSymbolClass());
  }

  public boolean isConstructor() {
    return _isConstructor;
  }
}
