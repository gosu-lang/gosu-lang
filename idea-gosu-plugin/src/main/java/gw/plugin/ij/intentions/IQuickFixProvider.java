/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.lang.annotation.AnnotationHolder;
import gw.lang.UnstableAPI;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;

@UnstableAPI
public interface IQuickFixProvider {

  void collectQuickFixes(AbstractGosuClassFileImpl psiFile, AnnotationHolder holder);

}
