/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

import com.intellij.psi.PsiCodeBlock;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMember;
import org.jetbrains.annotations.NotNull;

public interface IGosuClassInitializer extends IGosuMember {
  IGosuClassInitializer[] EMPTY_ARRAY = new IGosuClassInitializer[0];

  @NotNull
  PsiCodeBlock getBlock();

  boolean isStatic();
}
