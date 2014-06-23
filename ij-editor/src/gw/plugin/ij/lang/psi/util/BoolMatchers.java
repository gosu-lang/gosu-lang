/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiMatcherExpression;

public class BoolMatchers {

  public static PsiMatcherExpression and(final PsiMatcherExpression... expressions) {
    return new PsiMatcherExpression() {

      public Boolean match(PsiElement element) {
        for (PsiMatcherExpression expr : expressions) {
          if (!expr.match(element)) {
            return Boolean.FALSE;
          }
        }
        return Boolean.TRUE;
      }
    };
  }

  public static PsiMatcherExpression or(final PsiMatcherExpression... expressions) {
    return new PsiMatcherExpression() {

      public Boolean match(PsiElement element) {
        for (PsiMatcherExpression expr : expressions) {
          if (expr.match(element)) {
            return Boolean.TRUE;
          }
        }
        return Boolean.FALSE;
      }
    };
  }


}
