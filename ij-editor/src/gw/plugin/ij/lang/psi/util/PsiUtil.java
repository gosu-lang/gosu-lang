/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.psi.PsiElement;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiUtil {

  private PsiUtil() {
  }

  @Nullable
  public static String getQualifiedReferenceText(@NotNull IGosuCodeReferenceElement referenceElement) {
    StringBuilder builder = new StringBuilder();
    if (!appendName(referenceElement, builder)) {
      return null;
    }

    return builder.toString();
  }

  private static boolean appendName(@NotNull IGosuCodeReferenceElement referenceElement, @NotNull StringBuilder builder) {
    String refName = referenceElement.getReferenceName();
    if (refName == null) {
      return false;
    }
    IGosuCodeReferenceElement qualifier = referenceElement.getQualifier();
    if (qualifier != null) {
      appendName(qualifier, builder);
      builder.append(".");
    }

    builder.append(refName);
    return true;
  }

  @Nullable
  public static PsiElement skipWhitespaces(@Nullable PsiElement elem, boolean forward) {
    //noinspection ConstantConditions
    while (elem != null &&
           elem.getNode() != null &&
           GosuTokenSets.WHITE_SPACES_OR_COMMENTS.contains(elem.getNode().getElementType())) {
      if (forward) {
        elem = elem.getNextSibling();
      }
      else {
        elem = elem.getPrevSibling();
      }
    }
    return elem;
  }
}
