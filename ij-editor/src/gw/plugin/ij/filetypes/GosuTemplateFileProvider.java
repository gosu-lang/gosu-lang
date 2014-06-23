/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.impl.GosuTemplateFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuTemplateFileProvider implements IGosuFileTypeProvider {
  public static final String EXT_TEMPLATE = "gst";

  @NotNull
  @Override
  public IGosuFile createGosuFile(@NotNull FileViewProvider viewProvider) {
    return new GosuTemplateFileImpl(viewProvider);
  }

  @Nullable
  @Override
  public Icon getIcon(@NotNull VirtualFile file) {
    return GosuIcons.FILE_TEMPLATE;
  }

  public OpenFileDescriptor getOpenFileDescriptor(Project project, VirtualFile virtualFile, int offset) {
    return new OpenFileDescriptor(project, virtualFile, offset);
  }

  public static boolean inTemplateFile(@NotNull PsiElement element) {
    final PsiFile psiFile = element.getContainingFile();
    if (psiFile != null) {
      final VirtualFile file = psiFile.getVirtualFile();
      if (file != null) {
        return EXT_TEMPLATE.equals(file.getExtension());
      }
    }
    return false;
  }
}
