/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import gw.plugin.ij.lang.psi.IGosuFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface IGosuFileTypeProvider {
  @NotNull
  IGosuFile createGosuFile(@NotNull FileViewProvider viewProvider);

  @Nullable
  Icon getIcon(@NotNull VirtualFile file);

  OpenFileDescriptor getOpenFileDescriptor(Project project, VirtualFile virtualFile, int offset);
}
