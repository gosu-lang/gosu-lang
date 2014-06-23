/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import com.intellij.psi.PsiElement;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.Nullable;

public interface IGosuPsiElement extends PsiElement {
  @Nullable
  IParsedElement getParsedElement();

  void accept(GosuElementVisitor visitor);

  void acceptChildren(GosuElementVisitor visitor);
}
