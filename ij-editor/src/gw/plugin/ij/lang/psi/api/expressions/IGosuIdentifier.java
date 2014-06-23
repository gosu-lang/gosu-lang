/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.expressions;

import com.intellij.psi.PsiIdentifier;
import org.jetbrains.annotations.Nullable;

public interface IGosuIdentifier extends PsiIdentifier {
  @Nullable
  String getName();
}
