/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.dynamic;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.Expando;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IExpando;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.SimpleParameterInfo;

import java.util.Collections;
import java.util.List;

/**
 */
public class DynamicConstructorInfo extends BaseFeatureInfo implements IConstructorInfo {
  private IParameterInfo[] _paramInfos;

  public DynamicConstructorInfo( ITypeInfo dynamicTypeInfo, IType... paramTypes ) {
    super( dynamicTypeInfo );
    makeParameters( paramTypes );
  }

  private void makeParameters( IType[] paramTypes ) {
    _paramInfos = new IParameterInfo[paramTypes.length];
    for( int i = 0; i < paramTypes.length; i++ ) {
      _paramInfos[i] = new SimpleParameterInfo( this, paramTypes[i], i );
    }
  }

  @Override
  public boolean isStatic() {
    return false;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public String getName() {
    return DynamicType.RNAME + "()";
  }

  @Override
  public IType getType() {
    return getOwnersType();
  }

  @Override
  public IParameterInfo[] getParameters() {
    return _paramInfos;
  }

  @Override
  public IConstructorHandler getConstructor() {
    return new ConstructorHandler();
  }

  @Override
  public List<IExceptionInfo> getExceptions() {
    return Collections.emptyList();
  }

  @Override
  public boolean isDefault() {
    return true;
  }

  private class ConstructorHandler implements IConstructorHandler {
    @Override
    public Object newInstance( Object... args ) {
      if( args == null || args.length == 0 ) {
        return new Expando();
      }
      else if( args.length == 1 && args[0] instanceof IExpando ) {
        return args[0];
      }
      throw new IllegalArgumentException();
    }
  }
}
