/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuDocUtil {

  private GosuDocUtil() {
  }

  @Nullable
  public static PsiDocComment findDocCommnentNode(@NotNull ASTNode node) {
    ASTNode parent = node.getTreeParent();
    ASTNode[] children = parent.getChildren(null);
    boolean found = false;
    for (int i = children.length - 1; i >= 0; i--) {
      if (!found && children[i] == node) {
        found = true;
        continue;
      }
      if (found) {
        if (children[i] instanceof PsiDocComment) {
          return (PsiDocComment) children[i];
        }
        if (!(children[i] instanceof PsiWhiteSpace) && children[i].getTextLength() != 0 ) {
          break;
        }
      }
    }
    return null;
  }

}
