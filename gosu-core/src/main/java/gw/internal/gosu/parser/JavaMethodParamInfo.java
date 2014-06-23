/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IParamNode;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaParameterDescriptor;

import java.util.Collections;
import java.util.List;

/**
 */
public class JavaMethodParamInfo extends BaseFeatureInfo implements IParameterInfo
{
  private IJavaParameterDescriptor _pd;
  private IDocRef<IParamNode> _docs;

  /**
   * @param container Typically this will be the containing IMethodInfo
   */
  public JavaMethodParamInfo( IFeatureInfo container, IJavaParameterDescriptor pd, IDocRef<IParamNode> docs )
  {
    super( container );
    _pd = pd;
    _docs = docs;
  }

  @Override
  public String getName()
  {
    return hasValidDocs() ? getDocRef().getName() : _pd.getName();
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  @Override
  public String getDisplayName()
  {
    return hasValidDocs() ? getDocRef().getDescription() : _pd.getDisplayName();
  }

  public String getShortDescription()
  {
    return hasValidDocs() ? getDocRef().getDescription() : _pd.getShortDescription();
  }

  @Override
  public String getDescription()
  {
    return hasValidDocs() ? getDocRef().getDescription() : _pd.getShortDescription();
  }

  @Override
  public IType getFeatureType()
  {
    return _pd.getFeatureType();
  }

  @Override
  public boolean isHidden()
  {
    return _pd.isHidden();
  }

  @Override
  public boolean isStatic()
  {
    return false;
  }

  private IParamNode getDocRef()
  {
    return _docs.get();
  }

  private boolean hasValidDocs()
  {
    return _docs != null && getDocRef() != null;
  }

}
