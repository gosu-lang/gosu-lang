/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class GosuCodeFileType extends LanguageFileType {
  public static final GosuCodeFileType INSTANCE = new GosuCodeFileType();

  private static final String EXT_CLASS = "gs";

  private GosuCodeFileType() {
    super(GosuLanguage.instance());
  }

  @NotNull
  public String getName() {
    return "GosuCode";
  }

  @NotNull
  public String getDescription() {
    return "Gosu code source files";
  }

  @NotNull
  public String getDefaultExtension() {
    return EXT_CLASS;
  }

  public Icon getIcon() {
    return GosuIcons.CLASS; // TODO: Should be some general icon, not class related
  }

  public boolean isJVMDebuggingSupported() {
    return true;
  }

  public static IGosuFileTypeProvider getFileTypeProvider(@NotNull VirtualFile file) {
    return GosuCodeFileTypesManager.INSTANCE.getFileTypeProvider(file.getExtension());
  }

  // IGosuFileType
  public List<String> getExtensions() {
    return GosuCodeFileTypesManager.INSTANCE.getRegisteredExtensions();
  }

  @Nullable
  public Icon getIcon(@NotNull VirtualFile file) {
    final IGosuFileTypeProvider provider = getFileTypeProvider(file);
    if (provider != null) {
      return provider.getIcon(file);
    } else {
      return null;
    }
  }

  public PsiFile createPsiFile(@NotNull FileViewProvider viewProvider) {
    final VirtualFile file = viewProvider.getVirtualFile();
    final IGosuFileTypeProvider provider = getFileTypeProvider(file);
    if (provider != null) {
      return provider.createGosuFile(viewProvider);
    } else {
      return new GosuProgramFileImpl(viewProvider); // For code injection (can't find provider by file extension)
    }
  }

}