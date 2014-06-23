/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IType;

import java.util.Iterator;
import java.util.List;

public abstract class FeatureReference<R, T> implements IFeatureReference<R, T>
{
  abstract protected Object evaluate( Iterator args );

  abstract protected List<IType> getFullArgTypes();
}
