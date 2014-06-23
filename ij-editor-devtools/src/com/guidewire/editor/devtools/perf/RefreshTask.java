/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.perf;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.util.Set;

public class RefreshTask implements PerfTask {
  IModule rootModule;
  String[] typeNames;

  public void setup() {
//    rootModule = TypeSystem.getGlobalModule();
//    DisplayKeyTypeLoader typeLoader = TypeSystem.getTypeLoader(DisplayKeyTypeLoader.class);
//    Set<? extends CharSequence> allTypeNames = typeLoader.getAllTypeNames();
//    for (CharSequence typeName : allTypeNames) {
//      TypeSystem.getByFullNameIfValid(typeName.toString(), rootModule);
//    }
//    typeNames = allTypeNames.toArray(new String[allTypeNames.size()]);
  }

  @Override
  public int getCount() {
    return 10;
  }

  public void run() {
    File file = new File("C:\\dev\\emerald\\eclipse\\app-cc\\cc\\config\\locale\\en_US\\display.properties");
    IFile iFile = CommonServices.getFileSystem().getIFile(file);
    TypeSystem.refreshed(iFile);

//    PLDependencies.getLocales().filesChanged();
//    PLDependencies.getLocales().reload();
  }
}
