/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.intellij.openapi.project.Project;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.module.IModule;

import javax.swing.tree.DefaultMutableTreeNode;

public class TypeMethodsTreeNode extends TypeInfoTreeNode {
  protected TypeMethodsTreeNode(Project project, IType type, ITypeInfo info) {
    super(project, type, info);
  }

  @Override
  protected void addChildren(IModule module) {
    for (IMethodInfo method : info.getMethods()) {
      add(new DefaultMutableTreeNode(method.getName() + ": " + method.getReturnType(), false));
    }
  }

  public String toString() {
    return "Methods";
  }
}

