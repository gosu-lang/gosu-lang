/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.search;

import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.DelegatingGlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScope;
import gw.plugin.ij.filetypes.GosuFileTypes;
import org.jetbrains.annotations.NotNull;

public class GosuSourceFilterScope extends DelegatingGlobalSearchScope {
  @NotNull
  private final ProjectFileIndex myIndex;

  public GosuSourceFilterScope(@NotNull final GlobalSearchScope delegate) {
    super(delegate, "gosu.sourceFilter");
    myIndex = ProjectRootManager.getInstance(getProject()).getFileIndex();
  }

  public boolean contains(final VirtualFile file) {
    return super.contains(file) &&
        GosuFileTypes.isGosuFile(file);
  }
}
