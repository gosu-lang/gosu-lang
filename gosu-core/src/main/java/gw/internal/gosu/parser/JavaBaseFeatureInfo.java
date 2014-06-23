/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.Deprecated;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import sun.reflect.annotation.AnnotationParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JavaBaseFeatureInfo extends BaseFeatureInfo
{
  public JavaBaseFeatureInfo( IFeatureInfo container )
  {
    super( container );
  }

  public JavaBaseFeatureInfo( IType intrType )
  {
    super( intrType );
  }

  protected abstract IJavaAnnotatedElement getAnnotatedElement();

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    List<IAnnotationInfo> retValue = new ArrayList<IAnnotationInfo>();
    for( IAnnotationInfo annotation : getAnnotatedElement().getDeclaredAnnotations() )
    {
      retValue.add( annotation );
    }
    return retValue;
  }

  protected abstract boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);
  protected abstract boolean isHiddenViaFeatureDescriptor();

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    boolean visible = super.isVisible(constraint) && isVisibleViaFeatureDescriptor(constraint);
    return visible || isProxyClassCompiling();
  }

  public boolean isHidden()
  {
    boolean bHidden = super.isHidden() || isHiddenViaFeatureDescriptor();
    return bHidden && !isProxyClassCompiling();
  }

  private boolean isProxyClassCompiling()
  {
    IType compilingClass = GosuClassCompilingStack.getCurrentCompilingType();
    if (compilingClass != null && getOwnersType() != null) {
      IType owner = getOwnersType();
      if (compilingClass instanceof IGosuClassInternal && ((IGosuClassInternal) compilingClass).isProxy()) {
        return owner.isAssignableFrom(compilingClass);  // the proxy class should have the extended type in its hierarchy
      }
    }
    return false;
  }

  protected gw.lang.Deprecated makeDeprecated(String reason) {
    Map<String, Object> annotationMap = new HashMap<String, Object>();
    annotationMap.put("value", reason);
    annotationMap.put("version", "");
    return (Deprecated) AnnotationParser.annotationForMap(Deprecated.class, annotationMap);
  }

  protected abstract boolean isDefaultEnumFeature();

}
