/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.impl.GosuClassFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuClassFileProvider implements IGosuFileTypeProvider {
  @NotNull
  @Override
  public IGosuFile createGosuFile(@NotNull FileViewProvider viewProvider) {
    return new GosuClassFileImpl(viewProvider);
  }

  @Nullable
  @Override
  public Icon getIcon(@NotNull VirtualFile file) {
    return GosuIcons.FILE_CLASS;
  }

  public OpenFileDescriptor getOpenFileDescriptor(Project project, VirtualFile virtualFile, int offset) {
    return new OpenFileDescriptor(project, virtualFile, offset);
  }
}
