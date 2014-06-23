/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.intellij.openapi.project.Project;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.module.IModule;

import javax.swing.tree.DefaultMutableTreeNode;

public class TypePropertiesTreeNode extends TypeInfoTreeNode {
  protected TypePropertiesTreeNode(Project project, IType type, ITypeInfo info) {
    super(project, type, info);
  }

  @Override
  protected void addChildren(IModule module) {
    for (IPropertyInfo property : info.getProperties()) {
      add(new DefaultMutableTreeNode((property.isStatic() ? "static " : "") + property.getName() + ": " + property.getFeatureType(), false));
    }
  }

  public String toString() {
    return "Properties";
  }
}
