/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.intellij.openapi.project.Project;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.module.IModule;

import javax.swing.tree.DefaultMutableTreeNode;

public class TypeConstructorsTreeNode extends TypeInfoTreeNode {
  protected TypeConstructorsTreeNode(Project project, IType type, ITypeInfo info) {
    super(project, type, info);
  }

  @Override
  protected void addChildren(IModule module) {
    for (IConstructorInfo constructor : info.getConstructors()) {
      add(new DefaultMutableTreeNode(constructor.getName(), false));
    }
  }

  public String toString() {
    return "Constructors";
  }
}

