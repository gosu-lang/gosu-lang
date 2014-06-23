/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.*;
import gw.lang.reflect.*;

import java.util.List;

public interface IGosuConstructorInfo extends IAttributedFeatureInfo, IGenericMethodInfo, IConstructorInfo, IOptionalParamCapable, IDFSBackedFeatureInfo
{
  List<IReducedSymbol> getArgs();
  IGosuConstructorInfo getBackingConstructorInfo();
}
