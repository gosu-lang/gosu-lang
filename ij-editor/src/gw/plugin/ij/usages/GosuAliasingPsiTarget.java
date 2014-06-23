/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.google.common.base.Objects;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.targets.AliasingPsiTarget;
import org.jetbrains.annotations.NotNull;

public class GosuAliasingPsiTarget extends AliasingPsiTarget {
  private String name;

  public GosuAliasingPsiTarget(@NotNull PsiNamedElement target) {
    super(target);
  }

  @NotNull
  @Override
  public AliasingPsiTarget setName(@NotNull String newName) {
    name = newName;
    return this;
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(@NotNull Object o) {
    if (o instanceof GosuAliasingPsiTarget) {
      return super.equals(o) && Objects.equal(name, ((GosuAliasingPsiTarget) o).name);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), name);
  }
}
