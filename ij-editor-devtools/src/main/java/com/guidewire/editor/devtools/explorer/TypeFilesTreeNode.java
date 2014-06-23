/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.FileUtil;

import javax.swing.tree.DefaultMutableTreeNode;

public class TypeFilesTreeNode extends TypeInfoTreeNode {
  protected TypeFilesTreeNode(Project project, IType type, ITypeInfo info) {
    super(project, type, info);
  }

  @Override
  protected void addChildren(IModule module) {
    for (VirtualFile file : FileUtil.getTypeResourceFiles(type)) {
      add(new DefaultMutableTreeNode(file.getPath(), false));
    }
  }

  public String toString() {
    return "Files";
  }
}
