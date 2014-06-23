/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.perf;

import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;

public class AllTypeNamesTask implements PerfTask {

  public void setup() {
  }

  public void run() {
    for (ITypeLoader loader : TypeSystem.getAllTypeLoaders()) {
      if (!(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader)) {
        IModule module = loader.getModule();
        TypeSystem.pushModule(module);
        try {
          Set<? extends CharSequence> allTypeNames = loader.getAllTypeNames();
//          print(loader, allTypeNames);
        } finally {
          TypeSystem.popModule(module);
        }
      }
    }
  }

  private void print(ITypeLoader loader, Set<? extends CharSequence> allTypeNames) {
    try {
      CharSequence[] charSequences = allTypeNames.toArray(new CharSequence[allTypeNames.size()]);
      Arrays.sort(charSequences);
      PrintWriter printWriter = new PrintWriter(new File("C:\\" + loader.getClass().getSimpleName() + ".txt"));
      for (CharSequence name : charSequences) {
        printWriter.println(name);
      }
      printWriter.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getCount() {
    return 1;
  }
}
