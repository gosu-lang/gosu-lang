/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.intellij.openapi.project.Project;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.GosuModuleUtil;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class TypeInfoTreeNode extends DefaultMutableTreeNode {
  private boolean initialzied = false;

  protected final Project project;
  protected final IType type;
  protected final ITypeInfo info;

  protected TypeInfoTreeNode(Project project, IType type, ITypeInfo info) {
    this.project = project;
    this.type = type;
    this.info = info;
  }

  public boolean isLeaf() {
    return false;
  }

  protected void addChildren() {
    final ITypeLoader typeLoader = getInfo().getOwnersType().getTypeLoader();
    final IModule module = typeLoader == null ? TypeSystem.getGlobalModule() : GosuModuleUtil.getGlobalModule(project);
    TypeSystem.pushModule(module);
    try {
      addChildren(module);
    } finally {
      TypeSystem.popModule(module);
    }
  }

  protected abstract void addChildren(IModule module);

  public ITypeInfo getInfo() {
    return info;
  }

  public int getChildCount() {
    if (!initialzied) {
      initialzied = true;
      addChildren();
    }
    return super.getChildCount();
  }
}