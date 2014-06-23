/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.view;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class GosuFileViewProviderFactory implements FileViewProviderFactory {
  @NotNull
  @Override
  public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, @NotNull Language language, @NotNull PsiManager manager, boolean physical) {
    return new GosuFileViewProvider(manager, file, physical, language);
  }
}
