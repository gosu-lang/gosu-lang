
/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.psi.PsiModifier;
import com.intellij.refactoring.ui.ComboBoxVisibilityPanel;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;

import static com.intellij.util.VisibilityUtil.toPresentableText;

public class GosuComboBoxVisibilityPanel extends ComboBoxVisibilityPanel<String> implements PsiModifier {
  private static final String[] MODIFIERS = {
          IGosuModifier.PRIVATE,
          IGosuModifier.INTERNAL,
          IGosuModifier.PROTECTED,
          IGosuModifier.PUBLIC
  };

  public GosuComboBoxVisibilityPanel() {
    super(MODIFIERS, MODIFIERS);
  }
}
