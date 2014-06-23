/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.util.GosuModuleUtil;

import java.util.List;

public class PrintTypeSystemInfo extends TypeSystemAwareAction {
  @Override
  public void actionPerformed(AnActionEvent e) {
    TypeSystem.tyeLoadingCounter.saveTo("c:\\loading-info.txt");
    TypeSystem.tyeLoadingCounter.saveListTo("c:\\loading-list.txt");
    TypeSystem.tyeRequestCounter.saveTo("c:\\request-info.txt");
    TypeSystem.tyeRequestCounter.saveListTo("c:\\request-list.txt");

    System.out.println("Typesystem Module Dump:\n=======================");
    for (IModule module : GosuModuleUtil.getModules(e.getProject())) {
      System.out.print(module.getName() + ": ");
      for (Dependency dependency : module.getDependencies()) {
        System.out.print(dependency.getModule() + ", ");
      }
      System.out.println();
    }
    System.out.println();
  }
}
