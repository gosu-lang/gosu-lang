/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.javadoc.IExceptionNode;
import gw.lang.javadoc.IDocRef;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.util.GosuClassUtil;

public class JavaExceptionInfo implements IExceptionInfo
{
  private IFeatureInfo _methodInfo;
  private IJavaClassInfo _exceptionClass;
  private IDocRef<IExceptionNode> _docs;

  public JavaExceptionInfo(IFeatureInfo methodInfo, IJavaClassInfo exceptionClass, IDocRef<IExceptionNode> docs) {
    _methodInfo = methodInfo;
    _exceptionClass = exceptionClass;
    _docs = docs;
  }

  @Override
  public IFeatureInfo getContainer()
  {
    return _methodInfo;
  }

  @Override
  public IType getOwnersType()
  {
    return _methodInfo.getOwnersType();
  }

  @Override
  public String getName()
  {
    return _exceptionClass.getName();
  }

  @Override
  public String getDisplayName()
  {
    return GosuClassUtil.getShortClassName( _exceptionClass.getName() );
  }

  @Override
  public String getDescription()
  {
    return _docs == null || _docs.get() == null ? null : _docs.get().getDescription();
  }

  @Override
  public IType getExceptionType()
  {
    return _exceptionClass.getJavaType();
  }
}
