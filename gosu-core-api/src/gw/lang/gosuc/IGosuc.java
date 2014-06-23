/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;

import java.util.List;

public interface IGosuc {
  void initializeGosu();
  List<IType> compile( String moduleName, List<String> typesToCompile );
  List<IType> compile( IModule module, List<String> typesToCompile );
}
