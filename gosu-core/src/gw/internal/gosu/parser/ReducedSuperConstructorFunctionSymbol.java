/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.*;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IGosuMethodInfo;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ReducedSuperConstructorFunctionSymbol extends ReducedDynamicFunctionSymbol {

  ReducedSuperConstructorFunctionSymbol(SuperConstructorFunctionSymbol dfs) {
    super( dfs );
  }
}
