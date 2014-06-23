/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.util.PsiExpressionTrimRenderer;
import com.intellij.util.Function;

public class GosuRenderFuction implements Function<PsiExpression, String> {
  @Override
  public String fun(PsiExpression psiExpression) {
    return render(psiExpression);
  }

  public static String render(PsiExpression expression) {
    StringBuilder buf = new StringBuilder();
    expression.accept(new PsiExpressionTrimRenderer(buf));
    if (buf.length() == 0) {
      buf.append(expression.getText());
    }
    return buf.toString();
  }
}