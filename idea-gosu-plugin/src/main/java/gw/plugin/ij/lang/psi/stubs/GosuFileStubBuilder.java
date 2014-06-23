/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.DefaultStubBuilder;
import com.intellij.psi.stubs.StubElement;
import gw.plugin.ij.lang.psi.IGosuFile;
import org.jetbrains.annotations.NotNull;

public class GosuFileStubBuilder extends DefaultStubBuilder {
  protected StubElement createStubForFile(@NotNull final PsiFile file) {
    if (file instanceof IGosuFile) {
      return new GosuFileStub((IGosuFile) file);
    }
    return super.createStubForFile(file);
  }
}