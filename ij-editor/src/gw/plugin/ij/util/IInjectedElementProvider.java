/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.psi.PsiFile;

public interface IInjectedElementProvider {
  PsiFile getInjectedFile();
}
