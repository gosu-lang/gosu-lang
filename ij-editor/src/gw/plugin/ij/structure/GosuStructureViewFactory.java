/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.structure;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import org.jetbrains.annotations.NotNull;

public class GosuStructureViewFactory implements PsiStructureViewFactory {
  public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
    if (!(psiFile instanceof IGosuFileBase)) return null;
    return new TreeBasedStructureViewBuilder() {
      @NotNull
      public StructureViewModel createStructureViewModel() {
        return new GosuStructureViewModel((IGosuFileBase) psiFile);
      }
    };
  }
}