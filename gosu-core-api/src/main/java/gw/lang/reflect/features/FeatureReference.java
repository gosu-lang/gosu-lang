/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

import gw.lang.reflect.java.JavaTypes;
import java.util.Iterator;
import java.util.List;

public abstract class FeatureReference<R, T> implements IFeatureReference<R, T>
{
  abstract protected Object evaluate( Iterator args );

  abstract protected List<IType> getFullArgTypes();

  protected boolean hasReturn()
  {
    IFeatureInfo fi = getFeatureInfo();
    return fi instanceof IMethodInfo
           ? ((IMethodInfo)fi).getReturnType() != JavaTypes.pVOID()
           : fi == null ||
             fi instanceof IConstructorInfo ||
             fi instanceof IPropertyInfo && ((IPropertyInfo)fi).isReadable( fi.getOwnersType() );
  }
}
