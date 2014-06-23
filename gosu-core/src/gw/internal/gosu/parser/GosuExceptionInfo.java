/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.TypeSystem;

/**
 */
public class GosuExceptionInfo implements IExceptionInfo
{
  private IFeatureInfo _container;
  private String _exceptionName;
  private String _exceptionDescription;

  public GosuExceptionInfo(IFeatureInfo container, String exceptionName, String exceptionDescription) {
    _container = container;
    _exceptionName = exceptionName;
    _exceptionDescription = exceptionDescription;
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IType getOwnersType()
  {
    return _container.getOwnersType();
  }

  public String getName()
  {
    return _exceptionName;
  }

  public String getDisplayName()
  {
    return _exceptionName;
  }

  public String getDescription()
  {
    return _exceptionDescription;
  }

  public IType getExceptionType()
  {
    return TypeSystem.getByFullNameIfValid( getName() );
  }
}
