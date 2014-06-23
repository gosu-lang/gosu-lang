/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.intellij.pom.PomDeclarationSearcher;
import com.intellij.pom.PomTarget;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.Consumer;
import gw.lang.parser.Keyword;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import org.jetbrains.annotations.NotNull;

public class GosuDeclarationSearcher extends PomDeclarationSearcher {
  @Override
  public void findDeclarationsAt(@NotNull PsiElement element, int offsetInElement, @NotNull Consumer<PomTarget> consumer) {
    PsiElement parent = element.getParent();
    if (element instanceof LeafPsiElement && element.getText().equals(Keyword.KW_construct.getName()) && parent instanceof GosuMethodImpl) {
      GosuMethodImpl method = (GosuMethodImpl) parent;
      PsiClass psiClass = method.getContainingClass();
      if (method.isConstructor() && (psiClass.isEnum() || psiClass.getContainingClass() != null)) {
        consumer.consume(method);
      }
    }
  }
}
